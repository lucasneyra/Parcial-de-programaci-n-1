package com.ecoride.model;

import java.util.ArrayList;
import java.util.List;

public class EstacionAnclaje {
    private String nombreUnico;
    private List<Vehiculo> vehiculos;

    public EstacionAnclaje(String nombreUnico) {
        this.nombreUnico = nombreUnico;
        this.vehiculos = new ArrayList<>();
    }

    // Este es el método que te marcaba en rojo
    public Vehiculo buscarVehiculoPorPatente(String patente) {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) {
                return v;
            }
        }
        return null;
    }

    public String getNombreUnico() { return nombreUnico; }
    public List<Vehiculo> getVehiculos() { return vehiculos; }
}