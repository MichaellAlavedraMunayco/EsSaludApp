package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.aplicacion.essalud.adapters.HorariosAdapter;

public class HorariosActivity extends AppCompatActivity {

    ListView lvHorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        lvHorarios = (ListView) findViewById(R.id.lvHorarios);
        lvHorarios.setAdapter(new HorariosAdapter(this));
    }
}
