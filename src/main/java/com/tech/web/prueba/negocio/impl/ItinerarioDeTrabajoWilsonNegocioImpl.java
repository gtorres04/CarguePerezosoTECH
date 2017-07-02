/**
 * 
 */
package com.tech.web.prueba.negocio.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.tech.web.prueba.dominio.ArticuloATrastiar;
import com.tech.web.prueba.dominio.DiaDeTrabajo;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
@Repository
public class ItinerarioDeTrabajoWilsonNegocioImpl implements IItinerarioDeTrabajoWilsonNegocio {

	private DiaDeTrabajo[] itinerarioDeTrabajo;
	/**
	 * alamcena true si se va agregar un articulo
	 */
	private boolean agregarCantidadArticulosDeldia;
	/**
	 * alamcena true si se va agregar un el peso de un articulo.
	 */
	private boolean agregarPesoDelArticulo;
	/**
	 * Se almacena el numero del dia en gestion.
	 */
	private int ordenDelDiaEnGestion;
	/**
	 * Almacena el numero del articulo del dia en gestion.
	 */
	private int ordenDelArticuloEnGestion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio#
	 * agregarItinerario(java.lang.String)
	 */
	@Override
	public void agregarItinerario(String rutaArchivo) throws CargaPerezosaException {
		rastrearArchivoInput(rutaArchivo);
	}

	/**
	 * Recorre el archivo y se toman los datos del archivo Input.
	 * 
	 * @param rutaArchivo
	 * @throws CargaPerezosaException
	 */
	private void rastrearArchivoInput(String rutaArchivo) throws CargaPerezosaException {
		String cadena;
		FileReader f;
		int numeroLinea = 0;
		try {
			f = new FileReader(rutaArchivo);
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {

				if (0 == numeroLinea) {
					crearDiasDeTrabajo(cadena);
					this.agregarCantidadArticulosDeldia = true;
					numeroLinea++;
					continue;
				}

				if (this.agregarCantidadArticulosDeldia) {
					crearArticulosDelDiaATrastear(cadena);
					numeroLinea++;
					continue;
				}

				if (this.agregarPesoDelArticulo) {
					asignarValoresAArticulosATrastiar(cadena);
					numeroLinea++;
					continue;
				}
				
			}
			b.close();
		} catch (IOException e) {
			throw new CargaPerezosaException(e.getMessage());
		}
	}

	/**
	 * Se asigna el nombre y el peso de los articulos.
	 * 
	 * @param cadenaPesoDelArticuloATrastiar
	 * @throws CargaPerezosaException
	 */
	private void asignarValoresAArticulosATrastiar(String cadenaPesoDelArticuloATrastiar)
			throws CargaPerezosaException {
		int pesoDelArticuloATrastiar = 0;
		try {
			pesoDelArticuloATrastiar = Integer.parseInt(cadenaPesoDelArticuloATrastiar);
			if (1 >= pesoDelArticuloATrastiar && 100 <= pesoDelArticuloATrastiar) {
				throw new CargaPerezosaException(
						"Error en el peso del articulo a trastear, se debe recibir un numero no menor que 1 y no mayor de 100");
			}
		} catch (NumberFormatException e) {
			throw new CargaPerezosaException("Error en el peso del articulo a trastear, se debe recibir un numero");
		}
		itinerarioDeTrabajo[this.ordenDelDiaEnGestion-1].getArticulosATrastiar()[this.ordenDelArticuloEnGestion]
				.setNombre("Articulo" + (this.ordenDelDiaEnGestion-1) + this.ordenDelArticuloEnGestion);
		itinerarioDeTrabajo[this.ordenDelDiaEnGestion-1].getArticulosATrastiar()[this.ordenDelArticuloEnGestion]
				.setPeso(pesoDelArticuloATrastiar);
		this.ordenDelArticuloEnGestion++;
		if (this.ordenDelArticuloEnGestion == itinerarioDeTrabajo[this.ordenDelDiaEnGestion-1]
				.getArticulosATrastiar().length) {
			this.agregarPesoDelArticulo = false;
			this.agregarCantidadArticulosDeldia = true;
		}
		
	}

	/**
	 * Se crean los objetos de articulos a trastear, validando las restricciones
	 * de la cantidad de articulos a trastear diarios.
	 * 
	 * @param cadenaCantidadDeArticuloAtrastear
	 * @throws CargaPerezosaException
	 */
	private void crearArticulosDelDiaATrastear(String cadenaCantidadDeArticuloAtrastear) throws CargaPerezosaException {
		int cantidadDeArticuloAtrastear = 0;
		try {
			cantidadDeArticuloAtrastear = Integer.parseInt(cadenaCantidadDeArticuloAtrastear);
			if (1 >= cantidadDeArticuloAtrastear && 100 <= cantidadDeArticuloAtrastear) {
				throw new CargaPerezosaException(
						"Error en la cantidad de articulos a trastear, se debe recibir un numero no menor que 1 y no mayor de 100");
			}
		} catch (NumberFormatException e) {
			throw new CargaPerezosaException("Error en la cantidad de articulos a trastear, se debe recibir un numero");
		}
		ArticuloATrastiar[] articulosATrastiar = new ArticuloATrastiar[cantidadDeArticuloAtrastear];
		for (int i = 0; i < articulosATrastiar.length; i++) {
			articulosATrastiar[i] = new ArticuloATrastiar();
		}
		itinerarioDeTrabajo[this.ordenDelDiaEnGestion].setArticulosATrastiar(articulosATrastiar);
		this.agregarCantidadArticulosDeldia = false;
		this.ordenDelDiaEnGestion++;
		this.ordenDelArticuloEnGestion = 0;
		this.agregarPesoDelArticulo = true;
	}

	/**
	 * Se crean los objetos de dias de trabajos de wilson, validando las
	 * restricciones de la cantidad de dias de trabajo.
	 * 
	 * @param cantidadDeDias
	 */
	private void crearDiasDeTrabajo(String cadenaCantidadDeDias) throws CargaPerezosaException {
		int cantidadDeDias = 0;
		try {
			cantidadDeDias = Integer.parseInt(cadenaCantidadDeDias);
			if (1 >= cantidadDeDias && 500 <= cantidadDeDias) {
				throw new CargaPerezosaException(
						"Error en la cantidad de dias de trabajo, se debe recibir un numero no menor que 1 y no mayor de 500");
			}
		} catch (NumberFormatException e) {
			throw new CargaPerezosaException("Error en la cantidad de dias de trabajo, se debe recibir un numero");
		}
		itinerarioDeTrabajo = new DiaDeTrabajo[cantidadDeDias];
		for (int i = 0; i < itinerarioDeTrabajo.length; i++) {
			itinerarioDeTrabajo[i] = new DiaDeTrabajo();
		}
	}
}
