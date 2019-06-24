package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aplicacion.essalud.adapters.MotivosAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MotivosActivity extends AppCompatActivity {

    TextInputLayout tilMotivo;
    TextInputEditText tietMotivo;
    ListView lvMotivos;
    Button btnAgregarMotivo;
    Button btnRegistrarMotivos;

    MotivosAdapter motivosAdapter;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivos);
        // Modificación de ToolBar
        ((Toolbar) findViewById(R.id.myToolbar)).setTitle("Motivos de la Cita");
        tilMotivo = (TextInputLayout) findViewById(R.id.tilMotivo);
        tietMotivo = (TextInputEditText) findViewById(R.id.tietMotivo);
        lvMotivos = (ListView) findViewById(R.id.lvMotivos);
        btnAgregarMotivo = (Button) findViewById(R.id.btnAgregarMotivo);
        btnRegistrarMotivos = (Button) findViewById(R.id.btnRegistrarMotivos);
        firebaseDatabase = FirebaseDatabase.getInstance();
        motivosAdapter = new MotivosAdapter(this);
        lvMotivos.setAdapter(motivosAdapter);
        btnAgregarMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motivo = Objects.requireNonNull(tietMotivo.getText()).toString();
                if (TextUtils.isEmpty(motivo)) {
                    tilMotivo.setError("Ingresa un motivo");
                } else {
                    motivosAdapter.add(motivo);
                    tietMotivo.getText().clear();
                }
            }
        });
        btnRegistrarMotivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MotivosActivity.this, DetalleCitaActivity.class);
                i.putStringArrayListExtra("motivos", (ArrayList<String>) motivosAdapter.getList());
                i.putExtras(Objects.requireNonNull(getIntent().getExtras()));
//                i.putExtra("fecha", getIntent().getStringExtra("fecha"));
//                i.putExtra("hora", getIntent().getStringExtra("hora"));
//                i.putExtra("consultorio", getIntent().getStringExtra("consultorio"));
//                i.putExtra("medico_id", getIntent().getStringExtra("medico_id"));
//                i.putExtra("medico_name", horario.getMedico().getNombre());
//                i.putExtra("servicio_id", horario.getServicio().getId());
//                i.putExtra("servicio_name", horario.getServicio().getNombre());
                startActivity(i);
            }
        });
    }
}
