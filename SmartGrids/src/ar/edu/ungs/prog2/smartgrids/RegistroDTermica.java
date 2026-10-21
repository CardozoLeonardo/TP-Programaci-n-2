
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;


public class RegistroDTermica extends DatosDeMonitoreo {

    private double co2Emitido;
    private double combustibleUtilizado;

    public RegistroDTermica(Planta planta, LocalDateTime fechaHora,
                            double energiaGenerada,
                            int incidencias,
                            double co2Emitido,
                            double combustibleUtilizado) {
    	super(planta, fechaHora, energiaGenerada, incidencias);
        this.co2Emitido = co2Emitido;
        this.combustibleUtilizado = combustibleUtilizado;
    }

    public double consultarCo2Emitido() {
        return co2Emitido;
    }

    public double consultarCombustibleUtilizado() {
        return combustibleUtilizado;
    }
}
