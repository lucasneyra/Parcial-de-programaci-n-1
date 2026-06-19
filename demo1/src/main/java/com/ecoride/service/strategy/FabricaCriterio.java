package com.ecoride.service.strategy;

// Aca elegimos que cobro usar segun el texto que nos manden
public class FabricaCriterio {

    public static CriterioTarifa obtenerCriterio(String tipo) {
        if (tipo == null) {
            return new CriterioEstandar();
        }
        switch (tipo.toUpperCase()) {
            case "ESTANDAR":
                return new CriterioEstandar();
            case "HORA_PICO":
                return new CriterioHoraPico();
            case "CLIMATICO":
                return new CriterioClimatico();
            default:
                throw new IllegalArgumentException("Criterio de facturacion no soportado: " + tipo);
        }
    }
}
