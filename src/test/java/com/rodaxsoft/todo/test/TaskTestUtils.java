/*
  TaskTestUtils.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.domain.Profile;
import com.rodaxsoft.todo.domain.Task;

/**
 * TaskTestUtils class
 *
 */
public class TaskTestUtils {

	public static final String DESCRIPTION = "Description of Test task 1 ";
	public static final Date DUE_DATE = LocalDate.now().plusDays(14).toDate();
	public static final String TITLE = "Test Task 1";

	public static List<Task> create100Tasks() {
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Task task = createMockTask();
			if(i > 0 && i < 80) {
				Date lastDue = tasks.get(i-1).getDue();
				LocalDate ld = new LocalDate(lastDue.getTime());
				ld = ld.plusDays(1);
				task.setDue(ld.toDate());
			} else if(i >= 80){
				task.setDue(null);
			}
			
			tasks.add(task);
		}
		return tasks;
	}
	
	public static ApplicationUser createMockApplicationUser() {
		ApplicationUser mockUser = new ApplicationUser();
		mockUser.setEmail("john@example.com");
		mockUser.setName("John Doe");
		mockUser.setPassword("ycPwxjU6");
		return mockUser;
	}
	
	public static String createMockApplicationUserJson() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(createMockApplicationUser());
	}

	public static Task createMockTask() {
		Task task = new Task();
		task.setDue(DUE_DATE);
		task.setDescription(DESCRIPTION);
		task.setTitle(TITLE);
		return task;
	}
	
	public static Profile getProfileFromJson(String json) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, String> profileMap = new ObjectMapper().readValue(json, Map.class);
		return new Profile() {
			
			@Override
			public String getAvatarUrl() {
				return null;
			}
			
			@Override
			public String getEmail() {
				return profileMap.get("email");
			}
			
			@Override
			public String getName() {
				return profileMap.get("name");
			}
		};
	}
	
	public static ApplicationUser saveMockApplicationUser(TestBeanProvider provider) {
		ApplicationUser mockUser = createMockApplicationUser();
		//Encrypt password
		mockUser.setPassword(provider.getBCryptPasswordEncoder().encode(mockUser.getPassword()));
		return provider.getApplicationUserRepository().save(mockUser);
	}

}
