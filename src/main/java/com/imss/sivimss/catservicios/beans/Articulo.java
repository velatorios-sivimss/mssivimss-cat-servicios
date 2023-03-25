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
public class Articulo {

	private Integer id;
	private Integer idTipo;
	private String descripcion;
	private Float precio;
	private Integer cantidad;
	private Float costo;
	
}
