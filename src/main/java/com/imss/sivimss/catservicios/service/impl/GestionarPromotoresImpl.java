package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;

import java.util.logging.Level;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.GestionarPromotores;
import com.imss.sivimss.catservicios.model.DiasDescansoModel;
import com.imss.sivimss.catservicios.model.request.FiltrosPromotorRequest;
import com.imss.sivimss.catservicios.model.request.PersonaRequest;
import com.imss.sivimss.catservicios.model.request.UsuarioDto;
import com.imss.sivimss.catservicios.model.response.PromotorResponse;
import com.imss.sivimss.catservicios.service.GestionarPromotoresService;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.ConvertirGenerico;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.LogUtil;
import com.imss.sivimss.catservicios.util.MensajeResponseUtil;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GestionarPromotoresImpl implements GestionarPromotoresService{
	
	@Autowired
	private LogUtil logUtil;
	
	@Value("${endpoints.rutas.dominio-consulta}")
	private String urlConsulta;
	@Value("${endpoints.rutas.dominio-consulta-paginado}")
	private String urlPaginado;
	@Value("${endpoints.rutas.dominio-crear}")
	private String urlCrear;
	@Value("${endpoints.rutas.dominio-crear-multiple}")
	private String urlCrearMultiple;
	@Value("${endpoints.rutas.dominio-insertar-multiple}")
	private String urlInsertarMultiple;
	@Value("${endpoints.rutas.dominio-actualizar}")
	private String urlActualizar;
	@Value("${formato-fecha}")
	private String fecFormat;
	
	private static final String BAJA = "baja";
	private static final String ALTA = "alta";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	GestionarPromotores promotores=new GestionarPromotores();
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Response<?> mostrarCatalogo(DatosRequest request, Authentication authentication) throws IOException {
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		FiltrosPromotorRequest filtros = gson.fromJson(datosJson, FiltrosPromotorRequest.class);
		 Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
	        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
	        filtros.setTamanio(tamanio.toString());
	        filtros.setPagina(pagina.toString());
	    	UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
	        Response<?> response = providerRestTemplate.consumirServicio(promotores.catalogoPromotores(request, filtros, fecFormat).getDatos(), urlPaginado,
				authentication);
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CONTRATANTES OK", CONSULTA, authentication, usuario);
		return response;
	}
	
	@Override
	public Response<?> verDetalle(DatosRequest request, Authentication authentication) throws IOException {
		String palabra = request.getDatos().get("palabra").toString();
		List<PromotorResponse> detallePromotorResponse;
		List<DiasDescansoModel> promotorDescansos;
		PromotorResponse promoResponse;
		UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		Response<?> response= MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(promotores.detalle(request, palabra, fecFormat).getDatos(), urlConsulta,
				authentication), "EXITO");
		if(response.getCodigo()==200) {
			detallePromotorResponse = Arrays.asList(modelMapper.map(response.getDatos(), PromotorResponse[].class));
			 promotorDescansos = Arrays.asList(modelMapper.map(providerRestTemplate.consumirServicio(promotores.buscarDiasDescanso(request, palabra, fecFormat).getDatos(), urlConsulta, authentication).getDatos(), DiasDescansoModel[].class));
			promoResponse = detallePromotorResponse.get(0);
			promoResponse.setPromotorDiasDescanso(promotorDescansos);
			response.setCodigo(200);
            response.setError(false);
            response.setMensaje("Exito");
			 response.setDatos(ConvertirGenerico.convertInstanceOfObject(promoResponse));
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DETALLE PROMOTOR OK", CONSULTA, authentication, usuario);
		}
		return response;
	}
	
	
	@Override
	public Response<?> agregarPromotor(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		Response<?> response = new Response<>();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PersonaRequest personaRequest = gson.fromJson(datosJson, PersonaRequest.class);	
		UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		    promotores=new GestionarPromotores(personaRequest);
		    promotores.setFecIngreso(formatFecha(personaRequest.getPromotor().getFecIngreso()));
		    promotores.setFecNacimiento(formatFecha(personaRequest.getFecNac()));
			promotores.setIdUsuarioAlta(usuario.getIdUsuario());
	
			if(validarCurp(personaRequest.getCurp(), authentication)) {
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"EL PROMOTOR QUE DESEAS INGRESAR YA SE ENCUENTRA REGISTRADO EN EL SISTEMA.", ALTA, authentication, usuario);
				response.setCodigo(200);
				response.setError(true);
				response.setMensaje("42");
				return response;
			}
			try {
				
				if(personaRequest.getPromotor().getFecPromotorDiasDescanso()==null) {
					response = providerRestTemplate.consumirServicio(promotores.insertarPersona(personaRequest.getPromotor()).getDatos(), urlCrearMultiple,
							authentication);
					logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"PROMOTOR AGREGADO CORRECTAMENTE", ALTA, authentication, usuario);
				}
				else {
					response = providerRestTemplate.consumirServicio(promotores.insertarPersona(personaRequest.getPromotor()).getDatos(), urlCrear,
							authentication);
					logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DATOS GENERALES CORRECTAMENTE", ALTA, authentication, usuario);
				if(response.getCodigo()==200) {
					Integer idPersona =Integer.parseInt(response.getDatos().toString());
					providerRestTemplate.consumirServicio(promotores.insertarPromotor(idPersona, personaRequest.getPromotor()).getDatos(), urlCrearMultiple,
							authentication);
					logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DIAS DE DESCANSOS AGREGADOS CORRECTAMENTE", ALTA, authentication, usuario);
				}
				}
			return response;
			}catch (Exception e) {
				String consulta = promotores.insertarPersona(personaRequest.getPromotor()).getDatos().get("query").toString();
				String encoded = new String(DatatypeConverter.parseBase64Binary(consulta));
				log.error("Error al ejecutar la query" +encoded);
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"error", MODIFICACION, authentication, usuario);
				throw new IOException("5", e.getCause()) ;
			}
			
	      
}

	
/*
	@Override
	public Response<?> actualizarPromotor(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PersonaRequest promotoresRequest = gson.fromJson( String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PersonaRequest.class);
		
		if (promotoresRequest.getIdPromotor() == null ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta ");
		}
		promotores= new GestionarPromotores(promotoresRequest);
		promotores.setFecIngreso(formatFecha(promotoresRequest.getFecIngreso()));
		promotores.setIdUsuarioModifica(usuarioDto.getIdUsuario());
		promotores.setIdUsuarioBaja(usuarioDto.getIdUsuario());
		
		Response<?> response =  providerRestTemplate.consumirServicio(promotores.actualizar().getDatos(), urlActualizar,
				authentication);
		log.info("codigo : {} ", response.getCodigo());
		if(response.getCodigo()==200 && promotoresRequest.getFecPromotorDiasDescanso()!=null) {
			for(int i=0; i<promotoresRequest.getFecPromotorDiasDescanso().size(); i++) {
				String fecha = formatFecha(promotores.getFecPromotorDiasDescanso().get(i));
			log.info("fechas {} ", fecha);
			 providerRestTemplate.consumirServicio(promotores.actualizarDiasDescanso(fecha, promotores.getIdPromotor()).getDatos(),
                     urlActualizar, authentication);
			}
			}else if(response.getCodigo()==200) {
					return response;
				}else {
				throw new BadRequestException(HttpStatus.BAD_REQUEST, "Error al actualizar promotor");
					}
				return response;
			}

	@Override
	public Response<?> cambiarEstatusPromotor(DatosRequest request, Authentication authentication) throws IOException {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PersonaRequest promotoresRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PersonaRequest.class);
		
		if (promotoresRequest.getIdPromotor()== null || promotoresRequest.getIndEstatus()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		promotores= new GestionarPromotores(promotoresRequest);
		promotores.setIdUsuarioBaja(usuarioDto.getIdUsuario());
		promotores.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		return providerRestTemplate.consumirServicio(promotores.cambiarEstatus().getDatos(), urlActualizar,
				authentication);
	}

	@Override
	public Response<?> cambiarEstatusDescansos(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		 Response<?> response=null;
		PersonaRequest promotoresRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PersonaRequest.class);
		
		if (promotoresRequest.getIdPromotor()== null || promotoresRequest.getFecPromotorDiasDescanso()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		promotores= new GestionarPromotores(promotoresRequest);
		for(int i=0; i<promotoresRequest.getFecPromotorDiasDescanso().size(); i++) {
			String fecha=formatFecha(promotoresRequest.getFecPromotorDiasDescanso().get(i));
		response = providerRestTemplate.consumirServicio(promotores.cambiarEstatusDescansos(fecha, promotoresRequest.getIdPromotor()).getDatos(), urlActualizar,
				authentication);
		}
		return response;
	}	 */
	
	private boolean validarCurp(String curp, Authentication authentication) throws IOException {
	/*	String regex="[A-Z]{4}+\\d{6}+[HM]+[A-Z]{2}+[B-DF-HJ-NP-TV-Z]{3}+[A-Z0-9]+[0-9]";
		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(desCurp).matches()) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Curp no valida: " +desCurp);
		} */
		Response<?> response= MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(promotores.buscarCurp(curp).getDatos(), urlConsulta,
				authentication), "EXITO");
			Object rst=response.getDatos();
			return !rst.toString().equals("[]");	
			
	}
	
	    public String formatFecha(String fecha) throws ParseException {
		Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		DateFormat fecForma = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
		return fecForma.format(dateF);       
	}
		}
