package com.imss.sivimss.catservicios.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.beans.BusquedaDto;
import com.imss.sivimss.catservicios.model.request.PaqueteDto;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Paquete {
	
	private Integer id;
	private String nomPaquete;
	private String desPaquete;
	private ArrayList<Integer> articulos;
	private ArrayList<Integer> servicios;
	private Float costo;
	private Float precio;
	private Boolean isRegion;
	private String claveSat;
	private Integer idProducto;
	private String producto;
	private Integer idUsuarioAlta;
	private Integer idUsuarioModifica;
	private Integer idUsuarioBaja;
	private Integer estatus;
	
	public Paquete(PaqueteDto paqueteDto) {
		this.id = paqueteDto.getId();
		this.nomPaquete = paqueteDto.getNomPaquete();
		this.desPaquete = paqueteDto.getDesPaquete();
		this.articulos = paqueteDto.getArticulos();
		this.servicios = paqueteDto.getServicios();
		this.costo = paqueteDto.getCosto();
		this.precio = paqueteDto.getPrecio();
		this.isRegion = paqueteDto.getIsRegion();
		this.claveSat = paqueteDto.getClaveSat();
		this.idProducto = paqueteDto.getIdProducto();
		this.producto = paqueteDto.getProducto();
		this.estatus = paqueteDto.getEstatus();
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
    
    public DatosRequest tiposServicios() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_TIPO_SERVICIO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
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
		String query = "SELECT ID_ARTICULO AS id, ID_CATEGORIA_ARTICULO AS idCategoria, DES_ARTICULO AS nombre FROM SVT_ARTICULO";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

    public DatosRequest detallePaquete(DatosRequest request) {

    	String idPaquete = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT p.ID_PAQUETE AS id, p.NOM_PAQUETE AS nomPaquete, p.DES_PAQUETE AS desPaquete, p.MON_COSTO_REFERENCIA AS costo, " +
				 " p.MON_PRECIO AS precio, p.IDN_REGION AS isRegion, c.DES_UNIDAD_SAT AS claveSat, c.ID_PRODUCTOS_SERVICIOS AS idProducto, c.DES_PRODUCTOS_SERVICIOS AS producto, " +
				 " p.CVE_ESTATUS AS estatus ");
		query.append(" FROM SVT_PAQUETE p  LEFT JOIN SVC_CLAVES_PRODUCTOS_SERVICIOS c ON p.ID_PRODUCTOS_SERVICIOS = c.ID_PRODUCTOS_SERVICIOS ");
		query.append(" WHERE p.ID_PAQUETE = " + idPaquete);
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
		
    }
    
    public DatosRequest serviciosPaquete(DatosRequest request) {
    	
    	String idPaquete = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT sv.ID_SERVICIO as id, sv.DES_DESCRIPCION AS servicio, ts.ID_TIPO_SERVICIO AS idTipo, " +
				"ts.DES_TIPO_SERVICIO as tipo, 0.0 AS precio, 0.0 AS costo \n FROM svt_paquete_servicio ps \n");
		query.append("LEFT JOIN svt_servicio sv ON ps.ID_SERVICIO = sv.ID_SERVICIO \n");
		query.append("LEFT JOIN svc_tipo_servicio ts ON sv.ID_TIPO_SERVICIO = ts.ID_TIPO_SERVICIO \n");
		query.append("WHERE ps.ID_PAQUETE = " + idPaquete);
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
		
    }
    
    public DatosRequest articulosPaquete(DatosRequest request) {
    	
    	String idPaquete = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT ar.ID_ARTICULO AS id, ar.DES_ARTICULO AS articulo, ar.ID_TIPO_ARTICULO AS idTipo, " +
				"ta.DES_TIPO_ARTICULO AS tipo, 0.0 AS precio, 0.0 AS costo \n FROM svt_paquete_articulo pa \n");
		query.append("LEFT JOIN svt_articulo ar ON pa.ID_ARTICULO = ar.ID_ARTICULO \n");
		query.append("LEFT JOIN svc_tipo_articulo ta ON ar.ID_TIPO_ARTICULO = ta.ID_TIPO_ARTICULO \n");
		query.append("WHERE pa.ID_PAQUETE = " + idPaquete);
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
		
    }
    
    public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PAQUETE");
		q.agregarParametroValues("NOM_PAQUETE","'" + this.nomPaquete + "'");
		q.agregarParametroValues("DES_PAQUETE","'" + this.desPaquete + "'");
		q.agregarParametroValues("MON_COSTO_REFERENCIA","" + this.costo + "");
		q.agregarParametroValues("MON_PRECIO","" + this.precio + "");
		q.agregarParametroValues("IDN_REGION","" + this.isRegion + "");
		q.agregarParametroValues("ID_PRODUCTOS_SERVICIOS","'" + this.idProducto + "'");
		q.agregarParametroValues("CVE_ESTATUS", "1");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);

		return request;
	}
    
    public DatosRequest insertarArtServ() {
    	DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		
		StringBuilder query = new StringBuilder();
		for (Integer articulo : this.articulos) {
		   query.append("INSERT INTO SVT_PAQUETE_ARTICULO (ID_ARTICULO, ID_PAQUETE, CVE_ESTATUS) VALUES (");
		   query.append(articulo + ", " + this.id + ", 1);$$"); 
		}
		Integer ultimoServicio = this.servicios.get(this.servicios.size() - 1);
		for (Integer servicio : this.servicios) {
			   query.append("INSERT INTO SVT_PAQUETE_SERVICIO (ID_SERVICIO, ID_PAQUETE, CVE_ESTATUS) VALUES (");
			   if (servicio.equals(ultimoServicio)) { 
			       query.append(servicio + ", " + this.id + ", 1)");
			   } else {
				   query.append(servicio + ", " + this.id + ", 1);$$");
			   }
		}
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		parametro.put("separador", "$$");
		parametro.put("replace", this.id);
		request.setDatos(parametro);
		
		return request;
		
    }
    
    public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		
		final QueryHelper q = new QueryHelper("UPDATE SVT_PAQUETE");
		q.agregarParametroValues("NOM_PAQUETE","'" + this.nomPaquete + "'");
		q.agregarParametroValues("DES_PAQUETE","'" + this.desPaquete + "'");
		q.agregarParametroValues("MON_COSTO_REFERENCIA","" + this.costo + "");
		q.agregarParametroValues("MON_PRECIO","" + this.precio + "");
		q.agregarParametroValues("IDN_REGION","" + this.isRegion + "");
		q.agregarParametroValues("ID_PRODUCTOS_SERVICIOS","'" + this.idProducto + "'");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "'" + this.idUsuarioModifica + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "CURRENT_TIMESTAMP()");
		q.addWhere("ID_PAQUETE = " + this.id);
		
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
		
	}
    
    public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "UPDATE SVT_PAQUETE SET CVE_ESTATUS=!CVE_ESTATUS , FEC_BAJA=CURRENT_TIMESTAMP(), ID_USUARIO_BAJA='"
				+ this.idUsuarioBaja + "' WHERE ID_PAQUETE = " + this.id + ";";
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
    }
    
}
