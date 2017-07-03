/**
 * 
 */
package com.tech.web.prueba.dto;

import java.io.Serializable;
import java.util.Date;


/**
 * @author gerlinorlandotorressaavedra
 *
 */
public class TrazaIntentoDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1657951251538093116L;

	private ArchivoDto archivoInput;
	
	private ArchivoDto archivoOutput;

	private String cedula;
	
	private Date fechaEjecucion;

	
	/**
	 * @return the fechaEjecucion
	 */
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion the fechaEjecucion to set
	 */
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return the archivoInput
	 */
	public ArchivoDto getArchivoInput() {
		return archivoInput;
	}

	/**
	 * @param archivoInput the archivoInput to set
	 */
	public void setArchivoInput(ArchivoDto archivoInput) {
		this.archivoInput = archivoInput;
	}

	/**
	 * @return the archivoOutput
	 */
	public ArchivoDto getArchivoOutput() {
		return archivoOutput;
	}

	/**
	 * @param archivoOutput the archivoOutput to set
	 */
	public void setArchivoOutput(ArchivoDto archivoOutput) {
		this.archivoOutput = archivoOutput;
	}

	/**
	 * @return the cedula
	 */
	public String getCedula() {
		return cedula;
	}

	/**
	 * @param cedula the cedula to set
	 */
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
}
