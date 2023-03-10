package com.imss.sivimss.catservicios.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.catservicios.service.UsuarioService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class UsuarioController {

	private UsuarioService usuarioService;
	
	@PostMapping("usuarios/consulta")
	public Response<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.consultarUsuarios(request,authentication);
      
	}
	
	@PostMapping("usuario/buscar")
	public Response<?> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.buscarUsuario(request,authentication);
      
	}

	@PostMapping("usuario/buscar-filtros")
	public Response<?> buscarFiltros(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return usuarioService.buscarUsuarioFiltros(request,authentication);
		
	}
	
	@PostMapping("usuario")
	public Response<?> catalogo(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.catalogoUsuario(request,authentication);
      
	}
	
	@PostMapping("usuario/detalle")
	public Response<?> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.detalleUsuario(request,authentication);
      
	}
	
	@PostMapping("usuario/agregar")
	public Response<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.agregarUsuario(request,authentication);
      
	}
	
	@PostMapping("usuario/actualizar")
	public Response<?> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.actualizarUsuario(request,authentication);
      
	}
	
	@PostMapping("usuario/cambiar-estatus")
	public Response<?> cambiarEstatus(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.cambiarEstatusUsuario(request,authentication);
      
	}
	
	
	@PostMapping("usuario/generarDocumento")
	public Response<?> generarDocumento(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		
		return usuarioService.descargarDocumento(request,authentication);
		
	}
}
