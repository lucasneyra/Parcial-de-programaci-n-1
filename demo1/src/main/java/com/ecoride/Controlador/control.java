package com.ecoride.Controlador;

// Faltaban todos estos imports para que reconozca las clases
import com.ecoride.service.alquilerService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/alquileres")
public class control {

    @Autowired
    private alquilerService servicio;

    @GetMapping("/desbloquear")
    public ResponseEntity<String> desbloquearVehiculo(@RequestBody Map<String, String> body) {
        try {
            // sacamos datos de json
            String idUsuario = body.get("idUsuario");
            String patente = body.get("patente");
            String metodoPago = body.get("metodoPago");

            // CORRECCIÓN: El método se llama desbloquearVehiculo según tu UML
            // Le agregamos .toString() al final porque en el UML devuelve un Object
            String resultado = servicio.desbloquearVehiculo(idUsuario, patente, metodoPago).toString();
            
            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            // errores capturados
            String error = e.getMessage();
            if (error.equals("Vehiculo No Encontrado") || error.equals("Bateria Insuficiente")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alerta del Sistema: " + error);
            }
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + error);
        }
    }
}