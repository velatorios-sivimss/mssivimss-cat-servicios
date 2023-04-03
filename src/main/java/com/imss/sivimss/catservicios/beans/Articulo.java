package com.imss.sivimss.catservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;

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
	private String nombre;
	private Integer idTipo;
	private String tipo;
	private Integer idCategoria;
	private String categoria;
	private Float precio;
	private Float costo;
	
	public Articulo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	public DatosRequest tiposArticulos() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_TIPO_ARTICULO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
    
    public DatosRequest listadoArticulos() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT ID_ARTICULO AS id, ID_CATEGORIA_ARTICULO AS idCategoria, DES_CATEGORIA_ARTICULO AS categoria, DES_ARTICULO AS nombre " +
				"FROM SVT_ARTICULO a LEFT JOIN svc_categoria_articulo ca USING (ID_CATEGORIA_ARTICULO) WHERE ID_TIPO_ARTICULO = " + this.idTipo;
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
    
}
