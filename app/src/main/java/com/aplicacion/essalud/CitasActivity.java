package com.aplicacion.essalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aplicacion.essalud.adapters.CitasAdapter;
import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.Medico;
import com.aplicacion.essalud.models.Servicio;
import com.aplicacion.essalud.models.database.LocalDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.aplicacion.essalud.methods.Methods.decrypt;
import static com.aplicacion.essalud.methods.Methods.encrypt;

public class CitasActivity extends AppCompatActivity {

    ListView lvCitas;
    CitasAdapter citasAdapter;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        // Modificación de ToolBar
        ((Toolbar) findViewById(R.id.myToolbar)).setTitle("Citas");
        lvCitas = (ListView) findViewById(R.id.lvCitas);
        citasAdapter = new CitasAdapter(this);
        lvCitas.setAdapter(citasAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        LoadCitas();
    }

    public void LoadCitas() {
        SharedPreferences PREFERENCES = getSharedPreferences(LocalDB.PREFS_NAME, MODE_PRIVATE);
        final String PacienteId = decrypt(PREFERENCES.getString(encrypt(LocalDB.PREF_PACIENTE_ID), null));
        firebaseDatabase.getReference("citas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dsCitas) {
                for (DataSnapshot dsCita : dsCitas.getChildren()) {
                    String bd_paciente_id = Objects.requireNonNull(dsCita.child("PACIENTE_ID").getValue()).toString();
                    final String bd_medico_id = Objects.requireNonNull(dsCita.child("MEDICO_ID").getValue()).toString();
                    String bd_fecha = Objects.requireNonNull(dsCita.child("FECHA").getValue()).toString();
                    String bd_hora = Objects.requireNonNull(dsCita.child("HORA").getValue()).toString();
                    Date fechahora = null;
                    try {
                        fechahora = StringToDate(bd_fecha.concat(" ").concat(bd_hora));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (!isRestToDay(fechahora)) continue;
                    if (bd_paciente_id.equals(PacienteId)) {
                        final Horario horario = new Horario();
                        horario.setFecha(bd_fecha);
                        horario.setHora(bd_hora);
                        final Servicio servicio = new Servicio();
                        firebaseDatabase.getReference("medicos").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dsMedicos) {
                                for (DataSnapshot dsMedico : dsMedicos.getChildren()) {
                                    String ds_medico_key = dsMedico.getKey();
                                    if (bd_medico_id.equals(ds_medico_key)) {
                                        final String bd_especialidad_id = Objects.requireNonNull(dsMedico.child("ESPECIALIDAD_ID").getValue()).toString();
                                        final String bd_persona_id = Objects.requireNonNull(dsMedico.child("PERSONA_ID").getValue()).toString();
                                        firebaseDatabase.getReference("especialidades").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dsEspecialidades) {
                                                for (DataSnapshot dsEspecialidad : dsEspecialidades.getChildren()) {
                                                    String bd_especialidad_key = dsEspecialidad.getKey();
                                                    assert bd_especialidad_key != null;
                                                    if (bd_especialidad_key.equals(bd_especialidad_id)) {
                                                        String bd_especialidad_nombre = Objects.requireNonNull(dsEspecialidad.child("NOMBRE").getValue()).toString();
                                                        servicio.setNombre(bd_especialidad_nombre);
                                                        horario.setServicio(servicio);
                                                        firebaseDatabase.getReference("personas").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dsPersonas) {
                                                                for (DataSnapshot dsPersona : dsPersonas.getChildren()) {
                                                                    String bd_persona_key = dsPersona.getKey();
                                                                    assert bd_persona_key != null;
                                                                    if (bd_persona_key.equals(bd_persona_id)) {
                                                                        String bd_medico_name = Objects.requireNonNull(dsPersona.child("NOMBRE").getValue()).toString();
                                                                        String bd_medico_lastname = Objects.requireNonNull(dsPersona.child("APELLIDO").getValue()).toString();
                                                                        Medico medico = new Medico();
                                                                        medico.setNombre(bd_medico_lastname.concat(" ").concat(bd_medico_name));
                                                                        horario.setMedico(medico);
                                                                        citasAdapter.add(horario);
                                                                        break;
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
                                        break;
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

    @SuppressLint("SimpleDateFormat")
    public Date StringToDate(String string) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy hh:mm:ss aa");
        return format.parse(string);
    }

    public boolean isRestToDay(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(date);
        return (now.getTimeInMillis() <= cdate.getTimeInMillis());
    }
}
