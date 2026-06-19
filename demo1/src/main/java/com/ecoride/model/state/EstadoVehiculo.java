package com.ecoride.model.state;

import com.ecoride.model.Vehiculo;

// Interfaz para los estados Dice que cosas puede hacer el vehiculo segun como este
public interface EstadoVehiculo {
    void desbloquear(Vehiculo v);
    void finalizarViaje(Vehiculo v);
    void enviarAReparacion(Vehiculo v);
    void finalizarReparacion(Vehiculo v);
    String getNombre(); // Metodo para que nos devuelva el nombre del estado como texto
}
