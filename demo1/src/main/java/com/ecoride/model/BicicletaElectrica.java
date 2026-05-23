package com.ecoride.model;

public class BicicletaElectrica extends Vehiculo {
    private int canastoCC;

    public BicicletaElectrica(String patente, int bateria, double tarifaBase, int canastoCC) {
        super(patente, bateria, tarifaBase);
        this.canastoCC = canastoCC;
    }

    public int getCanastoCC() { return canastoCC; }
}