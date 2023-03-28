package com.imss.sivimss.catservicios.util;

/**
 * Clase para la paginacion
 *
 * @author    
 * @puesto dev
 * @date 24 nov. 2022
 */
public class AppConstantes {
	
	public static final String NUMERO_DE_PAGINA = "0";
	public static final String TAMANIO_PAGINA = "10";
	public static final String ORDER_BY= "id";
	public static final String ORDER_DIRECTION= "asc";
	public static final String SUPERVISOR = "Supervisor";
	
	public static final String DATOS= "datos";
	public static final String QUERY= "query";
	public static final String STATUSEXCEPTION = "status";
	public static final String EXPIREDJWTEXCEPTION = "expired";
	public static final String MALFORMEDJWTEXCEPTION = "malformed";
	public static final String UNSUPPORTEDJWTEXCEPTION = "unsupported";
	public static final String ILLEGALARGUMENTEXCEPTION  = "illegalArgument";
	public static final String SIGNATUREEXCEPTION  = "signature";
	public static final String FORBIDDENEXCEPTION  = "forbidden";
	
	public static final String EXPIREDJWTEXCEPTION_MENSAJE = "Token expirado.";
	public static final String MALFORMEDJWTEXCEPTION_MENSAJE = "Token mal formado.";
	public static final String UNSUPPORTEDJWTEXCEPTION_MENSAJE = "Token no soportado.";
	public static final String ILLEGALARGUMENTEXCEPTION_MENSAJE  = "Token vacío.";
	public static final String SIGNATUREEXCEPTION_MENSAJE  = "Fallo la firma.";
	public static final String FORBIDDENEXCEPTION_MENSAJE  = "No tiene autorización para realizar la solicitud.";

	public static final String IND_ESTATUS  = "IND_ESTATUS";
	public static final String CURRENT_TIMESTAMP  = "CURRENT_TIMESTAMP()";
	
	private AppConstantes() {
	    throw new IllegalStateException("AppConstantes class");
	  }

}
