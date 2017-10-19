/*
  JWTAuthorizationFilter.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.security;

import static com.rodaxsoft.todo.security.JWTUtil.authenticate;
import static com.rodaxsoft.todo.security.JWTUtil.getAccessTokenCookie;
import static com.rodaxsoft.todo.security.SecurityConstants.HEADER_STRING;
import static com.rodaxsoft.todo.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * JWTAuthorizationFilter class 
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }
    
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		Cookie cookie = getAccessTokenCookie(req);
		String header = req.getHeader(HEADER_STRING);

		if (cookie == null && null == header) {
			chain.doFilter(req, res);
			return;
		}

		Authentication authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}
    
    
	private Authentication getAuthentication(HttpServletRequest request) {
		
		//Check for header token
		String token = request.getHeader(HEADER_STRING);
		if (null == token) {
			
			//Check for Cookie token
			Cookie cookie = getAccessTokenCookie(request);
			if (null == cookie) {
				throw new JSONWebTokenException("Unable to authenticate without header or cookie");
			}
			
			token = cookie.getValue();
		}
		
		return authenticate(token);
	}
}
