package ar.edu.ungs.prog2.smartgrids;

public abstract class plantaRenovable extends Planta {
    String fuentePrimaria;
    double eficienciaPromedio;

    public plantaRenovable(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                           String fuentePrimaria, double eficienciaPromedio, String tipoPlanta) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoPlanta);
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }
    public String consultarFuentePrimaria() {
        return fuentePrimaria;
    }
}