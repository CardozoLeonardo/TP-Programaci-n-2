package ar.edu.ungs.prog2.smartgrids;

public class plantaTermica extends Planta {
    private String tipoCombustible;
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public plantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                         String tipoCombustible, double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, "Termica");
        this.tipoCombustible = tipoCombustible;
        this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
        this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
    }

    @Override
    public double capacidadGeneracion() {
    	return capacidadMaxima / 2.0;
    }
    
    @Override
    public boolean cumplioCuota(DatosDeMonitoreo reg) {
        if (reg == null) return false;
        
        if (reg instanceof RegistroDTermica) {
            RegistroDTermica regTermica = (RegistroDTermica) reg;
            return regTermica.consultarEnergiaGenerada() >= capacidadGeneracion()
                && regTermica.consultarCo2Emitido() < this.objetivoEmisionesCo2PorHora;
        }
        return reg.consultarEnergiaGenerada() >= capacidadGeneracion();
    }
    @Override
    public String toString() {
        return super.toString() + " [Térmica - Combustible: " + this.tipoCombustible + ", Objetivo CO2/h: " + this.objetivoEmisionesCo2PorHora + ", Almacén: " + this.capacidadAlmacenamientoCombustible + "]";
    }
}