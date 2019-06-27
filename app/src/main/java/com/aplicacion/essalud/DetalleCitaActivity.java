package com.aplicacion.essalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.aplicacion.essalud.adapters.DetalleCitaAdapter;
import com.aplicacion.essalud.models.ItemCita;
import com.aplicacion.essalud.models.database.LocalDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aplicacion.essalud.methods.Methods.decrypt;
import static com.aplicacion.essalud.methods.Methods.encrypt;

public class DetalleCitaActivity extends AppCompatActivity {

    TextView txvCodigoCita;
    CheckBox chbGoogleCalendar;
    CheckBox chbLocalCalendar;
    Button btnReservarCita;
    Button btnCancelar;
    ListView lvItemsDC;
    DetalleCitaAdapter detalleCitaAdapter;

    int HospitalId;
    String HospitalAddress;
    int ServicioId;
    String ServicioName;
    String Consultorio;
    int MedicoId;
    String MedicoName;
    ArrayList<String> Motivos;
    String Fecha;
    String Hora;

    FirebaseDatabase firebaseDatabase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);
        txvCodigoCita = (TextView) findViewById(R.id.txvCodigoCita);
        lvItemsDC = (ListView) findViewById(R.id.lvItemsDC);
        chbGoogleCalendar = (CheckBox) findViewById(R.id.chbGoogleCalendar);
        chbLocalCalendar = (CheckBox) findViewById(R.id.chbLocalCalendar);
        btnReservarCita = (Button) findViewById(R.id.btnReservarCita);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        detalleCitaAdapter = new DetalleCitaAdapter(this);
        lvItemsDC.setAdapter(detalleCitaAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        HospitalId = bundle.getInt("hospital_id");
        HospitalAddress = bundle.getString("hospital_address");
        ServicioId = bundle.getInt("servicio_id");
        ServicioName = bundle.getString("servicio_name");
        Consultorio = bundle.getString("consultorio");
        MedicoId = bundle.getInt("medico_id");
        MedicoName = bundle.getString("medico_name");
        Motivos = bundle.getStringArrayList("motivos");
        Fecha = bundle.getString("fecha");
        Hora = bundle.getString("hora");
        final int[] CitaId = {0};
        // CURRENT DATA
        firebaseDatabase.getReference("citas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CitaId[0] = (int) dataSnapshot.getChildrenCount() + 1;
                txvCodigoCita.setText("CITA N° " + CitaId[0]);
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_location_on_white_24dp, "Hospital", HospitalAddress));
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_favorite_blue_24dp, "Servicio", ServicioName));
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_nature_people_black_24dp, "Consultorio", Consultorio));
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_person_black_24dp, "Médico", MedicoName));
                StringBuilder strmotivos = new StringBuilder();
                for (int i = 0; i < Motivos.size(); i++) {
                    strmotivos.append(Motivos.get(i));
                    if (i != Motivos.size() - 1) {
                        strmotivos.append(", ");
                    }
                }
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_local_hospital_blue_24dp, "Motivos", TextUtils.isEmpty(strmotivos.toString()) ? "No registraste motivos" : strmotivos.toString()));
                detalleCitaAdapter.add(new ItemCita(R.drawable.ic_event_blue_24dp, "Fecha y Hora", Fecha + " a las " + Hora));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnReservarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences PREFERENCES = getSharedPreferences(LocalDB.PREFS_NAME, MODE_PRIVATE);
                final String PacienteId = decrypt(PREFERENCES.getString(encrypt(LocalDB.PREF_PACIENTE_ID), null));

                firebaseDatabase.getReference("citas").child(Integer.toString(CitaId[0]))
                        .setValue(new HashMap<String, String>() {{
                            put("FECHA", Fecha);
                            put("HORA", Hora);
                            put("CONSULTORIO", Consultorio);
                            put("MEDICO_ID", Integer.toString(MedicoId));
                            put("PACIENTE_ID", PacienteId);
                        }});
                for (final String motivo: Motivos) {
                    firebaseDatabase.getReference("motivos").push()
                            .setValue(new HashMap<String, String>() {{
                                put("MOTIVO", motivo);
                                put("CITA_ID", Integer.toString(CitaId[0]));
                                put("PACIENTE_ID", PacienteId);
                            }});
                }
            }
        });
    }
}
