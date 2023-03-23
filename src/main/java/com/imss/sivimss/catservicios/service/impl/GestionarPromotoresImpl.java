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
	public Response<?> agregarVelatorio(DatosRequest request, Authentication authentication) throws IOException {
	
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

	private boolean validarCurp(String desCurp, Authentication authentication) throws IOException {
		Response<?> response= providerRestTemplate.consumirServicio(promotores.buscarCurp(desCurp).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		if (response.getCodigo()==200){
			Object rst=response.getDatos();
			return !rst.toString().equals("[]");	
			}
		 throw new BadRequestException(HttpStatus.BAD_REQUEST, "ERROR AL REGISTRAR EL PROMOTOR ");
	}


}
