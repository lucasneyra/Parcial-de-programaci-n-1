package com.ecoride.factory;

public class TarjetaCredito implements ProcesadorPago {
    @Override
    public void procesarCobro(double monto) {
        // Texto calcado de la consigna para simular el éxito
        System.out.println("Cobro exitoso de $" + monto + " realizado con Tarjeta de Crédito");
    }
}