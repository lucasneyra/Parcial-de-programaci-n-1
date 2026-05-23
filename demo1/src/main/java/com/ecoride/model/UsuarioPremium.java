package com.ecoride.model;

public class UsuarioPremium extends Usuario {
    private double descuento; // Ej: 0.15 para el 15%

    public UsuarioPremium(String id, String nombre, double descuento) {
        super(id, nombre);
        this.descuento = descuento;
    }

    @Override
    public double calcularDescuento(double monto) {
        return monto - (monto * descuento); // Restamos el descuento correspondiente
    }
}