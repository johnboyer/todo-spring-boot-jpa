/*
  ApplicationUserService.java

  Created by John Boyer on Oct 19, 2017
  Copyright (c) 2017 Rodax Software, Inc.
  
  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.security.JSONWebToken;
import com.rodaxsoft.todo.security.JWTToken;

/**
 * Application user service interface
 * @author John Boyer
 *
 */
public interface ApplicationUserService {
	/**
	 * @return BCryptPasswordEncoder instance
	 */
	BCryptPasswordEncoder getBCryptPasswordEncoder();
	/**
	 * Logs in the given <code>user</code> and returns a JWT token object
	 * @param user
	 * @return a JWT token object
	 */
	JSONWebToken loginUser(ApplicationUser user);
	/**
	 * Signs up a user and returns a JWT token object
	 * @param user The user to sign-up
	 * @return a JWT token object
	 */
	JSONWebToken signUpUser(ApplicationUser user);
	/**
	 * Logs out the user
	 * @param token A JWT token object
	 * @param listener An application user service listener
	 */
	void logoutUser(JSONWebToken token, ApplicationUserServiceListener listener);
	/**
	 * Refreshes the access token
	 * @param token The current access token
	 * @param listener An application user service listener
	 * @return A map with {@link JWTToken#ACCESS_TOKEN_JSON_KEY} ask the key for the value.
	 */
	Map<String, String> refreshAccessToken(JSONWebToken token, ApplicationUserServiceListener listener);
	/**
	 * Returns the user id for the given <code>token</code>
	 * @param token The user token value
	 */
	String getUserIdForToken(String token);
	/**
	 * Returns the user for the given <code>email</code>.
	 * @param email The user's email
	 */
	ApplicationUser storedApplicationUserForEmail(String email);

}