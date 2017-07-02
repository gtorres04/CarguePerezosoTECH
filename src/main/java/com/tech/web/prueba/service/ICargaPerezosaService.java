/**
 * 
 */
package com.tech.web.prueba.service;

import com.tech.web.prueba.dominio.TrazaIntento;
import com.tech.web.prueba.exception.CargaPerezosaException;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public interface ICargaPerezosaService {
	/**
	 * Se obtiene el archivo cargado con la cedula y se envia al modelo de
	 * negocio para procesar los datos en los dominios correspondientes.
	 * 
	 * @return
	 * @throws CargaPerezosaException
	 */
	void procesarEntrada(TrazaIntento trazaIntentoDto) throws CargaPerezosaException;
}
