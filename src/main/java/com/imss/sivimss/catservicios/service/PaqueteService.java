package com.imss.sivimss.catservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

public interface PaqueteService {

	Response<?> consultarPaquetes(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> buscarPaquetes(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> catalogoServicios(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> catalogoArticulos(DatosRequest request, Authentication authentication) throws IOException;
	
    Response<?> listadoServicios(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> listadoArticulos(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detallePaquete(DatosRequest request, Authentication authentication) throws IOException;

}
