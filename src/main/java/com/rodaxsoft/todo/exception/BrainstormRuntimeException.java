/*
  BrainstormRuntimeException.java

  Created by John Boyer on Sep 21, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.exception;

/**
 * BrainstormRuntimeException
 */
public abstract class BrainstormRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 2043229172794187329L;
	private String debugMessage;
	
	protected BrainstormRuntimeException() {
		super();
	}
	
	protected BrainstormRuntimeException(String message) {
		super(message);
	}
	
	/**
	 * @return the debugMessage
	 */
	public String getDebugMessage() {
		return debugMessage != null ? debugMessage : getMessage();
	}

	/**
	 * @param debugMessage the debugMessage to set
	 */
	protected void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

}
