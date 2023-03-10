package com.imss.sivimss.catservicios.model.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@JsonIgnoreType(value = true)
public class OoadRequest {

	private String nomOoad;
	
	private String fechaAlta;
	
	private Integer activo;
}
