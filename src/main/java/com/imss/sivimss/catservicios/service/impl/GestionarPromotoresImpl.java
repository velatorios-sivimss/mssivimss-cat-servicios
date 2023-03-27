package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
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

	@Autowired
	private ModelMapper modelMapper;
	
	Gson gson = new Gson();

	GestionarPromotores promotores=new GestionarPromotores();
	
	@Override
	public Response<?> agregarPromotor(DatosRequest request, Authentication authentication) throws IOException {
	
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PromotoresRequest promotoresRequest = gson.fromJson(datosJson, PromotoresRequest.class);	
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		    promotores =new GestionarPromotores(promotoresRequest);
			promotores.setIdUsuarioAlta(usuarioDto.getId());
			
			if(!promotoresRequest.getDesCurp().matches("[A-Z]{4}\\d{6}[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9][0-9]")) {
				throw new BadRequestException(HttpStatus.BAD_REQUEST, "Curp no valida: " +promotoresRequest.getDesCurp());
			}
			
			if(!validarCurp(promotoresRequest.getDesCurp(), authentication)) {
				return providerRestTemplate.consumirServicio(promotores.insertar().getDatos(), urlDominioConsulta + "/generico/crearMultiple",
						authentication);
			}
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Promotor ya registrado en el sistema: " +promotoresRequest.getDesCurp());
	      
}

	@Override
	public Response<?> actualizarPromotor(DatosRequest request, Authentication authentication) throws IOException {
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PromotoresRequest promotoresRequest = gson.fromJson( String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PromotoresRequest.class);
		
		if (promotoresRequest.getIdPromotor() == null ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		promotores= new GestionarPromotores(promotoresRequest);
		promotores.setIdUsuarioModifica(usuarioDto.getId());
		promotores.setIdUsuarioBaja(usuarioDto.getId());
		
		Response<?> response =  providerRestTemplate.consumirServicio(promotores.actualizar().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
		log.info("codigo :" +response.getCodigo());
		if(response.getCodigo()==200 && promotoresRequest.getFecPromotorDiasDescanso()!=null) {
			for(int i=0; i<promotoresRequest.getFecPromotorDiasDescanso().size(); i++) {
			log.info("fechas " +promotores.getFecPromotorDiasDescanso().get(i));
			 providerRestTemplate.consumirServicio(promotores.actualizarDiasDescanso(promotores.getFecPromotorDiasDescanso().get(i), promotores.getIdPromotor()).getDatos(),
                     urlDominioConsulta + "/generico/actualizar", authentication);
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
	//	velatorio.setIndEstatus(velatorioRequest.getIndEstatus());
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

	

		}
