package com.imss.sivimss.catservicios.beans;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catservicios.model.request.BuscarPromotoresRequest;
import com.imss.sivimss.catservicios.model.request.FiltrosPromotorRequest;
import com.imss.sivimss.catservicios.model.request.PromotoresRequest;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.QueryHelper;
import com.imss.sivimss.catservicios.util.SelectQueryUtil;

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
		//this.fecNacimiento = promotoresRequest.getFecNacimiento();
		//this.fecIngreso = promotoresRequest.getFecIngreso();
		this.monSueldoBase = promotoresRequest.getMonSueldoBase();
		this.idVelatorio = promotoresRequest.getIdVelatorio();
		this.desCorreo = promotoresRequest.getDesCorreo();
		this.desPuesto = promotoresRequest.getDesPuesto();
		this.desCategoria = promotoresRequest.getDesCategoria();
		this.indEstatus = promotoresRequest.getIndEstatus();
		this.idDelegacion = promotoresRequest.getIdDelegacion();
	this.fecPromotorDiasDescanso = promotoresRequest.getFecPromotorDiasDescanso();
	}


	public DatosRequest catalogoPromotores(DatosRequest request, FiltrosPromotorRequest filtros, String fecFormat) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("PR.ID_PROMOTOR AS idPromotor",
				"PR.NUM_EMPLEADO AS numEmpleado",
				"SP.CVE_CURP AS curp",
				"SP.NOM_PERSONA AS nombre",
				"SP.NOM_PRIMER_APELLIDO AS primerApellido",
				"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido",
				"DATE_FORMAT(SP.FEC_NAC, '"+fecFormat+"') AS fecNac",
				"DATE_FORMAT(PR.FEC_INGRESO, '"+fecFormat+"') AS fecIngreso",
				"DATE_FORMAT(PR.FEC_BAJA, '"+fecFormat+"') AS fecBaja",
				"PR.MON_SUELDOBASE AS sueldoBase",
				"SV.DES_VELATORIO AS velatorio",
				"COUNT(DIA.FEC_PROMOTOR_DIAS_DESCANSO) AS diasDescanso",
				"GROUP_CONCAT(DATE_FORMAT(DIA.FEC_PROMOTOR_DIAS_DESCANSO, '"+fecFormat+"')) AS fecDescansos",
				"IF(TIMESTAMPDIFF(MONTH, PR.FEC_INGRESO, CURRENT_TIMESTAMP()) < 12, "
				+ "CONCAT(TIMESTAMPDIFF(MONTH, PR.FEC_INGRESO, CURRENT_TIMESTAMP()), ' meses'), "
				+ "CONCAT(TIMESTAMPDIFF(YEAR, PR.FEC_INGRESO, CURRENT_TIMESTAMP()), ' año (s)') )AS antiguedad",
				"SP.DES_CORREO AS correo",
				"PR.DES_PUESTO AS puesto",
				"PR.DES_CATEGORIA AS categoria",
				"PR.IND_ACTIVO AS estatus")
		.from("SVT_PROMOTOR PR")
		.join("SVC_PERSONA SP", "PR.ID_PERSONA = SP.ID_PERSONA")
		.join("SVC_VELATORIO SV ", "PR.ID_VELATORIO = SV.ID_VELATORIO")
		.join("SVT_PROMOTOR_DIAS_DESCANSO DIA", "PR.ID_PROMOTOR = DIA.ID_PROMOTOR");
		queryUtil.where("DIA.IND_ACTIVO = 1");
		if(filtros.getIdDelegacion()!=null) {
			queryUtil.where("SV.ID_DELEGACION = "+ filtros.getIdDelegacion() + "");
		}
		if(filtros.getIdVelatorio()!=null){
			queryUtil.where("SV.ID_VELATORIO = " + filtros.getIdVelatorio() + "");	
		}
		if(filtros.getNomPromotor()!=null){
			queryUtil.where("CONCAT(SP.NOM_PERSONA,' ', "
					+"SP.NOM_PRIMER_APELLIDO,' ', "
					+ "SP.NOM_SEGUNDO_APELLIDO) LIKE '%" + filtros.getNomPromotor() + "%'");	
		}
		String query = obtieneQuery(queryUtil);
		log.info("promotores "+query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    parametros.put("pagina",filtros.getPagina());
        parametros.put("tamanio",filtros.getTamanio());
        request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}
	
	
	public DatosRequest detalle(DatosRequest request, String palabra, String fecFormat) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("PR.ID_PROMOTOR AS idPromotor",
				"PR.NUM_EMPLEADO AS numEmpleado",
				"SV.ID_VELATORIO AS idVelatorio",
				"SP.CVE_CURP AS curp",
				"SP.NOM_PERSONA AS nombre",
				"SP.NOM_PRIMER_APELLIDO AS primerApellido",
				"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido",
				"DATE_FORMAT(SP.FEC_NAC, '"+fecFormat+"') AS fecNac",
				"DATE_FORMAT(PR.FEC_INGRESO, '"+fecFormat+"') AS fecIngreso",
				"DATE_FORMAT(PR.FEC_BAJA, '"+fecFormat+"') AS fecBaja",
				"PR.MON_SUELDOBASE AS sueldoBase",
				"SV.DES_VELATORIO AS velatorio",
				"IF(TIMESTAMPDIFF(MONTH, PR.FEC_INGRESO, CURRENT_TIMESTAMP()) < 12, "
				+ "CONCAT(TIMESTAMPDIFF(MONTH, PR.FEC_INGRESO, CURRENT_TIMESTAMP()), ' meses'), "
				+ "CONCAT(TIMESTAMPDIFF(YEAR, PR.FEC_INGRESO, CURRENT_TIMESTAMP()), ' año (s)') )AS antiguedad",
				"SP.DES_CORREO AS correo",
				"PR.DES_PUESTO AS puesto",
				"PR.DES_CATEGORIA AS categoria",
				"PR.IND_ACTIVO AS estatus")
		.from("SVT_PROMOTOR PR")
		.join("SVC_PERSONA SP", "PR.ID_PERSONA = SP.ID_PERSONA")
		.join("SVC_VELATORIO SV ", "PR.ID_VELATORIO = SV.ID_VELATORIO");
		queryUtil.where("PR.ID_PROMOTOR = :id")
		.setParameter("id", Integer.parseInt(palabra));
		String query = obtieneQuery(queryUtil);
		log.info("promotores "+query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
        request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}
	
	
	public DatosRequest buscarDiasDescanso(DatosRequest request, String palabra, String fecFormat) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("DIA.ID_PROMOTOR_DIAS_DESCANSO AS id",
				"DATE_FORMAT(DIA.FEC_PROMOTOR_DIAS_DESCANSO, '"+fecFormat+"') AS fecDescanso")
		.from("SVT_PROMOTOR PR")
		.leftJoin("SVT_PROMOTOR_DIAS_DESCANSO DIA", "PR.ID_PROMOTOR = DIA.ID_PROMOTOR");
		queryUtil.where("PR.ID_PROMOTOR = :id").and("DIA.IND_ACTIVO=1")
		.setParameter("id", Integer.parseInt(palabra));
		String query = obtieneQuery(queryUtil);
		log.info("dias de descanso: "+query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
        request.getDatos().remove(AppConstantes.DATOS);
	    request.setDatos(parametros);
		return request;
	}
	
	
	public DatosRequest insertar() throws ParseException {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_PROMOTOR");
		q.agregarParametroValues(" NUM_EMPLEDO", "'" + this.numEmpleado + "'");
		q.agregarParametroValues("DES_CURP", "'" + this.desCurp + "'");
		q.agregarParametroValues("NOM_PROMOTOR", "'" + this.nomPromotor + "'");
		q.agregarParametroValues("NOM_PAPELLIDO", "'" + this.aPaterno + "'");
		q.agregarParametroValues("NOM_SAPELLIDO", "'"+ this.aMaterno + "'");
		q.agregarParametroValues("FEC_NACIMIENTO", "'" +fecNacimiento +"'");
		q.agregarParametroValues("FEC_INGRESO", "'" +fecIngreso +"'");
		q.agregarParametroValues("MON_SUELDOBASE", ""+ this.monSueldoBase +"");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("DES_CORREO", "'" + this.desCorreo + "'");
		q.agregarParametroValues("DES_PUESTO", "'" + this.desPuesto + "'");
		q.agregarParametroValues("DES_CATEGORIA", "'" + this.desCategoria + "'");
		q.agregarParametroValues("" +AppConstantes.IND_ESTATUS+ "", " 1 ");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +idUsuarioAlta+ "");
		q.agregarParametroValues("FEC_ALTA", "" +AppConstantes.CURRENT_TIMESTAMP + "");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		
		String query = q.obtenerQueryInsertar();
		StringBuilder queries= new StringBuilder();
		queries.append(query);
		//for(int i=0; i<this.fecPromotorDiasDescanso.size(); i++) {
		for(String descansos: fecPromotorDiasDescanso) {
			Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(descansos);
	        DateFormat fechaDescanso = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
	        String fecha=fechaDescanso.format(dateF);
			queries.append(" $$ " + insertarDiasDescanso(fecha));
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
		q.agregarParametroValues("" +AppConstantes.IND_ESTATUS+ "", " 1 ");
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
			request.getDatos().remove(""+AppConstantes.DATOS+"");
			return request;
	}


	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVT_PROMOTOR");
		q.agregarParametroValues("FEC_INGRESO", "'" + fecIngreso +"'");
		q.agregarParametroValues("MON_SUELDOBASE", ""+ this.monSueldoBase +"");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("DES_CORREO", "'" + this.desCorreo + "'");
		q.agregarParametroValues("DES_PUESTO", "'" + this.desPuesto + "'");
		q.agregarParametroValues("DES_CATEGORIA", "'" + this.desCategoria + "'");
		q.agregarParametroValues("" +AppConstantes.IND_ESTATUS+ "", "" +this.indEstatus+ "");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "" +idUsuarioModifica+ "");
		q.agregarParametroValues("FEC_ACTUALIZACION", "" +AppConstantes.CURRENT_TIMESTAMP + "");
		if(this.indEstatus==0) {
			q.agregarParametroValues("FEC_BAJA", "" +AppConstantes.CURRENT_TIMESTAMP + "");
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
		q.agregarParametroValues("" +AppConstantes.IND_ESTATUS+ "", "1");
	
		String query = q.obtenerQueryInsertar();
		parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
		request.setDatos(parametro);

		return request;
	
	}


	public DatosRequest cambiarEstatus() {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		final QueryHelper q = new QueryHelper("UPDATE SVT_PROMOTOR");
		q.agregarParametroValues("" +AppConstantes.IND_ESTATUS+ "", "" + this.indEstatus +"");
		if(this.indEstatus==0) {
			q.agregarParametroValues("FEC_BAJA", "" +AppConstantes.CURRENT_TIMESTAMP + "");
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


	public DatosRequest cambiarEstatusDescansos(String fechasDescansos, Integer idPromotor) {
		
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "UPDATE SVT_PROMOTOR_DIAS_DESCANSO SET IND_ESTATUS= 0 "
				+ " WHERE ID_PROMOTOR= "+ idPromotor +" AND FEC_PROMOTOR_DIAS_DESCANSO= '"+fechasDescansos+"' " ;
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		log.info("estoy en: " +query);
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
		
	}
	
	private static String obtieneQuery(SelectQueryUtil queryUtil) {
        return queryUtil.build();
    }
	
	private static String encodedQuery(String query) {
        return DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
    }
	
	private String setValor(String valor) {
        if (valor==null || valor.equals("")) {
            return "NULL";
        }else {
            return "'"+valor+"'";
        }
    }


}
