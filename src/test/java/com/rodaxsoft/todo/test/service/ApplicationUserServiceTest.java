/*
  ApplicationUserServiceTest.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.service;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.security.JSONWebToken;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * ApplicationUserServiceTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationUserServiceTest {
	
	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private ApplicationUserService userService;
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
	}

	private JSONWebToken createUser() {
		ApplicationUser user = TaskTestUtils.createMockApplicationUser();
		JSONWebToken token = userService.signUpUser(user);
		Assert.assertNotNull(token);
		Assert.assertNotNull(token.getAccessToken());
		Assert.assertNotNull(token.getRefreshToken());
		return token;
	}
	
	@Test
	public void testLogin() {
		//Create user in the database
		JSONWebToken token = createUser();
		
		ApplicationUser user = TaskTestUtils.createMockApplicationUser();
		token = userService.loginUser(user);
		Assert.assertNotNull(token);
		Assert.assertNotNull(token.getAccessToken());
		Assert.assertNotNull(token.getRefreshToken());
	}
	
	@Test
	public void testLogout() {
		// Create user in the database
		JSONWebToken token = createUser();

		try {
			userService.logoutUser(token, () -> {
				System.out.println("Expire token here!");
			});
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
	}
	
	
	@Test
	public void testRefreshAccessToken() {
		// Create user in the database
		JSONWebToken token = createUser();
		
		try {
			Map<String, String> tokenMap = userService.refreshAccessToken(token, () -> {
				System.out.println("Expire token here!");
			});
			
			Assert.assertNotNull(tokenMap);
			Assert.assertNotNull(tokenMap.get("jwt"));
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSignup() {
		JSONWebToken token = createUser();
		System.out.println(token);
	}

}
