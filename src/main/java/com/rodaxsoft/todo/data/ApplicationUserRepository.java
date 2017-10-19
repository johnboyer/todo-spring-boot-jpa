/*
  ApplicationUserRepository.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rodaxsoft.todo.domain.ApplicationUser;

/**
 * ApplicationUserRepository interface
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    ApplicationUser findByEmail(String email);
}
