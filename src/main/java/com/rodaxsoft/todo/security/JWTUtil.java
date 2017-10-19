/*
  JWTUtil.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.security;

import static com.rodaxsoft.todo.security.SecurityConstants.COOKIE_STRING;
import static com.rodaxsoft.todo.security.SecurityConstants.EXPIRATION_TIME;
import static com.rodaxsoft.todo.security.SecurityConstants.SECRET;
import static com.rodaxsoft.todo.security.SecurityConstants.TOKEN_PREFIX;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWTUtil class
 *
 */
public class JWTUtil {
	
	private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 14;


	private static final class MinimalAuthentication implements ApplicationAuthentication {
		
		private final Authentication authentication;
		
		public MinimalAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}

		@Override
		public String getUsername() {
			Object principal = authentication.getPrincipal();
			if(principal instanceof User) {
				return ((User)authentication.getPrincipal()).getUsername();
			} else {
				return authentication.getPrincipal().toString();
			}
		}
	}
	
	/**
	 * Returns An authentication token object
	 * @param token JWT token to authenticate
	 * @return An authentication token object
	 */
	static Authentication authenticate(String token) {
		return internalParseToken(token);
	}
	
	/**
	 * Generates a JSON web token object
	 * @param creds The user credentials
	 * @return A JSON web token object
	 */
	public static JSONWebToken generateJsonWebToken(ApplicationAuthentication auth) {
		String token = generateAccessToken(auth);
		String refreshToken = generateRefreshToken(token);
		return new JWTToken(token, refreshToken);
	}

	/**
	 * Generates a refresh token from the given token.
	 * Note: <b>Clients should use {@link #generateJsonWebToken(Credentials)} instead.</b>
	 * @param token A valid token
	 * @return A refresh token
	 */
	static String generateRefreshToken(String token) {
		Claims claims = parseTokenClaims(token);
		
		//Set the refresh token expiration to 14 days
		Date exp = claims.getExpiration();
		final Instant inst = exp.toInstant();
		LocalDateTime ldt = LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
		ldt = ldt.plusDays(REFRESH_TOKEN_EXPIRATION_DAYS);
		final ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		//New expiration date in 14 days
		exp = Date.from(zdt.toInstant());
		
		//Reset expiration date
		claims.setExpiration(exp);
		
		String refreshToken = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		return refreshToken;
	}
	
	/**
	 * Refreshes the access token with the given token
	 * @param jwtToken A JSON web token
	 * @return A new access token
	 * @return A a new access token
	 */
	public static String refreshAccessToken(JSONWebToken jwtToken) {
		
		if(null == jwtToken.getAccessToken() || null == jwtToken.getRefreshToken()) {
			throw new JSONWebTokenException("tokens cannot be null");
		}
		
		//Access token
		final String accessToken = jwtToken.getAccessToken();
		final String accessTokenUsername = parseToken(accessToken).getUsername();

		//Refresh token
		final String refreshToken = jwtToken.getRefreshToken();
		final String refreshTokenUsername = parseToken(refreshToken).getUsername();

		//Ensure usernames match
		if(!accessTokenUsername.equals(refreshTokenUsername)) {
			throw new JSONWebTokenException("access and refresh token mismatch");
		}
		
		if(isTokenExpired(refreshToken)) {
			throw new JSONWebTokenException("refresh token expired");
		}

		return generateAccessToken(() -> { return accessTokenUsername; });
	}

	/**
	 * Generates a JWT access token
	 * @param auth User credentials objects
	 * @return A JWT access token
	 */
	static String generateAccessToken(ApplicationAuthentication auth) {
		String token = Jwts.builder()
				           .setSubject(auth.getUsername())
				           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				           .signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return token;
	}
	
	/**
	 * Returns the JWT access token as a cookie
	 * @param req The servlet request
	 * @return The JWT access token as a cookie or <code>null</code>.
	 */
	public static Cookie getAccessTokenCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(COOKIE_STRING)) {
					return cookie;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param auth
	 * @return
	 */
	public static Credentials getCredentials(Authentication auth) {
		return new Credentials() {
			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public String getUsername() {
				return new MinimalAuthentication(auth).getUsername();
			}
		};
	}

	/**
	 * @param token
	 * @param upaToken
	 * @return
	 */
	private static UsernamePasswordAuthenticationToken internalParseToken(String token) {
		// parse the token.
		UsernamePasswordAuthenticationToken upaToken = null;
		if (token != null) {

			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();

			if (user != null) {
				upaToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			} else {
				throw new JSONWebTokenException("invalid token");
			}
		}
		return upaToken;
	}

	/**
	 * Returns the application authentication object.
	 * 
	 * @param token The token to parse
	 * @return The authentication token object or
	 *         <code>null</code>.
	 */
	public static ApplicationAuthentication parseToken(String token) {
		UsernamePasswordAuthenticationToken upaToken = null;

		if (token != null) {
			upaToken = internalParseToken(token);
		}
		
		return new MinimalAuthentication(upaToken);
	}
	
	 public static Claims parseTokenClaims(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody();
		
		return claims;
	}
	 
	public static boolean isTokenExpired(String token) {
		Claims claims = parseTokenClaims(token);
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}
	
	
	private JWTUtil() {

	}

}
