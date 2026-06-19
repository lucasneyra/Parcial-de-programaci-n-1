package com.ecoride.service.strategy;

// Criterio estandar de facturacion (tarifa base * minutos).
public class CriterioEstandar implements CriterioTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        return tarifaBase * minutos;
    }

    @Override
    public String getNombre() {
        return "Estándar";
    }
}
