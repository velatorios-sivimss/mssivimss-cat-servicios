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
public class PaqueteDto {

	private Integer id;
	private String nomPaquete;
	private String desPaquete;
	private Float costo;
	private Float precio;
	private Boolean isRegion;
	private String claveSat;
	private Integer idProducto;
	private String producto;
	private Integer estatus;
	
}
