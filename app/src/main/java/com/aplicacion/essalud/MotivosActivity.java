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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MotivosActivity extends AppCompatActivity {

    TextInputLayout tilMotivo;
    TextInputEditText tietMotivo;
    ListView lvMotivos;
    Button btnAgregarMotivo;
    Button btnRegistrarMotivos;
    Button btnOmitir;

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
        btnOmitir = (Button) findViewById(R.id.btnOmitir);
        firebaseDatabase = FirebaseDatabase.getInstance();
        motivosAdapter = new MotivosAdapter(this);
        lvMotivos.setAdapter(motivosAdapter);
        btnAgregarMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilMotivo.setError("");
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
                GoToNextActivity();
            }
        });
        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToNextActivity();
            }
        });
    }

    public void GoToNextActivity() {
        Intent i = new Intent(MotivosActivity.this, DetalleCitaActivity.class);
        i.putStringArrayListExtra("motivos", (ArrayList<String>) motivosAdapter.getList());
        i.putExtras(Objects.requireNonNull(getIntent().getExtras()));
        startActivity(i);
    }
}
