package com.aplicacion.essalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.essalud.adapters.HorariosAdapter;
import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.Hospital;
import com.aplicacion.essalud.models.Medico;
import com.aplicacion.essalud.models.Servicio;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HorariosActivity extends AppCompatActivity {

    TextView txvHospitalNombre;
    ListView lvHorarios;
    MaterialButton mbFiltrarHorario;
    AutoCompleteTextView actvMedico;
    AutoCompleteTextView actvServicio;
    TextInputEditText tietFecha;
    MaterialButton btnFecha;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        txvHospitalNombre = (TextView) findViewById(R.id.txvHospital);
        lvHorarios = (ListView) findViewById(R.id.lvHorarios);
        mbFiltrarHorario = (MaterialButton) findViewById(R.id.mbFiltrarHorario);
        // Modificación de ToolBar
        ((Toolbar) findViewById(R.id.myToolbar)).setTitle("Horarios");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mbFiltrarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog().show();
            }
        });
        ShowHorarios(1, null);
    }

    public AlertDialog createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HorariosActivity.this);
        builder.setTitle("Búsqueda de servicios");
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.filtro_horario, null);
        actvServicio = (AutoCompleteTextView) view.findViewById(R.id.actvServicio);
        actvServicio.setThreshold(3);
        actvServicio.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item,
                new String[]{"Servicio 1", "Servicio 2", "Servicio 3", "Servicio 4"}));
        actvMedico = (AutoCompleteTextView) view.findViewById(R.id.actvMedico);
        actvMedico.setThreshold(3);
        actvMedico.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item,
                new String[]{"Médico 1", "Médico 2", "Médico 3", "Médico 4"}));
        tietFecha = (TextInputEditText) view.findViewById(R.id.tietFechaFiltro);
        btnFecha = (MaterialButton) view.findViewById(R.id.btnFecha);
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(HorariosActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tietFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        builder.setView(view);
        builder.setPositiveButton(R.string.buscar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ejecutar busqueda
                Toast.makeText(HorariosActivity.this, "Ejecutando búsqueda", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancelado
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public void ShowHorarios(final int idHospital, final Date fechafiltro) {
        final List<Horario> listHorarios = new ArrayList<>();
        firebaseDatabase.getReference("hospitales").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dsHospitales) {
                final Hospital hospital = new Hospital();
                for (DataSnapshot dsHospital : dsHospitales.getChildren()) {
                    if (Integer.parseInt(Objects.requireNonNull(dsHospital.getKey())) == idHospital) {
                        hospital.setId(Integer.parseInt(Objects.requireNonNull(dsHospital.getKey())));
                        hospital.setDireccion(Objects.requireNonNull(dsHospital.child("DIRECCION").getValue()).toString());
                        firebaseDatabase.getReference("unidades_medicas").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dsUnidadesMedicas) {
                                for (final DataSnapshot dsUnidadMedica : dsUnidadesMedicas.getChildren()) {
                                    if (Integer.parseInt(Objects.requireNonNull(dsUnidadMedica.child("HOSPITAL_ID").getValue()).toString()) == hospital.getId()) {
                                        final Servicio servicio = new Servicio();
                                        servicio.setId(Integer.parseInt(Objects.requireNonNull(dsUnidadMedica.getKey())));
                                        servicio.setNombre(Objects.requireNonNull(dsUnidadMedica.child("NOMBRE_UNIDAD").getValue()).toString());
                                        final Calendar c = Calendar.getInstance();
                                        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd yyyy");
                                        @SuppressLint("SimpleDateFormat") final SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss aa");
                                        final Date[] fecha = {(fechafiltro == null) ? c.getTime() : fechafiltro};
                                        final Date[] hora = {(fechafiltro == null) ? c.getTime() : fechafiltro};
                                        firebaseDatabase.getReference("medicos").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dsMedicos) {
                                                for (final DataSnapshot dsMedico : dsMedicos.getChildren()) {
                                                    firebaseDatabase.getReference("especialidades").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dsEspecialidades) {
                                                            for (final DataSnapshot dsEspecialidad : dsEspecialidades.getChildren()) {
                                                                if (Objects.requireNonNull(dsEspecialidad.child("NOMBRE").getValue()).toString().equals(Objects.requireNonNull(dsUnidadMedica.child("NOMBRE_UNIDAD").getValue()).toString())
                                                                        && Integer.parseInt(Objects.requireNonNull(dsEspecialidad.getKey())) == Integer.parseInt(Objects.requireNonNull(dsMedico.child("ESPECIALIDAD_ID").getValue()).toString())) {
                                                                    final Medico medico = new Medico();
                                                                    medico.setId(Integer.parseInt(Objects.requireNonNull(dsMedico.getKey())));
                                                                    medico.setFotoPerfil(R.drawable.ic_person_white_24dp);
                                                                    final int personaId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(dsMedico.child("PERSONA_ID").getValue()).toString()));
                                                                    firebaseDatabase.getReference("personas").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dsPersonas) {
                                                                            String nombreMedico = "";
                                                                            for (DataSnapshot dsPersona : dsPersonas.getChildren()) {
                                                                                if (Integer.parseInt(Objects.requireNonNull(dsPersona.getKey())) == personaId) {
                                                                                    nombreMedico = Objects.requireNonNull(dsPersona.child("APELLIDO").getValue()).toString() + ", " + Objects.requireNonNull(dsPersona.child("NOMBRE").getValue()).toString();
                                                                                    break;
                                                                                }
                                                                            }
                                                                            medico.setNombre(nombreMedico);
                                                                            for (int i = 0; i < 10; i++) {
                                                                                Horario horario = new Horario();
                                                                                horario.setServicio(servicio);
                                                                                horario.setMedico(medico);
                                                                                horario.setFecha(dateformat.format(fecha[0]));
                                                                                horario.setHora(timeformat.format(hora[0]));
                                                                                listHorarios.add(horario);
                                                                                c.add(Calendar.HOUR, 2);
                                                                                fecha[0] = c.getTime();
                                                                                hora[0] = c.getTime();
                                                                            }
                                                                            lvHorarios.setAdapter(new HorariosAdapter(HorariosActivity.this, listHorarios));

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
