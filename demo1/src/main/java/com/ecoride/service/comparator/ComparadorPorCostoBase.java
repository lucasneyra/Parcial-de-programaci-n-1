package com.ecoride.service.comparator;

import com.ecoride.model.Vehiculo;
import java.util.Comparator;

// Clase para ordenar los vehiculos segun la tarifa base de mas caro a mas barato
public class ComparadorPorCostoBase implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Double.compare(v2.getTarifaBase(), v1.getTarifaBase());
    }
}
