package com.imss.sivimss.catservicios.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.catservicios.service.GestionarPromotoresService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/promotor")
public class GestionarPromotoresController {
	

	private GestionarPromotoresService gestionarPromotoresService;
	
	@PostMapping("/agregar")
	public Response<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
	
		return gestionarPromotoresService.agregarVelatorio(request,authentication);
	}

}
