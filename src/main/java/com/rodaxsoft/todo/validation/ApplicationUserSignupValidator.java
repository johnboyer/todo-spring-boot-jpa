/*
  ApplicationUserSignupValidator.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.todo.domain.ApplicationUser;

/**
 * ApplicationUserSignupValidator class
 *
 */
public class ApplicationUserSignupValidator implements Validator {
	
	private static final String FIELD_REQUIRED_CODE = "field.required";
	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
	/**
	 * Password Regex string.
	 * Password (at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 number)
	 */
	public static final String PASSWORD_REGEX = "(?=^.{8,}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
	private static final RegexValidator PASSWORD_VALIDATOR = new RegexValidator(PASSWORD_REGEX);


	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationUser.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", FIELD_REQUIRED_CODE);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", FIELD_REQUIRED_CODE);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", FIELD_REQUIRED_CODE);
		
		ApplicationUser user = (ApplicationUser) target;
		if(!EMAIL_VALIDATOR.isValid(user.getEmail())) {
			errors.rejectValue("email", "email.invalid");			
		}
		
		if(!PASSWORD_VALIDATOR.isValid(user.getPassword())) {
			errors.rejectValue("password", "password.invalid", "Password must have at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 number.");
		}
		
	}

}
