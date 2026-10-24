package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Planta {
	protected Map<LocalDateTime, DatosDeMonitoreo> registros;
    String nombre;
    String direccion;
    double capacidadMaxima;
    boolean activa;
    LocalDateTime fechaEnFuncionamiento;
    String tipoPlanta;
    private static class PeriodoInactividad {
        LocalDateTime desde;
        LocalDateTime hasta; // null mientras siga inactiva

        PeriodoInactividad(LocalDateTime desde) {
            this.desde = desde;
        }

        boolean contiene(LocalDateTime t) {
            boolean despuesDelInicio = !t.isBefore(desde);
            boolean antesDelFin = (hasta == null) || t.isBefore(hasta);
            return despuesDelInicio && antesDelFin;
        }
    }

    private List<PeriodoInactividad> estuvoInactiva;
    

    public Planta(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoPlanta) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaEnFuncionamiento = LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
        this.tipoPlanta = tipoPlanta;
        this.activa = true;
        this.registros = new HashMap<>();
        this.estuvoInactiva = new ArrayList<>();
    }

    
    public String getNombre() {
        return nombre;
    }

    public boolean isActiva() {
        return activa;
    }
    public void activar(LocalDateTime fechaHora) {
        if (!this.activa) {
            PeriodoInactividad ultimo = estuvoInactiva.get(estuvoInactiva.size() - 1);
            ultimo.hasta = fechaHora;
        }
        this.activa = true;
    }

    public void desactivar(LocalDateTime fechaHora) {
        if (this.activa) {
            estuvoInactiva.add(new PeriodoInactividad(fechaHora));
        }
        this.activa = false;
    }

    public boolean estaActiva() {
        return this.activa;
    }
    
    public abstract double capacidadGeneracion();
    
    public abstract boolean cumplioCuota(DatosDeMonitoreo reg);
    
    public boolean nuncaCumplioCuota() {
    	if (this.registros == null || registros.isEmpty()) {
    		return true;
    	}	
    	for (DatosDeMonitoreo reg : registros.values()) {
    		if (cumplioCuota(reg)) {
    			return false;
    		}
    	}
    	return true;
    }    
    
    public int limpiarRegistrosVacios() { 
    	int contador = 0;
    	Iterator<DatosDeMonitoreo> it = registros.values().iterator();
    	while (it.hasNext()) {
    		DatosDeMonitoreo reg = it.next();
    		if (reg.estaVacio()) {
    			it.remove();
    			contador++;
    		}
    	}
    	return contador;
    }

    public boolean horaInactiva(LocalDateTime fechaHora) {
    	for (PeriodoInactividad p : estuvoInactiva) {
            if (p.contiene(fechaHora)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        String estado = activa ? "ACTIVA" : "INACTIVA";
        String fecha = fechaEnFuncionamiento.toString();

        if (fecha.length() == 16) {
            fecha = fecha + ":00";
        }

        return nombre + " (" + estado + "): " + fecha;
    }

}