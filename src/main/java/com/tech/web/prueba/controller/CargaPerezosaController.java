package com.tech.web.prueba.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.tech.web.prueba.dto.ArchivoDto;
import com.tech.web.prueba.dto.TrazaIntentoDto;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.service.ICargaPerezosaService;
import com.tech.web.prueba.support.AdmonLogger;
import com.tech.web.prueba.support.Constantes.Mensajes;
import com.tech.web.prueba.support.ConstantesMappingURL;

@Controller
public class CargaPerezosaController {

	/**
	 * instancia logger
	 */
	private static final AdmonLogger LOGGER = AdmonLogger.getInstance(Logger.getLogger(CargaPerezosaController.class));

	@Autowired
	ICargaPerezosaService iCargaPerezosaService;

	@Autowired
	@Qualifier("miServletFileUpload")
	private ServletFileUpload servletFileUpload;

	/**
	 * Descargar el archivo output.
	 * 
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = ConstantesMappingURL.DESCARGAR_ARCHIVO_OUTPUT_URL_MAPPING
			+ "{nombreArchivo}", method = RequestMethod.GET)
	public void descargarArchivo(@PathVariable("nombreArchivo") String nombre, HttpServletRequest request,
			HttpServletResponse response) {
		String nombreArchivo = nombre;
		ArchivoDto archivoDto = iCargaPerezosaService.getArchivoDtoByNombreArchivoTemporal(nombreArchivo);
		byte[] archivo = archivoDto.getArchivo();
		String mimeType = archivoDto.getMimeType();
		String extensionArchivo = archivoDto.getExtension();
		try {
			iCargaPerezosaService.descargaArchivoEnCliente(archivo, mimeType, request, response, nombreArchivo,
					extensionArchivo);
		} catch (CargaPerezosaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Se recibe el archivo INPUT para procesar.
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = ConstantesMappingURL.CARGAR_ARCHIVO_URL_MAPPING, method = RequestMethod.POST)
	public String cargarInputCargaPerezosa(Model model, HttpServletRequest request) {
		LOGGER.debug("Hola");
		System.out.println(Mensajes.ERROR_DESCONOCIDO.getMensaje());
		String view = ConstantesMappingURL.PAGINA_PRINCIPAL_PAG;
		TrazaIntentoDto trazaIntentoDto = new TrazaIntentoDto();
		trazaIntentoDto.setFechaEjecucion(new Date());
		try {
			List<?> fileItemsList = servletFileUpload.parseRequest(request);
			Iterator<?> it = fileItemsList.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (item.isFormField()) {
					String fieldvalue = item.getString();
					trazaIntentoDto.setCedula(fieldvalue);
				} else {
					ArchivoDto archivo;
					try {
						archivo = getArchivoByFileItem(item);
						trazaIntentoDto.setArchivoInput(archivo);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				iCargaPerezosaService.procesarEntrada(trazaIntentoDto);
				model.addAttribute("trazaIntento", trazaIntentoDto);
				model.addAttribute("historialTrazaIntento", iCargaPerezosaService.historialTrazaIntentos);
			} catch (CargaPerezosaException e) {
				model.addAttribute("error", e.getMessage());
			}
		} catch (FileUploadException e) {
			model.addAttribute("error", e.getMessage());
		}

		return view;
	}

	/**
	 * Se obtiene una instancia de tipo Archivo a partir de un Archivo de tipo
	 * FileItem.
	 * 
	 * @param fileItem
	 * @return una instancia de tipo Archivo.
	 * @throws Exception
	 */
	private ArchivoDto getArchivoByFileItem(FileItem fileItem) throws Exception {
		ArchivoDto archivo = null;
		if (!fileItem.isFormField()) {
			archivo = new ArchivoDto();
			String nombre = fileItem.getName();
			String[] nombreArchivo = nombre.split("\\.");
			archivo.setNombreOriginal(nombreArchivo[0]);
			archivo.setExtension(nombreArchivo[1]);
			archivo.setMimeType(fileItem.getContentType());
			File archivoADisco = new File(nombre);
			archivoADisco = new File(archivoADisco.getName());
			fileItem.write(archivoADisco);
			File inputFile = new File(archivoADisco.getAbsolutePath());
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] fileBytes = new byte[(int) inputFile.length()];
			inputStream.read(fileBytes);
			inputStream.close();
			archivo.setArchivo(fileBytes);
			if (inputFile.delete())
				System.out.println("El fichero ha sido borrado satisfactoriamente");
			else
				System.out.println("El fichero no puede ser borrado");
		}
		return archivo;
	}
}