package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class DatosDeMonitoreo {

    private Planta planta;
    private LocalDateTime fechaHora;
    private double totalDeEnergiaGenerado;
    private int cantidadIncidencias;

    public DatosDeMonitoreo(Planta planta, LocalDateTime fechaHora,
            double energiaGenerada, int incidencias) {
        this.planta = planta;
        this.fechaHora = fechaHora;
        this.totalDeEnergiaGenerado = energiaGenerada;
        this.cantidadIncidencias = incidencias;
    }

    public LocalDateTime consultarFechaHora() {
        return fechaHora;
    }

    public double consultarEnergiaGenerada() {
        return totalDeEnergiaGenerado;
    }

    public int consultarIncidencias() {
        return cantidadIncidencias;
    }
    public boolean estaVacio() {
    	return this.totalDeEnergiaGenerado == 0.0 && this.cantidadIncidencias == 0;
    }
   
}