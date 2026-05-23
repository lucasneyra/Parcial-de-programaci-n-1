
package com.ecoride.model;

public abstract class Vehiculo {
    private String patente;
    private int porcentajeBateria;
    private double tarifaBase;

    public Vehiculo(String patente, int porcentajeBateria, double tarifaBase) {
        this.patente = patente;
        this.porcentajeBateria = porcentajeBateria;
        this.tarifaBase = tarifaBase;
    }

    public String getPatente() { return patente; }
    public int getPorcentajeBateria() { return porcentajeBateria; }
    public double getTarifaBase() { return tarifaBase; }
}

