package com.imss.sivimss.catservicios.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

/**
 * Clase principal para manejar las excepciones BadRequestException de la aplicacion
 *
 * @author Pablo Nolasco
 * @puesto dev
 * @date 24 nov. 2022
 */
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus codigo;
	
	private String mensaje;

	private String datos;

	private boolean error;

	public BadRequestException(HttpStatus codigo, String mensaje) {
		super(mensaje);
		this.codigo = codigo;
		this.datos="";
		this.mensaje = mensaje;
		this.error=true;
	}

	public HttpStatus getEstado() {
		return codigo;
	}

	public void setEstado(HttpStatus estado) {
		this.codigo = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}