/*
  ApplicationUserLoginValidator.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.validation;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;

/**
 * ApplicationUserLoginValidator class
 */
public class ApplicationUserLoginValidator implements Validator {
	
	private static final String FIELD_REQUIRED_CODE = "field.required";
	private static final String FIELD_REQUIRED_MSG = " Field required";
	
	private StoredApplicationUserProvider storedApplicationUserProvider;
	
	public ApplicationUserLoginValidator(StoredApplicationUserProvider storedApplicationUserProvider) {
		this.storedApplicationUserProvider = storedApplicationUserProvider;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationUser.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", FIELD_REQUIRED_CODE, FIELD_REQUIRED_MSG);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", FIELD_REQUIRED_CODE, FIELD_REQUIRED_MSG);

		ApplicationUser user = (ApplicationUser) target;

		String email = user.getEmail();
		ApplicationUser storedUser = storedApplicationUserProvider.storedApplicationUserForEmail(email);
		if(null == storedUser) {
			throw new ResourceNotFoundException("Email address not found: " + email);
		} else {

			if(!storedUser.getEmail().equals(email)) {
				errors.rejectValue("email", "email.mismatch", "Email address mismatch.");
			}

			String rawPassword = user.getPassword();
			BCryptPasswordEncoder encoder = storedApplicationUserProvider.getBCryptPasswordEncoder();
			if(!encoder.matches(rawPassword, storedUser.getPassword())) {
				errors.rejectValue("password", "password.mismatch", "Password mismatch.");
			}
		}
	}

}
