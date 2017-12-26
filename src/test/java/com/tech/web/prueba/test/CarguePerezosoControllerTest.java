/**
 * 
 */
package com.tech.web.prueba.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tech.web.prueba.support.Constantes;
import com.tech.web.prueba.support.ConstantesMappingURL;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.util.HashMap;

/**
 * @author gerlinorlandotorressaavedra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/spring/applicationContextWeb.xml" })
public class CarguePerezosoControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DefaultMockMvcBuilder<?> builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	@Test
	public void ejecucionIncial() throws Exception {
		ResultMatcher ok = MockMvcResultMatchers.status().isOk();
		ResultMatcher historial = MockMvcResultMatchers.model().attribute("historialTrazaIntento",
				Constantes.historialTrazaIntentos);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/");
		this.mockMvc.perform(builder).andExpect(ok).andExpect(forwardedUrl("/WEB-INF/views/index.jsp"))
				.andExpect(historial);
	}

	@Test
	public void cargaInputCarguePerezoso() throws Exception {
		/*FileInputStream fis = new FileInputStream(
				Constantes.RUTA_TEMPORAL + "/lazy_loading_input_45772784851112845.txt");
		MockMultipartFile firstFile = new MockMultipartFile("archivo", "filename.txt", "text/plain", "1\n2\n50\n4".getBytes());
		
		HashMap<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
        
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(ConstantesMappingURL.CARGAR_ARCHIVO_URL_MAPPING)
				.file(firstFile)
				.param("cedula", "4")
				.contentType(mediaType))
				.andExpect(status().isOk());*/
	}

}
