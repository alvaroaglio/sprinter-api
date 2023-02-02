package com.sprinter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sprinter.persistence.entity.ProductEntity;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	private ProductEntity productEntity;

	@BeforeEach
	public void setUp() {
		this.productEntity = ProductEntity.builder().id(1).name("T-shirt").description("Black T-shirt").build();
	}

	/**
	 * Test findAll
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findAll() throws Exception {

		List<ProductEntity> resultList = productRepository.findAll();

		assertFalse(resultList.isEmpty());
	}

	/**
	 * Test findById
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById() throws Exception {

		ProductEntity result = productRepository.findById(productEntity.getId()).get();

		assertNotNull(result);
        assertEquals(result.getId(), productEntity.getId());
	}
	
	/**
	 * Test create
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create() throws Exception {
		
		ProductEntity newProduct = ProductEntity.builder().id(5).name("T-shirt").description("Football T-shirt").build();
		
		ProductEntity savedProduct = productRepository.save(newProduct); 
		
		assertNotNull(savedProduct);
        assertEquals(savedProduct.getId(), newProduct.getId());
        assertEquals(savedProduct.getName(), newProduct.getName());
        assertEquals(savedProduct.getDescription(), newProduct.getDescription());
		
	}
	
	/**
	 * Test update
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update() throws Exception {
		
		ProductEntity savedProduct = productRepository.findById(productEntity.getId()).get();
		productEntity.setName(productEntity.getName());
		productEntity.setDescription(productEntity.getDescription());
		
		ProductEntity updateProduct = productRepository.save(savedProduct); 
		
		assertNotNull(updateProduct);
        assertEquals(updateProduct.getId(), savedProduct.getId());
        assertEquals(updateProduct.getName(), savedProduct.getName());
        assertEquals(updateProduct.getDescription(), savedProduct.getDescription());
		
	}
	
	/**
	 * Test deleteById
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_deleteById() throws Exception {

		productRepository.deleteById(productEntity.getId());

		Optional<ProductEntity> productEntityDeleted = productRepository.findById(productEntity.getId());
		
		assertFalse(productEntityDeleted.isPresent());

	}
}
