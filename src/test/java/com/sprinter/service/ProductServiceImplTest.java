package com.sprinter.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sprinter.exception.ConflictException;
import com.sprinter.exception.NotFoundException;
import com.sprinter.mapper.ProductMapper;
import com.sprinter.model.ProductModel;
import com.sprinter.persistence.entity.ProductEntity;
import com.sprinter.persistence.repository.ProductRepository;
import com.sprinter.service.impl.ProductServiceImpl;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ProductMapper.class })
public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	private ProductEntity productEntity;
	private ProductEntity productEntityModified;

	@BeforeEach
	public void setUp() {
		this.productEntity = ProductEntity.builder().id(1).name("T-shirt").description("Black T-shirt").build();
		this.productEntityModified = ProductEntity.builder().id(1).name("T-shirt").description("Yellow T-shirt")
				.build();
	}

	/**
	 * Test findAll not empty list
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findAll_not_empty_list() throws Exception {

		List<ProductEntity> productEntityList = new ArrayList<>();
		productEntityList.add(productEntity);

		lenient().when(productRepository.findAll()).thenReturn(productEntityList);

		List<ProductModel> resultList = productService.findAll();

		assertFalse(resultList.isEmpty());

	}

	/**
	 * Test findAll empty list
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findAll_empty_list() throws Exception {

		lenient().when(productRepository.findAll()).thenReturn(new ArrayList<ProductEntity>());

		List<ProductModel> resultList = productService.findAll();

		assertTrue(resultList.isEmpty());

	}

	/**
	 * Test findById OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById_OK() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

		ProductModel result = productService.findById(productEntity.getId());

		assertNotNull(result);

	}

	/**
	 * Test findById not found
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_findById_not_found() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			productService.findById(productEntity.getId());
		});

	}

	/**
	 * Test create OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_OK() throws Exception {

		lenient().when(productRepository.save(productEntity)).thenReturn(productEntity);

		ProductModel result = productService.create(ProductMapper.INSTANCE.productEntityToProductModel(productEntity));

		assertNotNull(result);

	}

	/**
	 * Test create product already exist
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_product_exist() throws Exception {

		lenient().when(productRepository.existsById(productEntity.getId())).thenReturn(true);

		assertThrows(ConflictException.class, () -> {
			productService.create(ProductMapper.INSTANCE.productEntityToProductModel(productEntity));
		});

	}

	/**
	 * Test update OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update_OK() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
		lenient().when(productRepository.save(productEntityModified)).thenReturn(productEntityModified);

		ProductModel result = productService
				.update(ProductMapper.INSTANCE.productEntityToProductModel(productEntityModified));

		assertNotNull(result);

	}

	/**
	 * Test update product not found
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_update_not_found() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			productService.update(ProductMapper.INSTANCE.productEntityToProductModel(productEntityModified));
		});

	}

	/**
	 * Test delete OK
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_delete_OK() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
		doNothing().when(productRepository).deleteById(productEntity.getId());

		productService.deleteById(productEntity.getId());

	}

	/**
	 * Test delete product not found
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_delete_product_not_found() throws Exception {

		lenient().when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			productService.deleteById(productEntity.getId());
		});

	}

}
