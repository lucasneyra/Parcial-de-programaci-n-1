package com.ecoride.service.strategy;

// Aca calculamos el precio comun multiplicamos la tarifa base por los minutos
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
