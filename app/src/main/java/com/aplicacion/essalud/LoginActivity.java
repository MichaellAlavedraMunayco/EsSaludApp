package com.aplicacion.essalud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

import static com.aplicacion.essalud.methods.Methods.encrypt;
import static com.aplicacion.essalud.methods.Methods.showSnackBar;

public class LoginActivity extends AppCompatActivity {

    // Variables de selección
    private final String DNI = "DNI";
    private final String CE = "Carnet de Extranjería";
    // Controles
    private MaterialSpinner msDocumentType;
    private TextInputLayout tilDocumentNumber;
    private TextInputEditText tietDocumentNumber;
    private TextInputLayout tilPassword;
    private TextInputEditText tietPassword;
    private CheckBox chbRemember;
    private MaterialButton mbtnLogin;
    // Preferencias
    private SharedPreferences PREFERENCES;
    private SharedPreferences.Editor EDITOR;
    public static final String PREFS_NAME = "USERDATA";
    private static final String PREF_DOCUMENT_NUMBER_DNI = "document_number_ce";
    private static final String PREF_DOCUMENT_NUMBER_CE = "document_number_dni";
    private static final String PREF_PASSWORD = "password";
    // Firebase
    FirebaseDatabase fdEsSaludBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Validar si existe autenticación
        PREFERENCES = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String document_number_ce = PREFERENCES.getString(encrypt(PREF_DOCUMENT_NUMBER_CE), null);
        String document_number_dni = PREFERENCES.getString(encrypt(PREF_DOCUMENT_NUMBER_DNI), null);
        String password = PREFERENCES.getString(encrypt(PREF_PASSWORD), null);
        if (document_number_ce != null && document_number_dni != null && password != null)
            InitChatBotActivity();
        // Declaración de controles
        msDocumentType = (MaterialSpinner) findViewById(R.id.msDocumentType);
        tilDocumentNumber = (TextInputLayout) findViewById(R.id.tilDocumentNumber);
        tietDocumentNumber = (TextInputEditText) findViewById(R.id.tietDocumentNumber);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        tietPassword = (TextInputEditText) findViewById(R.id.tietPassword);
        chbRemember = (CheckBox) findViewById(R.id.chbRemember);
        mbtnLogin = (MaterialButton) findViewById(R.id.mbtnLogin);
        // Modificación de ToolBar
        ((Toolbar) findViewById(R.id.myToolbar)).setTitle("Autenticación EsSalud");
        // Llenado de variables de selección a msDocumentType
        msDocumentType.setItems(DNI, CE);
        msDocumentType.setSelectedIndex(0);
        tilDocumentNumber.setHelperText(getResources().getString(R.string.document_type_0));
        tilDocumentNumber.setCounterMaxLength(8);
        // Crear instancia de base de datos
        fdEsSaludBD = FirebaseDatabase.getInstance();
        // Funcionalidad de selección de item en mbsDocumentType
        msDocumentType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position) {
                    case 0: // DNI
                        tilDocumentNumber.setHelperText(DNI);
                        tilDocumentNumber.setCounterMaxLength(8);
                        break;
                    case 1: // CE
                        tilDocumentNumber.setHelperText(CE);
                        tilDocumentNumber.setCounterMaxLength(9);
                        break;
                }
            }
        });
        // Inicio de sesión
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener credenciales ingresadas
                String documentType = msDocumentType.getText().toString();
                final String documentNumber = Objects.requireNonNull(tietDocumentNumber.getText()).toString();
                final String password = Objects.requireNonNull(tietPassword.getText()).toString();
                final boolean remember = chbRemember.isChecked();
                // Validar credenciales
                clearControls();
                if (TextUtils.isEmpty(documentNumber)) {
                    tilDocumentNumber.setError("Ingresa tu número de " + msDocumentType.getText());
                    return;
                } else if (documentNumber.length() != tilDocumentNumber.getCounterMaxLength()) {
                    tilDocumentNumber.setError("Cantidad de dígitos incorrectos");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    tilPassword.setError("Ingresa tu contraseña");
                    return;
                }
                // Filtrar en la BD
                DatabaseReference refUsuarios = fdEsSaludBD.getReference("usuarios");
                refUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dsUsuarios) {
                        for (DataSnapshot sUsuario : dsUsuarios.getChildren()) {
                            String bdDocumentNumberDNI = Objects.requireNonNull(sUsuario.child("dni").getValue()).toString();
                            String bdDocumentNumberCE = Objects.requireNonNull(sUsuario.child("ce").getValue()).toString();
                            String bdPassword = Objects.requireNonNull(sUsuario.child("password").getValue()).toString();
                            if ((bdDocumentNumberCE.equals(documentNumber)
                                    || bdDocumentNumberDNI.equals(documentNumber))
                                    && bdPassword.equals(password)) {
                                if (remember) {
                                    EDITOR = PREFERENCES.edit();
                                    EDITOR.putString(encrypt(PREF_DOCUMENT_NUMBER_DNI), encrypt(bdDocumentNumberDNI))
                                            .putString(encrypt(PREF_DOCUMENT_NUMBER_CE), encrypt(bdDocumentNumberCE))
                                            .putString(encrypt(PREF_PASSWORD), encrypt(bdPassword))
                                            .commit();
                                }
                                InitChatBotActivity();
                                return;
                            }
                        }
                        showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Credenciales incorrectas", Snackbar.LENGTH_LONG));
                        tietDocumentNumber.getText().clear();
                        tietPassword.getText().clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void InitChatBotActivity() {
        startActivity(new Intent(this, TestChatBotActivity.class));
    }

    // Limpiar errores de controles
    private void clearControls() {
        tilDocumentNumber.setError("");
        tilPassword.setError("");
    }
}
