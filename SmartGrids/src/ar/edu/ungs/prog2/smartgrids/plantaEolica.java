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

	    boolean[] hayRegistros = new boolean[24];
	    boolean[] todasConPocoViento = new boolean[24];
	    double[] velocidadPorHora = new double[24];

	    for (int i = 0; i < 24; i++) {
	        todasConPocoViento[i] = true;
	    }

	    for (DatosDeMonitoreo datos : registros.values()) {
	        if (datos instanceof RegistroDEolica) {
	            RegistroDEolica regE = (RegistroDEolica) datos;

	            int hora = regE.consultarFechaHora().getHour();
	            double velocidad = regE.consultarVelocidadViento();

	            hayRegistros[hora] = true;

	            if (velocidad >= velocidadMinimaViento) {
	                todasConPocoViento[hora] = false;
	            } else {
	                velocidadPorHora[hora] = velocidad;
	            }
	        }
	    }

	    boolean hayHorasParaInformar = false;

	    for (int hora = 0; hora < 24; hora++) {
	        if (hayRegistros[hora] && todasConPocoViento[hora]) {
	            hayHorasParaInformar = true;
	            break;
	        }
	    }

	    if (!hayHorasParaInformar) {
	        return "";
	    }

	    sb.append("- ")
	      .append(getNombre())
	      .append(": ")
	      .append(velocidadMinimaViento)
	      .append(" m/s\n");

	    for (int hora = 0; hora < 24; hora++) {
	        if (hayRegistros[hora] && todasConPocoViento[hora]) {
	            String horaFormato = (hora < 10 ? "0" : "") + hora;

	            sb.append(" ")
	              .append(horaFormato)
	              .append(": ")
	              .append(velocidadPorHora[hora])
	              .append(" m/s\n");
	        }
	    }

	    return sb.toString();
	}
    
}