package com.ecoride.service.comparator;

import com.ecoride.model.Vehiculo;
import java.util.Comparator;

// Comparador alternativo externo por costo base de tarifa, de mayor a menor.
public class ComparadorPorCostoBase implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Double.compare(v2.getTarifaBase(), v1.getTarifaBase());
    }
}
