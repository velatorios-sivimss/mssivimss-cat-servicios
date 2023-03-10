package com.imss.sivimss.catservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDto {

	private Integer id;

	private Integer rol;
	
	private String nombre;

	private String correo;
}
