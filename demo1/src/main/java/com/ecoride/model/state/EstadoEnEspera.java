package com.ecoride.model.state;

import com.ecoride.model.Vehiculo;

// Representa el vehiculo estacionado en la base listo para alquilar
public class EstadoEnEspera implements EstadoVehiculo {
    
    private static final EstadoEnEspera INSTANCE = new EstadoEnEspera();
    
    public static EstadoEnEspera getInstance() {
        return INSTANCE;
    }
    
    private EstadoEnEspera() {}

    @Override
    public void desbloquear(Vehiculo v) {
        v.setEstado(EstadoEnViaje.getInstance());
    }

    @Override
    public void finalizarViaje(Vehiculo v) {
        throw new IllegalStateException("No se puede finalizar viaje porque el vehiculo no esta en viaje.");
    }

    @Override
    public void enviarAReparacion(Vehiculo v) {
        v.setEstado(EstadoEnReparacion.getInstance());
    }

    @Override
    public void finalizarReparacion(Vehiculo v) {
        throw new IllegalStateException("El vehiculo no esta en reparacion en este momento.");
    }

    @Override
    public String getNombre() {
        return "En Espera";
    }
}
