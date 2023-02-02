package com.sprinter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

	private long id;
	private String name;
	private String description;

}
