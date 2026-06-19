package com.ecoride.dto;

// Clase para mandarle los datos del alquiler a la aplicacion de afuera
public class AlquilerResponseDTO {
    private String patente;
    private String faseActual;
    private double costoCalculado;
    private int tiempoTranscurrido;
    private String mensaje;

    public AlquilerResponseDTO() {}

    public AlquilerResponseDTO(String patente, String faseActual, double costoCalculado, int tiempoTranscurrido, String mensaje) {
        this.patente = patente;
        this.faseActual = faseActual;
        this.costoCalculado = costoCalculado;
        this.tiempoTranscurrido = tiempoTranscurrido;
        this.mensaje = mensaje;
    }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getFaseActual() { return faseActual; }
    public void setFaseActual(String faseActual) { this.faseActual = faseActual; }

    public double getCostoCalculado() { return costoCalculado; }
    public void setCostoCalculado(double costoCalculado) { this.costoCalculado = costoCalculado; }

    public int getTiempoTranscurrido() { return tiempoTranscurrido; }
    public void setTiempoTranscurrido(int tiempoTranscurrido) { this.tiempoTranscurrido = tiempoTranscurrido; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
