package com.ecoride.service.strategy;

// Cobramos un extra de 150 pesos si el dia esta feo
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
