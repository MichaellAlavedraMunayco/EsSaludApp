package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class DetalleCitaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Log.e("APPDATA",
                bundle.getString("fecha") + ", " +
                        bundle.getString("hora") + ", " +
                        bundle.getString("consultorio") + ", " +
                        bundle.getInt("medico_id") + ", " +
                        bundle.getString("medico_name") + ", " +
                        bundle.getInt("servicio_id") + ", " +
                        bundle.getString("servicio_name") + ", " +
                        bundle.getStringArrayList("motivos"));
    }
}
