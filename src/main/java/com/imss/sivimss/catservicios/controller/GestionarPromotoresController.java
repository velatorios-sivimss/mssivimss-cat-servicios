package com.imss.sivimss.catservicios.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
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
	
    @Autowired
	private GestionarPromotoresService gestionarPromotoresService;
    
    @PostMapping("/buscar")
	public Response<?> catalogoPromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.mostrarCatalogo(request,authentication);
	}
    
    @PostMapping("/detalle")
	public Response<?> detallePromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.verDetalle(request,authentication);
      
	}
	
	@PostMapping("/agregar")
	public Response<?> insertarPromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException{
	
		return gestionarPromotoresService.agregarPromotor(request,authentication);
	}
	
	@PostMapping("/modificar")
	public Response<?> modificarPromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return gestionarPromotoresService.actualizarPromotor(request,authentication);
      
	}

	
	@PostMapping("/cambiar-estatus")
	public Response<?> cambiarEstatusPromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.cambiarEstatus(request,authentication);
      
	}
	
	/*
	@PostMapping("/estatus-descansos")
	public Response<?> cambiarEstatusFechasDescansos(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return gestionarPromotoresService.cambiarEstatusDescansos(request,authentication);
      
	} */
	
}
