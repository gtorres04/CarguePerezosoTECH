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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tech.web.prueba.dominio.DiaDeTrabajo;
import com.tech.web.prueba.dto.ArchivoDto;
import com.tech.web.prueba.dto.TrazaIntentoDto;
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
	@SuppressWarnings("static-access")
	public void procesarEntrada(TrazaIntentoDto trazaIntentoDto) throws CargaPerezosaException {
		try {
			String nombreTempArchivo = guardarArchivoFisicamente(trazaIntentoDto.getArchivoInput().getArchivo(),
					trazaIntentoDto.getArchivoInput().getExtension());
			trazaIntentoDto.getArchivoInput().setNombreTemporal(nombreTempArchivo.split("\\.")[0]);
			DiaDeTrabajo[] itinerarioDeTrabajo = iItinerarioDeTrabajoWilsonNegocio
					.organizarItinerario(this.rutaTemporal + separadorDeArchivos + nombreTempArchivo);
			String nombreTempOutput = nombreTempArchivo.replace(Constantes.PREFIJO_ARCHIVO_INPUT,
					Constantes.PREFIJO_ARCHIVO_OUTPUT);
			int ordenDiaDeTrabajo = 1;
			for (DiaDeTrabajo diaDeTrabajo : itinerarioDeTrabajo) {
				int numeroMaxViajes = iItinerarioDeTrabajoWilsonNegocio.maximoNumeroDeViajesEnElDia(diaDeTrabajo);
				String texto = "Case #" + ordenDiaDeTrabajo + ": " + numeroMaxViajes;
				escribirArchivoFisico(this.rutaTemporal + separadorDeArchivos + nombreTempOutput, texto);
				ordenDiaDeTrabajo++;
			}
			ArchivoDto archivoOutput = new ArchivoDto();
			archivoOutput.setNombreOriginal(Constantes.PREFIJO_ARCHIVO_OUTPUT);
			archivoOutput.setArchivo(this.obtenerArchivoFisicamente(nombreTempOutput));
			archivoOutput.setMimeType(trazaIntentoDto.getArchivoInput().getMimeType());
			String[] nombreArchivo = nombreTempOutput.split("\\.");
			archivoOutput.setNombreTemporal(nombreArchivo[0]);
			archivoOutput.setExtension(nombreArchivo[1]);
			trazaIntentoDto.setArchivoOutput(archivoOutput);
			this.historialTrazaIntentos.add(trazaIntentoDto);
		} catch (IOException e) {
			throw new CargaPerezosaException(e.getMessage());
		}
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
		for (TrazaIntentoDto trazaIntentoDto : historialTrazaIntentos) {
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
	private void escribirArchivoFisico(String rutaArchivo, String texto) throws CargaPerezosaException {
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

	/**
	 * Descargar el archivo en cliente.
	 * 
	 * @param archivo
	 * @param mimeType
	 * @param request
	 * @param response
	 * @param nombreArchivo
	 * @param extensionArchivo
	 * @throws CargaPerezosaException
	 */
	@Override
	public void descargaArchivoEnCliente(byte[] archivo, String mimeType, HttpServletRequest request,
			HttpServletResponse response, String nombreArchivo, String extensionArchivo) throws CargaPerezosaException {
		try {
			byte[] archivoADescargar = archivo;

			response.setContentType(mimeType);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", nombreArchivo + "." + extensionArchivo);
			response.setHeader(headerKey, headerValue);

			OutputStream outStream = response.getOutputStream();
			outStream.write(archivoADescargar);
			outStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
			try {
				response.getWriter().print("IO Error: " + ex.getMessage());
			} catch (IOException e) {
				throw new CargaPerezosaException(e.getMessage());
			}
		} catch (Exception e) {
			throw new CargaPerezosaException(e.getMessage());
		}
	}
}
