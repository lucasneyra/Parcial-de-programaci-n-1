package com.ecoride.factory;

public class BilleteraVirtual implements ProcesadorPago {
    @Override
    public void procesarCobro(double monto) {
        System.out.println("Cobro exitoso de $" + monto + " realizado con Billetera Virtual");
    }
}