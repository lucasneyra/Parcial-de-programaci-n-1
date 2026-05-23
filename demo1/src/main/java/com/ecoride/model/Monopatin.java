package com.ecoride.model;

public class Monopatin extends Vehiculo {
    private boolean amortiguacion;

    public Monopatin(String patente, int bateria, double tarifaBase, boolean amortiguacion) {
        super(patente, bateria, tarifaBase);
        this.amortiguacion = amortiguacion;
    }

    public boolean isAmortiguacion() { return amortiguacion; }
}