package com.imss.sivimss.catservicios.model;


import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DiasDescansoModel {
	
   private Integer id;
	private String fecDescanso;
}
