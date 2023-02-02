package com.sprinter.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sprinter.controller.ProductApiController;
import com.sprinter.exception.ConflictException;
import com.sprinter.exception.NotFoundException;
import com.sprinter.mapper.ProductMapper;
import com.sprinter.model.ProductModel;
import com.sprinter.persistence.entity.ProductEntity;
import com.sprinter.persistence.repository.ProductRepository;
import com.sprinter.service.ProductService;

import lombok.extern.java.Log;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Log
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	static final String notFoundException = "Product not found: ";
	static final String conflictException = "Product with id already exists: ";

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Method to find all products
	 * 
	 * @return List<ProductModel>
	 */
	@Override
	@Cacheable("products")
	public List<ProductModel> findAll() {

		List<ProductEntity> result = productRepository.findAll();

		log.info("Find " + result.size() + " products");

		if (result.isEmpty())
			return new ArrayList<ProductModel>();

		return result.stream().map(ProductMapper.INSTANCE::productEntityToProductModel).collect(Collectors.toList());
	}

	/**
	 * Method to find a product by id
	 * 
	 * @param id long
	 * 
	 * @return ProductModel
	 */
	@Override
	public ProductModel findById(long id) {

		Optional<ProductEntity> productEntityData = productRepository.findById(id);

		if (!productEntityData.isPresent())
			throw new NotFoundException(notFoundException + id);

		return ProductMapper.INSTANCE.productEntityToProductModel(productEntityData.get());
	}

	/**
	 * Method to create a product
	 * 
	 * @param productModel ProductModel
	 * 
	 * @return ProductModel
	 */
	@Override
	public ProductModel create(ProductModel productModel) {

		if (productRepository.existsById(productModel.getId()))
			throw new ConflictException(conflictException + productModel.getId());

		return ProductMapper.INSTANCE.productEntityToProductModel(
				productRepository.save(ProductMapper.INSTANCE.productModelToProductEntity(productModel)));
	}

	/**
	 * Method to update a product
	 * 
	 * @param productModel ProductModel
	 * 
	 * @return ProductModel
	 */
	@Override
	public ProductModel update(ProductModel productModel) {

		Optional<ProductEntity> productEntityData = productRepository.findById(productModel.getId());

		if (!productEntityData.isPresent())
			throw new NotFoundException(notFoundException + productModel.getId());

		ProductEntity productEntity = productEntityData.get();
		productEntity.setName(productModel.getName());
		productEntity.setDescription(productModel.getDescription());

		return ProductMapper.INSTANCE.productEntityToProductModel(productRepository.save(productEntity));

	}

	/**
	 * Method to delete a product by id
	 * 
	 * @param id long
	 * 
	 */
	@Override
	public void deleteById(long id) {

		Optional<ProductEntity> productEntityData = productRepository.findById(id);

		if (!productEntityData.isPresent())
			throw new NotFoundException(notFoundException + id);

		productRepository.deleteById(id);
	}

}
