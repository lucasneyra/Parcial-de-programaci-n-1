package com.ecoride.model.state;

import com.ecoride.model.Vehiculo;

// Representa el vehiculo en movimiento, alquilado por el usuario.
public class EstadoEnViaje implements EstadoVehiculo {
    
    private static final EstadoEnViaje INSTANCE = new EstadoEnViaje();
    
    public static EstadoEnViaje getInstance() {
        return INSTANCE;
    }
    
    private EstadoEnViaje() {}

    @Override
    public void desbloquear(Vehiculo v) {
        throw new IllegalStateException("El vehiculo ya esta en viaje. No se puede volver a alquilar.");
    }

    @Override
    public void finalizarViaje(Vehiculo v) {
        v.setEstado(EstadoEnEspera.getInstance());
    }

    @Override
    public void enviarAReparacion(Vehiculo v) {
        throw new IllegalStateException("El vehiculo esta en viaje, no se puede mandar al taller directo.");
    }

    @Override
    public void finalizarReparacion(Vehiculo v) {
        throw new IllegalStateException("No se puede finalizar reparacion de un vehiculo que esta viajando.");
    }

    @Override
    public String getNombre() {
        return "En Viaje";
    }
}
