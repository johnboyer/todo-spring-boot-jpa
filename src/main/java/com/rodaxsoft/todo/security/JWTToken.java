/*
  JWTToken.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWTToken class
 *
 */
public class JWTToken implements JSONWebToken {
	
	public static final String ACCESS_TOKEN_JSON_KEY = "jwt";
	public static final String REFRESH_TOKEN_JSON_KEY = "refresh_token";
	
	public static JWTToken fromJson(String json) throws IOException {
		return new ObjectMapper().readValue(json, JWTToken.class);
	}
	
	public static String toAccessTokenJson(String accessToken) throws JsonProcessingException {
		Map<String, String> mapToken = new HashMap<>();
		mapToken.put(ACCESS_TOKEN_JSON_KEY, accessToken);
		return new ObjectMapper().writeValueAsString(mapToken);
	}
	
	public static String toRefreshTokenJson(String refreshToken) throws JsonProcessingException {
		Map<String, String> mapToken = new HashMap<>();
		mapToken.put(REFRESH_TOKEN_JSON_KEY, refreshToken);
		return new ObjectMapper().writeValueAsString(mapToken);
	}
	
	private String accessToken;

	private String refreshToken;
	
	public JWTToken() {
		
	}
	
	JWTToken(String token, String refreshToken) {
		this.accessToken = token;
		this.refreshToken = refreshToken;
	}
	/**
	 * @return the access token
	 */
	@Override
	@JsonGetter(ACCESS_TOKEN_JSON_KEY)
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * @return the refresh token
	 */
	@Override
	@JsonGetter(REFRESH_TOKEN_JSON_KEY)
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * @param token the token to set
	 */
	@JsonSetter(ACCESS_TOKEN_JSON_KEY)
	public void setAccessToken(String token) {
		this.accessToken = token;
	}
	
	/**
	 * @param refreshToken the refreshToken to set
	 */
	@JsonSetter(REFRESH_TOKEN_JSON_KEY)
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String toAccessTokenJson() throws JsonProcessingException {
		return toAccessTokenJson(accessToken);
	}
	
	public String toRefreshTokenJson() throws JsonProcessingException {
		return toRefreshTokenJson(refreshToken);
	}
	
}
