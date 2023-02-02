package com.sprinter.exception;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String detail) {
		super(detail);
	}

}
