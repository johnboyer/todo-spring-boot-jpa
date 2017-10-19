/*
  Task.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.util.JdkIdGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Task class
 *
 */
@Entity
public class Task {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date completed;


	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	private Date created;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date due;

	@Id
	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	private Date modified;


	private TaskStatus status = TaskStatus.OPEN;
	
	private String title;
	
	@JsonIgnore
	private String userId;

	/**
	 * @return the completed
	 */
	public Date getCompleted() {
		return completed;
	}
	
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	@PrePersist
	protected void onCreate() {
		created = new Date();
		id = new JdkIdGenerator().generateId().toString();
	}
	
	@PreUpdate
	protected void onUpdate() {
		setModified(new Date());
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Date completionDate) {
		this.completed = completionDate;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date createdDate) {
		this.created = createdDate;
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
	public void setDue(Date dueDate) {
		this.due = dueDate;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modifiedDate) {
		this.modified = modifiedDate;
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

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [completed=" + completed + ", created=" + created + ", description=" + description + ", due="
				+ due + ", id=" + id + ", modified=" + modified + ", status=" + status + ", title=" + title + "]";
	}
	
}

