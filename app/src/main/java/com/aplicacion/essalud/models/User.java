package com.aplicacion.essalud.models;

public class User {

    private String dni;
    private String ce;
    private String firstLastName;
    private String secondLastName;
    private String names;
    private String birthdate;
    private String gender;

    public User() {
    }

    public User(String dni, String ce, String firstLastName, String secondLastName, String names, String birthdate, String gender) {
        this.dni = dni;
        this.ce = ce;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.names = names;
        this.birthdate = birthdate;
        this.gender = gender;
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

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
