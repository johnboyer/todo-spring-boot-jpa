/*
  JWTUtilTest.java

  Created by John Boyer on Sep 7, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test;

import static com.rodaxsoft.todo.security.JWTUtil.generateJsonWebToken;
import static com.rodaxsoft.todo.security.JWTUtil.parseToken;
import static com.rodaxsoft.todo.security.JWTUtil.parseTokenClaims;
import static com.rodaxsoft.todo.security.JWTUtil.refreshAccessToken;
import static com.rodaxsoft.todo.test.TaskTestUtils.createMockApplicationUser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.security.JSONWebToken;

import io.jsonwebtoken.Claims;

/**
 * JWTUtilTest class
 */
@RunWith(SpringRunner.class)
public class JWTUtilTest  {
	
	
	private JSONWebToken createToken() {
		return generateJsonWebToken(createMockApplicationUser());
	}
	
	@Test
	public void testGenerateJsonWebToken() {
		JSONWebToken webToken = createToken();
		final String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		Claims accessTokenClaims = parseTokenClaims(accessToken);
		Claims refreshTokenClaims = parseTokenClaims(refreshToken);
		
		Assert.assertTrue(accessTokenClaims.getExpiration().before(refreshTokenClaims.getExpiration()));
	}
	
	@Test
	public void testParseToken() {
		JSONWebToken webToken = createToken();
		final String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
	}
	
	@Test
	public void testRefreshAccessToken() {
		JSONWebToken webToken = createToken();
		String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		Claims accessTokenClaims = parseTokenClaims(accessToken);
		Claims refreshTokenClaims = parseTokenClaims(refreshToken);
		
		Assert.assertTrue(accessTokenClaims.getExpiration().before(refreshTokenClaims.getExpiration()));
		
		accessToken = refreshAccessToken(webToken);
		Assert.assertNotNull(accessToken);	
		
		username = parseToken(accessToken).getUsername();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
	}
	
	
	

}
