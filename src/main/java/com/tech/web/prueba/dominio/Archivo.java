/**
 * 
 */
package com.tech.web.prueba.dominio;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
public class Archivo {
	private String nombreOriginal;
	private String nombreTemporal;
	private String extension;
	private String mimeType;
	private byte[] archivo;
	/**
	 * @return the nombreOriginal
	 */
	public String getNombreOriginal() {
		return nombreOriginal;
	}
	/**
	 * @param nombreOriginal the nombreOriginal to set
	 */
	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
	}
	/**
	 * @return the nombreTemporal
	 */
	public String getNombreTemporal() {
		return nombreTemporal;
	}
	/**
	 * @param nombreTemporal the nombreTemporal to set
	 */
	public void setNombreTemporal(String nombreTemporal) {
		this.nombreTemporal = nombreTemporal;
	}
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	/**
	 * @return the archivo
	 */
	public byte[] getArchivo() {
		return archivo;
	}
	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
