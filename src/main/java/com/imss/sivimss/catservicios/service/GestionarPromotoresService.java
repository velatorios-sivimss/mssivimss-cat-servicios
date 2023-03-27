package com.imss.sivimss.catservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

public interface GestionarPromotoresService {

	Response<?> agregarPromotor(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> actualizarPromotor(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> cambiarEstatusPromotor(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> mostrarCatalogo(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> busquedas(DatosRequest request, Authentication authentication) throws IOException;


}
