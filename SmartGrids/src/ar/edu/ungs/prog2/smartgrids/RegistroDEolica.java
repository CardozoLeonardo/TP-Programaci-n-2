package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class RegistroDEolica extends DatosDeMonitoreo {

    private double velocidadViento;

    public RegistroDEolica(Planta planta, LocalDateTime fechaHora,
            double energiaGenerada, int incidencias,
            double velocidadViento) {

        super(planta, fechaHora, energiaGenerada, incidencias);
        this.velocidadViento = velocidadViento;
    }

    public double consultarVelocidadViento() {
        return velocidadViento;
    }

}


