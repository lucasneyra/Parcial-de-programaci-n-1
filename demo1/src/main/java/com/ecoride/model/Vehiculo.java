
package com.ecoride.model;

import com.ecoride.model.state.EstadoVehiculo;
import com.ecoride.model.state.EstadoEnEspera;

public abstract class Vehiculo implements Comparable<Vehiculo> {
    private String patente;
    private int porcentajeBateria;
    private double tarifaBase;
    private EstadoVehiculo estado;
    private EstacionAnclaje estacionActual;

    public Vehiculo(String patente, int porcentajeBateria, double tarifaBase) {
        this.patente = patente;
        this.porcentajeBateria = porcentajeBateria;
        this.tarifaBase = tarifaBase;
        // Estado inicial por defecto en espera
        this.estado = EstadoEnEspera.getInstance();
    }

    // Orden natural por bateria de menor a mayor para prioridad de carga
    @Override
    public int compareTo(Vehiculo otro) {
        return Integer.compare(this.porcentajeBateria, otro.porcentajeBateria);
    }

    public String getPatente() { return patente; }
    public int getPorcentajeBateria() { return porcentajeBateria; }
    
    // Necesitamos setter para bateria si se actualiza despues de un viaje
    public void setPorcentajeBateria(int porcentajeBateria) { this.porcentajeBateria = porcentajeBateria; }
    
    public double getTarifaBase() { return tarifaBase; }

    public EstadoVehiculo getEstado() { return estado; }
    public void setEstado(EstadoVehiculo estado) { this.estado = estado; }

    public EstacionAnclaje getEstacionActual() { return estacionActual; }
    public void setEstacionActual(EstacionAnclaje estacionActual) { this.estacionActual = estacionActual; }
}

