/*
  TestBeanProvider.java

  Created by John Boyer on Sep 7, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rodaxsoft.todo.data.ApplicationUserRepository;


/**
 * TestBeanProvider interface
 */
public interface TestBeanProvider {
	ApplicationUserRepository getApplicationUserRepository();
	BCryptPasswordEncoder getBCryptPasswordEncoder();
}
