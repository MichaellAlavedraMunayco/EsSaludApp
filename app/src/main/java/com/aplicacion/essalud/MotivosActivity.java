package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aplicacion.essalud.adapters.MotivosAdapter;
import com.aplicacion.essalud.models.Motivo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import static com.aplicacion.essalud.methods.Methods.showSnackBar;

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
                if(TextUtils.isEmpty(motivo)){
                    tilMotivo.setError("Ingresa un motivo");
                } else {
                    motivosAdapter.add(new Motivo(motivo));
                }
            }
        });
        btnRegistrarMotivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Motivo> listMotivos = motivosAdapter.getList();
                if(listMotivos.size() > 0){
                    DatabaseReference dbr = firebaseDatabase.getReference("motivos");
                    for (Motivo motivo: listMotivos) {
                        // Añadir motivo a firebase database
                    }
                }
            }
        });
    }
}
