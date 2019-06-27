package com.aplicacion.essalud.models;

public class Hospital {

    private int id;
    private String direccion;

    public Hospital() {
    }

    public Hospital(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
