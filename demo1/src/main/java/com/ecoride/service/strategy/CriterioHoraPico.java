package com.ecoride.service.strategy;

// Si es hora pico cobramos un 40 por ciento mas de recargo
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
