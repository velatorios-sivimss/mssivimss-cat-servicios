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
public class GestionarPromotoresServiceImpl implements GestionarPromotoresService{
	
	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;
	
	Gson gson = new Gson();

	GestionarPromotores promotores=new GestionarPromotores();
//	PromotorDiaDescansoRequest pr=new PromotorDiaDescansoRequest();
	
	@Override
	public Response<?> agregarVelatorio(DatosRequest request, Authentication authentication) throws IOException {
	
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PromotoresRequest promotoresRequest = gson.fromJson(datosJson, PromotoresRequest.class);	
	//	log.info("estoy en:" +promotoresRequest.getFecPromotorDiasDescanso().getPromotorDescanso());
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		
		    promotores =new GestionarPromotores(promotoresRequest);
			promotores.setIdUsuarioAlta(usuarioDto.getId());
			//pr.setPromotorDescanso(promotoresRequest.getFecPromotorDiasDescanso().getPromotorDescanso());
		
			return providerRestTemplate.consumirServicio(promotores.insertar().getDatos(), urlDominioConsulta + "/generico/crearMultiple",
						authentication);
		
	}

}
