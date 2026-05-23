package com.ecoride.model;

public class UsuarioRegular extends Usuario {
    public UsuarioRegular(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public double calcularDescuento(double monto) {
        return monto; // No tiene descuento
    }
}