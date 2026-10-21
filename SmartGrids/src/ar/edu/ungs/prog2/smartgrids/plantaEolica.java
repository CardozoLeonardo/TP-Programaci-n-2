package ar.edu.ungs.prog2.smartgrids;

public class plantaEolica extends plantaRenovable {
    int cantidadDeAerogeneradores;
    double alturaTorres;
    double velocidadMinimaViento;

    public plantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                        String fuentePrimaria, double eficienciaPromedio, int cantidadDeAerogeneradores, 
                        double alturaTorres, double velocidadMinimaViento) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, "Eolica");
        this.cantidadDeAerogeneradores = cantidadDeAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.velocidadMinimaViento = velocidadMinimaViento;
    }

    @Override
    public double capacidadGeneracion() {
        return capacidadMaxima * eficienciaPromedio;
    }
    
    @Override
    public boolean cumplioCuota(DatosDeMonitoreo reg) {
        if (reg == null) return false;
        
        if (reg instanceof RegistroDEolica) {
            RegistroDEolica regEolica = (RegistroDEolica) reg;
            return regEolica.consultarVelocidadViento() >= this.velocidadMinimaViento 
                && regEolica.consultarEnergiaGenerada() >= capacidadGeneracion();
        }
        return reg.consultarEnergiaGenerada() >= capacidadGeneracion();
    }
    
    public boolean alcanzoVelocidadMinima(double velocidadViento) {
    	return velocidadViento >= this.velocidadMinimaViento;
    }

	public String generarReporteDPocoViento() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for(DatosDeMonitoreo datos : registros.values()) {
			if(datos instanceof RegistroDEolica) {
				RegistroDEolica regE = (RegistroDEolica) datos;
				
				if (regE.consultarVelocidadViento() < this.velocidadMinimaViento) {
					if (sb.length()==0) {
						sb.append(getNombre()+ ": " + this.velocidadMinimaViento + "m/s \n");
					}
					
					int hora = regE.consultarFechaHora().getHour();
					double vel = regE.consultarVelocidadViento();
					String horaFormato = (hora < 10 ? "0" + hora : "" + hora);
					
					sb.append(" " + horaFormato + ": " + vel + "m/s \n");
				}
			}
		}
		
		
		return sb.toString();
	}
    
}