package com.aplicacion.essalud.models.database;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.aplicacion.essalud.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {

    private final String refConsultorios = "consultorios";
    private final String refEspecialidades = "especialidades";
    private final String refHospitales = "hospitales";
    private final String refMedicos= "medicos";
    private final String refPacientes= "pacientes";
    private final String refPersonas = "personas";
    private final String refUnidadesMedicas = "unidades_medicas";
    private final String refUsuarios = "usuarios";

    private Map<String, DatabaseReference> referencias;
    public FirebaseDatabase firebaseDatabase;


    @SuppressLint("UseSparseArrays")
    public FirebaseDB(){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.referencias = new HashMap<>();
        this.referencias.put(this.refConsultorios, firebaseDatabase.getReference(this.refConsultorios));
        this.referencias.put(this.refEspecialidades, firebaseDatabase.getReference(this.refEspecialidades));
        this.referencias.put(this.refHospitales, firebaseDatabase.getReference(this.refHospitales));
        this.referencias.put(this.refMedicos, firebaseDatabase.getReference(this.refMedicos));
        this.referencias.put(this.refPacientes, firebaseDatabase.getReference(this.refPacientes));
        this.referencias.put(this.refPersonas, firebaseDatabase.getReference(this.refPersonas));
        this.referencias.put(this.refUnidadesMedicas, firebaseDatabase.getReference(this.refUnidadesMedicas));
        this.referencias.put(this.refUsuarios, firebaseDatabase.getReference(this.refUsuarios));
    }

    public DatabaseReference getReferencia(String nombreReferencia){
        return referencias.get(nombreReferencia);
    }

    public boolean validarLogin(final Usuario usuario){
        getReferencia(this.refUsuarios).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dsUsuarios) {
                for (DataSnapshot dsUsuario : dsUsuarios.getChildren()) {
                    String ce = dsUsuario.child("ce").toString();
                    String dni = dsUsuario.child("dni").toString();
                    String password = dsUsuario.child("password").toString();
                    if((usuario.getCe().equals(ce) || usuario.getDni().equals(dni))
                            && usuario.getPassword().equals(password)){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return false;
    }
}
