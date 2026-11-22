package ar.edu.ungs.prog2.smartgrids;

public class plantaHidraulica extends plantaRenovable {
    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public plantaHidraulica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                            String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, "Hidraulica");
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    @Override
    public double capacidadGeneracion() {
    	return capacidadMaxima * eficienciaPromedio;
    }
    @Override
    public boolean cumplioCuota(DatosDeMonitoreo reg) {
        if (reg == null) return false;
        return reg.consultarEnergiaGenerada() >= capacidadGeneracion();
    }
    @Override
    public String toString() {
        return super.toString() + " [Hidráulica - Turbinas: " + this.cantidadTurbinas + ", Embalse: " + this.capacidadEmbalse + "]";
    }
}