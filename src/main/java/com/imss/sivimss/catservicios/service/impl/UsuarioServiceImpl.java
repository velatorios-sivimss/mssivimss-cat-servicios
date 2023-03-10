package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.beans.Usuario;
import com.imss.sivimss.catservicios.exception.BadRequestException;
import com.imss.sivimss.catservicios.model.request.ReporteDto;
import com.imss.sivimss.catservicios.model.request.UsuarioDto;
import com.imss.sivimss.catservicios.model.request.UsuarioFiltros;
import com.imss.sivimss.catservicios.model.request.UsuarioRequest;
import com.imss.sivimss.catservicios.model.response.UsuarioResponse;
import com.imss.sivimss.catservicios.service.UsuarioService;
import com.imss.sivimss.catservicios.util.AppConstantes;
import com.imss.sivimss.catservicios.util.ConvertirGenerico;
import com.imss.sivimss.catservicios.util.DatosReporteDTO;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catservicios.util.QueryHelper;
import com.imss.sivimss.catservicios.util.Response;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Override
	public Response<?> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.obtenerUsuarios(request).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication);


	}

	@Override
	public Response<?> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.buscarUsuario(request).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication);
	}
	
	@Override
	public Response<?> buscarUsuarioFiltros(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		
		UsuarioFiltros usuarioFiltros=gson.fromJson(datosJson, UsuarioFiltros.class);
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.buscarUsuarioFiltros(request, usuarioFiltros).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication);
	}

	@Override
	public Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.detalleUsuario(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
	}

	@Override
	public Response<?> catalogoUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		List<UsuarioResponse> usuarioResponses;
		Response<?> response = providerRestTemplate.consumirServicio(usuario.catalogoUsuario().getDatos(),
				urlDominioConsulta + "/generico/consulta", authentication);
		if (response.getCodigo()==200) {
			usuarioResponses = Arrays.asList(modelMapper.map(response.getDatos(), UsuarioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(usuarioResponses));
		}
		return response;
	}

	@Override
	public Response<?> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveAlta(usuarioDto.getCorreo());
		
		return providerRestTemplate.consumirServicio(usuario.insertar().getDatos(), urlDominioConsulta + "/generico/crear",
				authentication);
	}

	@Override
	public Response<?> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		if (usuarioRequest.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveModifica(usuarioDto.getCorreo());
		
		return providerRestTemplate.consumirServicio(usuario.actualizar().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}

	@Override
	public Response<?> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		if (usuarioRequest.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveBaja(usuarioDto.getCorreo());
		return providerRestTemplate.consumirServicio(usuario.cambiarEstatus().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}

	@Override
	public Response<?> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = new Usuario().generarReporte(reporteDto);
		return providerRestTemplate.consumirServicioReportes(envioDatos,"reporte-importe-factura-combustible",reporteDto.getTipoReporte(), urlReportes + "/importe-facturacion-combustible/mensual/generar-reporte",
				authentication);
	}

	

}
