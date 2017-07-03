/**
 * 
 */
package com.tech.web.prueba.support;

import org.apache.log4j.Logger;

/**
 * @author gtorress
 *
 */
public class AdmonLogger {
	/**
	 * Referencia logger
	 */
	private Logger logger;

	/**
	 * @param logger
	 */
	private AdmonLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Crea una instancia de AdmonLogger recibiendo la instancia de Logger
	 * 
	 * @param logger
	 * @return
	 */
	public static AdmonLogger getInstance(Logger logger) {
		return new AdmonLogger(logger);
	}

	/**
	 * Imprime en el appender un error: mensaje y excepcion.
	 * 
	 * @param mensaje
	 * @param exception
	 */
	public void error(String mensaje, Throwable exception) {
		logger.error(mensaje, exception);
	}

	/**
	 * Imprime en el appender un error, con una excepcion.
	 * 
	 * @param exception
	 */
	public void error(Throwable exception) {
		logger.error(exception);
	}

	/**
	 * Imprime en el appender un error, con un mensaje.
	 * 
	 * @param mensaje
	 */
	public void error(String mensaje) {
		logger.error(mensaje);
	}

	/**
	 * Imprime en el appender un warn: mensaje y excepcion.
	 * 
	 * @param mensaje
	 * @param exception
	 */
	public void warn(String mensaje, Throwable exception) {
		logger.warn(mensaje, exception);
	}

	/**
	 * Imprime en el appender un warn, con una excepcion.
	 * 
	 * @param exception
	 */
	public void warn(Throwable exception) {
		logger.warn(exception);
	}

	/**
	 * Imprime en el appender un warn, con un mensaje.
	 * 
	 * @param mensaje
	 */
	public void warn(String mensaje) {
		logger.warn(mensaje);
	}

	/**
	 * Imprime en el appender un debug: mensaje y excepcion. Ademas Verifica si
	 * en el Logger de esta clase esta habilitado el nivel debug.
	 * 
	 * @param mensaje
	 * @param exception
	 */
	public void debug(String mensaje, Throwable exception) {
		if (logger.isDebugEnabled()) {
			logger.debug(mensaje, exception);
		}
	}

	/**
	 * Imprime en el appender un debug, con una excepcion. Ademas Verifica si en
	 * el Logger de esta clase esta habilitado el nivel debug.
	 * 
	 * @param exception
	 */
	public void debug(Throwable exception) {
		if (logger.isDebugEnabled()) {
			logger.debug(exception);
		}
	}

	/**
	 * Imprime en el appender un debug, con un mensaje. Ademas Verifica si en el
	 * Logger de esta clase esta habilitado el nivel debug.
	 * 
	 * @param mensaje
	 */
	public void debug(String mensaje) {
		if (logger.isDebugEnabled()) {
			logger.debug(mensaje);
		}
	}

	/**
	 * Imprime en el appender un info: mensaje y excepcion. Ademas Verifica si
	 * en el Logger de esta clase esta habilitado el nivel info.
	 * 
	 * @param mensaje
	 * @param exception
	 */
	public void info(String mensaje, Throwable exception) {
		if (logger.isInfoEnabled()) {
			logger.info(mensaje, exception);
		}
	}

	/**
	 * Imprime en el appender un info, con la excepcion. Ademas Verifica si en
	 * el Logger de esta clase esta habilitado el nivel info.
	 * 
	 * @param exception
	 */
	public void info(Throwable exception) {
		if (logger.isInfoEnabled()) {
			logger.info(exception);
		}
	}

	/**
	 * Imprime en el appender un info, con un mensaje. Ademas Verifica si en el
	 * Logger de esta clase esta habilitado el nivel info.
	 * 
	 * @param mensaje
	 */
	public void info(String mensaje) {
		if (logger.isInfoEnabled()) {
			logger.info(mensaje);
		}
	}

	/**
	 * @param mensaje
	 * @param exception
	 */
	public void trace(String mensaje, Throwable exception) {
		if (logger.isTraceEnabled()) {
			logger.trace(mensaje, exception);
		}
	}

	/**
	 * @param exception
	 */
	public void trace(Throwable exception) {
		if (logger.isTraceEnabled()) {
			logger.trace(exception);
		}
	}

	/**
	 * @param mensaje
	 */
	public void trace(String mensaje) {
		if (logger.isTraceEnabled()) {
			logger.trace(mensaje);
		}
	}
}
