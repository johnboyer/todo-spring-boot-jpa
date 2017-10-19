/*
  TaskControllerTest.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.rest;


import static com.rodaxsoft.todo.security.SecurityConstants.HEADER_STRING;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rodaxsoft.todo.TaskApplication;
import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.security.JWTToken;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.test.TaskTestUtils;
import com.rodaxsoft.todo.test.UploadableTestTask;

/**
 * TaskControllerTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = TaskApplication.class)
@AutoConfigureMockMvc
public class TaskControllerTest {
	

	private static final String TASKS_ENDPOINT = "/tasks";
	
	@Autowired
	private MockMvc mvc;
	
	private MockHttpServletResponse response;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private ApplicationUserService userService;

	private JWTToken token;
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
		taskRepository.deleteAll();
	}
	
	@Before
	public void setUp() {
		
		try {
			ApplicationUser user = TaskTestUtils.createMockApplicationUser();
			String json = new ObjectMapper().writeValueAsString(user);
			MvcResult result;
			
			result = mvc.perform(post("/users")
					     .contentType(MediaType.APPLICATION_JSON)
					     .content(json))
					   .andExpect(status().isOk())
					   .andExpect(jsonPath("$.jwt", notNullValue()))
			           .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse localRes = result.getResponse();
			token = JWTToken.fromJson(localRes.getContentAsString());
		
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testCreateTask() {
		try {
			
			Task task = TaskTestUtils.createMockTask();
			String json = new ObjectMapper().writeValueAsString(task);
			MvcResult result = mvc.perform(post(TASKS_ENDPOINT)
					                .contentType(MediaType.APPLICATION_JSON)
					                .content(json).header(HEADER_STRING, token.getAccessToken()))
					                            .andExpect(status().isOk())
					                            .andExpect(jsonPath("$.id", notNullValue()))
					                            .andExpect(jsonPath("$.due", notNullValue()))
					                            .andExpect(jsonPath("$.title", notNullValue()))
					                            .andExpect(jsonPath("$.status", notNullValue()))
					                            .andExpect(jsonPath("$.created", notNullValue()))
					                            .andDo(print())
					               .andReturn();
			
			response = result.getResponse();
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}
	
	@Test
	public void testDeleteTask() {
		Task task = TaskTestUtils.createMockTask();
		task = taskService.createTask(task);

		
		try {
			MvcResult result;
			result = mvc.perform(delete(TASKS_ENDPOINT + "/" + task.getId())
			        .contentType(MediaType.TEXT_PLAIN_VALUE)
			        .header(HEADER_STRING, token.getAccessToken()))
			                    .andExpect(status().isOk())
			                    .andDo(print())
			       .andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			Assert.fail(e.getMessage());

		}
	}
	
	@Test
	public void testGetTasks() {
		List<Task> tasks = TaskTestUtils.create100Tasks();
		String userId = userService.getUserIdForToken(token.getAccessToken());
		for (Task task : tasks) {
			task.setUserId(userId);
			taskService.createTask(task);
		}
		
		MvcResult result;
		try {

			result = mvc.perform(get(TASKS_ENDPOINT)
			        .contentType(MediaType.APPLICATION_JSON)
			        .header(HEADER_STRING, token.getAccessToken()))
			                    .andExpect(status().isOk())
	                            .andExpect(jsonPath("$[*].id", notNullValue()))
	                            .andExpect(jsonPath("$[*].due", notNullValue()))
	                            .andExpect(jsonPath("$[*].title", notNullValue()))
	                            .andExpect(jsonPath("$[*].status", notNullValue()))
	                            .andExpect(jsonPath("$[*].created", notNullValue()))
			                    .andDo(print())
			       .andReturn();
			
			response = result.getResponse();
			
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory factory = mapper.getTypeFactory();
			CollectionType type = factory.constructCollectionType(List.class, Task.class);
			String json = response.getContentAsString();
			tasks = mapper.readValue(json, type);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("test-tasks.json"), tasks);
			 
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}
	
	@Test
	public void testUpdateTask() {
		Task task = TaskTestUtils.createMockTask();
		task = taskService.createTask(task);
		task.setDescription("All new description");
		task.setTitle("My brand new title");
		
		UploadableTestTask upldTask = new UploadableTestTask();
		BeanUtils.copyProperties(task, upldTask);

		try {
			
			String json = new ObjectMapper().writeValueAsString(upldTask);
			MvcResult result = mvc.perform(put(TASKS_ENDPOINT + "/" + task.getId())
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(json).header(HEADER_STRING, token.getAccessToken()))
			                    .andExpect(status().isOk())
			                    .andExpect(jsonPath("$.id", notNullValue()))
	                            .andExpect(jsonPath("$.due", notNullValue()))
	                            .andExpect(jsonPath("$.title", notNullValue()))
	                            .andExpect(jsonPath("$.status", notNullValue()))
	                            .andExpect(jsonPath("$.created", notNullValue()))
	                            .andExpect(jsonPath("$.description", notNullValue()))
	                            .andExpect(jsonPath("$.modified", notNullValue()))
			                    .andDo(print())
			       .andReturn();
			
			response = result.getResponse();
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}

}
