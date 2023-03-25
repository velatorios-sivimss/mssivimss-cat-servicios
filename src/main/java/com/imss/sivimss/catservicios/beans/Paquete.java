package com.imss.sivimss.catservicios.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.beans.BusquedaDto;
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
public class Paquete {
	
	private Integer id;
	private String nombre;
	private String descrpcion;
	private ArrayList<Articulo> articulos;
	private ArrayList<Servicio> servicios;
	private Float costo;
	private Float precio;
	private Integer idRegion;
	private String claveSat;
	private Integer estatus;
	
	
	public Paquete(Integer id, String nombre, String descrpcion, Float costo, Float precio, 
			Integer idRegion, String claveSat, Integer estatus) {
		this.id = id;
		this.nombre = nombre;
		this.descrpcion = descrpcion;
		this.costo = costo;
		this.precio = precio;
		this.idRegion = idRegion;
		this.claveSat = claveSat;
		this.estatus = estatus;
	}
	
	public DatosRequest obtenerPaquetes(DatosRequest request) {
		
		StringBuilder query = new StringBuilder("SELECT p.ID_PAQUETE, p.NOM_PAQUETE, p.MON_COSTO_REFERENCIA, p.MON_PRECIO, " +
			 " p.IDN_REGION, c.DES_UNIDAD_SAT, p.CVE_ESTATUS FROM SVT_PAQUETE p JOIN SVC_CLAVES_PRODUCTOS_SERVICIOS c "  +
			 " ON p.ID_PRODUCTOS_SERVICIOS = c.ID_PRODUCTOS_SERVICIOS ");
        query.append(" ORDER BY p.ID_PAQUETE DESC");
        
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;	
	}

    public DatosRequest buscarPaquetes(DatosRequest request, BusquedaDto busqueda) {
		
		StringBuilder query = new StringBuilder("SELECT p.ID_PAQUETE, p.NOM_PAQUETE, p.MON_COSTO_REFERENCIA, p.MON_PRECIO, " +
			 " p.IDN_REGION, c.DES_UNIDAD_SAT, p.CVE_ESTATUS FROM SVT_PAQUETE p LEFT JOIN SVC_CLAVES_PRODUCTOS_SERVICIOS c "  +
			 " ON p.ID_PRODUCTOS_SERVICIOS = c.ID_PRODUCTOS_SERVICIOS ");
		if (busqueda.getNombre() != null) {
			query.append(" WHERE p.NOM_PAQUETE LIKE '%" + busqueda.getNombre() + "%'");
		}
        query.append(" ORDER BY p.ID_PAQUETE DESC");
        
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;	
	}
    
    public DatosRequest catalogoServicios() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_TIPO_SERVICIO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
    
    public DatosRequest catalogoArticulos() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_TIPO_ARTICULO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
    
    public DatosRequest listadoServicios() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT ID_SERVICIO AS id, NOM_SERVICIO AS nombre FROM SVT_SERVICIO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
    
    public DatosRequest listadoArticulos() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT ID_ARTICULO AS id, DES_ARTICULO AS nombre FROM SVT_ARTICULO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

    public DatosRequest detallePaquete(DatosRequest request) {

    	String idPaquete = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT p.ID_PAQUETE, p.NOM_PAQUETE, p.MON_COSTO_REFERENCIA, p.MON_PRECIO, " +
				 " p.IDN_REGION, c.DES_UNIDAD_SAT, p.CVE_ESTATUS, c.DES_PRODUCTOS_SERVICIOS FROM SVT_PAQUETE p ");
		query.append(" LEFT JOIN SVC_CLAVES_PRODUCTOS_SERVICIOS c ON p.ID_PRODUCTOS_SERVICIOS = c.ID_PRODUCTOS_SERVICIOS ");
		query.append(" WHERE p.ID_PAQUETE = " + idPaquete);
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
		
    }
    
    public DatosRequest serviciosPaquete(DatosRequest request) {
    	
    	String idPaquete = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("");
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
		
    }
    
}
