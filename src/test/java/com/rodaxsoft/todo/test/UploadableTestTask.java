/*
  UploadableTestTask.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rodaxsoft.todo.domain.TaskStatus;

/**
 * UploadableTestTask class
 */
public class UploadableTestTask {
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date completed;
	
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date due;
	private TaskStatus status;
	private String title;
	/**
	 * @return the completed
	 */
	public Date getCompleted() {
		return completed;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the due
	 */
	public Date getDue() {
		return due;
	}
	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Date completed) {
		this.completed = completed;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param due the due to set
	 */
	public void setDue(Date due) {
		this.due = due;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
