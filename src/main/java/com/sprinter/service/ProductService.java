package com.sprinter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sprinter.model.ProductModel;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Service
public interface ProductService {

	List<ProductModel> findAll();

	ProductModel findById(long id);

	ProductModel create(ProductModel productModel);

	ProductModel update(ProductModel productModel);

	void deleteById(long id);

}
