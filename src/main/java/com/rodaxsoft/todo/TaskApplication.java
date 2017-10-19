/*
  TaskApplication.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rodaxsoft.todo.validation.ApplicationUserSignupValidator;
import com.rodaxsoft.todo.validation.TaskCreateAndUpdateValidator;

@SpringBootApplication
public class TaskApplication {
	
	@Bean 
	public ApplicationUserSignupValidator applicationUserSignupValidator() {
		return new ApplicationUserSignupValidator();
	}
	
	@Bean
	public TaskCreateAndUpdateValidator taskCreateAndUpdateValidator() {
		return new TaskCreateAndUpdateValidator();
	}
	
	@Bean
	 public BCryptPasswordEncoder bCryptPasswordEncoder() {
			 return new BCryptPasswordEncoder();
	 }

	public static void main(String[] args) {
		
		SpringApplication.run(TaskApplication.class, args);
	}
}
