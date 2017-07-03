/**
 * 
 */
package com.tech.web.prueba.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.web.prueba.dominio.DiaDeTrabajo;
import com.tech.web.prueba.dto.ArchivoDto;
import com.tech.web.prueba.dto.TrazaIntentoDto;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio;
import com.tech.web.prueba.service.ICargaPerezosaService;
import com.tech.web.prueba.support.AdmonLogger;
import com.tech.web.prueba.support.Constantes;
import com.tech.web.prueba.support.Utilidades;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
@Service
public class CargaPerezosaServiceImpl implements ICargaPerezosaService {
	/**
	 * instancia logger
	 */
	private static final AdmonLogger LOGGER = AdmonLogger.getInstance(Logger.getLogger(CargaPerezosaServiceImpl.class));

	private String rutaTemporal;
	private String separadorDeArchivos;

	@Autowired
	IItinerarioDeTrabajoWilsonNegocio iItinerarioDeTrabajoWilsonNegocio;

	public CargaPerezosaServiceImpl() {
		separadorDeArchivos = Constantes.SEPARADOR_ARCHIVOS;
		rutaTemporal = Constantes.RUTA_TEMPORAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tech.web.prueba.service.ICargaPerezosaService#procesarEntrada()
	 */
	public void procesarEntrada(TrazaIntentoDto trazaIntentoDto) throws CargaPerezosaException {
		LOGGER.debug("procesando archivo");
		String nombreTempArchivo = Utilidades.guardarArchivoFisicamente(trazaIntentoDto.getArchivoInput().getArchivo(),
				trazaIntentoDto.getArchivoInput().getExtension(), rutaTemporal);
		trazaIntentoDto.getArchivoInput().setNombreTemporal(nombreTempArchivo.split("\\.")[0]);
		DiaDeTrabajo[] itinerarioDeTrabajo = iItinerarioDeTrabajoWilsonNegocio
				.organizarItinerario(this.rutaTemporal + separadorDeArchivos + nombreTempArchivo);
		String nombreTempOutput = nombreTempArchivo.replace(Constantes.PREFIJO_ARCHIVO_INPUT,
				Constantes.PREFIJO_ARCHIVO_OUTPUT);
		int ordenDiaDeTrabajo = 1;
		for (DiaDeTrabajo diaDeTrabajo : itinerarioDeTrabajo) {
			int numeroMaxViajes = iItinerarioDeTrabajoWilsonNegocio.maximoNumeroDeViajesEnElDia(diaDeTrabajo);
			String texto = "Case #" + ordenDiaDeTrabajo + ": " + numeroMaxViajes;
			Utilidades.escribirArchivoFisico(this.rutaTemporal + separadorDeArchivos + nombreTempOutput, texto);
			ordenDiaDeTrabajo++;
		}
		ArchivoDto archivoOutput = new ArchivoDto();
		archivoOutput.setNombreOriginal(Constantes.PREFIJO_ARCHIVO_OUTPUT);
		archivoOutput.setArchivo(Utilidades.obtenerArchivoFisicamente(nombreTempOutput));
		archivoOutput.setMimeType(trazaIntentoDto.getArchivoInput().getMimeType());
		String[] nombreArchivo = nombreTempOutput.split("\\.");
		archivoOutput.setNombreTemporal(nombreArchivo[0]);
		archivoOutput.setExtension(nombreArchivo[1]);
		trazaIntentoDto.setArchivoOutput(archivoOutput);
		Constantes.historialTrazaIntentos.add(trazaIntentoDto);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tech.web.prueba.service.ICargaPerezosaService#
	 * getArchivoDtoByNombreArchivoTemporal(java.lang.String)
	 */
	@Override
	public ArchivoDto getArchivoDtoByNombreArchivoTemporal(String nombreArchivoTemporal) {
		ArchivoDto archivoDto = null;
		for (TrazaIntentoDto trazaIntentoDto : Constantes.historialTrazaIntentos) {
			if (nombreArchivoTemporal.equals(trazaIntentoDto.getArchivoInput().getNombreTemporal())) {
				archivoDto = trazaIntentoDto.getArchivoInput();
				break;
			}
			if (nombreArchivoTemporal.equals(trazaIntentoDto.getArchivoOutput().getNombreTemporal())) {
				archivoDto = trazaIntentoDto.getArchivoOutput();
				break;
			}
		}
		return archivoDto;
	}

}
