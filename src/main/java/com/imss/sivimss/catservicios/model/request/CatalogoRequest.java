package com.imss.sivimss.catservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class CatalogoRequest {

	private Integer id;
	private String nombre;
	private Integer idTipo;
	
}
