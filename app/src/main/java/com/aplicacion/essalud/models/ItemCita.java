package com.aplicacion.essalud.models;

public class ItemCita {

    int Icon;
    String Nombre;
    String Descripcion;

    public ItemCita() {
    }

    public ItemCita(int icon, String nombre, String descripcion) {
        Icon = icon;
        Nombre = nombre;
        Descripcion = descripcion;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}

