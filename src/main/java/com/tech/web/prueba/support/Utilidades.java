package com.tech.web.prueba.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.tech.web.prueba.dominio.ArticuloATrastear;
import com.tech.web.prueba.dto.ArchivoDto;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.support.Constantes.Mensajes;

public class Utilidades {
	/**
	 * instancia logger
	 */
	private static final AdmonLogger LOGGER = AdmonLogger.getInstance(Logger.getLogger(Utilidades.class));

	private Utilidades() {
	}

	/**
	 * Se obtiene el arreglo del viaje que menos peso tiene.
	 * 
	 * @return
	 */
	public static int[] getViajeMenosPesado(List<int[]> viajesArticulos) {
		int[] viajeMenor = new int[0];
		if (!viajesArticulos.isEmpty()) {
			viajeMenor = viajesArticulos.get(0);
			int sumatoriaAnterior = -1;
			for (int[] pesoArticulos : viajesArticulos) {
				int sumatorioPeso = 0;
				for (int peso : pesoArticulos) {
					sumatorioPeso += peso;
				}
				if (-1 != sumatoriaAnterior && sumatorioPeso < sumatoriaAnterior) {
					viajeMenor = pesoArticulos;
				}

			}
		}

		return viajeMenor;
	}

	/**
	 * Se obtiene un arreglo de tipo int con los pesos de los articulos a
	 * trastiar.
	 * 
	 * @param articulosATrastiar
	 * @return
	 */
	public static List<Integer> getCollectionDePesosDeLosArticulosDelDia(ArticuloATrastear[] articulosATrastear) {
		List<Integer> listaArticulosATrastear = null;
		if (null != articulosATrastear) {
			listaArticulosATrastear = new ArrayList<>(articulosATrastear.length);
			for (ArticuloATrastear articuloATrastear : articulosATrastear) {
				listaArticulosATrastear.add(articuloATrastear.getPeso());
			}
			Collections.sort(listaArticulosATrastear, Collections.reverseOrder());
		}

		return listaArticulosATrastear;
	}

	/**
	 * Se obtiene una instancia de tipo Archivo a partir de un Archivo de tipo
	 * FileItem.
	 * 
	 * @param fileItem
	 * @return una instancia de tipo Archivo.
	 * @throws CargaPerezosaException
	 * @throws Exception
	 */
	public static ArchivoDto getArchivoByFileItem(FileItem fileItem) throws CargaPerezosaException {
		ArchivoDto archivo = null;
		if (!fileItem.isFormField()) {
			archivo = new ArchivoDto();
			String nombre = fileItem.getName();
			String[] nombreArchivo = nombre.split("\\.");
			if (!"txt".equals(nombreArchivo[1])) {
				throw new CargaPerezosaException(Mensajes.ERROR_EXTENSION_ARCHIVO.getMensaje());
			}
			archivo.setNombreOriginal(nombreArchivo[0]);
			archivo.setExtension(nombreArchivo[1]);
			archivo.setMimeType(fileItem.getContentType());
			File archivoADisco = new File(nombre);
			archivoADisco = new File(archivoADisco.getName());
			try {
				fileItem.write(archivoADisco);
			} catch (Exception e) {
				throw new CargaPerezosaException(String.format(
						Mensajes.ERROR_ARCHIVO_SIN_PERMISOS_ESCRITURA.getMensaje(), archivoADisco.getAbsolutePath()));
			}
			File inputFile = new File(archivoADisco.getAbsolutePath());
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(inputFile);
			} catch (FileNotFoundException e) {
				throw new CargaPerezosaException(String.format(Mensajes.ERROR_ARCHIVO_NO_ENCONTRADO.getMensaje(),
						archivoADisco.getAbsolutePath()));
			}
			byte[] fileBytes = new byte[(int) inputFile.length()];
			try {
				inputStream.read(fileBytes);
				inputStream.close();
			} catch (IOException e) {
				throw new CargaPerezosaException(String.format(Mensajes.ERROR_ARCHIVO_SIN_PERMISOS_LECTURA.getMensaje(),
						archivoADisco.getAbsolutePath()));
			}
			archivo.setArchivo(fileBytes);
			if (inputFile.delete())
				LOGGER.debug("El fichero ha sido borrado satisfactoriamente");
			else
				LOGGER.debug("El fichero no puede ser borrado");
		}
		return archivo;
	}

	/**
	 * Guarda un archivo temporar en la carpeta home del usuario en sesion.
	 * 
	 * @param archivo
	 * @param extension
	 * @return
	 * @throws CargaPerezosaException
	 * @throws IOException
	 */
	public static String guardarArchivoFisicamente(byte[] archivo, String extension, String ruta)
			throws CargaPerezosaException {
		File carpetaTemporal;
		carpetaTemporal = new File(ruta);
		File temp = null;
		try {
			temp = File.createTempFile(Constantes.PREFIJO_ARCHIVO_INPUT, "." + extension, carpetaTemporal);
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(String.format(Mensajes.ERROR_ARCHIVO_NO_CREADO.getMensaje(),
					Constantes.PREFIJO_ARCHIVO_INPUT, "." + extension, ruta));
		}
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			fos.write(archivo);
		} catch (FileNotFoundException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(String.format(Mensajes.ERROR_ARCHIVO_NO_ENCONTRADO.getMensaje(),
					ruta + Constantes.SEPARADOR_ARCHIVOS + temp.getName()));
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(String.format(Mensajes.ERROR_ARCHIVO_SIN_PERMISOS_ESCRITURA.getMensaje(),
					ruta + Constantes.SEPARADOR_ARCHIVOS + temp.getName()));
		}
		return temp.getName();
	}

	/**
	 * Se obtiene el arreglo de Byte a partir del nombre temporal del archivo.
	 * 
	 * @param nombreTemporal
	 * @return
	 * @throws CargaPerezosaException
	 * @throws IOException
	 */
	public static byte[] obtenerArchivoFisicamente(String nombreTemporal) throws CargaPerezosaException {
		Path path = Paths.get(Constantes.RUTA_TEMPORAL + Constantes.SEPARADOR_ARCHIVOS + nombreTemporal);
		byte[] data;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(
					String.format(Mensajes.ERROR_ARCHIVO_NO_ENCONTRADO.getMensaje(), path.toString()));
		}
		return data;
	}

	/**
	 * Escribe en un archivo un texto, sino existe el archivo lo crea.
	 * 
	 * @param rutaArchivo
	 * @param texto
	 * @throws CargaPerezosaException
	 */
	public static void escribirArchivoFisico(String rutaArchivo, String texto) throws CargaPerezosaException {
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
			LOGGER.warn(e);
			throw new CargaPerezosaException(
					String.format(Mensajes.ERROR_ARCHIVO_SIN_PERMISOS_ESCRITURA.getMensaje(), rutaArchivo));
		}

	}

	/**
	 * Obtener la cadena del archivo completa.
	 * 
	 * @param rutaArchivo
	 * @return
	 * @throws CargaPerezosaException
	 */
	public static String getCadenaDelContenidoArchivo(String rutaArchivo) throws CargaPerezosaException {
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
			LOGGER.warn(e);
			throw new CargaPerezosaException(
					String.format(Mensajes.ERROR_ARCHIVO_NO_ENCONTRADO.getMensaje(), rutaArchivo));
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(
					String.format(Mensajes.ERROR_ARCHIVO_SIN_PERMISOS_LECTURA.getMensaje(), rutaArchivo));
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
	public static void descargaArchivoEnCliente(byte[] archivo, String mimeType, HttpServletResponse response,
			String nombreArchivo, String extensionArchivo) throws CargaPerezosaException {
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
			LOGGER.warn(ex);
			ex.printStackTrace();
			try {
				response.getWriter().print("IO Error: " + ex.getMessage());
			} catch (IOException e) {
				LOGGER.warn(e);
				throw new CargaPerezosaException(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw new CargaPerezosaException(Mensajes.ERROR_DESCONOCIDO.getMensaje());
		}
	}
}
