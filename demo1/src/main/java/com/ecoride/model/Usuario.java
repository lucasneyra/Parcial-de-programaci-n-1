package com.ecoride.model;

public abstract class Usuario {
    private String id;
    private String nombre;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public abstract double calcularMonto(double monto);

    public String getId() { return id; }
    public String getNombre() { return nombre; }
}