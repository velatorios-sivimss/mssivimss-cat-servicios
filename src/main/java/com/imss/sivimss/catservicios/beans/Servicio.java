package com.imss.sivimss.catservicios.beans;

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
public class Servicio {

	private Integer id;
	private String servicio;
	private Integer idTipo;
	private String tipo;
	private Float precio;
	private Float costo;
	
}
