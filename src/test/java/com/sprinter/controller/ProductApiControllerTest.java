package com.sprinter.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprinter.SprinterApplication;
import com.sprinter.exception.ConflictException;
import com.sprinter.exception.NotFoundException;
import com.sprinter.model.ProductModel;
import com.sprinter.service.ProductService;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@SpringBootTest(classes = SprinterApplication.class)
public class ProductApiControllerTest {

	private MockMvc mvc;

	@Autowired
	private ProductApiController productApiController;

	@MockBean
	private ProductService productService;

	private ObjectMapper mapper;

	static final String contextPath = "/api/product";
	static final String parameterId = "/{id}";
	static final String notFound = "Not Found";
	static final String notExist = "Not exist";

	private ProductModel productModel;

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(productApiController).build();

		this.mapper = new ObjectMapper();
		this.productModel = ProductModel.builder().id(1).name("T-shirt").description("Black T-shirt").build();
	}

	/**
	 * Test findAll OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findAll_OK() throws Exception {

		List<ProductModel> productModelList = new ArrayList<>();
		productModelList.add(new ProductModel());
		productModelList.add(new ProductModel());
		productModelList.add(new ProductModel());

		when(productService.findAll()).thenReturn(productModelList);

		ResultActions response = mvc.perform(get(contextPath).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());

		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		List<ProductModel> resultList = Arrays.asList(mapper.readValue(jsonResponse, ProductModel[].class));

		assertThat(!resultList.isEmpty());

	}

	/**
	 * Test findAll KO
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findAll_KO() throws Exception {

		when(productService.findAll()).thenThrow(new RuntimeException());

		ResultActions response = mvc.perform(get(contextPath).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isInternalServerError());

	}

	/**
	 * Test findById OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById_OK() throws Exception {

		when(productService.findById(productModel.getId())).thenReturn(productModel);

		ResultActions response = mvc
				.perform(get(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());

		String jsonResponse = response.andReturn().getResponse().getContentAsString();
		ProductModel result = mapper.readValue(jsonResponse, ProductModel.class);

		assertNotNull(result);

	}

	/**
	 * Test findById not Found
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById_not_found() throws Exception {

		when(productService.findById(productModel.getId())).thenThrow(new NotFoundException(notFound));

		ResultActions response = mvc
				.perform(get(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isNotFound());

	}

	/**
	 * Test findById KO
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById_KO() throws Exception {

		when(productService.findById(productModel.getId())).thenThrow(new RuntimeException());

		ResultActions response = mvc
				.perform(get(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isInternalServerError());

	}

	/**
	 * Test create OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_OK() throws Exception {

		when(productService.create(productModel)).thenReturn(productModel);

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(post(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isCreated());

		String jsonResponse = response.andReturn().getResponse().getContentAsString();

		assertEquals(jsonResponse, productModelJson);

	}

	/**
	 * Test create product exist exception
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_product_exist() throws Exception {

		when(productService.create(productModel)).thenThrow(new ConflictException(notExist));

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(post(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isConflict());
	}

	/**
	 * Test create KO
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_KO() throws Exception {

		when(productService.create(productModel)).thenThrow(new RuntimeException());

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(post(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isInternalServerError());
	}

	/**
	 * Test update OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update_OK() throws Exception {

		when(productService.update(productModel)).thenReturn(productModel);

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(put(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());

		String jsonResponse = response.andReturn().getResponse().getContentAsString();

		assertEquals(jsonResponse, productModelJson);

	}

	/**
	 * Test update KO
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update_KO() throws Exception {

		when(productService.update(productModel)).thenThrow(new RuntimeException());

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(put(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isInternalServerError());

	}

	/**
	 * Test update product not found
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update_not_found() throws Exception {

		when(productService.update(productModel)).thenThrow(new NotFoundException(notFound));

		String productModelJson = mapper.writeValueAsString(productModel);

		ResultActions response = mvc.perform(put(contextPath).accept(MediaType.APPLICATION_JSON)
				.content(productModelJson).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isNotFound());

	}

	/**
	 * Test deleteById OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_deleteById_OK() throws Exception {

		ResultActions response = mvc.perform(
				delete(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());

	}

	/**
	 * Test deleteById KO
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_deleteById_KO() throws Exception {

		doThrow(new RuntimeException()).when(productService).deleteById(productModel.getId());

		ResultActions response = mvc.perform(
				delete(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isInternalServerError());

	}

	/**
	 * Test deleteById product
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_deleteById_product_not_found() throws Exception {

		doThrow(new NotFoundException(notFound)).when(productService).deleteById(productModel.getId());

		ResultActions response = mvc.perform(
				delete(contextPath + parameterId, productModel.getId()).contentType(MediaType.APPLICATION_JSON));
		response.andExpect(status().isNotFound());

	}

}
