/*
  TaskTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.data;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.domain.Task;
import com.rodaxsoft.todo.domain.TaskStatus;

/**
 * TaskTest class
 */
public class TaskTest {
	
	public static Task createMockTask() {
		Task task = new Task();
		task.setDue(LocalDate.now().plusDays(14).toDate());
		task.setCreated(new Date());
		task.setDescription("description of Test task 1 ");
		task.setTitle("Test Task 1");
		task.setId(UUID.randomUUID().toString());
		return task;
	}
	
	@Test
	public void testCreate() {
		
		Task task = createMockTask();

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
			task = mapper.readValue(result, Task.class);
			System.out.println(task);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test 
	public void testUpdate() {
		Task task = createMockTask();
		task.setCompleted(new Date());
		task.setModified(new Date());
		task.setStatus(TaskStatus.COMPLETED);

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
			task = mapper.readValue(result, Task.class);
			System.out.println(task);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		
	}

}
