/**
 * 
 */
package com.tech.web.prueba.dominio;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public class DiaDeTrabajo {
	
	private ArticuloATrastiar[] articulosATrastiar;
	
	public DiaDeTrabajo(int cantidadDeArticulos){
		articulosATrastiar = new ArticuloATrastiar[cantidadDeArticulos];
	}
	
	public DiaDeTrabajo(){}

	/**
	 * @return the articulosATrastiar
	 */
	public ArticuloATrastiar[] getArticulosATrastiar() {
		return articulosATrastiar;
	}

	/**
	 * @param articulosATrastiar the articulosATrastiar to set
	 */
	public void setArticulosATrastiar(ArticuloATrastiar[] articulosATrastiar) {
		this.articulosATrastiar = articulosATrastiar;
	}
	
}
