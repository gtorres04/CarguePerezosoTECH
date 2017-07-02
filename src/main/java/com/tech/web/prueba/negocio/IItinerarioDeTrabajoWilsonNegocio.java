/**
 * 
 */
package com.tech.web.prueba.negocio;

import com.tech.web.prueba.exception.CargaPerezosaException;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public interface IItinerarioDeTrabajoWilsonNegocio {

	void agregarItinerario(String rutaArchivo) throws CargaPerezosaException;

}
