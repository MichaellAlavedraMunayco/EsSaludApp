package com.aplicacion.essalud.models;

public class Usuario {

    private String dni;
    private String ce;
    private String password;

    public Usuario() {
    }

    public Usuario(String dni, String ce, String password) {
        this.dni = dni;
        this.ce = ce;
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
