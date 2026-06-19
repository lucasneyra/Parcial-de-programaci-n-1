package com.ecoride.model.state;

import com.ecoride.model.Vehiculo;

// Representa el vehiculo en el taller mecanico. No se puede usar.
public class EstadoEnReparacion implements EstadoVehiculo {
    
    private static final EstadoEnReparacion INSTANCE = new EstadoEnReparacion();
    
    public static EstadoEnReparacion getInstance() {
        return INSTANCE;
    }
    
    private EstadoEnReparacion() {}

    @Override
    public void desbloquear(Vehiculo v) {
        throw new IllegalStateException("Bloqueado: El vehiculo esta en el taller por reparacion o baja bateria.");
    }

    @Override
    public void finalizarViaje(Vehiculo v) {
        throw new IllegalStateException("El vehiculo esta en reparacion. No registra viaje activo.");
    }

    @Override
    public void enviarAReparacion(Vehiculo v) {
        throw new IllegalStateException("El vehiculo ya se encuentra en reparacion.");
    }

    @Override
    public void finalizarReparacion(Vehiculo v) {
        v.setEstado(EstadoEnEspera.getInstance());
    }

    @Override
    public String getNombre() {
        return "En Reparación";
    }
}
