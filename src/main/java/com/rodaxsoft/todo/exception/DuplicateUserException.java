/*
  DuplicateUserException.java

  Created by John Boyer on Sep 21, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DuplicateUserException class
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException {

	private static final long serialVersionUID = -6391206997652962051L;

	/**
	 * CTOR
	 * @param message The error message
	 */
	public DuplicateUserException(String string) {
		super(string);
	}

}
