/**
 * 
 */
package com.tech.web.prueba.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.web.prueba.dominio.DiaDeTrabajo;
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
	private String separadorDeArchivos;

	@Autowired
	IItinerarioDeTrabajoWilsonNegocio iItinerarioDeTrabajoWilsonNegocio;

	public CargaPerezosaServiceImpl() {
		Properties properties = System.getProperties();
		String ruta = properties.getProperty("user.home");
		separadorDeArchivos = properties.getProperty("file.separator");
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
			DiaDeTrabajo[] itinerarioDeTrabajo = iItinerarioDeTrabajoWilsonNegocio
					.organizarItinerario(this.rutaTemporal + separadorDeArchivos + nombreTempArchivo);
			String nombreTempOutput = nombreTempArchivo.replace(Constantes.PREFIJO_ARCHIVO_INPUT,
					Constantes.PREFIJO_ARCHIVO_OUTPUT);
			for (DiaDeTrabajo diaDeTrabajo : itinerarioDeTrabajo) {
				int numeroMaxViajes=iItinerarioDeTrabajoWilsonNegocio.maximoNumeroDeViajesEnElDia(diaDeTrabajo);
			}
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
		File temp = File.createTempFile(Constantes.PREFIJO_ARCHIVO_INPUT, "." + extension, carpetaTemporal);
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
		Path path = Paths.get(rutaTemporal + separadorDeArchivos + nombreTemporal);
		byte[] data = Files.readAllBytes(path);
		return data;
	}

	/**
	 * Escribe en un archivo un texto, sino existe el archivo lo crea.
	 * 
	 * @param rutaArchivo
	 * @param texto
	 * @throws CargaPerezosaException
	 */
	public void escribirArchivoFisico(String rutaArchivo, String texto) throws CargaPerezosaException {
		File archivo = new File(rutaArchivo);
		BufferedWriter bw;
		try {
			if (archivo.exists()) {
				texto = getCadenaDelContenidoArchivo(rutaArchivo) + texto;
			}
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(texto);
			bw.close();
		} catch (IOException e) {
			throw new CargaPerezosaException("Problemas de escritura con el archivo temporal: " + rutaArchivo);
		}

	}

	/**
	 * Obtener la cadena del archivo completa.
	 * 
	 * @param rutaArchivo
	 * @return
	 * @throws CargaPerezosaException
	 */
	private String getCadenaDelContenidoArchivo(String rutaArchivo) throws CargaPerezosaException {
		String cadena;
		StringBuilder cadenaArchivo = new StringBuilder();
		FileReader f;
		try {
			f = new FileReader(rutaArchivo);
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				cadenaArchivo.append(cadena + "\n");
			}
			b.close();
		} catch (FileNotFoundException e) {
			throw new CargaPerezosaException("No se encontro el archivo temporal: " + rutaArchivo);
		} catch (IOException e) {
			throw new CargaPerezosaException("Problemas de lectura con el archivo temporal: " + rutaArchivo);
		}
		return cadenaArchivo.toString();
	}

}
