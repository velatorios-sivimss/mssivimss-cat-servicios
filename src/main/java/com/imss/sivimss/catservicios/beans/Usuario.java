package com.imss.sivimss.catservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.model.request.ReporteDto;
import com.imss.sivimss.catservicios.model.request.UsuarioFiltros;
import com.imss.sivimss.catservicios.model.request.UsuarioRequest;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.QueryHelper;

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
public class Usuario {
	private Integer id;
	private String materno;
	private String nombre;
	private String correo;
	private String curp;
	private String claveUsuario;
	private String claveMatricula;
	private String password;
	private String paterno;
	private Integer idOficina;
	private Integer idVelatorio;
	private Integer idRol;
	private Integer idDelegacion;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;

	public Usuario(UsuarioRequest usuarioRequest) {
		this.id = usuarioRequest.getId();
		this.nombre = usuarioRequest.getNombre();
		this.paterno = usuarioRequest.getPaterno();
		this.materno = usuarioRequest.getMaterno();
		this.curp = usuarioRequest.getCurp();
		this.correo = usuarioRequest.getCorreo();
		this.password = usuarioRequest.getPassword();
		this.claveUsuario = usuarioRequest.getClaveUsuario();
		this.claveMatricula = usuarioRequest.getClaveMatricula();
		this.idOficina = usuarioRequest.getIdOficina();
		this.idVelatorio = usuarioRequest.getIdVelatorio();
		this.idRol = usuarioRequest.getIdRol();
		this.idDelegacion = usuarioRequest.getIdDelegacion();
	}
	
	

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_USUARIOS");
		q.agregarParametroValues("DES_CURP", "'" + this.curp + "'");
		q.agregarParametroValues("CVE_MATRICULA", "'" + this.claveUsuario + "'");
		q.agregarParametroValues("CVE_USUARIO", "'" + this.claveUsuario + "'");
		q.agregarParametroValues("NOM_USUARIO", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_APELLIDO_PATERNO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_APELLIDO_MATERNO", "'" + this.materno + "'");
		q.agregarParametroValues("FEC_NACIMIENTO", "NULL");
		q.agregarParametroValues("DES_CORREOE", "'" + this.correo + "'");
		q.agregarParametroValues("ID_OFICINA", "" + this.idOficina + "");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("ID_ROL", "" + this.idRol + "");
		q.agregarParametroValues("CVE_ESTATUS", "1");
		q.agregarParametroValues("CVE_CONTRASENIA", "'" + this.password + "'");
		q.agregarParametroValues("FEC_ALTA", "NOW()");
		q.agregarParametroValues("CVE_MATRICULA_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NULL");
		q.agregarParametroValues("FEC_BAJA", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_BAJA", "NULL");
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);

		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("UPDATE SVT_USUARIOS");
		q.agregarParametroValues("DES_CURP", "'" + this.curp + "'");
		q.agregarParametroValues("NOM_USUARIO", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_APELLIDO_PATERNO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_APELLIDO_MATERNO", "'" + this.materno + "'");
		q.agregarParametroValues("DES_CORREOE", "'" + this.correo + "'");
		q.agregarParametroValues("ID_OFICINA", "" + this.idOficina + "");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("ID_ROL", "" + this.idRol + "");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "'" + this.claveModifica + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.addWhere("ID_USUARIO = " + this.id);
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "UPDATE SVT_USUARIOS SET CVE_ESTATUS=!CVE_ESTATUS , FEC_BAJA=NOW(), CVE_MATRICULA_BAJA='"
				+ this.claveBaja + "' WHERE ID_USUARIO=" + this.id + ";";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest obtenerUsuarios(DatosRequest request) {
		
		String query = "SELECT * FROM SVT_USUARIOS ORDER BY ID_USUARIO DESC";	
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest buscarUsuario(DatosRequest request) {
		String palabra = request.getDatos().get("palabra").toString();
		String query = "SELECT ID_USUARIO as id, DES_CURP as curp, CVE_MATRICULA as matricula, CONCAT(NOM_USUARIO,' ',NOM_APELLIDO_PATERNO,' ' ,NOM_APELLIDO_MATERNO) as nombre, DES_CORREOE as correo, CVE_ESTATUS as estatus \r\n"
				+ " FROM SVT_USUARIOS WHERE NOM_USUARIO = '" + palabra + "' ORDER BY ID_USUARIO DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("palabra");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest buscarUsuarioFiltros(DatosRequest request, UsuarioFiltros usuarioFiltros) {
		
		String query = "SELECT ID_USUARIO as id, DES_CURP as curp, CVE_MATRICULA as matricula, CONCAT(NOM_USUARIO,' ',NOM_APELLIDO_PATERNO,' ' ,NOM_APELLIDO_MATERNO) as nombre, DES_CORREOE as correo, CVE_ESTATUS as estatus \r\n"
				+ " FROM SVT_USUARIOS WHERE NOM_USUARIO = '" + usuarioFiltros.getNombreUsuario() + "' OR ID_OFICINA ="+usuarioFiltros.getIdOficina()+"  ORDER BY ID_USUARIO DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("datos");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleUsuario(DatosRequest request) {
		String idUsuario = request.getDatos().get("id").toString();
		String query = "SELECT ID_USUARIO as id, DES_CURP as curp, CVE_MATRICULA as matricula, CONCAT(NOM_USUARIO,' ',NOM_APELLIDO_PATERNO,' ' ,NOM_APELLIDO_MATERNO) as nombre, DES_CORREOE as correo, CVE_ESTATUS as estatus \r\n"
				+ " FROM SVT_USUARIOS WHERE ID_USUARIO = " + Integer.parseInt(idUsuario) + " ORDER BY ID_USUARIO DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest catalogoUsuario() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVT_USUARIOS";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	public Map<String, Object> generarReporte(ReporteDto reporteDto){
		Map<String, Object> envioDatos = new HashMap<>();
		envioDatos.put("logoImss", "");
		envioDatos.put("logoSistema", "");
		envioDatos.put("ooad", reporteDto.getOoad());
		envioDatos.put("idOoad", reporteDto.getIdOoad());
		envioDatos.put("mes", reporteDto.getMes());
		envioDatos.put("anio", reporteDto.getAnio());
		envioDatos.put("nombreMes", reporteDto.getNombreMes());
		
		return envioDatos;
	}
	
	
}
