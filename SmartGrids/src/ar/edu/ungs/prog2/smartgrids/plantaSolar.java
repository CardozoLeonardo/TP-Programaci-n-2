package ar.edu.ungs.prog2.smartgrids;

public class plantaSolar extends plantaRenovable {
    double superficieCubierta;
    int cantidadDePanelesInstalados;
    double horasDeRadiacionDiaria;

    public plantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                       String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, 
                       int cantidadDePanelesInstalados, double horasDeRadiacionDiaria) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, "Solar");
        this.superficieCubierta = superficieCubierta;
        this.cantidadDePanelesInstalados = cantidadDePanelesInstalados;
        this.horasDeRadiacionDiaria = horasDeRadiacionDiaria;
    }

    @Override
    public double capacidadGeneracion() {
        return capacidadMaxima * eficienciaPromedio * horasDeRadiacionDiaria / 24.0;
    }
    
    public boolean cumplioCuota(DatosDeMonitoreo reg) {
        if (reg == null) return false;
        return reg.consultarEnergiaGenerada() >= capacidadGeneracion();
    }
}