package com.imss.sivimss.catservicios.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.catservicios.service.PaqueteService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catservicios.util.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/paquetes")
public class PaquetesController {
	
	@Autowired
	private PaqueteService paqueteService;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("/consulta")
	public CompletableFuture<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.consultarPaquetes(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/buscar")
	public CompletableFuture<?> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.buscarPaquetes(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/tip-serv")
	public CompletableFuture<?> tiposServicios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.tiposServicios(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}

	@PostMapping("/tip-arti")
	public CompletableFuture<?> tiposArticulos(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.tiposArticulos(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/servicios")
	public CompletableFuture<?> listaServicios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.listadoServicios(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}

	@PostMapping("/articulos")
	public CompletableFuture<?> listaArticulos(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.listadoArticulos(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/detalle")
	public CompletableFuture<?> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.detallePaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/det-serv")
	public CompletableFuture<?> detalleServicios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.detallePaqServicios(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/det-arti")
	public CompletableFuture<?> detalleArticulos(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.detallePaqArticulos(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/agregar")
	public CompletableFuture<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.agregarPaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/agr-art-serv")
	public CompletableFuture<?> agregarArtServ(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.agregarArtServ(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	@PostMapping("/actualizar")
	public CompletableFuture<?> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.actualizarPaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/cambiar-estatus")
	public CompletableFuture<?> cambiarEstatus(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		Response<?> response = paqueteService.cambiarEstatusPaquete(request, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
		
	}
	
	/**
	 * fallbacks generico
	 * 
	 * @return respuestas
	 */
	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			CallNotPermittedException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			RuntimeException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			NumberFormatException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
}
