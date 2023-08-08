package com.imss.sivimss.catservicios.model.request;

import java.util.List;

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
public class PersonaRequest {
	
	private Integer idPersona;
	private String curp;
	private String nomPromotor;
	private String aPaterno;
	private String aMaterno;
	private String fecNac;
	private String correo;
	private PromotorRequest promotor;
}
