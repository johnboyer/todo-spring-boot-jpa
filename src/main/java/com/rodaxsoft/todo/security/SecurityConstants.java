/*
  SecurityConstants.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.security;

/**
 * SecurityConstants interface
 */
public interface SecurityConstants {
	/**
	 * Secret string
	 */
    String SECRET = "5ba9b05f-267f-472a-bf2a-ecae5da6a9e9";
    /**
     * Expiration time in milliseconds
     */
    long EXPIRATION_TIME = 600_000; // 10 minutes
    /**
     * Token prefix, e.g., <code>Bearer</code>
     */
    String TOKEN_PREFIX = "";
    /**
     * Header string, e.g., <code>Authorization</code> or<code> x-access-token</code>
     */
    String HEADER_STRING = "x-access-token";
    /**
     * Cookie key, e.g., <code>ACCESS_TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJleHAi...</code>
     */
    String COOKIE_STRING = "ACCESS_TOKEN";
    /**
     * Sign-up URL
     */
    String SIGN_UP_URL = "/users";
    /**
     * Login URL
     */
    String LOGIN_URL = "/access-tokens";
    
	/**
	 * Max cookie age is 600 s or 10 min.
	 */
	int MAX_COOKIE_AGE = (int) (EXPIRATION_TIME/1000);
}
