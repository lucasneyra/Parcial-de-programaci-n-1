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

    // Metodo GET para desbloquear el vehiculo
    // Recibe los datos y devuelve una respuesta simple
    @GetMapping("/alquileres/desbloquear")
    public ResponseEntity<Object> desbloquearVehiculo(@RequestBody Map<String, String> body) {
        try {
            String idUsuario = body.get("idUsuario");
            String patente = body.get("patente");
            String metodoPago = body.get("metodoPago");

            AlquilerResponseDTO response = servicio.desbloquearVehiculo(idUsuario, patente, metodoPago);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Si algo sale mal (como poca bateria o si esta roto) tiramos error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Alerta del Sistema: " + e.getMessage());
        }
    }

    // Metodo POST para terminar el viaje
    // Cobra segun la forma de cobro activa y devuelve los datos
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

    // Metodo POST para cambiar la forma de cobrar mientras la app esta corriendo
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

    // Metodo GET para listar los vehiculos ordenados de menos a mas bateria
    @GetMapping("/vehiculos/prioridad-carga")
    public ResponseEntity<List<VehiculoResponseDTO>> obtenerPrioridadCarga() {
        List<VehiculoResponseDTO> listado = servicio.obtenerPrioridadCarga();
        return ResponseEntity.ok(listado);
    }

    // Metodo GET para listar los vehiculos ordenados del mas caro al mas barato
    @GetMapping("/vehiculos/tarifa-descendente")
    public ResponseEntity<List<VehiculoResponseDTO>> obtenerTarifaDescendente() {
        List<VehiculoResponseDTO> listado = servicio.obtenerTarifaDescendente();
        return ResponseEntity.ok(listado);
    }

    // Metodo POST para sacar los GPS repetidos de la lista
    @PostMapping("/vehiculos/deduplicar-gps")
    public ResponseEntity<List<ReporteGPS>> deduplicarGPS(@RequestBody List<ReporteGPS> reportes) {
        List<ReporteGPS> filtrados = servicio.deduplicarAlertasGPS(reportes);
        return ResponseEntity.ok(filtrados);
    }
}