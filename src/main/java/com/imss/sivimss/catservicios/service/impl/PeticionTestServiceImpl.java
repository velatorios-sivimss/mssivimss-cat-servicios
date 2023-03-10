package com.imss.sivimss.catservicios.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.imss.sivimss.catservicios.model.request.OoadRequest;
import com.imss.sivimss.catservicios.service.PeticionTestService;
import com.imss.sivimss.catservicios.util.DatosRequest;
import com.imss.sivimss.catservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catservicios.util.Response;

@Service
public class PeticionTestServiceImpl implements PeticionTestService {

	@Value("${endpoints.consulta-ooads}")
	private String urlConsultaOoad;

	@Value("${endpoints.paginado-unidad}")
	private String urlPaginadoUnidad;

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	private static final String ADMIN = "Administrador";
	private static final String NORMATIVO = "Normativo";

	private static final Logger log = LoggerFactory.getLogger(PeticionTestServiceImpl.class);

	@Override
	public Response<?> consultarOoad(DatosRequest request, Authentication authentication) throws IOException {
		
		String query="SELECT * FROM SVT_USUARIOS ORDER BY ID_USUARIO DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put("query", encoded);
		return providerRestTemplate.consumirServicio(request.getDatos(),
				urlDominioConsulta+"/mygeneric/select-generico", authentication);
	}

	@Override
	public Response<?> agregarOoad(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get("datos"));

		Map<String, Object> envioDatos = new HashMap<>();
		OoadRequest ooadRequest = gson.fromJson(datosJson, OoadRequest.class);
		ooadRequest.setFechaAlta(LocalDateTime.now().toString());
		ooadRequest.setActivo(1);
		envioDatos.put("datos", ooadRequest);

		return providerRestTemplate.consumirServicio(envioDatos, "http://localhost:8090/dominio/peticion/ooad/agregar",
				authentication);
	}

	@Override
	public Response<?> agregarOoadConArchivo(MultipartFile [] files,String datos, Authentication authentication) throws IOException {

		Gson gson = new Gson();
		Map<String, Object> envioDatos = new HashMap<>();
		envioDatos.put("datos", datos);
	
		return providerRestTemplate.consumirServicio(envioDatos, "http://localhost:8090/dominio/peticion/ooad/agregar",
				authentication);
	}

}
