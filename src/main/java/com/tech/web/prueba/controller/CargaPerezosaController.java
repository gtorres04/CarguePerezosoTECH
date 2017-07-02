package com.tech.web.prueba.controller;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.tech.web.prueba.dominio.Archivo;
import com.tech.web.prueba.dominio.TrazaIntento;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.service.ICargaPerezosaService;
import com.tech.web.prueba.support.ConstantesMappingURL;

@Controller
public class CargaPerezosaController {

	@Autowired
	ICargaPerezosaService iCargaPerezosaService;

	@Autowired
	@Qualifier("miServletFileUpload")
	private ServletFileUpload servletFileUpload;

	/**
	 * Se recibe el archivo INPUT para procesar.
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ConstantesMappingURL.CARGAR_ARCHIVO, method = RequestMethod.POST)
	public String cargarInputCargaPerezosa(Model model, HttpServletRequest request) {
		TrazaIntento trazaIntentoDto = new TrazaIntento();
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
					Archivo archivo;
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
			} catch (CargaPerezosaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Se obtiene una instancia de tipo Archivo a partir de un Archivo de tipo
	 * FileItem.
	 * 
	 * @param fileItem
	 * @return una instancia de tipo Archivo.
	 * @throws Exception
	 */
	private Archivo getArchivoByFileItem(FileItem fileItem) throws Exception {
		Archivo archivo = null;
		if (!fileItem.isFormField()) {
			archivo = new Archivo();
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