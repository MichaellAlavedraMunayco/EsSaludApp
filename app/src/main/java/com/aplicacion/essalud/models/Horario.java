package com.aplicacion.essalud.models;

public class Horario {

    private Hospital hospital;
    private Servicio servicio;
    private Medico medico;
    private String fecha;
    private String hora;

    public Horario() {
    }

    public Horario(Hospital hospital, Servicio servicio, Medico medico, String fecha, String hora) {
        this.hospital = hospital;
        this.servicio = servicio;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "servicio=" + servicio +
                ", medico=" + medico +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
