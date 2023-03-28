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
	
	@PostMapping("/agregar")
	public Response<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
	
		return gestionarPromotoresService.agregarPromotor(request,authentication);
	}
	
	@PostMapping("/actualiza")
	public Response<?> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.actualizarPromotor(request,authentication);
      
	}
	
	@PostMapping("/estatus")
	public Response<?> cambiarEstatus(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.cambiarEstatusPromotor(request,authentication);
      
	}
	
	@PostMapping("/catalogo")
	public Response<?> catalogoPromotor(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.mostrarCatalogo(request,authentication);
      
	}
	
	@PostMapping("/filtros")
	public Response<?> filtrosBusqueda(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return gestionarPromotoresService.busquedas(request,authentication);
      
	}

}