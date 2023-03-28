package com.imss.sivimss.catservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaqueteDto {

	private Integer id;
	private String nomPaquete;
	private String desPaquete;
	private Float costo;
	private Float precio;
	private Boolean isRegion;
	private String claveSat;
	private String producto;
	private Integer estatus;
	
}
