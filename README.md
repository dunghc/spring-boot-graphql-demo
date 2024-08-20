## Introduction
This project demonstrates a simple GraphQL API built with Spring Boot and Spring GraphQL. 

## Authentication
This project integrates OAuth2 using the Auth0 service to secure the GraphQL API. 
**Key features:**
* **Authorization:** Leveraging Auth0 for user authentication and authorization.
* **Token-Based Authentication:** Employs industry-standard OAuth2 tokens for secure access. 
**To learn more about setting up and configuring Auth0, refer to their official documentation: [https://auth0.com/docs](https://auth0.com/docs)** 

## Data Persistence
This project utilizes Spring Data JPA with an H2 in-memory database for efficient and convenient data management.
**Key features:**
* **Spring Data JPA:** Simplifies database interactions through powerful repositories.
* **H2 Database:** Provides a fast and lightweight in-memory database for development and testing.
This setup allows for seamless data persistence without the need for a full-fledged external database during development.
## Getting Started
  **For login auth:**

	  curl --location 'http://localhost:8080/api/login' \
		--header 'Content-Type: application/json' \
		--data-raw '{"username":"dunghc@nextcore.vn","password":"NextCore@123"}

  **GraphQL end-point:**

  	http://localhost:8080/graphql
	Authorization: Bearer Token {access_token from login api}
