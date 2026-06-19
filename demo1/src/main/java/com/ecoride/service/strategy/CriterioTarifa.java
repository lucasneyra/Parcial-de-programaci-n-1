package com.ecoride.service.strategy;

// Estrategia de facturacion dinamica para cambiar el costo del viaje en caliente.
public interface CriterioTarifa {
    double calcularCosto(double tarifaBase, int minutos);
    String getNombre();
}
