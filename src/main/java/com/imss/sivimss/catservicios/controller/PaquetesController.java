package com.imss.sivimss.catservicios.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.catservicios.service.PaqueteService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/paquetes")
public class PaquetesController {
	
	private PaqueteService paqueteService;
	
	@PostMapping("/consulta")
	public Response<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.consultarPaquetes(request, authentication);
		
	}
	
	@PostMapping("/buscar")
	public Response<?> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.buscarPaquetes(request, authentication);
		
	}
	
	@PostMapping("/cat-serv")
	public Response<?> catalogoServicios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.catalogoServicios(request, authentication);
		
	}

	@PostMapping("/cat-arti")
	public Response<?> catalogoArticulos(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.catalogoArticulos(request, authentication);
		
	}
	
	@PostMapping("/servicios")
	public Response<?> listaServicios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.listadoServicios(request, authentication);
		
	}

	@PostMapping("/articulos")
	public Response<?> listaArticulos(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.listadoArticulos(request, authentication);
		
	}
	
	@PostMapping("/detalle")
	public Response<?> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return paqueteService.detallePaquete(request, authentication);
		
	}
	
}
