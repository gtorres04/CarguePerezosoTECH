/**
 * 
 */
package com.tech.web.prueba.service;

import com.tech.web.prueba.dto.ArchivoDto;
import com.tech.web.prueba.dto.TrazaIntentoDto;
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
	 * @param trazaIntentoDto
	 * @throws CargaPerezosaException
	 */
	void procesarEntrada(TrazaIntentoDto trazaIntentoDto) throws CargaPerezosaException;

	/**
	 * Se obtiene una instancia de ArchivoDto a partir del nombre del Archivo
	 * temporal.
	 * 
	 * @param nombreArchivoTemporal
	 * @return
	 */
	ArchivoDto getArchivoDtoByNombreArchivoTemporal(String nombreArchivoTemporal);
}
