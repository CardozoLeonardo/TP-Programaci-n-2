package ar.edu.ungs.prog2.smartgrids;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;


public class SmartGrids implements ISmartGrids {
	private Map<String, Planta> plantas = new LinkedHashMap<String, Planta>();
	private Map<String, List<DatosDeMonitoreo>> monitoreos = new LinkedHashMap<String, List<DatosDeMonitoreo>>();

	
// 1)a. REGISTRO DE PLANTAS SEGUN SU TIPO
   @Override
	public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
	   
		if (plantas.containsKey(nombre)) {
	        throw new RuntimeException("Ya existe una planta con ese nombre: " + nombre);
	    }
		plantaSolar solar = new plantaSolar(nombre, direccion, capacidadMaxima,
	            fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio,
	            superficieCubierta, cantidadPaneles, horasRadiacionDiaria);
	    
	    
	    plantas.put(nombre, solar);

	}

	@Override
	public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
		
		if (plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta con ese nombre: " + nombre);
        }
        
        plantaEolica eolica = new plantaEolica(nombre, direccion, capacidadMaxima, 
                fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, 
                cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
        
        plantas.put(nombre, eolica);
    
	}

	@Override
	public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadTurbinas, double capacidadEmbalse) {
		
		if (plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta con ese nombre: " + nombre);
        }
        
        plantaHidraulica hidraulica = new plantaHidraulica(nombre, direccion, capacidadMaxima, 
                fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, 
                cantidadTurbinas, capacidadEmbalse);
        
        plantas.put(nombre, hidraulica);
	}

	@Override
	public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
			double capacidadAlmacenamientoCombustible) {
		
		if (plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta con ese nombre: " + nombre);
        }
        
        plantaTermica termica = new plantaTermica(nombre, direccion, capacidadMaxima, 
                fechaHoraPuestaEnFuncionamiento, tipoCombustible, 
                objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
        
        plantas.put(nombre, termica);
	}

//1.b) CONSULTAR PLANTAS EXISTENTES
	@Override
	public List<String> consultarPlantas() {
	    List<String> resultado = new ArrayList<>();

	    for (Planta planta : plantas.values()) {
	        resultado.add(planta.estado());
	    }

	    return resultado;
	
	}

//2. CALCULO DE LA CUOTA MINIMA SEGUN PLANTA
	@Override
	public double capacidadMinimaCuota(String nombre) {
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    return planta.capacidadGeneracion();
	}

//3. CONSULTA DE PLANTA (ACTIVA/INACTIVA)
	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    planta.activar(LocalDateTime.parse(fechaHora));
	}

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    planta.desactivar(LocalDateTime.parse(fechaHora));
	}

	@Override
	public boolean estaActiva(String nombre) {
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    return planta.estaActiva();

	}

//4. CREAR UN REGISTRO DE MONITOREO SEGUN LA PLANTA ESPECIFICADA
	
	//Registro de plantas Solares/Hidraulicas
	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
		 Planta planta = plantas.get(nombre);

		    if (planta == null) {
		        throw new RuntimeException("La planta no existe");
		    }
		    if (planta instanceof plantaEolica || planta instanceof plantaTermica) {
		        throw new RuntimeException("Tipo de monitoreo incorrecto para la planta: " + nombre);
		    }

		    if (!planta.estaActiva()) {
		        throw new RuntimeException("La planta está inactiva");
		    }

		    if (fechaHora == null || fechaHora.isEmpty()) {
		        throw new RuntimeException("Falta la fecha y hora");
		    }

		    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

		    if (planta.anteriorInicio(fecha)) {
		        throw new RuntimeException("Fecha anterior a la puesta en marcha");
		    }

		    if (energiaGenerada < 0 || incidencias < 0) {
		        throw new RuntimeException("La energía o las incidencias no pueden ser negativas");
		    }

		    if (!monitoreos.containsKey(nombre)) {
		        monitoreos.put(nombre, new ArrayList<DatosDeMonitoreo>());
		    }

		    for (DatosDeMonitoreo registro : monitoreos.get(nombre)) {
		        if (registro.consultarFechaHora().equals(fecha)) {
		            throw new RuntimeException("Ya existe un registro para esa hora");
		        }
		    }

		    DatosDeMonitoreo nuevoRegistro = new DatosDeMonitoreo(planta, fecha, energiaGenerada, incidencias);
		    monitoreos.get(nombre).add(nuevoRegistro);
		    planta.agregarRegistro(fecha, nuevoRegistro);
		}

	
	//Registro de plantas Eólicas
	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double velocidadViento) {
		Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	    if (!(planta instanceof plantaEolica)) {
	        throw new RuntimeException("La planta no es eólica: " + nombre);
	    }

	    if (!planta.estaActiva()) {
	        throw new RuntimeException("La planta está inactiva");
	    }

	    if (fechaHora == null || fechaHora.isEmpty()) {
	        throw new RuntimeException("Falta la fecha y hora");
	    }

	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

	    if (planta.anteriorInicio(fecha)) {
	        throw new RuntimeException(
	            "Fecha anterior a la puesta en marcha");
	    }

	    if (energiaGenerada < 0 || incidencias < 0
	            || velocidadViento < 0) {
	        throw new RuntimeException("Hay valores negativos");
	    }

	    if (!monitoreos.containsKey(nombre)) {
	        monitoreos.put(nombre,
	            new ArrayList<DatosDeMonitoreo>());
	    }

	    for (DatosDeMonitoreo registro : monitoreos.get(nombre)) {
	        if (registro.consultarFechaHora().equals(fecha)) {
	            throw new RuntimeException(
	                "Ya existe un registro para esa hora");
	        }
	    }

	    RegistroDEolica nuevoRegistro = new RegistroDEolica(
	    	    planta, fecha, energiaGenerada, incidencias, velocidadViento
	    	);

	    	monitoreos.get(nombre).add(nuevoRegistro);
	    	planta.agregarRegistro(fecha, nuevoRegistro);
	}
	
	
	//Registro de plantas Térmicas
	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double co2Emitido, double combustibleUtilizado) {
		 Planta planta = plantas.get(nombre);

		    if (planta == null) {
		        throw new RuntimeException("La planta no existe");
		    }
		    if (!(planta instanceof plantaTermica)) {
		        throw new RuntimeException("La planta no es térmica: " + nombre);
		    }

		    if (!planta.estaActiva()) {
		        throw new RuntimeException("La planta está inactiva");
		    }

		    if (fechaHora == null || fechaHora.isEmpty()) {
		        throw new RuntimeException("Falta la fecha y hora");
		    }

		    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

		    if (planta.anteriorInicio(fecha)) {
		        throw new RuntimeException(
		            "Fecha anterior a la puesta en marcha");
		    }

		    if (energiaGenerada < 0 || incidencias < 0
		            || co2Emitido < 0 || combustibleUtilizado < 0) {
		        throw new RuntimeException("Hay valores negativos");
		    }

		    if (!monitoreos.containsKey(nombre)) {
		        monitoreos.put(nombre,
		            new ArrayList<DatosDeMonitoreo>());
		    }

		    for (DatosDeMonitoreo registro : monitoreos.get(nombre)) {
		        if (registro.consultarFechaHora().equals(fecha)) {
		            throw new RuntimeException(
		                "Ya existe un registro para esa hora");
		        }
		    }

		    RegistroDTermica nuevoRegistro = new RegistroDTermica(planta, fecha, energiaGenerada,
		            incidencias, co2Emitido, combustibleUtilizado);
		    monitoreos.get(nombre).add(nuevoRegistro);
		    planta.agregarRegistro(fecha, nuevoRegistro);
	}


//5. DEVUELVE EL TOTAL DE ENERGIA GENERADA
	@Override
	public double totalGenerado(String nombre) {
		Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }

	    double total = 0;

	    if (monitoreos.containsKey(nombre)) {
	        for (DatosDeMonitoreo registro : monitoreos.get(nombre)) {
	            total += registro.consultarEnergiaGenerada();
	        }
	    }

	    return total;
	}
	
	
//6. DEVUELVE UN ARREGLO DE INCIDENCIAS REGISTRADAS POR HORA
	@Override
	public int[] incidenciasPorHora(String nombre) {
		Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }

	    int[] incidencias = new int[24];

	    if (monitoreos.containsKey(nombre)) {
	        for (DatosDeMonitoreo registro : monitoreos.get(nombre)) {
	            int hora = registro.consultarFechaHora().getHour();

	            incidencias[hora] += registro.consultarIncidencias();
	        }
	    }

	    return incidencias;
	}

	
//7. DEVUELVE UN ARREGLO CON 24 pos, X CADA HORA DEL DIA
	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		 Planta planta = plantas.get(nombre);

		    if (planta == null) {
		        throw new RuntimeException("La planta no existe: " + nombre);
		    }

		    LocalDate desde = LocalDate.parse(fechaDesde);
		    LocalDate hasta = LocalDate.parse(fechaHasta);

		    if (desde.isAfter(hasta)) {
		        throw new RuntimeException("La fecha inicial es posterior a la final");
		    }

		    int[] histograma = new int[24];
		    Set<String> diasContados = new HashSet<>();

		    List<DatosDeMonitoreo> registros = monitoreos.get(nombre);

		    if (registros == null) {
		        return histograma;
		    }

		    for (DatosDeMonitoreo registro : registros) {
		        LocalDate fecha = registro.consultarFechaHora().toLocalDate();

		        if (!fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
		            int hora = registro.consultarFechaHora().getHour();

		            if (planta.cumplioCuota(registro)) {
		                String clave = fecha + "-" + hora;

		                if (diasContados.add(clave)) {
		                    histograma[hora]++;
		                }
		            }
		        }
		    }

		    return histograma;
	}

	
//8. RETORNA UN MAP CON LA CANT. DE PLANTAS RENOVABLES EXISTENTES
	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		Map<String, Integer> ret = new TreeMap<String, Integer>();

	    plantas.values().forEach(planta -> {
	        if (planta instanceof plantaRenovable) {

	            plantaRenovable renovable = (plantaRenovable) planta;
	            String fuente = renovable.consultarFuentePrimaria();

	            if (ret.containsKey(fuente)) {
	                ret.put(fuente, ret.get(fuente) + 1);
	            } else {
	                ret.put(fuente, 1);
	            }
	        }
	    });
	

	    return ret;
	}

	
//9. DEVUELVE UNA LISTA DE PLANTAS QUE NUNCA CUMPLIERON CON LA CUOTA
	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		List<String> resultado = new ArrayList<>();
	    
	    for (Planta planta : this.plantas.values()) {
	        if (planta.nuncaCumplioCuota()) {
	        	resultado.add(planta.getNombre());
	        	}
	        }
		return resultado;
	}


//10. DEVUELVE PARA C/PLANTA EOLICA LAS HORAS EN LAS QUE NUNCA ALCANZÓ LA VEL. MIN. DE VIENTO
	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
		StringBuilder sb = new StringBuilder("Plantas Eolicas: Horas con poco viento \n");
		for (Planta planta : plantas.values()) {
			if (planta instanceof plantaEolica) {
				plantaEolica eolica = (plantaEolica) planta;
				String reportePlanta = eolica.generarReporteDPocoViento();
				if(!reportePlanta.isEmpty()) {
					sb.append(reportePlanta);
				}
			}
		}
		return sb.toString();
	}


//11. ELIMINA LOS REGISTROS QUE TIENEN 0 ENERGIA Y 0 INCIDENCIAS
	@Override
	public int limpiarMonitoreosSinDatos() {
		  int borrados = 0;

		    for (Planta planta : plantas.values()) {
		        borrados += planta.limpiarRegistrosVacios();
		    }

		    for (List<DatosDeMonitoreo> lista : monitoreos.values()) {
		        int i = 0;

		        while (i < lista.size()) {
		            if (lista.get(i).estaVacio()) {
		                lista.remove(i);
		            } else {
		                i++;
		            }
		        }
		    }

		    return borrados;
		}

	
//12. DEVUELVE UNA LISTA DE PLANTAS ORDENADAS DE MAYOR A MENOR SEGUN LA ENERGIA GENERADA
	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
		 List<String> nombres = new ArrayList<>();
		    Map<String, Double> totales = new HashMap<>();

		    // Calculamos la energía total generada por cada planta.
		    for (String nombre : plantas.keySet()) {
		        nombres.add(nombre);
		        totales.put(nombre, totalGenerado(nombre));
		    }

		    // Ordenamos los nombres de mayor a menor energía generada.
		    Collections.sort(nombres, (nombre1, nombre2) ->
		        Double.compare(totales.get(nombre2), totales.get(nombre1))
		    );

		    // Construimos la lista con el formato solicitado.
		    List<String> resultado = new ArrayList<>();

		    for (String nombre : nombres) {
		        StringBuilder linea = new StringBuilder();
		        linea.append(nombre).append(": ").append(totales.get(nombre));
		        resultado.add(linea.toString());
		    }

		    return resultado;
	}


//13. CONSULTA PLANTAS INACTIVAS EN LA FECHA Y HORA INDICADA Y LAS DEVUELVE EN UNA LISTA
	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
		List<String> inactivas = new ArrayList<>();
		LocalDateTime fechaParsed = LocalDateTime.parse(fechaHora);

	    for (Planta planta : plantas.values()) {
	        if (planta.horaInactiva(fechaParsed)) {
	            inactivas.add(planta.getNombre());
	        }
	    }
	    return inactivas;
	}
	@Override
	 public String toString() {
	        StringBuilder sb = new StringBuilder();

	        sb.append("Sistema SmartGrids\n");
	        sb.append("Plantas registradas:\n");

	        for (String planta : consultarPlantas()) {
	            sb.append("- ").append(planta).append("\n");
	        }

	        return sb.toString();
	    }
	 

}


