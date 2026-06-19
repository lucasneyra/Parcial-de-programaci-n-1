package com.ecoride.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Representa la estacion fisica. Migrada de List a HashMap para busquedas O(1) internas.
public class EstacionAnclaje {
    private String nombreUnico;
    private Map<String, Vehiculo> vehiculos;

    public EstacionAnclaje(String nombreUnico) {
        this.nombreUnico = nombreUnico;
        this.vehiculos = new HashMap<>();
    }

    // Busqueda O(1) optimizada
    public Vehiculo buscarVehiculoPorPatente(String patente) {
        if (patente == null) return null;
        return vehiculos.get(patente);
    }

    public void registrarVehiculo(Vehiculo v) {
        if (v != null) {
            vehiculos.put(v.getPatente(), v);
        }
    }

    public void eliminarVehiculo(Vehiculo v) {
        if (v != null) {
            vehiculos.remove(v.getPatente());
        }
    }

    public String getNombreUnico() { return nombreUnico; }
    
    // Devuelve los vehiculos disponibles como coleccion de solo lectura
    public Collection<Vehiculo> getVehiculos() { 
        return vehiculos.values(); 
    }
}