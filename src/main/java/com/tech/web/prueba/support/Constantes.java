package com.tech.web.prueba.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.tech.web.prueba.dto.TrazaIntentoDto;

public class Constantes {
	
	private Constantes() {}

	private static ResourceBundle bundle;
	private static Properties properties;
	
	static {
		bundle = ResourceBundle.getBundle("com.tech.web.nl.cargueperezosoResource");
		properties = System.getProperties();
	}
	
	public static final String PREFIJO_ARCHIVO_INPUT = "lazy_loading_input";
	public static final String PREFIJO_ARCHIVO_OUTPUT = "lazy_loading_output";
	public static final String RUTA_TEMPORAL = properties.getProperty("user.home");
	public static final String SEPARADOR_ARCHIVOS = properties.getProperty("file.separator");
	
	public static List<TrazaIntentoDto> historialTrazaIntentos = new ArrayList<>();

	public enum Mensajes {
		
		ERROR_DESCONOCIDO(bundle.getString("msn.error_desconocido")),
		ERROR_EXTENSION_ARCHIVO(bundle.getString("msn.error_extension_archivo")),
		
		ERROR_ARCHIVO_NO_ENCONTRADO(bundle.getString("msn.error_archivo_no_econtrado")),
		ERROR_ARCHIVO_NO_CREADO(bundle.getString("msn.error_archivo_no_creado")),
		ERROR_ARCHIVO_SIN_PERMISOS_ESCRITURA(bundle.getString("msn.error_archivo_no_permisos_escritura")),
		ERROR_ARCHIVO_SIN_PERMISOS_LECTURA(bundle.getString("msn.error_archivo_no_permisos_lectura")),
		
		ERROR_RESTRICCION_PESO_ARTICULO(bundle.getString("msn.error.restriccion.peso_articulo")),
		ERROR_CASTING_PESO_ARTICULO(bundle.getString("msn.error_casting_string_integer_peso")),
		
		ERROR_RESTRICCION_CANTIDAD_ARTICULOS_TRATEAR(bundle.getString("msn.error.restriccion.cantidad_articulos_trastear")),
		ERROR_CASTING_CANTIDAD_ARTICULOS_TRASTEAR(bundle.getString("msn.error_casting_string_integer_cantidad_articulos_trastear")),
		
		ERROR_RESTRICCION_CANTIDAD_DIAS_LABORALES(bundle.getString("msn.error.restriccion.dias_laborales")),
		ERROR_CASTING_CANTIDAD_DIAS_LABORALES(bundle.getString("msn.error_casting_string_integer_dias_laborales"));
		

		Mensajes(String msn) {
			mensaje = msn;
		}

		private String mensaje;

		public String getMensaje() {
			return mensaje;
		}
	}

}
