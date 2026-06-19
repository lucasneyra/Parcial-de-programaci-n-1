package com.ecoride.model;

public class UsuarioPremium extends Usuario {
    private double descuento; // 015 para el 15 por ciento

    public UsuarioPremium(String id, String nombre, double descuento) {
        super(id, nombre);
        this.descuento = descuento;
    }

    @Override
    public double calcularMonto(double monto) {
        return monto - (monto * descuento); // Restamos el descuento correspondiente
    }
}