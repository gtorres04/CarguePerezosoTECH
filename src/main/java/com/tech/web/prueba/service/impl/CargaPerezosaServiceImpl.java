/**
 * 
 */
package com.tech.web.prueba.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.web.prueba.dominio.TrazaIntento;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio;
import com.tech.web.prueba.service.ICargaPerezosaService;
import com.tech.web.prueba.support.Constantes;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
@Service
public class CargaPerezosaServiceImpl implements ICargaPerezosaService {

	private String rutaTemporal;

	@Autowired
	IItinerarioDeTrabajoWilsonNegocio iItinerarioDeTrabajoWilsonNegocio;

	public CargaPerezosaServiceImpl() {
		Properties properties = System.getProperties();
		String ruta = properties.getProperty("user.home");
		rutaTemporal = ruta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tech.web.prueba.service.ICargaPerezosaService#procesarEntrada()
	 */
	public void procesarEntrada(TrazaIntento trazaIntentoDto) throws CargaPerezosaException {
		try {
			String nombreTempArchivo = guardarArchivoFisicamente(trazaIntentoDto.getArchivoInput().getArchivo(),
					trazaIntentoDto.getArchivoInput().getExtension());
			iItinerarioDeTrabajoWilsonNegocio.agregarItinerario(this.rutaTemporal+"/"+nombreTempArchivo);
		} catch (IOException e) {
			throw new CargaPerezosaException(e.getMessage());
		}
	}

	/**
	 * Guarda un archivo temporar en la carpeta home del usuario en sesion.
	 * 
	 * @param archivo
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	private String guardarArchivoFisicamente(byte[] archivo, String extension) throws IOException {
		File carpetaTemporal;
		carpetaTemporal = new File(rutaTemporal);
		File temp = File.createTempFile(Constantes.PREFIJO_ARCHIVO, "." + extension, carpetaTemporal);
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			fos.write(archivo);
		}
		return temp.getName();
	}

	/**
	 * Se obtiene el arreglo de Byte a partir del nombre temporal del archivo.
	 * 
	 * @param nombreTemporal
	 * @return
	 * @throws IOException
	 */
	private byte[] obtenerArchivoFisicamente(String nombreTemporal) throws IOException {
		Path path = Paths.get(rutaTemporal + "/" + nombreTemporal);
		byte[] data = Files.readAllBytes(path);
		return data;
	}

}
