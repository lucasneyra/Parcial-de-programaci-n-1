package com.ecoride.factory;

public class TarjetaCredito implements ProcesadorPago {
    @Override
    public void efectuarCobro(double monto) {
        // Texto calcado de la consigna para simular el éxito
        System.out.println("Cobro exitoso de $" + String.format(java.util.Locale.US, "%.2f", monto) + " realizado con Tarjeta de Crédito");
    }
}