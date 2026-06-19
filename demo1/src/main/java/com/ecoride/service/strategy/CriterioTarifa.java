package com.ecoride.service.strategy;

// Interfaz para calcular cuanto sale el viaje Cambia segun el clima o la hora
public interface CriterioTarifa {
    double calcularCosto(double tarifaBase, int minutos);
    String getNombre();
}
