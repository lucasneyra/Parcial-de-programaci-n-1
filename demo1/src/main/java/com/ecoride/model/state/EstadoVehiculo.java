package com.ecoride.model.state;

import com.ecoride.model.Vehiculo;

// Interfaz para el patron State. Define las acciones de transicion
// del ciclo de vida de cualquier vehiculo en la plataforma.
public interface EstadoVehiculo {
    void desbloquear(Vehiculo v);
    void finalizarViaje(Vehiculo v);
    void enviarAReparacion(Vehiculo v);
    void finalizarReparacion(Vehiculo v);
    String getNombre(); // Nombre amigable para mostrar en la API
}
