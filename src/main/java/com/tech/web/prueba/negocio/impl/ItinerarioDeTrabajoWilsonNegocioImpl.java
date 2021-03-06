/**
 * 
 */
package com.tech.web.prueba.negocio.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.tech.web.prueba.dominio.ArticuloATrastear;
import com.tech.web.prueba.dominio.DiaDeTrabajo;
import com.tech.web.prueba.exception.CargaPerezosaException;
import com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio;
import com.tech.web.prueba.support.AdmonLogger;
import com.tech.web.prueba.support.Constantes.Mensajes;
import com.tech.web.prueba.support.Utilidades;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
@Repository
public class ItinerarioDeTrabajoWilsonNegocioImpl implements IItinerarioDeTrabajoWilsonNegocio {
	/**
	 * instancia logger
	 */
	private static final AdmonLogger LOGGER = AdmonLogger
			.getInstance(Logger.getLogger(ItinerarioDeTrabajoWilsonNegocioImpl.class));

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
	 * organizarItinerario(java.lang.String)
	 */
	@Override
	public DiaDeTrabajo[] organizarItinerario(String rutaArchivo) throws CargaPerezosaException {
		rastrearArchivoInput(rutaArchivo);
		agregarCantidadArticulosDeldia = false;
		agregarPesoDelArticulo = false;
		ordenDelDiaEnGestion = 0;
		ordenDelArticuloEnGestion = 0;
		return this.itinerarioDeTrabajo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tech.web.prueba.negocio.IItinerarioDeTrabajoWilsonNegocio#
	 * maximoNumeroDeViajesEnElDia(com.tech.web.prueba.dominio.DiaDeTrabajo)
	 */
	@Override
	public int maximoNumeroDeViajesEnElDia(DiaDeTrabajo diaDeTrabajo) {
		List<Integer> pesosArticulos = Utilidades
				.getCollectionDePesosDeLosArticulosDelDia(diaDeTrabajo.getArticulosATrastiar());
		List<int[]> viajesArticulos = new ArrayList<>();
		while (!pesosArticulos.isEmpty()) {
			int peso = pesosArticulos.get(0);
			int numeroDeProductosDebajoDeEl = (int) (Math.ceil(50.0 / peso) - 1);
			if ((numeroDeProductosDebajoDeEl + 1) > pesosArticulos.size()) {
				int[] viajeMenor = Utilidades.getViajeMenosPesado(viajesArticulos);
				int[] viajeNuevoAgregandoUltimosArticulos = new int[viajeMenor.length + pesosArticulos.size()];
				int i = 0;
				for (i = 0; i < viajeMenor.length; i++) {
					viajeNuevoAgregandoUltimosArticulos[i] = viajeMenor[i];
				}
				for (int pesoRestante : pesosArticulos) {
					viajeNuevoAgregandoUltimosArticulos[i++] = pesoRestante;
				}
				viajesArticulos.remove(viajeMenor);
				viajesArticulos.add(viajeNuevoAgregandoUltimosArticulos);
				pesosArticulos.clear();
				continue;
			}
			pesosArticulos.remove(0);
			int[] viajeDeArticulos = new int[numeroDeProductosDebajoDeEl + 1];
			int index = 0;
			viajeDeArticulos[index++] = peso;
			if (1 <= numeroDeProductosDebajoDeEl) {
				int controlIndiceAgregar = numeroDeProductosDebajoDeEl;
				while (0 < controlIndiceAgregar)
					viajeDeArticulos[index++] = pesosArticulos.get(pesosArticulos.size() - (controlIndiceAgregar--));
				controlIndiceAgregar = numeroDeProductosDebajoDeEl;
				for (int i = 0; i < controlIndiceAgregar; i++) {
					pesosArticulos.remove(pesosArticulos.size() - 1);
				}
			}
			viajesArticulos.add(viajeDeArticulos);
		}
		return viajesArticulos.size();
	}

	/**
	 * Recorre el archivo y se toman los datos del archivo Input.
	 * 
	 * @param rutaArchivo
	 * @throws CargaPerezosaException
	 */
	private void rastrearArchivoInput(String rutaArchivo) throws CargaPerezosaException {
		String cadena;
		FileReader f = null;
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
			f.close();
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(
					String.format(Mensajes.ERROR_ARCHIVO_NO_ENCONTRADO.getMensaje(), rutaArchivo));
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
				throw new CargaPerezosaException(Mensajes.ERROR_RESTRICCION_PESO_ARTICULO.getMensaje());
			}
		} catch (NumberFormatException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(Mensajes.ERROR_CASTING_PESO_ARTICULO.getMensaje());
		}
		itinerarioDeTrabajo[this.ordenDelDiaEnGestion - 1].getArticulosATrastiar()[this.ordenDelArticuloEnGestion]
				.setNombre("Articulo" + (this.ordenDelDiaEnGestion - 1) + this.ordenDelArticuloEnGestion);
		itinerarioDeTrabajo[this.ordenDelDiaEnGestion - 1].getArticulosATrastiar()[this.ordenDelArticuloEnGestion]
				.setPeso(pesoDelArticuloATrastiar);
		this.ordenDelArticuloEnGestion++;
		if (this.ordenDelArticuloEnGestion == itinerarioDeTrabajo[this.ordenDelDiaEnGestion - 1]
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
				throw new CargaPerezosaException(Mensajes.ERROR_RESTRICCION_CANTIDAD_ARTICULOS_TRATEAR.getMensaje());
			}
		} catch (NumberFormatException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(Mensajes.ERROR_CASTING_CANTIDAD_ARTICULOS_TRASTEAR.getMensaje());
		}
		ArticuloATrastear[] articulosATrastiar = new ArticuloATrastear[cantidadDeArticuloAtrastear];
		for (int i = 0; i < articulosATrastiar.length; i++) {
			articulosATrastiar[i] = new ArticuloATrastear();
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
				throw new CargaPerezosaException(Mensajes.ERROR_RESTRICCION_CANTIDAD_DIAS_LABORALES.getMensaje());
			}
		} catch (NumberFormatException e) {
			LOGGER.warn(e);
			throw new CargaPerezosaException(Mensajes.ERROR_CASTING_CANTIDAD_DIAS_LABORALES.getMensaje());
		}
		itinerarioDeTrabajo = new DiaDeTrabajo[cantidadDeDias];
		for (int i = 0; i < itinerarioDeTrabajo.length; i++) {
			itinerarioDeTrabajo[i] = new DiaDeTrabajo();
		}
	}

}
