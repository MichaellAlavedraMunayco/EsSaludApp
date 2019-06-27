package com.aplicacion.essalud.models;

public class Medico {

    private int id;
    private int fotoPerfil;
    private String nombre;

    public Medico() {
    }

    public Medico(int id, int fotoPerfil, String nombre) {
        this.id = id;
        this.fotoPerfil = fotoPerfil;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", fotoPerfil=" + fotoPerfil +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
