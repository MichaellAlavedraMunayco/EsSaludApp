package com.aplicacion.essalud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.aplicacion.essalud.adapters.HorariosAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class HorariosActivity extends AppCompatActivity {

    ListView lvHorarios;
    MaterialButton mbFiltrarHorario;
    AutoCompleteTextView actvMedico;
    AutoCompleteTextView actvServicio;
    TextInputEditText tietFecha;
    MaterialButton btnFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        lvHorarios = (ListView) findViewById(R.id.lvHorarios);
        mbFiltrarHorario = (MaterialButton) findViewById(R.id.mbFiltrarHorario);
        // Modificación de ToolBar
        ((Toolbar) findViewById(R.id.myToolbar)).setTitle("Horarios");
        mbFiltrarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog().show();
            }
        });
        lvHorarios.setAdapter(new HorariosAdapter(this));
    }

    public AlertDialog createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HorariosActivity.this);
        builder.setTitle("Búsqueda de servicios");
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.filtro_horario, null);
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

}
