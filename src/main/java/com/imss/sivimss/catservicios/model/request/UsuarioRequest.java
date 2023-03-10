package com.imss.sivimss.catservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class UsuarioRequest {
	private Integer id;
	private String materno;
	private String  nombre;
	private String correo;
	private String curp;
	private String claveUsuario;
	private String claveMatricula;
	private String password;
	private String paterno;
	private Integer idOficina;
	private Integer idVelatorio;
	private Integer idRol;
	private Integer idDelegacion;
}
