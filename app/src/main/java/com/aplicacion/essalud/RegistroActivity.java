package com.aplicacion.essalud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aplicacion.essalud.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ToggleGroup;
import androidx.fragment.app.DialogFragment;

import static com.aplicacion.essalud.methods.Methods.showSnackBar;

public class RegistroActivity extends AppCompatActivity {

    private final String DNI = "DNI";
    private final String CE = "CE";
    private final String[] DOCUMENTS_TYPE = new String[]{
            DNI, CE
    };

    private static final String[] MONTHS = new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo",
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
            "Noviembre", "Diciembre"
    };

    private final String URL = "http://aplicaciones007.jne.gob.pe/srop_publico/consulta/afiliado/GetNombresCiudadano?DNI=";
    String gender;
    private MaterialBetterSpinner spnDocumentType;
    private TextInputLayout tilDocumentNumber;
    private EditText edtDocumentNumber;
    private EditText edtFirstLastName;
    private EditText edtSecondLastName;
    private EditText edtNames;
    private TextInputLayout tilBirthDate;
    private MaterialButton btnBirthDate;
    static EditText edtBitrhDate;
    private ToggleGroup tggGender;
    private MaterialButton btnNext;
    private CheckBox chbTerms;
    // Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Llenado de datos del Spinner|DropDown|ComboBox para el tipo de documentos
        spnDocumentType = (MaterialBetterSpinner) findViewById(R.id.spnDocument);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DOCUMENTS_TYPE);
        spnDocumentType.setAdapter(adapter);
        // Declaración de controles
        tilDocumentNumber = (TextInputLayout) findViewById(R.id.tilDocumentNumber);
        edtDocumentNumber = (EditText) tilDocumentNumber.getEditText();
        edtFirstLastName = (EditText) ((TextInputLayout) findViewById(R.id.tilUserFirstLastName)).getEditText();
        edtSecondLastName = (EditText) ((TextInputLayout) findViewById(R.id.tilUserSecondLastName)).getEditText();
        edtNames = (EditText) ((TextInputLayout) findViewById(R.id.tilNames)).getEditText();
        tilBirthDate = (TextInputLayout) findViewById(R.id.tilBirthDate);
        edtBitrhDate = (EditText) tilBirthDate.getEditText();
        btnBirthDate = (MaterialButton) findViewById(R.id.btnBirthDate);
        tggGender = (ToggleGroup) findViewById(R.id.tggGender);
        chbTerms = (CheckBox) findViewById(R.id.chbTerms);
        btnNext = (MaterialButton) findViewById(R.id.btnNext);
        // Creación de instancia Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("USERS");
        // Declaración de datos de inicio de controles
        spnDocumentType.setText(adapter.getItem(0));
        tilDocumentNumber.setHelperText(getResources().getString(R.string.document_type_0));
        tilDocumentNumber.setCounterMaxLength(8);
        edtDocumentNumber.requestFocus();
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
        // Selección de Sexo

        tggGender.setOnCheckedChangeListener(new ToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ToggleGroup group, int[] checkedId) {
                switch (checkedId[0]) {
                    case R.id.tgbFemale:
                        gender = "F";
                        break;
                    case R.id.tgbMale:
                        gender = "M";
                        break;
                }
            }
        });
        // Consulta de datos mediante DNI
        edtDocumentNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userdni = edtDocumentNumber.getText().toString();
                if (spnDocumentType.getText().toString().equals(DNI) && userdni.length() == 8) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + userdni, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            String fullName = response.toString();
                            String firstLastName = fullName.split(Pattern.quote("|"))[0];
                            String secondLastName = fullName.split(Pattern.quote("|"))[1];
                            String names = fullName.split(Pattern.quote("|"))[2];
                            edtFirstLastName.setText(firstLastName);
                            edtSecondLastName.setText(secondLastName);
                            edtNames.setText(names);
                            edtBitrhDate.requestFocus();
                            showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Extracción exitosa", Snackbar.LENGTH_LONG));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Error", Snackbar.LENGTH_LONG));
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }
        });
        // Finalización de Registro
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validación de campos
                // ...
                // Obtención de datos ingresados
                String dni = null;
                String ce = null;
                String firstLastName;
                String secondLastName;
                String names;
                String birthdate;
                boolean acepptedTerms;
                if (spnDocumentType.getText().toString().equals(DNI))
                    dni = edtDocumentNumber.getText().toString();
                else
                    ce = edtDocumentNumber.getText().toString();
                firstLastName = edtFirstLastName.getText().toString();
                secondLastName = edtSecondLastName.getText().toString();
                names = edtNames.getText().toString();
                birthdate = edtBitrhDate.getText().toString();
                acepptedTerms = chbTerms.isChecked();
                // Uso de clase
                User user = new User(dni, ce, firstLastName, secondLastName, names, birthdate, gender);
                // Inserción a la base de datos
                if (acepptedTerms)
                    databaseReference.push().setValue(user);
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
            edtBitrhDate.setText(birthday);
        }
    }
}
