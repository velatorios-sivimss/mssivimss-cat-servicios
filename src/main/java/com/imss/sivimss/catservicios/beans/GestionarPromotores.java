package com.imss.sivimss.catservicios.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.model.request.PromotoresRequest;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class GestionarPromotores {
	
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
	private String fecBaja;
	private String desCorreo;
	private String desPuesto;
	private String desCategoria;
	private Integer indEstatus;
	private Integer idUsuarioAlta;
	private Date fecAlta;
	private Date fecActualizacion;
	private Integer idUsuarioModifica;
	private Integer idUsuarioBaja;
	private Integer idDelegacion;
   private List<String> fecPromotorDiasDescanso;
	private Integer indEstatusDescanso;
	
	public GestionarPromotores(PromotoresRequest promotoresRequest) {
		this.idPromotor = promotoresRequest.getIdPromotor();
		this.numEmpleado = promotoresRequest.getNumEmpleado();
		this.desCurp = promotoresRequest.getDesCurp();
		this.nomPromotor = promotoresRequest.getNomPromotor();
		this.aPaterno = promotoresRequest.getAPaterno();
		this.aMaterno = promotoresRequest.getAMaterno();
		this.fecNacimiento = promotoresRequest.getFecNacimiento();
		this.fecIngreso = promotoresRequest.getFecIngreso();
		this.monSueldoBase = promotoresRequest.getMonSueldoBase();
		this.idVelatorio = promotoresRequest.getIdVelatorio();
		this.desCorreo = promotoresRequest.getDesCorreo();
		this.desPuesto = promotoresRequest.getDesPuesto();
		this.desCategoria = promotoresRequest.getDesCategoria();
		this.indEstatus = promotoresRequest.getIndEstatus();
		this.idDelegacion = promotoresRequest.getIdDelegacion();
	this.fecPromotorDiasDescanso = promotoresRequest.getFecPromotorDiasDescanso();
	}


	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PROMOTOR");
		q.agregarParametroValues(" NUM_EMPLEDO", "'" + this.numEmpleado + "'");
		q.agregarParametroValues("DES_CURP", "'" + this.desCurp + "'");
		q.agregarParametroValues("NOM_PROMOTOR", "'" + this.nomPromotor + "'");
		q.agregarParametroValues("NOM_PAPELLIDO", "'" + this.aPaterno + "'");
		q.agregarParametroValues("NOM_SAPELLIDO", "'"+ this.aMaterno + "'");
		q.agregarParametroValues("FEC_NACIMIENTO", "'" + this.fecNacimiento +"'");
		q.agregarParametroValues("FEC_INGRESO", "'" + this.fecIngreso +"'");
		q.agregarParametroValues("MON_SUELDOBASE", ""+ this.monSueldoBase +"");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("DES_CORREO", "'" + this.desCorreo + "'");
		q.agregarParametroValues("DES_PUESTO", "'" + this.desPuesto + "'");
		q.agregarParametroValues("DES_CATEGORIA", "'" + this.desCategoria + "'");
		q.agregarParametroValues("IND_ESTATUS", " 1 ");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +idUsuarioAlta+ "");
		q.agregarParametroValues("FEC_ALTA", " CURRENT_TIMESTAMP() ");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		
		String query = q.obtenerQueryInsertar();
		StringBuilder queries= new StringBuilder();
		queries.append(query);
		for(int i=0; i<this.fecPromotorDiasDescanso.size(); i++) {
			queries.append(" $$ " + insertarDiasDescanso(this.fecPromotorDiasDescanso.get(i)));
			  String encoded = DatatypeConverter.printBase64Binary(queries.toString().getBytes());
		        parametro.put(AppConstantes.QUERY, encoded);
		        parametro.put("separador","$$");
		        parametro.put("replace","idTabla");
		        request.setDatos(parametro);
		}
		log.info("estoy en: " +queries);
		return request;
	      
	}


	public String insertarDiasDescanso(String descansos) {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PROMOTOR_DIAS_DESCANSO");
		q.agregarParametroValues("ID_PROMOTOR", "idTabla");
		log.info(descansos);
		q.agregarParametroValues("FEC_PROMOTOR_DIAS_DESCANSO", "'" +descansos+ "'");
		q.agregarParametroValues("IND_ESTATUS", " 1 ");
		String query = q.obtenerQueryInsertar();
		parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
		request.setDatos(parametro);
		log.info(query);
		return query;
	}


	public DatosRequest buscarCurp(String desCurp) {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
			String query = "SELECT DES_CURP FROM SVT_PROMOTOR WHERE DES_CURP=  '"+desCurp +"' ";
			String encoded=DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return request;
	}


	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVT_PROMOTOR");
		q.agregarParametroValues("FEC_INGRESO", "'" + this.fecIngreso +"'");
		q.agregarParametroValues("MON_SUELDOBASE", ""+ this.monSueldoBase +"");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("DES_CORREO", "'" + this.desCorreo + "'");
		q.agregarParametroValues("DES_PUESTO", "'" + this.desPuesto + "'");
		q.agregarParametroValues("DES_CATEGORIA", "'" + this.desCategoria + "'");
		q.agregarParametroValues("IND_ESTATUS", "" +this.indEstatus+ "");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "" +idUsuarioModifica+ "");
		q.agregarParametroValues("FEC_ACTUALIZACION", " NOW() ");
		if(this.indEstatus==0) {
			q.agregarParametroValues("FEC_BAJA", " CURRENT_TIMESTAMP() ");
			q.agregarParametroValues("ID_USUARIO_BAJA", "" + idUsuarioBaja + "");
		}
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		q.addWhere("ID_PROMOTOR = " + this.idPromotor);
		
				String query = q.obtenerQueryActualizar();
				String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		        parametro.put(AppConstantes.QUERY, encoded);
		        request.setDatos(parametro);
		return request;
	}


	public DatosRequest actualizarDiasDescanso(String fecDescanso, Integer idPromotor) {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PROMOTOR_DIAS_DESCANSO");
		q.agregarParametroValues("ID_PROMOTOR", "" + idPromotor + "");
		q.agregarParametroValues("FEC_PROMOTOR_DIAS_DESCANSO", "'" + fecDescanso + "'");
		q.agregarParametroValues("IND_ESTATUS", "1");
	
		String query = q.obtenerQueryInsertar();
		parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
		request.setDatos(parametro);

		return request;
	
	}


	public DatosRequest cambiarEstatus() {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVT_PROMOTOR");
		q.agregarParametroValues("IND_ESTATUS", "" + this.indEstatus +"");
		if(this.indEstatus==0) {
			q.agregarParametroValues("FEC_BAJA", " CURRENT_TIMESTAMP() ");
			q.agregarParametroValues("ID_USUARIO_BAJA",  "'" + idUsuarioBaja + "'");
		} 
		log.info("estoy en : " +this.idPromotor);
		q.addWhere("ID_PROMOTOR = " + this.idPromotor);
			String query = q.obtenerQueryActualizar();
		    String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return request;	
	}


	public DatosRequest catalogoPromotores(DatosRequest request) {
		String query ="SELECT SP.ID_PROMOTOR AS idPromotor, SP.NUM_EMPLEDO AS numEmpleado, SP.DES_CURP AS curp, "
				+ "SP.NOM_PROMOTOR AS nomPromotor, SP.NOM_PAPELLIDO AS apellidoP, SP.NOM_SAPELLIDO AS apellidoM, "
				+ "SP.FEC_NACIMIENTO AS fecNacimiento, SP.FEC_INGRESO AS fecIngreso, SP.FEC_BAJA AS fecBaja, "
				+ "SP.MON_SUELDOBASE AS sueldoBase, SP.ID_VELATORIO AS idVelatorio, SV.NOM_VELATORIO AS velatorio, SP.DES_CORREO AS correo, "
				+ " SP.DES_PUESTO AS puesto, SP.DES_CATEGORIA AS categoria, SP.IND_ESTATUS AS estatus, SP.ID_DELEGACION AS idDelegacion, "
				+ " SD.DES_DELEGACION AS delegacion, "
		+ "(SELECT FEC_PROMOTOR_DIAS_DESCANSO FROM SVT_PROMOTOR_DIAS_DESCANSO LIMIT 1) AS fecDescansos "
		+ "FROM svt_promotor SP "
	//	+ "JOIN svt_promotor_dias_descanso SPDD ON SPDD.ID_PROMOTOR = SP.ID_PROMOTOR "
		+ "JOIN svc_velatorio SV ON SV.ID_VELATORIO = SP.ID_VELATORIO "
		+ "JOIN svc_delegacion SD ON SD.DES_DELEGACION = SP.ID_DELEGACION ";
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
		log.info(query);
		return request;
	}


	/* public DatosRequest actualizarDescansos(String fecPromotorDiasDescanso, Integer idPromotor) {
		DatosRequest request = new DatosRequest();
		log.info(fecPromotorDiasDescanso);
		log.info("promotor: "+idPromotor);
		String query ="INSERT INTO SVT_PROMOTOR_DIAS_DESCANSO "
				+ "(ID_PROMOTOR, FEC_PROMOTOR_DIAS_DESCANSO, IND_ESTATUS) "
				+ "SELECT " +idPromotor+ ", '" +fecPromotorDiasDescanso + "', 1 FROM DUAL "
				+ "WHERE NOT EXISTS (SELECT * FROM SVT_PROMOTOR_DIAS_DESCANSO "
				+ " WHERE ID_PROMOTOR=" +idPromotor+ " AND FEC_PROMOTOR_DIAS_DESCANSO='" +fecPromotorDiasDescanso+ "')";
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
		log.info(query);
		return request;
	} */



}
