package com.imss.sivimss.catservicios.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.imss.sivimss.catservicios.model.DiasDescansoModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PromotorResponse {
	
	private Integer idPromotor;
	private Integer idPersona;
	private String numEmpleado;
	private String velatorio;
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String categoria;
	private Integer idVelatorio;
	private String puesto;
	private Boolean estatus;
	private String fecBaja;
	private String correo;
	private String antiguedad;
	private String fecNac;
	private String fecIngreso;
	private Integer sueldoBase;
	private String curp;
	private List<DiasDescansoModel> promotorDiasDescanso;
	
}
