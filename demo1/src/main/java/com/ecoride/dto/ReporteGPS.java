package com.ecoride.dto;

import java.util.Objects;

// Representa un reporte de geolocalizacion del GPS de un vehiculo.
public class ReporteGPS {
    private double latitud;
    private double longitud;

    public ReporteGPS() {}

    public ReporteGPS(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    // Requerido para que el LinkedHashSet reconozca los duplicados en O(1) de manera nativa.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReporteGPS that = (ReporteGPS) o;
        return Double.compare(that.latitud, latitud) == 0 &&
               Double.compare(that.longitud, longitud) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitud, longitud);
    }
}
