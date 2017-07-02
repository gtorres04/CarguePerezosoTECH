/**
 * 
 */
package com.tech.web.prueba.negocio;

import com.tech.web.prueba.dominio.DiaDeTrabajo;
import com.tech.web.prueba.exception.CargaPerezosaException;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public interface IItinerarioDeTrabajoWilsonNegocio {


	/**
	 * Se organiza el itinerario de trabajo de Wilson
	 * @param rutaArchivo
	 * @return
	 * @throws CargaPerezosaException
	 */
	DiaDeTrabajo[] organizarItinerario(String rutaArchivo) throws CargaPerezosaException;
	
	/**
	 * Calcula el maximo numero de viajes que puede hacer Wilson en el dia
	 * laboral.
	 * 
	 * @param diaDeTrabajo
	 * @return
	 */
	int maximoNumeroDeViajesEnElDia(DiaDeTrabajo diaDeTrabajo);

}
