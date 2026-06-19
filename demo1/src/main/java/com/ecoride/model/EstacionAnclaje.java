package com.ecoride.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Esta es la estacion fisica Usamos HashMap para encontrar rapido los vehiculos con la patente
public class EstacionAnclaje {
    private String nombreUnico;
    private Map<String, Vehiculo> vehiculos;

    public EstacionAnclaje(String nombreUnico) {
        this.nombreUnico = nombreUnico;
        this.vehiculos = new HashMap<>();
    }

    // Aca buscamos rapido con la patente
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
    
    // Devolvemos todos los vehiculos que hay aca
    public Collection<Vehiculo> getVehiculos() { 
        return vehiculos.values(); 
    }
}