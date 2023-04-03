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
public class Servicio {

	private Integer id;
	private String nombre;
	private Integer idTipo;
	private String tipo;
	private Float precio;
	private Float costo;
	
	public Servicio(Integer idTipo) {
		this.idTipo = idTipo;
	}
	
	public DatosRequest tiposServicios() {
			DatosRequest request = new DatosRequest();
			Map<String, Object> parametro = new HashMap<>();
			String query = "SELECT * FROM SVC_TIPO_SERVICIO";
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return request;
	}
	
	 public DatosRequest listadoServicios() {
			DatosRequest request = new DatosRequest();
			Map<String, Object> parametro = new HashMap<>();
			String query = "SELECT ID_SERVICIO AS id, NOM_SERVICIO AS nombre FROM SVT_SERVICIO WHERE ID_TIPO_SERVICIO = " + this.idTipo;
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return request;
	}
	 
	 public DatosRequest productosServicios() {
		 DatosRequest request = new DatosRequest();
			Map<String, Object> parametro = new HashMap<>();
			String query = "SELECT ID_PRODUCTOS_SERVICIOS AS idProdServ, CVE_PRODUCTOS_SERVICIOS AS cveProdServ, DES_PRODUCTOS_SERVICIOS AS desProdServ, DES_UNIDAD_SAT AS unidadSat " +
					 "FROM SVC_CLAVES_PRODUCTOS_SERVICIOS";
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return request;
	 }
	
}
