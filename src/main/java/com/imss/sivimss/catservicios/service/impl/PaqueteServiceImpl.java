package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.BusquedaDto;
import com.imss.sivimss.catservicios.beans.Paquete;
import com.imss.sivimss.catservicios.beans.Articulo;
import com.imss.sivimss.catservicios.beans.Servicio;
import com.imss.sivimss.catservicios.exception.BadRequestException;
import com.imss.sivimss.catservicios.service.PaqueteService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;
import com.imss.sivimss.catservicios.model.request.UsuarioDto;
import com.imss.sivimss.catservicios.util.ConvertirGenerico;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;

import com.imss.sivimss.catservicios.model.response.TipoServicioResponse;
import com.imss.sivimss.catservicios.model.request.CatalogoRequest;
import com.imss.sivimss.catservicios.model.request.PaqueteDto;
import com.imss.sivimss.catservicios.model.response.TipoArticuloResponse;

@Service
public class PaqueteServiceImpl implements PaqueteService {
	
	@Value("${endpoints.generico-paginado}")
	private String urlGenericoPaginado;
	
	@Value("${endpoints.generico-consulta}")
	private String urlGenericoConsulta;
	
	@Value("${endpoints.generico-crear}")
	private String urlGenericoCrear;
	
	@Value("${endpoints.generico-actualizar}")
	private String urlGenericoActualizar;
	
	@Value("${endpoints.generico-multiple}")
	private String urlGenericoMultiple;

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
	public Response<?> tiposServicios(DatosRequest request, Authentication authentication) throws IOException {
		Servicio servicio = new Servicio();
		List<TipoServicioResponse> servicioResponse;
		Response<?> response = providerRestTemplate.consumirServicio(servicio.tiposServicios().getDatos(), urlGenericoConsulta, 
				authentication);
		if (response.getCodigo() == 200) {
			servicioResponse = Arrays.asList(modelMapper.map(response.getDatos(), TipoServicioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(servicioResponse));
		}
		return response;
	}

	@Override
	public Response<?> tiposArticulos(DatosRequest request, Authentication authentication) throws IOException {
		Articulo articulo = new Articulo();
		List<TipoArticuloResponse> articuloResponse;
		Response<?> response = providerRestTemplate.consumirServicio(articulo.tiposArticulos().getDatos(), urlGenericoConsulta, 
				authentication);
		if (response.getCodigo() == 200) {
			articuloResponse = Arrays.asList(modelMapper.map(response.getDatos(), TipoArticuloResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloResponse));
		}
		return response;
	}

	@Override
	public Response<?> listadoServicios(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		CatalogoRequest catalogoRequest = gson.fromJson(datosJson,CatalogoRequest.class);
		Servicio servicio = new Servicio(catalogoRequest.getIdTipo());
		
		Response<?> response = providerRestTemplate.consumirServicio(servicio.listadoServicios().getDatos(), urlGenericoConsulta, 
				authentication);
	
		return response;
	}

	@Override
	public Response<?> listadoArticulos(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		CatalogoRequest catalogoRequest = gson.fromJson(datosJson,CatalogoRequest.class);
		Articulo articulo = new Articulo(catalogoRequest.getIdTipo());
		
		Response<?> response = providerRestTemplate.consumirServicio(articulo.listadoArticulos().getDatos(), urlGenericoConsulta, 
				authentication);

		return response;
	}
	
	@Override
	public Response<?> detallePaquete(DatosRequest request, Authentication authentication) throws IOException {
		Paquete paquete = new Paquete();
		
		return providerRestTemplate.consumirServicio(paquete.detallePaquete(request).getDatos(), urlGenericoConsulta, 
				authentication);
	}

	@Override
	public Response<?> detallePaqServicios(DatosRequest request, Authentication authentication) throws IOException {
        Paquete paquete = new Paquete();
		
		return providerRestTemplate.consumirServicio(paquete.serviciosPaquete(request).getDatos(), urlGenericoConsulta, 
				authentication);
	}

	@Override
	public Response<?> detallePaqArticulos(DatosRequest request, Authentication authentication) throws IOException {
        Paquete paquete = new Paquete();
		
		return providerRestTemplate.consumirServicio(paquete.articulosPaquete(request).getDatos(), urlGenericoConsulta, 
				authentication);
	}

	@Override
	public Response<?> agregarPaquete(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PaqueteDto paqueteDto = gson.fromJson(datosJson, PaqueteDto.class);
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		
		Paquete paquete = new Paquete(paqueteDto);
		paquete.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		
		return providerRestTemplate.consumirServicio(paquete.insertar().getDatos(), urlGenericoCrear, 
				authentication);
	}
	
	
	@Override
	public Response<?> agregarArtServ(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PaqueteDto paqueteDto = gson.fromJson(datosJson, PaqueteDto.class);
		
		if (paqueteDto.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Paquete paquete = new Paquete(paqueteDto);
		
		return providerRestTemplate.consumirServicio(paquete.insertarArtServ().getDatos(), urlGenericoMultiple, 
				authentication);
	}
		

	@Override
	public Response<?> actualizarPaquete(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PaqueteDto paqueteDto = gson.fromJson(datosJson, PaqueteDto.class);
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		
		if (paqueteDto.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Paquete paquete = new Paquete(paqueteDto);
		paquete.setIdUsuarioModifica(usuarioDto.getIdUsuario());
	
		return providerRestTemplate.consumirServicio(paquete.actualizar().getDatos(), urlGenericoCrear, 
				authentication);
	
	}
	
	@Override
	public Response<?> cambiarEstatusPaquete(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		PaqueteDto paqueteDto = gson.fromJson(datosJson, PaqueteDto.class);
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		
		if (paqueteDto.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Paquete paquete = new Paquete(paqueteDto);
		paquete.setIdUsuarioBaja(usuarioDto.getIdUsuario());
	
		return providerRestTemplate.consumirServicio(paquete.cambiarEstatus().getDatos(), urlGenericoCrear, 
				authentication);
	
	}
	
}
