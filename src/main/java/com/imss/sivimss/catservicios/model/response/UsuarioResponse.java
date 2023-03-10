package com.imss.sivimss.catservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UsuarioResponse {

	@JsonProperty(value = "id")
	private Integer ID_USUARIO;
	
	@JsonProperty(value = "nombre")
	private String NOM_USUARIO;
}
