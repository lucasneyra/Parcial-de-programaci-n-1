package com.ecoride.service.strategy;

// Criterio climatico (tarifa base * minutos + $150 fijo).
public class CriterioClimatico implements CriterioTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        return (tarifaBase * minutos) + 150.0;
    }

    @Override
    public String getNombre() {
        return "Temporal Climático";
    }
}
