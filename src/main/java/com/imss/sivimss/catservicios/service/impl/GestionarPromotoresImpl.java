package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.logging.Level;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.GestionarPromotores;
import com.imss.sivimss.catservicios.exception.BadRequestException;
import com.imss.sivimss.catservicios.model.request.BuscarPromotoresRequest;
import com.imss.sivimss.catservicios.model.request.FiltrosPromotorRequest;
import com.imss.sivimss.catservicios.model.request.PromotoresRequest;
import com.imss.sivimss.catservicios.model.request.UsuarioDto;
import com.imss.sivimss.catservicios.model.response.PromotorResponse;
import com.imss.sivimss.catservicios.service.GestionarPromotoresService;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.ConvertirGenerico;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.LogUtil;
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
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String IMPRIMIR = "imprimir";
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	GestionarPromotores promotores=new GestionarPromotores();
	
	@Autowired
	private ModelMapper modelMapper;
	
//	private static final Logger log = LoggerFactory.getLogger(GestionarPromotoresImpl.class);
	
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
	public Response<?> agregarPromotor(DatosRequest request, Authentication authentication) throws IOException, ParseException {
	
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PromotoresRequest promotoresRequest = gson.fromJson(datosJson, PromotoresRequest.class);	
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		    promotores=new GestionarPromotores(promotoresRequest);
		    promotores.setFecIngreso(formatFecha(promotoresRequest.getFecIngreso()));
		    promotores.setFecNacimiento(formatFecha(promotoresRequest.getFecNacimiento()));
			promotores.setIdUsuarioAlta(usuarioDto.getIdUsuario());
				
			if(!validarCurp(promotoresRequest.getDesCurp(), authentication)) {
				return providerRestTemplate.consumirServicio(promotores.insertar().getDatos(), urlCrearMultiple,
						authentication);
			}
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Promotor ya registrado en el sistema: " +promotoresRequest.getDesCurp());
	      
}

	

	@Override
	public Response<?> actualizarPromotor(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PromotoresRequest promotoresRequest = gson.fromJson( String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PromotoresRequest.class);
		
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
		PromotoresRequest promotoresRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PromotoresRequest.class);
		
		if (promotoresRequest.getIdPromotor()== null || promotoresRequest.getIndEstatus()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		promotores= new GestionarPromotores(promotoresRequest);
		promotores.setIdUsuarioBaja(usuarioDto.getIdUsuario());
		promotores.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		return providerRestTemplate.consumirServicio(promotores.cambiarEstatus().getDatos(), urlActualizar,
				authentication);
	}
	
	
	
	private boolean validarCurp(String desCurp, Authentication authentication) throws IOException {
		String regex="[A-Z]{4}+\\d{6}+[HM]+[A-Z]{2}+[B-DF-HJ-NP-TV-Z]{3}+[A-Z0-9]+[0-9]";
		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(desCurp).matches()) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Curp no valida: " +desCurp);
		}
		
		Response<?> response= providerRestTemplate.consumirServicio(promotores.buscarCurp(desCurp).getDatos(), urlConsulta,
				authentication);
		if (response.getCodigo()==200){
			Object rst=response.getDatos();
			return !rst.toString().equals("[]");	
			}
		 throw new BadRequestException(HttpStatus.BAD_REQUEST, "ERROR AL REGISTRAR EL PROMOTOR ");
	}

	@Override
	public Response<?> busquedas(DatosRequest request, Authentication authentication) throws IOException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BuscarPromotoresRequest buscar = gson.fromJson(datosJson, BuscarPromotoresRequest.class);
		return providerRestTemplate.consumirServicio(promotores.filtrosBusqueda(request, buscar).getDatos(), urlConsulta,
				authentication);
	}

	@Override
	public Response<?> cambiarEstatusDescansos(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		 Response<?> response=null;
		PromotoresRequest promotoresRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PromotoresRequest.class);
		
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
	}	
	
	    public String formatFecha(String fecha) throws ParseException {
		Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		DateFormat fecFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
		return fecFormat.format(dateF);       
	}
		}
