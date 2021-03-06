package com.tech.web.prueba.controller;

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
import com.tech.web.prueba.support.Constantes;
import com.tech.web.prueba.support.ConstantesMappingURL;
import com.tech.web.prueba.support.Utilidades;

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
	 * Se inicia la aplicacion y se consulta si existe historial.
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/")
	public String inicio(Model model) {
		model.addAttribute("historialTrazaIntento", Constantes.historialTrazaIntentos);
		return "index";
	}

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
			Utilidades.descargaArchivoEnCliente(archivo, mimeType, response, nombreArchivo,
					extensionArchivo);
		} catch (CargaPerezosaException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Se recibe el archivo INPUT para procesar.
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ConstantesMappingURL.CARGAR_ARCHIVO_URL_MAPPING, method = RequestMethod.POST)
	public String cargarInputCargaPerezosa(Model model, HttpServletRequest request) {
		String view = ConstantesMappingURL.PAGINA_PRINCIPAL_PAG;
		TrazaIntentoDto trazaIntentoDto = new TrazaIntentoDto();
		trazaIntentoDto.setFechaEjecucion(new Date());

		List<?> fileItemsList;
		try {
			fileItemsList = servletFileUpload.parseRequest(request);

			Iterator<?> it = fileItemsList.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					if (item.isFormField()) {
						String fieldvalue = item.getString();
						trazaIntentoDto.setCedula(fieldvalue);
					} else {
						ArchivoDto archivo;
						archivo = Utilidades.getArchivoByFileItem(item);
						trazaIntentoDto.setArchivoInput(archivo);
					}
				}
				iCargaPerezosaService.procesarEntrada(trazaIntentoDto);
				model.addAttribute("trazaIntento", trazaIntentoDto);
			
		} catch (CargaPerezosaException | FileUploadException e) {
			model.addAttribute("error", e.getMessage());
		}
		model.addAttribute("historialTrazaIntento", Constantes.historialTrazaIntentos);
		return view;
	}

	
}