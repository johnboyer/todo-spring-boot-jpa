/*
  TaskRepositoryTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.data;

import static com.rodaxsoft.todo.test.TaskTestUtils.DESCRIPTION;
import static com.rodaxsoft.todo.test.TaskTestUtils.DUE_DATE;
import static com.rodaxsoft.todo.test.TaskTestUtils.TITLE;
import static com.rodaxsoft.todo.test.TaskTestUtils.createMockTask;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * TaskRepositoryTest class
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@After
	public void cleanup() {
		taskRepository.deleteAll();
	}
	
	
	@Test
	public void testGetTasks() {
		//Save a task
		Task task = createMockTask();
		task = taskRepository.save(task);
		String taskId = task.getId();
		
		List<Task> tasks = taskRepository.findAll();
		Assert.assertSame(1, tasks.size());
		Assert.assertSame(taskId, tasks.get(0).getId());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}

	
	@Test
	public void testSaveTask() {
		
		Task task = TaskTestUtils.createMockTask();
		
		task = taskRepository.save(task);
		System.out.println();
		System.out.println(task);
		System.out.println();
		
		Assert.assertSame(DESCRIPTION, task.getDescription());
		Assert.assertSame(TITLE, task.getTitle());
		Assert.assertSame(DUE_DATE, task.getDue());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testUpdateTask() {

		// Save a task
		Task task = createMockTask();
		task = taskRepository.save(task);
		
		final String desc = "An all new description";
		task.setDescription(desc);
		final String title = "The title has changed";
		task.setTitle(title);
		
		//Update the task
		task = taskRepository.save(task);
		Assert.assertSame(desc, task.getDescription());
		Assert.assertSame(title, task.getTitle());
		Assert.assertSame(TaskTestUtils.DUE_DATE, task.getDue());
			
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}

	}

}
