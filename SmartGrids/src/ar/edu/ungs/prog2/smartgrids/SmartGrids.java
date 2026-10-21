package ar.edu.ungs.prog2.smartgrids;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;



public class SmartGrids implements ISmartGrids {
	private Map<String, Planta> plantas = new LinkedHashMap<String, Planta>();
	private Map<String, List<DatosDeMonitoreo>> monitoreos = new LinkedHashMap<String, List<DatosDeMonitoreo>>();

	
   @Override
	public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if (plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta con ese nombre: " + nombre);
        }
        
        plantaTermica termica = new plantaTermica(nombre, direccion, capacidadMaxima, 
                fechaHoraPuestaEnFuncionamiento, tipoCombustible, 
                objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
        
        plantas.put(nombre, termica);
	}

	@Override
	public List<String> consultarPlantas() {
	    List<String> resultado = new ArrayList<>();

	    for (Planta planta : plantas.values()) {
	        resultado.add(planta.toString());
	    }

	    return resultado;
	
	}

	@Override
	public double capacidadMinimaCuota(String nombre) {
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    return planta.capacidadGeneracion();
	}

	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		// TODO Auto-generated method stub
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    planta.activar();
	}

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		// TODO Auto-generated method stub
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    planta.desactivar();
	}

	@Override
	public boolean estaActiva(String nombre) {
		// TODO Auto-generated method stub
		Planta planta = plantas.get(nombre);
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    return planta.estaActiva();

	}


	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
		// TODO Auto-generated method stub
		 Planta planta = plantas.get(nombre);

		    if (planta == null) {
		        throw new RuntimeException("La planta no existe");
		    }

		    if (!planta.estaActiva()) {
		        throw new RuntimeException("La planta está inactiva");
		    }

		    if (fechaHora == null || fechaHora.isEmpty()) {
		        throw new RuntimeException("Falta la fecha y hora");
		    }

		    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

		    if (fecha.isBefore(planta.fechaEnFuncionamiento)) {
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

		    monitoreos.get(nombre).add(
		        new DatosDeMonitoreo(planta, fecha, energiaGenerada, incidencias)
		    );
		}

	

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double velocidadViento) {
		// TODO Auto-generated method stub
		Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }

	    if (!planta.estaActiva()) {
	        throw new RuntimeException("La planta está inactiva");
	    }

	    if (fechaHora == null || fechaHora.isEmpty()) {
	        throw new RuntimeException("Falta la fecha y hora");
	    }

	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

	    if (fecha.isBefore(planta.fechaEnFuncionamiento)) {
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

	    monitoreos.get(nombre).add(
	        new RegistroDEolica(planta, fecha, energiaGenerada,
	            incidencias, velocidadViento)
	    );
	}
	

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double co2Emitido, double combustibleUtilizado) {
		// TODO Auto-generated method stub
		 Planta planta = plantas.get(nombre);

		    if (planta == null) {
		        throw new RuntimeException("La planta no existe");
		    }

		    if (!planta.estaActiva()) {
		        throw new RuntimeException("La planta está inactiva");
		    }

		    if (fechaHora == null || fechaHora.isEmpty()) {
		        throw new RuntimeException("Falta la fecha y hora");
		    }

		    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

		    if (fecha.isBefore(planta.fechaEnFuncionamiento)) {
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

		    monitoreos.get(nombre).add(
		        new RegistroDTermica(planta, fecha, energiaGenerada,
		            incidencias, co2Emitido, combustibleUtilizado)
		    );
	}

	@Override
	public double totalGenerado(String nombre) {
		// TODO Auto-generated method stub
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
	
	

	@Override
	public int[] incidenciasPorHora(String nombre) {
		// TODO Auto-generated method stub
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

	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		// TODO Auto-generated method stub
		Map<String, Integer> ret = new LinkedHashMap<String, Integer>();

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

	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		// TODO Auto-generated method stub
		List<String> resultado = new ArrayList<>();
	    
	    for (Planta planta : this.plantas.values()) {
	        if (planta.nuncaCumplioCuota()) {
	        	resultado.add(planta.getNombre());
	        	}
	        }
		return resultado;
	}

	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
		// TODO Auto-generated method stub
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

	@Override
	public int limpiarMonitoreosSinDatos() {
		// TODO Auto-generated method stub
		int borrados = 0;
		for (Planta planta : plantas.values()) {
			borrados += planta.limpiarRegistrosVacios();
		}
		return borrados;
	}

	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
		// TODO Auto-generated method stub
		List<String> inactivas = new ArrayList<>();
		LocalDateTime fechaParsed = LocalDateTime.parse(fechaHora);

	    for (Planta planta : plantas.values()) {
	        if (planta.horaInactiva(fechaParsed)) {
	            inactivas.add(planta.getNombre());
	        }
	    }
	    return inactivas;
	}

}
