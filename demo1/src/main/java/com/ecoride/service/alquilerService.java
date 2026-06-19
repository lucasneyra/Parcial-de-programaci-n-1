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
    
    // REFACTORIZACION DE OPTIMIZACION (B.1): Mapa global de vehiculos para busquedas O(1) inmediatas
    private Map<String, Vehiculo> flota = new HashMap<>();

    // PATRON STRATEGY (A.2): Criterio de facturacion activo, cambia en tiempo de ejecucion
    private CriterioTarifa criterioActivo = new CriterioEstandar();

    @Autowired
    private FabricaPago pagoFactory;

    public alquilerService() {
        // Datos de prueba iniciales
        usuarios.add(new UsuarioRegular("U001", "Gaston"));
        usuarios.add(new UsuarioPremium("U002", "Laura", 0.15));

        EstacionAnclaje est1 = new EstacionAnclaje("Centro");
        estaciones.add(est1);

        // Registro de vehiculos y asociacion a la estacion y al mapa de busqueda instantanea
        Vehiculo v1 = new Monopatin("AAA111", 80, 200.0, true);
        v1.setEstacionActual(est1);
        est1.registrarVehiculo(v1);
        flota.put(v1.getPatente(), v1);

        // Vehiculo con bateria insuficiente (10%) para probar las validaciones
        Vehiculo v2 = new Monopatin("BBB222", 10, 200.0, false);
        v2.setEstacionActual(est1);
        est1.registrarVehiculo(v2);
        flota.put(v2.getPatente(), v2);

        Vehiculo v3 = new BicicletaElectrica("CCC333", 50, 300.0, 250);
        v3.setEstacionActual(est1);
        est1.registrarVehiculo(v3);
        flota.put(v3.getPatente(), v3);

        // Vehiculo en reparacion para probar las alertas del patron State
        Vehiculo v4 = new Monopatin("DDD444", 90, 150.0, true);
        v4.setEstado(com.ecoride.model.state.EstadoEnReparacion.getInstance());
        flota.put(v4.getPatente(), v4);
    }

    // Cambiar la estrategia de precios en caliente usando la fabrica
    public void cambiarCriterio(String tipo) {
        // Uso de la fabrica para desacoplamiento dinamico de estrategias
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

        // BUSQUEDA OPTIMIZADA O(1) (B.1): Busqueda en el mapa global de patentes
        Vehiculo vehiculo = flota.get(patente);
        if (vehiculo == null) {
            throw new RuntimeException("Vehiculo No Encontrado");
        }

        // VALIDACION REGLAS DE NEGOCIO (B.2): Bateria insuficiente
        if (vehiculo.getPorcentajeBateria() < 15) {
            throw new RuntimeException("Bateria Insuficiente");
        }

        // CONTROL DE ESTADOS (A.1): Transicion dinamica usando patron State.
        vehiculo.getEstado().desbloquear(vehiculo);

        // Desacoplamiento de la estacion física en O(1) usando la referencia directa
        EstacionAnclaje estOrigen = vehiculo.getEstacionActual();
        if (estOrigen != null) {
            estOrigen.eliminarVehiculo(vehiculo);
            vehiculo.setEstacionActual(null);
        }

        // Cobro de la tarifa base inicial del desbloqueo
        double precioDesbloqueo = user.calcularMonto(vehiculo.getTarifaBase());
        ProcesadorPago procesador = pagoFactory.obtenerProcesador(metodoPago);
        procesador.efectuarCobro(precioDesbloqueo);

        // Retornamos estructura DTO profesional (C.1)
        return new AlquilerResponseDTO(
            vehiculo.getPatente(),
            vehiculo.getEstado().getNombre(),
            precioDesbloqueo,
            0,
            "Desbloqueo y cobro inicial exitoso."
        );
    }

    // Operacion 2: Finalizar Alquiler (C.1)
    public AlquilerResponseDTO finalizarAlquiler(String patente, int minutos, String idUsuario, String metodoPago) {
        // BUSQUEDA OPTIMIZADA O(1) (B.1)
        Vehiculo vehiculo = flota.get(patente);
        if (vehiculo == null) {
            throw new RuntimeException("Vehiculo No Encontrado");
        }

        // CONTROL DE ESTADOS (A.1): Transicion usando patron State
        vehiculo.getEstado().finalizarViaje(vehiculo);

        // Buscar usuario para aplicar descuento si corresponde
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

        // PATRON STRATEGY (A.2): Calculo adaptativo del costo del viaje segun el criterio activo
        double costoEstrategia = criterioActivo.calcularCosto(vehiculo.getTarifaBase(), minutos);
        double costoFinal = user.calcularMonto(costoEstrategia);

        // Procesar pago uniforme
        ProcesadorPago procesador = pagoFactory.obtenerProcesador(metodoPago);
        procesador.efectuarCobro(costoFinal);

        // Devolvemos el vehiculo a la estacion de anclaje
        if (!estaciones.isEmpty()) {
            EstacionAnclaje estDestino = estaciones.get(0);
            vehiculo.setEstacionActual(estDestino);
            estDestino.registrarVehiculo(vehiculo);
        }

        // Simular consumo de bateria tras el uso
        int bateriaRestante = Math.max(0, vehiculo.getPorcentajeBateria() - (minutos / 2));
        vehiculo.setPorcentajeBateria(bateriaRestante);

        // Retornamos DTO
        return new AlquilerResponseDTO(
            vehiculo.getPatente(),
            vehiculo.getEstado().getNombre(),
            costoFinal,
            minutos,
            "Viaje finalizado exitosamente. Criterio aplicado: " + criterioActivo.getNombre()
        );
    }

    // ORDENAMIENTO (B.3): Reporte de prioridad de carga (Orden Natural: Comparable)
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

    // ORDENAMIENTO (B.3): Reporte de tarifa descendente (Orden Alternativo: ComparadorPorCostoBase externo)
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

    // DEDUPLICACIÓN DE REPORTES GPS (B.2): Complejidad O(N) en una sola pasada sin bucles anidados
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