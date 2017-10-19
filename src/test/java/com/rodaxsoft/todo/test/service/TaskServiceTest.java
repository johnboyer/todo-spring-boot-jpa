/*
  TaskServiceTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.domain.TaskStatus;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * TaskServiceTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
	@Autowired
	private TaskRepository taskRepsitory;
	
	@Autowired
	private TaskService taskService;
	
	private String userId;
	
	@Autowired
	private ApplicationUserRepository userRepository; 
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
		taskRepsitory.deleteAll();
	}
	
	/**
	 * @return
	 */
	private Task createMockTask() {
		Task task = new Task();
		task.setDue(LocalDate.now().plusDays(14).toDate());
		task.setDescription("Description of Test task 1 ");
		task.setTitle("Test Task 1");
		task.setUserId(userId);
		return task;
	}
	
	@Before
	public void setup() {
		ApplicationUser user = userRepository.save(TaskTestUtils.createMockApplicationUser());
		userId = user.getId();
	}
	
	
	@Test
	public void testCreateTask() {
		Task savedTask = taskService.createTask(createMockTask());
		System.out.println(savedTask);
		Assert.assertNotNull(savedTask.getCreated());
		Assert.assertNotNull(savedTask.getId());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteTask() {
		Task savedTask = taskService.createTask(createMockTask());
		taskService.deleteTask(savedTask.getId());
		System.out.println(savedTask);
		assertTrue(taskService.getTasks().isEmpty());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetTasks() {
		List<Task> tasks = TaskTestUtils.create100Tasks();
		Task prev = null;
		for (Task task : tasks) {
			Task savedTask = taskService.createTask(task);
			System.out.println(savedTask);
			Assert.assertNotNull(savedTask.getCreated());
			Assert.assertNotNull(savedTask.getId());
			
			if(prev != null && prev.getDue() != null && task.getDue() != null) {
				Assert.assertTrue(prev.getDue().before(task.getDue()));
			}
			
			prev = task;
		}
		
		tasks = taskService.getTasks();
		System.out.println(tasks);
	}
	
	@Test
	public void testUpdateTask() {
		Task savedTask = taskService.createTask(createMockTask());
		savedTask.setStatus(TaskStatus.COMPLETED);
		savedTask.setCompleted(new Date());
		savedTask = taskService.updateTask(savedTask);
		System.out.println(savedTask);
		Assert.assertNotNull(savedTask.getModified());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}

}
