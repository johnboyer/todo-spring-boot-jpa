/*
  ValidationException.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.exception;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ValidationException class
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationException extends BrainstormRuntimeException {

	
	private static final long serialVersionUID = -781206437165935086L;

	private static String makeExceptionMessage(Errors errors) {
		String msg = null;
		if(errors.hasFieldErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			StringBuilder buf = new StringBuilder();
			for (FieldError fieldError : fieldErrors) {
				buf.append('`')
				   .append(fieldError.getField())
				   .append("`: ")
				   .append(fieldError.getDefaultMessage())
				   .append("; ");
			}
			
			msg = buf.toString();
		}
		
		return msg;
	}
	
	private static String makeDebugMessage(Errors errors) {
		String msg = null;
		if(errors.hasFieldErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			msg = fieldErrors.stream().map(FieldError::toString).collect(Collectors.joining("; "));
		}
		
		return msg;
	}
		
	/**
	 * CTOR
	 * @param result The validation binding result
	 */
	public ValidationException(Errors errors) {
		this(makeExceptionMessage(errors));
		setDebugMessage(makeDebugMessage(errors));
	}

	/**
	 * CTOR
	 * @param message The error message
	 */
	public ValidationException(String string) {
		super(string);
	}
	
}
