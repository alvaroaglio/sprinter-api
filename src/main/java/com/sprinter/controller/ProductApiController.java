package com.sprinter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sprinter.exception.ConflictException;
import com.sprinter.exception.NotFoundException;
import com.sprinter.model.ProductModel;
import com.sprinter.service.ProductService;

import lombok.extern.java.Log;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Log
@RestController
@RequestMapping("/api/product")
public class ProductApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductApiController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * Method to clear cache
	 * 
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void clearCacheSchedule() {
		for (String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear();
		}
	}

	/**
	 * Method to find all products
	 * 
	 * @return ResponseEntity List<ProductModel>
	 */
	@GetMapping
	public ResponseEntity<List<ProductModel>> findAll() {

		log.info("Find all products");

		try {

			return new ResponseEntity<List<ProductModel>>(productService.findAll(), HttpStatus.OK);

		} catch (Exception ex) {

			log.severe("Internar server error");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ", ex);

		}
	}

	/**
	 * Method to find a product by id
	 * 
	 * @param id long
	 * 
	 * @return ResponseEntity ProductModel
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ProductModel> findById(@PathVariable long id) {

		log.info("Find product by id: " + id);

		try {

			return new ResponseEntity<ProductModel>(productService.findById(id), HttpStatus.OK);

		} catch (NotFoundException ex) {

			log.severe("Product not found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exception ", ex);

		} catch (Exception ex) {

			log.severe("Internar server error");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ", ex);

		}
	}

	/**
	 * Method to create a product
	 * 
	 * @param productModel ProductModel
	 * 
	 * @return ResponseEntity ProductModel
	 */
	@PostMapping
	public ResponseEntity<ProductModel> create(@RequestBody ProductModel productModel) {

		log.info("Creating product with id: " + productModel.getId());

		try {

			return new ResponseEntity<ProductModel>(productService.create(productModel), HttpStatus.CREATED);

		} catch (ConflictException ex) {

			log.severe("Product already exist");
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Exception ", ex);

		} catch (Exception ex) {

			log.severe("Internar server error");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ", ex);

		}
	}

	/**
	 * Method to update a product
	 * 
	 * @param productModel ProductModel
	 * 
	 * @return ResponseEntity ProductModel
	 */
	@PutMapping
	public ResponseEntity<ProductModel> update(@RequestBody ProductModel productModel) {

		log.info("Updating product with id: " + productModel.getId());

		try {

			return new ResponseEntity<ProductModel>(productService.update(productModel), HttpStatus.OK);

		} catch (NotFoundException ex) {

			log.severe("Product not found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exception ", ex);

		} catch (Exception ex) {

			log.severe("Internar server error");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ", ex);

		}
	}

	/**
	 * Method to delete a product by id
	 * 
	 * @param id long
	 * 
	 * @return ResponseEntity HttpStatus
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteById(@PathVariable long id) {

		log.info("Deleting product with id: " + id);

		try {

			productService.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);

		} catch (NotFoundException ex) {

			log.severe("Product not found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exception ", ex);

		} catch (Exception ex) {

			log.severe("Internar server error");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ", ex);

		}
	}
}
