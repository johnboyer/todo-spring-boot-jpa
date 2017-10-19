/*
  Profile.java

  Created by John Boyer on Sep 4, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

/**
 * Profile interface
 */
public interface Profile {
	String getEmail();
	String getName();
	String getAvatarUrl();
}
