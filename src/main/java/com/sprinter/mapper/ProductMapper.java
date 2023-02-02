package com.sprinter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sprinter.model.ProductModel;
import com.sprinter.persistence.entity.ProductEntity;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Mapper
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	ProductModel productEntityToProductModel(ProductEntity productEntity);

	ProductEntity productModelToProductEntity(ProductModel productModel);
}
