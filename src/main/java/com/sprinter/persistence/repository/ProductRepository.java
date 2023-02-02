package com.sprinter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprinter.persistence.entity.ProductEntity;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
