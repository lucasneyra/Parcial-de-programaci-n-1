package com.ecoride.service;

import com.ecoride.model.*;
import com.ecoride.factory.*; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class alquilerService {

    private List<EstacionAnclaje> estaciones = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();

    // ACÁ ESTABA EL ERROR OCULTO: Tu clase se llama FabricaPago, no FabricaProcesadorPago
    @Autowired
    private FabricaPago pagoFactory; 

    public alquilerService() {
        // Datos de prueba iniciales
        usuarios.add(new UsuarioRegular("U001", "Gaston"));
        usuarios.add(new UsuarioPremium("U002", "Laura", 0.15));

        EstacionAnclaje est1 = new EstacionAnclaje("Centro");
        est1.getVehiculos().add(new Monopatin("AAA111", 80, 200.0, true));
        est1.getVehiculos().add(new Monopatin("BBB222", 10, 200.0, false));
        est1.getVehiculos().add(new BicicletaElectrica("CCC333", 50, 300.0, 250));

        estaciones.add(est1);
    }

    public Object desbloquearVehiculo(String idUsuario, String patente, String metodoPago) {
        
        Usuario user = null;
        for (Usuario u : usuarios) {
            if (u.getId().equals(idUsuario)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Vehiculo vehiculo = null;
        EstacionAnclaje estacionOrigen = null;

        for (EstacionAnclaje est : estaciones) {
            Vehiculo encontrado = est.buscarVehiculoPorPatente(patente);
            if (encontrado != null) {
                vehiculo = encontrado;
                estacionOrigen = est;
                break;
            }
        }

        if (vehiculo == null) {
            throw new RuntimeException("Vehiculo No Encontrado");
        }

        if (vehiculo.getPorcentajeBateria() < 15) {
            throw new RuntimeException("Bateria Insuficiente");
        }

        double precioFinal = user.calcularMonto(vehiculo.getTarifaBase());

        ProcesadorPago procesador = pagoFactory.obtenerProcesador(metodoPago);
        procesador.efectuarCobro(precioFinal);

  
        if (estacionOrigen != null) {
            estacionOrigen.getVehiculos().remove(vehiculo);
        }

        return "Desbloqueo exitoso. Rodado: " + patente + ". Total cobrado: $" + precioFinal;
    }
}