package com.ecoride.Controlador;

import com.ecoride.dto.*;
import com.ecoride.service.alquilerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class control {

    @Autowired
    private alquilerService servicio;

    // GET /api/alquileres/desbloquear
    // Recibe JSON body y devuelve DTO profesional.
    @GetMapping("/alquileres/desbloquear")
    public ResponseEntity<Object> desbloquearVehiculo(@RequestBody Map<String, String> body) {
        try {
            String idUsuario = body.get("idUsuario");
            String patente = body.get("patente");
            String metodoPago = body.get("metodoPago");

            AlquilerResponseDTO response = servicio.desbloquearVehiculo(idUsuario, patente, metodoPago);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Manejo de errores de negocio (Ej: baterias bajas, reparacion activa, vehiculo no encontrado)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Alerta del Sistema: " + e.getMessage());
        }
    }

    // POST /api/alquileres/finalizar
    // Finaliza el viaje, calcula tarifas con Strategy y devuelve DTO.
    @PostMapping("/alquileres/finalizar")
    public ResponseEntity<Object> finalizarAlquiler(@RequestBody Map<String, Object> body) {
        try {
            String patente = (String) body.get("patente");
            int minutos = Integer.parseInt(body.get("minutos").toString());
            String idUsuario = (String) body.get("idUsuario");
            String metodoPago = (String) body.get("metodoPago");

            AlquilerResponseDTO response = servicio.finalizarAlquiler(patente, minutos, idUsuario, metodoPago);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Alerta del Sistema: " + e.getMessage());
        }
    }

    // POST /api/alquileres/criterio
    // Endpoint administrativo para cambiar la estrategia de precios en caliente.
    @PostMapping("/alquileres/criterio")
    public ResponseEntity<String> cambiarCriterio(@RequestBody Map<String, String> body) {
        try {
            String criterio = body.get("criterio");
            servicio.cambiarCriterio(criterio);
            return ResponseEntity.ok("Criterio cambiado con éxito a: " + servicio.getCriterioActivo().getNombre());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /api/vehiculos/prioridad-carga
    // Devuelve listado de flota ordenada por bateria de menor a mayor (Comparable nativo).
    @GetMapping("/vehiculos/prioridad-carga")
    public ResponseEntity<List<VehiculoResponseDTO>> obtenerPrioridadCarga() {
        List<VehiculoResponseDTO> listado = servicio.obtenerPrioridadCarga();
        return ResponseEntity.ok(listado);
    }

    // GET /api/vehiculos/tarifa-descendente
    // Devuelve listado de flota ordenada por tarifa base de mayor a menor (Comparator externo).
    @GetMapping("/vehiculos/tarifa-descendente")
    public ResponseEntity<List<VehiculoResponseDTO>> obtenerTarifaDescendente() {
        List<VehiculoResponseDTO> listado = servicio.obtenerTarifaDescendente();
        return ResponseEntity.ok(listado);
    }

    // POST /api/vehiculos/deduplicar-gps
    // Recibe lista de geolocalizaciones GPS con duplicados y devuelve lista limpia en O(N).
    @PostMapping("/vehiculos/deduplicar-gps")
    public ResponseEntity<List<ReporteGPS>> deduplicarGPS(@RequestBody List<ReporteGPS> reportes) {
        List<ReporteGPS> filtrados = servicio.deduplicarAlertasGPS(reportes);
        return ResponseEntity.ok(filtrados);
    }
}