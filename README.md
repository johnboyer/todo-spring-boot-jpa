# Todo API

Todo is a task management service that enables registered users to easily manage their tasks. Each task has a name as well as other optional values including *due date*, *status*, and *description*. The next version will include *rank*, so that tasks can be ordered by priority.

The Todo RESTful API is implemented as a [Spring Boot](https://projects.spring.io/spring-boot/) app running an embedded version of Tomcat. For demonstration purposes, the data store is a [HSQLDB](http://hsqldb.org) in-memory database accessed via [Spring JPA](https://projects.spring.io/spring-data-jpa/). It uses [JSON Web Tokens (JWT)](https://jwt.io/) for authentication.

# Table of Contents

1. [Build and Run](#build-and-run)
2. [Reference](#reference)
3. [Contributors](#contributors)
4. [License](#license)

# Build and Run

To build and run the project in place type: `$ gradle bootRun`

To just build the project type: `$ gradle build`

For information on installing Gradle go to https://gradle.org/install

# Reference

## Getting Started
To create and manage tasks, first create a user. See *signup* in the User section below.

Note: All dates are specified as strings, i.e., `yyyy-MM-dd` or `yyyy-MM-dd'T'hh:mm:ss`.

For a detailed reference of the Todo RESTful API go to https://todo.restlet.io.

## API Overview
### User

Method | HTTP Requests | Description | Returns
------------ | ------------- |-------|--------
*signup* | `POST /users`| Create a user | A JWT  object
*profile* | `GET /me` | View current user | User info
*login* |`POST/access-tokens` | Login user | A JWT object
*logout* |`DELETE/access-tokens` | Logout user | Nothing
*refresh* |`POST /access-tokens/refresh` |Refresh user token | JWT access token

### Tasks

Method | HTTP Requests | Description | Returns
------------ | ------------- |-------|--------
*insert* | `POST /tasks` | Create a task | A task object
*list* | `GET /tasks`| Returns tasks | An array of task objects
*update* |`PUT /tasks/:id` | Update a task | A task object
*delete* |`DELETE /tasks/:id` | Delete a task | Nothing

# Contributors

To reach a milestone for a major release, we'd like contributions for the following:
* Add task for ranking and priority sorting
* Implement task lists or categories
* Add support for SQL or NoSQL persistent data store

Contributions can be made by following these steps:

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

If you have any questions, please don't hesitate to contact me at john@rodaxsoft.com.

# License
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.

_By John Boyer, Rodax Software, Inc._
