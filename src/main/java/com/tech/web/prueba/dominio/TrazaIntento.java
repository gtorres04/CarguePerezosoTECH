/**
 * 
 */
package com.tech.web.prueba.dominio;

import java.util.Date;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public class TrazaIntento {
	
	private Archivo archivoInput;
	
	private Archivo archivoOutput;

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
	public Archivo getArchivoInput() {
		return archivoInput;
	}

	/**
	 * @param archivoInput the archivoInput to set
	 */
	public void setArchivoInput(Archivo archivoInput) {
		this.archivoInput = archivoInput;
	}

	/**
	 * @return the archivoOutput
	 */
	public Archivo getArchivoOutput() {
		return archivoOutput;
	}

	/**
	 * @param archivoOutput the archivoOutput to set
	 */
	public void setArchivoOutput(Archivo archivoOutput) {
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
