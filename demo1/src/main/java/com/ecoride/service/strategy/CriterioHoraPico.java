package com.ecoride.service.strategy;

// Criterio de hora pico (tarifa base * minutos + 40% recargo).
public class CriterioHoraPico implements CriterioTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        return (tarifaBase * minutos) * 1.40;
    }

    @Override
    public String getNombre() {
        return "Hora Pico";
    }
}
