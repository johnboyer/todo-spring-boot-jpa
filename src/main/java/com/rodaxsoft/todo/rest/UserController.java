/*
  UserController.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.rest;

import static com.rodaxsoft.todo.security.JWTUtil.parseToken;
import static com.rodaxsoft.todo.security.SecurityConstants.HEADER_STRING;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.domain.Profile;
import com.rodaxsoft.todo.exception.ValidationException;
import com.rodaxsoft.todo.security.JSONWebToken;
import com.rodaxsoft.todo.security.JWTToken;
import com.rodaxsoft.todo.security.JWTUtil;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.validation.ApplicationUserLoginValidator;
import com.rodaxsoft.todo.validation.StoredApplicationUserProvider;

/**
 * UserController class
 */
@RestController
public class UserController implements StoredApplicationUserProvider {

	private static final Log LOG = LogFactory.getLog(UserController.class);

	/**
	 * Login validator
	 */
	@Autowired
	private final Validator applicationUserLoginValidator;
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	/**
	 * Signup validator
	 */
	private final Validator applicationUserSignupValidator;
	
	@Autowired
	public UserController(Validator applicationUserSignupValidator) {
		this.applicationUserSignupValidator = applicationUserSignupValidator;
		this.applicationUserLoginValidator = new ApplicationUserLoginValidator(this);
	}

	@Override
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return applicationUserService.getBCryptPasswordEncoder();
	}

	/**
	 * Returns the current users info.
	 * @param token The JWT token
	 * @return Current user's profile
	 */
	@GetMapping(path = "me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Profile getProfile(@RequestHeader(HEADER_STRING) String token)  {
		return getUserProfile(token);
	}

	/**
	 * @param token
	 * @return
	 */
	private Profile getUserProfile(String token) {
		String username = parseToken(token).getUsername();
		ApplicationUser savedUser = storedApplicationUserForEmail(username);
		LOG.info("User retrieved");

		return savedUser.getProfile();
	}

	/**
	 * Handles errors in the binding result
	 * @param result The binding result
	 */
	private void handleErrors(BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result);
		}
	}

	private Map<String, String> handleRefreshAccessToken(JWTToken jwtToken, HttpServletRequest req,
			HttpServletResponse res) {
		return applicationUserService.refreshAccessToken(jwtToken, () -> {
			// Delete existing cookie
			Cookie cookie = JWTUtil.getAccessTokenCookie(req);
			if(cookie != null) {
				cookie.setMaxAge(0);
				res.addCookie(cookie);
			}
		});
	}

	private String internalLogout(JSONWebToken jwtToken, HttpServletRequest req, HttpServletResponse res) {

		applicationUserService.logoutUser(jwtToken, () -> {
			Cookie cookie = JWTUtil.getAccessTokenCookie(req);
			if (cookie != null) {
				// Delete the cookie
				cookie.setMaxAge(0);
				res.addCookie(cookie);
			} else {
				LOG.warn("Cookie access token cookie not found");
			}
		});

		return "";
	}


	/**
	 * Performs user login
	 * @param user The given user
	 * @param result The binding result
	 * @return The JWT token object
	 */
	@PostMapping(path = "/access-tokens", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public JSONWebToken login(@RequestBody ApplicationUser user, BindingResult result) {
		applicationUserLoginValidator.validate(user, result);
		handleErrors(result);
		
		return applicationUserService.loginUser(user);
	}

	/**
	 * User logout
	 * @param jwtToken The JWT token object containing a refresh token
	 * @param accessToken The access token
	 * @param req The servlet request
	 * @param res The servlet response
	 */
	@DeleteMapping(path = "/access-tokens", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String logout(@RequestBody JWTToken jwtToken, @RequestHeader(HEADER_STRING) String accessToken, HttpServletRequest req, HttpServletResponse res) {
		
		// Logout jwtToken has no access token in the body
		jwtToken.setAccessToken(accessToken);
		return internalLogout(jwtToken, req, res);
	}
	
	/**
	 * Refresh access token
	 * @param jwtToken The JWT token object containing a refresh token
	 * @param accessToken The access token
	 * @param req The servlet request
	 * @param res The servlet response
	 */
	@PostMapping(path = "/access-tokens/refresh", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, String> refreshAccessToken(@RequestBody JWTToken jwtToken, @RequestHeader(HEADER_STRING) String accessToken, HttpServletRequest req,HttpServletResponse res) {
		
		// Logout jwtToken has no access token in the body
	    jwtToken.setAccessToken(accessToken);
		return handleRefreshAccessToken(jwtToken, req, res);
	}

	
	/**
	 * User signup
	 * @param user The user to signup
	 * @param result The binding result
	 * @return The JWT token object
	 */
	@PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public JSONWebToken signUp(@RequestBody ApplicationUser user, BindingResult result) {
		applicationUserSignupValidator.validate(user, result);
		handleErrors(result);
		
		return applicationUserService.signUpUser(user);
	}

	@Override
	public ApplicationUser storedApplicationUserForEmail(String email) {
		return applicationUserService.storedApplicationUserForEmail(email);
	}

}
