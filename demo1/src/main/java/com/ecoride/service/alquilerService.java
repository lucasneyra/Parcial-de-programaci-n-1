package com.ecoride.service;

import com.ecoride.model.*;
import com.ecoride.factory.*;
import com.ecoride.dto.*;
import com.ecoride.service.strategy.*;
import com.ecoride.service.comparator.ComparadorPorCostoBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class alquilerService {

    private List<EstacionAnclaje> estaciones = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    
    // Aca guardamos todos los vehiculos por patente para buscarlos rapido sin dar vueltas
    private Map<String, Vehiculo> flota = new HashMap<>();

    // Aca guardamos como estamos cobrando ahora por defecto cobramos estandar
    private CriterioTarifa criterioActivo = new CriterioEstandar();

    @Autowired
    private FabricaPago pagoFactory;

    public alquilerService() {
        // Datos de prueba iniciales
        usuarios.add(new UsuarioRegular("U001", "Gaston"));
        usuarios.add(new UsuarioPremium("U002", "Laura", 0.15));

        EstacionAnclaje est1 = new EstacionAnclaje("Centro");
        estaciones.add(est1);

        // Metemos los vehiculos en la estacion y en nuestra lista rapida
        Vehiculo v1 = new Monopatin("AAA111", 80, 200.0, true);
        v1.setEstacionActual(est1);
        est1.registrarVehiculo(v1);
        flota.put(v1.getPatente(), v1);

        // Ponemos uno con poca bateria para probar si salta el error de bateria baja
        Vehiculo v2 = new Monopatin("BBB222", 10, 200.0, false);
        v2.setEstacionActual(est1);
        est1.registrarVehiculo(v2);
        flota.put(v2.getPatente(), v2);

        Vehiculo v3 = new BicicletaElectrica("CCC333", 50, 300.0, 250);
        v3.setEstacionActual(est1);
        est1.registrarVehiculo(v3);
        flota.put(v3.getPatente(), v3);

        // Ponemos uno roto para ver si anda el error de vehiculo en reparacion
        Vehiculo v4 = new Monopatin("DDD444", 90, 150.0, true);
        v4.setEstado(com.ecoride.model.state.EstadoEnReparacion.getInstance());
        flota.put(v4.getPatente(), v4);
    }

    // Cambiamos la forma de cobrar sobre la marcha
    public void cambiarCriterio(String tipo) {
        // Le pedimos a la fabrica que nos de la nueva forma de cobrar
        this.criterioActivo = FabricaCriterio.obtenerCriterio(tipo);
    }

    public CriterioTarifa getCriterioActivo() {
        return criterioActivo;
    }

    // Operacion 1: Desbloquear Vehiculo
    public AlquilerResponseDTO desbloquearVehiculo(String idUsuario, String patente, String metodoPago) {
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

        // Buscamos el vehiculo directamente por patente
        Vehiculo vehiculo = flota.get(patente);
        if (vehiculo == null) {
            throw new RuntimeException("Vehiculo No Encontrado");
        }

        // Si no tiene bateria suficiente no lo dejamos usar
        if (vehiculo.getPorcentajeBateria() < 15) {
            throw new RuntimeException("Bateria Insuficiente");
        }

        // Le cambiamos el estado al vehiculo segun las reglas
        vehiculo.getEstado().desbloquear(vehiculo);

        // Sacamos el vehiculo de la estacion donde estaba
        EstacionAnclaje estOrigen = vehiculo.getEstacionActual();
        if (estOrigen != null) {
            estOrigen.eliminarVehiculo(vehiculo);
            vehiculo.setEstacionActual(null);
        }

        // Le cobramos la tarifa base por empezar el viaje
        double precioDesbloqueo = user.calcularMonto(vehiculo.getTarifaBase());
        ProcesadorPago procesador = pagoFactory.obtenerProcesador(metodoPago);
        procesador.efectuarCobro(precioDesbloqueo);

        // Devolvemos el resultado del alquiler
        return new AlquilerResponseDTO(
            vehiculo.getPatente(),
            vehiculo.getEstado().getNombre(),
            precioDesbloqueo,
            0,
            "Desbloqueo y cobro inicial exitoso."
        );
    }

    // Aca termina el viaje
    public AlquilerResponseDTO finalizarAlquiler(String patente, int minutos, String idUsuario, String metodoPago) {
        // Buscamos el vehiculo
        Vehiculo vehiculo = flota.get(patente);
        if (vehiculo == null) {
            throw new RuntimeException("Vehiculo No Encontrado");
        }

        // Le cambiamos el estado para que vuelva a estar en espera
        vehiculo.getEstado().finalizarViaje(vehiculo);

        // Si el usuario es premium le hacemos descuento
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

        // Calculamos el costo del viaje segun como estemos cobrando ahora
        double costoEstrategia = criterioActivo.calcularCosto(vehiculo.getTarifaBase(), minutos);
        double costoFinal = user.calcularMonto(costoEstrategia);

        // Procesar pago uniforme
        ProcesadorPago procesador = pagoFactory.obtenerProcesador(metodoPago);
        procesador.efectuarCobro(costoFinal);

        // Devolvemos el vehiculo a la estacion
        if (!estaciones.isEmpty()) {
            EstacionAnclaje estDestino = estaciones.get(0);
            vehiculo.setEstacionActual(estDestino);
            estDestino.registrarVehiculo(vehiculo);
        }

        // Le bajamos la bateria por haberlo usado
        int bateriaRestante = Math.max(0, vehiculo.getPorcentajeBateria() - (minutos / 2));
        vehiculo.setPorcentajeBateria(bateriaRestante);

        // Devolvemos los datos del fin de viaje
        return new AlquilerResponseDTO(
            vehiculo.getPatente(),
            vehiculo.getEstado().getNombre(),
            costoFinal,
            minutos,
            "Viaje finalizado exitosamente. Criterio aplicado: " + criterioActivo.getNombre()
        );
    }

    // Ordenamos de menos bateria a mas bateria para saber cuales cargar primero
    public List<VehiculoResponseDTO> obtenerPrioridadCarga() {
        List<Vehiculo> lista = new ArrayList<>(flota.values());
        Collections.sort(lista);

        List<VehiculoResponseDTO> resultado = new ArrayList<>();
        for (Vehiculo v : lista) {
            String tipo = (v instanceof Monopatin) ? "Monopatín" : "Bicicleta Eléctrica";
            resultado.add(new VehiculoResponseDTO(
                v.getPatente(),
                v.getPorcentajeBateria(),
                v.getTarifaBase(),
                tipo,
                v.getEstado().getNombre()
            ));
        }
        return resultado;
    }

    // Ordenamos los vehiculos de mas caro a mas barato
    public List<VehiculoResponseDTO> obtenerTarifaDescendente() {
        List<Vehiculo> lista = new ArrayList<>(flota.values());
        Collections.sort(lista, new ComparadorPorCostoBase());

        List<VehiculoResponseDTO> resultado = new ArrayList<>();
        for (Vehiculo v : lista) {
            String tipo = (v instanceof Monopatin) ? "Monopatín" : "Bicicleta Eléctrica";
            resultado.add(new VehiculoResponseDTO(
                v.getPatente(),
                v.getPorcentajeBateria(),
                v.getTarifaBase(),
                tipo,
                v.getEstado().getNombre()
            ));
        }
        return resultado;
    }

    // Sacamos las alertas de GPS que esten repetidas usando un Set
    public List<ReporteGPS> deduplicarAlertasGPS(List<ReporteGPS> reportes) {
        if (reportes == null) return new ArrayList<>();
        
        java.util.LinkedHashSet<ReporteGPS> conjuntoUnicos = new java.util.LinkedHashSet<>();
        for (ReporteGPS reporte : reportes) {
            conjuntoUnicos.add(reporte);
        }

        List<ReporteGPS> filtrados = new ArrayList<>();
        for (ReporteGPS reporte : conjuntoUnicos) {
            filtrados.add(reporte);
        }
        return filtrados;
    }
}