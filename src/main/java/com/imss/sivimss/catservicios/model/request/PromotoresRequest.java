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
public class PromotoresRequest {
	
	private Integer idPromotor;
	private String numEmpleado;
	private String desCurp;
	private String nomPromotor;
	private String aPaterno;
	private String aMaterno;
	private String fecNacimiento;
	private String fecIngreso;
	private Integer monSueldoBase;
	private Integer idVelatorio;
	private String desCorreo;
	private String desPuesto;
	private String desCategoria;
	private Integer indEstatus;
	private Integer idDelegacion;
	private List<String> fecPromotorDiasDescanso;
	private Integer indEstatusDescanso;
}
