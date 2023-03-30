package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.GestionarPromotores;
import com.imss.sivimss.catservicios.exception.BadRequestException;
import com.imss.sivimss.catservicios.model.request.BuscarPromotoresRequest;
import com.imss.sivimss.catservicios.model.request.PromotoresRequest;
import com.imss.sivimss.catservicios.model.request.UsuarioDto;
import com.imss.sivimss.catservicios.service.GestionarPromotoresService;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catservicios.util.Response;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class GestionarPromotoresImpl implements GestionarPromotoresService{
	
	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	GestionarPromotores promotores=new GestionarPromotores();
	
	@Override
	public Response<?> agregarPromotor(DatosRequest request, Authentication authentication) throws IOException, ParseException {
	
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PromotoresRequest promotoresRequest = gson.fromJson(datosJson, PromotoresRequest.class);	
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		    promotores=new GestionarPromotores(promotoresRequest);
		    promotores.setFecIngreso(formatFecha(promotoresRequest.getFecIngreso()));
		    promotores.setFecNacimiento(formatFecha(promotoresRequest.getFecNacimiento()));
			promotores.setIdUsuarioAlta(usuarioDto.getId());
				
			if(!validarCurp(promotoresRequest.getDesCurp(), authentication)) {
				return providerRestTemplate.consumirServicio(promotores.insertar().getDatos(), urlDominioConsulta + "/generico/crearMultiple",
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
		promotores.setIdUsuarioModifica(usuarioDto.getId());
		promotores.setIdUsuarioBaja(usuarioDto.getId());
		
		Response<?> response =  providerRestTemplate.consumirServicio(promotores.actualizar().getDatos(), urlDominioConsulta + "/generico/actualizar ",
				authentication);
		log.info("codigo :" +response.getCodigo());
		if(response.getCodigo()==200 && promotoresRequest.getFecPromotorDiasDescanso()!=null) {
			for(int i=0; i<promotoresRequest.getFecPromotorDiasDescanso().size(); i++) {
				String fecha = formatFecha(promotores.getFecPromotorDiasDescanso().get(i));
			log.info("fechas " +fecha);
			 providerRestTemplate.consumirServicio(promotores.actualizarDiasDescanso(fecha, promotores.getIdPromotor()).getDatos(),
                     urlDominioConsulta + "/generico/actualizar ", authentication);
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
		promotores.setIdUsuarioBaja(usuarioDto.getId());
		promotores.setIdUsuarioAlta(usuarioDto.getId());
		return providerRestTemplate.consumirServicio(promotores.cambiarEstatus().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}
	
	@Override
	public Response<?> mostrarCatalogo(DatosRequest request, Authentication authentication) throws IOException {
		return providerRestTemplate.consumirServicio(promotores.catalogoPromotores(request).getDatos(), urlDominioConsulta + "/generico/paginado ",
				authentication);
	}
	
	private boolean validarCurp(String desCurp, Authentication authentication) throws IOException {
		String regex="[A-Z]{4}+\\d{6}+[HM]+[A-Z]{2}+[B-DF-HJ-NP-TV-Z]{3}+[A-Z0-9]+[0-9]";
		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(desCurp).matches()) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Curp no valida: " +desCurp);
		}
		
		Response<?> response= providerRestTemplate.consumirServicio(promotores.buscarCurp(desCurp).getDatos(), urlDominioConsulta + "/generico/consulta",
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
		return providerRestTemplate.consumirServicio(promotores.filtrosBusqueda(request, buscar).getDatos(), urlDominioConsulta + "/generico/paginado",
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
		response = providerRestTemplate.consumirServicio(promotores.cambiarEstatusDescansos(fecha, promotoresRequest.getIdPromotor()).getDatos(), urlDominioConsulta + "/generico/actualizar",
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
