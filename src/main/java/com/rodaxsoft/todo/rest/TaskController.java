/*
  TaskController.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.rest;

import static com.rodaxsoft.todo.security.SecurityConstants.HEADER_STRING;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;
import com.rodaxsoft.todo.exception.ValidationException;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.validation.TaskCreateAndUpdateValidator;

/**
 * TaskController class
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ApplicationUserService userService;
	
	@Autowired
	private TaskCreateAndUpdateValidator taskValidator;

	@PostMapping
	public Task createTask(@RequestBody Task task, BindingResult result, @RequestHeader(HEADER_STRING) String token) {
		taskValidator.validate(task, result);
		if(result.hasErrors()) {
			throw new ValidationException(result);
		}
		
		//Set user id here!
		String userId = userService.getUserIdForToken(token);
		task.setUserId(userId);
		
		return taskService.createTask(task);
	}
	
	@GetMapping
	public List<Task> getTasks(@RequestHeader(HEADER_STRING) String token) {
		return taskService.getTasks();	
	}
	
	@PutMapping(path = "/{id}")
	public Task updateTask(@PathVariable String id, @RequestBody Task updatedTask, BindingResult result) {
		if(!taskService.exists(id)) {
			throw new ResourceNotFoundException("Task not found");
		}
		
		taskValidator.validate(updatedTask, result);
		if (result.hasFieldErrors()) {
			throw new ValidationException(result);
		}
		
		updatedTask.setId(id);
		return taskService.updateTask(updatedTask);
	}
	
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteTask(@PathVariable String id) {
		if(!taskService.exists(id)) {
			throw new ResourceNotFoundException("Task not found");
		}

		taskService.deleteTask(id);
		return "";
	}

}
