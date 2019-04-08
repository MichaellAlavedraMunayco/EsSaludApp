package com.aplicacion.essalud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import static android.app.DatePickerDialog.*;
import static com.aplicacion.essalud.methods.Methods.showSnackBar;

public class RegistroActivity extends AppCompatActivity {

    private static final String[] DOCUMENTS_TYPE = new String[]{
            "DNI", "CE"
    };

    private static final String[] MONTHS = new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo",
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
            "Noviembre", "Diciembre"
    };

    private MaterialBetterSpinner spnDocumentType;
    private TextInputLayout tilDocumentNumber;
    static TextInputLayout tilBirthDate;
    private MaterialButton btnBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Llenado de datos del Spinner|DropDown|ComboBox para el tipo de documentos
        spnDocumentType = (MaterialBetterSpinner) findViewById(R.id.spnDocument);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DOCUMENTS_TYPE);
        spnDocumentType.setAdapter(adapter);
        // Autoselección de inicio: DNI por defecto
        spnDocumentType.setText(adapter.getItem(0));
        // Declaración de controles
        tilDocumentNumber = (TextInputLayout) findViewById(R.id.tilDocumentNumber);
        tilBirthDate = (TextInputLayout) findViewById(R.id.tilBirthDate);
        btnBirthDate = (MaterialButton) findViewById(R.id.btnBirthDate);
        // Declaración de datos de inicio de controles
        tilDocumentNumber.setHelperText(getResources().getString(R.string.document_type_0));
        tilDocumentNumber.setCounterMaxLength(8);
        // Ejecución de acciones cuando se seleccionan items del Spinner
        spnDocumentType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Evento que se ejecuta luego de seleccionar un item del Spinner
                int itemSelectedPosition = adapter.getPosition(s.toString());
                switch (itemSelectedPosition) {
                    case 0: // Para DNI
                        tilDocumentNumber.setHelperText(getResources().getString(R.string.document_type_0));
                        tilDocumentNumber.setCounterMaxLength(8);
                        break;
                    case 1: // Para CE
                        tilDocumentNumber.setHelperText(getResources().getString(R.string.document_type_1));
                        tilDocumentNumber.setCounterMaxLength(9);
                        break;
                }
                showSnackBar(Snackbar.make(findViewById(android.R.id.content), s.toString(), Snackbar.LENGTH_LONG));
            }
        });
        // Ingreso fecha de nacimiento
        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String birthday = MONTHS[month] + " " + day + ", " + year;
            Objects.requireNonNull(tilBirthDate.getEditText()).setText(birthday);
        }
    }
}
