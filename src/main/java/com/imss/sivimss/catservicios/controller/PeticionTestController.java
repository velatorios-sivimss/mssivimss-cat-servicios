package com.imss.sivimss.catservicios.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imss.sivimss.catservicios.service.PeticionTestService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/ooad")
public class PeticionTestController {

	@Autowired
	private PeticionTestService peticionTestService;
	
	
	private static final Logger log = LoggerFactory.getLogger(PeticionTestController.class);

	@PostMapping("/consulta")
	public Response<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return peticionTestService.consultarOoad(request,authentication);
      
	}

	@PostMapping("/catalogo")
	public Response<?> consultaCatalogo(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return peticionTestService.consultarOoad(request,authentication);
		
	}
	
	@PostMapping("/guardar")
	public Response<?> guardar(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
	
		return peticionTestService.agregarOoad(request,authentication);

	}
	
	
	@PostMapping("/guardar/archivos")
	public Response<?> guardarConArchivos(@RequestBody MultipartFile [] files, @RequestParam("datos") String datos, Authentication authentication) throws IOException {
		
		return peticionTestService.agregarOoadConArchivo(files,datos, authentication);
		
	}
	
}
