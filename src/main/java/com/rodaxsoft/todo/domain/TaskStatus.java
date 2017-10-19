/*
  TaskStatus.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TaskStatus enum
 */
public enum TaskStatus {
	RESERVED, OPEN {
		@Override
		@JsonValue
		public String toString() {
			return "open";
		}
	},

	COMPLETED {

		@Override
		@JsonValue
		public String toString() {
			return "completed";
		}
	};

	@JsonCreator
	public static TaskStatus getValue(String value) {
		String strVal = value.toUpperCase();
		switch (strVal) {
		case "OPEN":
			return OPEN;
		case "COMPLETED":
			return COMPLETED;
		default:
			throw new IllegalArgumentException("Unsupported value");
		}

	}

}
