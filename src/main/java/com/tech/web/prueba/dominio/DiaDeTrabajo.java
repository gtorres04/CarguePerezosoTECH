/**
 * 
 */
package com.tech.web.prueba.dominio;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public class DiaDeTrabajo {
	
	private ArticuloATrastear[] articulosATrastiar;
	
	public DiaDeTrabajo(int cantidadDeArticulos){
		articulosATrastiar = new ArticuloATrastear[cantidadDeArticulos];
	}
	
	public DiaDeTrabajo(){}

	/**
	 * @return the articulosATrastiar
	 */
	public ArticuloATrastear[] getArticulosATrastiar() {
		return articulosATrastiar;
	}

	/**
	 * @param articulosATrastiar the articulosATrastiar to set
	 */
	public void setArticulosATrastiar(ArticuloATrastear[] articulosATrastiar) {
		this.articulosATrastiar = articulosATrastiar;
	}
	
}
