package com.ecoride.factory;

public class BilleteraVirtual implements ProcesadorPago {
    @Override
    public void efectuarCobro(double monto) {
        System.out.println("Cobro exitoso de $" + String.format(java.util.Locale.US, "%.2f", monto) + " realizado con Billetera Virtual");
    }
}