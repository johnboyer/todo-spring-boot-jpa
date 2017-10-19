/*
  JpaTaskService.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;
import com.rodaxsoft.todo.exception.ValidationException;

/**
 * JpaTaskService class
 */
@Service
public class JpaTaskService implements TaskService {
	
	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Task createTask(Task task) {
		if(null == task.getTitle()) {
			throw new ValidationException("title cannot be null");
		}
		
		Task savedTask = taskRepository.save(task);
		return savedTask;
	}
	
	@Override
	public boolean exists(String id) {
		return taskRepository.exists(id);
	}
	
	@Override
	public boolean exists(Task task) {
		return exists(task.getId());
	}
	
	@Override
	public List<Task> getTasks() {
		Sort sort = new Sort(new Order(Direction.ASC, "due").nullsFirst());
		List<Task> tasks = taskRepository.findAll(sort);
		return tasks;
	}
	
	@Override
	public void deleteTask(String id) {
		if(!taskRepository.exists(id)) {
			throw new ResourceNotFoundException("Task not found");
		}
		
		taskRepository.delete(id);
	}
	
	@Override
	public Task updateTask(Task task) {
		final String id = task.getId();
		if(!taskRepository.exists(id)) {
			throw new ResourceNotFoundException("Task not found");
		}
		
		Task savedTask = taskRepository.findOne(id);
		//Non-modifiable properties: id, created, userId
		task.setId(savedTask.getId());
		task.setCreated(savedTask.getCreated());
		task.setUserId(savedTask.getUserId());
		//Ignore modified, it'll get updated
		
		savedTask = taskRepository.save(task)	;
		return savedTask;
	}
}
