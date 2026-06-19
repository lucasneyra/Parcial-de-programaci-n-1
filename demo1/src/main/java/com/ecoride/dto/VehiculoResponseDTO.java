package com.ecoride.dto;

// Clase para mandarle los datos del vehiculo a la aplicacion de afuera
public class VehiculoResponseDTO {
    private String patente;
    private int porcentajeBateria;
    private double tarifaBase;
    private String tipo;
    private String faseActual;

    public VehiculoResponseDTO() {}

    public VehiculoResponseDTO(String patente, int porcentajeBateria, double tarifaBase, String tipo, String faseActual) {
        this.patente = patente;
        this.porcentajeBateria = porcentajeBateria;
        this.tarifaBase = tarifaBase;
        this.tipo = tipo;
        this.faseActual = faseActual;
    }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public int getPorcentajeBateria() { return porcentajeBateria; }
    public void setPorcentajeBateria(int porcentajeBateria) { this.porcentajeBateria = porcentajeBateria; }

    public double getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(double tarifaBase) { this.tarifaBase = tarifaBase; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFaseActual() { return faseActual; }
    public void setFaseActual(String faseActual) { this.faseActual = faseActual; }
}
