package com.aplicacion.essalud;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aplicacion.essalud.adapters.HorariosAdapter;
import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.Hospital;
import com.aplicacion.essalud.models.Medico;
import com.aplicacion.essalud.models.Servicio;
import com.aplicacion.essalud.models.database.LocalDB;
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

import static com.aplicacion.essalud.methods.Methods.decrypt;
import static com.aplicacion.essalud.methods.Methods.encrypt;

public class HorariosActivity extends AppCompatActivity {

    TextView txvHospitalNombre;
    ListView lvHorarios;
    TextInputEditText actvMedico;
    TextInputEditText actvServicio;
    TextInputEditText tietFecha;
    MaterialButton btnFecha;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        txvHospitalNombre = (TextView) findViewById(R.id.txvHospital);
        lvHorarios = (ListView) findViewById(R.id.lvHorarios);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Horarios Disponibles");
        firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences PREFERENCES = getSharedPreferences(LocalDB.PREFS_NAME, MODE_PRIVATE);
        final String HospitalId = decrypt(PREFERENCES.getString(encrypt(LocalDB.PREF_HOSPITAL_ID), null));
        ShowHorarios(Integer.parseInt(HospitalId), null);
    }

    public AlertDialog createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HorariosActivity.this);
        builder.setTitle("Búsqueda de servicios");
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.filtro_horario, null);
        actvServicio = (TextInputEditText) view.findViewById(R.id.actvServicio);
        actvMedico = (TextInputEditText) view.findViewById(R.id.actvMedico);
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
                        txvHospitalNombre.setText(hospital.getDireccion());
                        firebaseDatabase.getReference("unidades_medicas").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dsUnidadesMedicas) {
                                for (final DataSnapshot dsUnidadMedica : dsUnidadesMedicas.getChildren()) {
                                    if (Integer.parseInt(Objects.requireNonNull(dsUnidadMedica.child("HOSPITAL_ID").getValue()).toString()) == hospital.getId()) {
                                        final Servicio servicio = new Servicio();
                                        servicio.setId(Integer.parseInt(Objects.requireNonNull(dsUnidadMedica.getKey())));
                                        servicio.setNombre(Objects.requireNonNull(dsUnidadMedica.child("NOMBRE_UNIDAD").getValue()).toString());
                                        final Calendar c = Calendar.getInstance();
                                        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateformat = new SimpleDateFormat("MMMM dd yyyy");
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
                                                                                if (!isTomorrow(c.getTimeInMillis())) {
                                                                                    Horario horario = new Horario();
                                                                                    horario.setHospital(hospital);
                                                                                    horario.setServicio(servicio);
                                                                                    horario.setMedico(medico);
                                                                                    horario.setFecha(dateformat.format(fecha[0]));
                                                                                    horario.setHora(timeformat.format(hora[0]));
                                                                                    listHorarios.add(horario);
                                                                                    c.add(Calendar.HOUR, 2);
                                                                                    fecha[0] = c.getTime();
                                                                                    hora[0] = c.getTime();
                                                                                } else break;
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

    public boolean isTomorrow(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, +1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itmSearch) {
            createDialog().show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
