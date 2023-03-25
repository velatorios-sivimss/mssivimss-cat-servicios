package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.BusquedaDto;
import com.imss.sivimss.catservicios.beans.Paquete;
import com.imss.sivimss.catservicios.service.PaqueteService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;
import com.imss.sivimss.catservicios.util.ConvertirGenerico;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;

import com.imss.sivimss.catservicios.model.response.TipoServicioResponse;
import com.imss.sivimss.catservicios.model.response.TipoArticuloResponse;

@Service
public class PaqueteServiceImpl implements PaqueteService {
	
	@Value("${endpoints.generico-paginado}")
	private String urlGenericoPaginado;
	
	@Value("${endpoints.generico-consulta}")
	private String urlGenericoConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger log = LoggerFactory.getLogger(PaqueteServiceImpl.class);


	@Override
	public Response<?> consultarPaquetes(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();

		return providerRestTemplate.consumirServicio(paquete.obtenerPaquetes(request).getDatos(), urlGenericoPaginado, 
				authentication);
		
	}

	@Override
	public Response<?> buscarPaquetes(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		return providerRestTemplate.consumirServicio(paquete.buscarPaquetes(request, busqueda).getDatos(), urlGenericoPaginado, 
				authentication);
		
	}

	@Override
	public Response<?> catalogoServicios(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		List<TipoServicioResponse> servicioResponse;
		Response<?> response = providerRestTemplate.consumirServicio(paquete.catalogoServicios().getDatos(), urlGenericoConsulta, 
				authentication);
		if (response.getCodigo() == 200) {
			servicioResponse = Arrays.asList(modelMapper.map(response.getDatos(), TipoServicioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(servicioResponse));
		}
		return response;
	}

	@Override
	public Response<?> catalogoArticulos(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		List<TipoArticuloResponse> articuloResponse;
		Response<?> response = providerRestTemplate.consumirServicio(paquete.catalogoArticulos().getDatos(), urlGenericoConsulta, 
				authentication);
		if (response.getCodigo() == 200) {
			articuloResponse = Arrays.asList(modelMapper.map(response.getDatos(), TipoArticuloResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloResponse));
		}
		return response;
	}

	@Override
	public Response<?> listadoServicios(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		//List<TipoServicioResponse> servicioResponse;
		Response<?> response = providerRestTemplate.consumirServicio(paquete.listadoServicios().getDatos(), urlGenericoConsulta, 
				authentication);
	
		return response;
	}

	@Override
	public Response<?> listadoArticulos(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		//List<TipoArticuloResponse> articuloResponse;
		Response<?> response = providerRestTemplate.consumirServicio(paquete.listadoArticulos().getDatos(), urlGenericoConsulta, 
				authentication);

		return response;
	}
	
	@Override
	public Response<?> detallePaquete(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		
		//Response<?> request1 = providerRestTemplate.consumirServicio(paquete.serviciosPaquete(request).getDatos(), urlGenericoConsulta,
		//		authentication);
		
		return providerRestTemplate.consumirServicio(paquete.detallePaquete(request).getDatos(), urlGenericoConsulta, 
				authentication);
	}
	
}
