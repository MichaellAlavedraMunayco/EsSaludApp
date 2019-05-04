package com.aplicacion.essalud.models;

public class Horario {

    int srcIcon;
    Medico medico;
    Servicio servicio;
    String fecha;
    String hora;

    public Horario() {
    }

    public Horario(int srcIcon, Medico medico, Servicio servicio, String fecha, String hora) {
        this.srcIcon = srcIcon;
        this.medico = medico;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getSrcIcon() {
        return srcIcon;
    }

    public void setSrcIcon(int srcIcon) {
        this.srcIcon = srcIcon;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
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
}
