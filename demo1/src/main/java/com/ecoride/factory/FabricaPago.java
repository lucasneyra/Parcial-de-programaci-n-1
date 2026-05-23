package com.ecoride.factory;

import org.springframework.stereotype.Component;

@Component
public class FabricaPago {
    
    public ProcesadorPago obtenerProcesador(String tipo) {
        if (tipo == null) throw new IllegalArgumentException("Metodo de pago nulo");
        
        switch (tipo.toUpperCase()) {
            case "TARJETA":
                return new TarjetaCredito(); // Fijate de tener esta clase creada!
            // case "BILLETERA":
            //    return new BilleteraVirtual();
            default:
                throw new IllegalArgumentException("Metodo no soportado");
        }
    }
}