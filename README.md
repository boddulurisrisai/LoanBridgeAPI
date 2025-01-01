## LoanBridge API

### Overview

This project is a Loan Management System that helps manage customer loan applications and eligibility checks. The frontend is developed using Angular 14, TypeScript, HTML, CSS, and Node.js, while the backend is powered by Java Spring Boot and Maven, with a MySQL database. The entire application is containerized using Docker and deployed on Render for global accessibility.

## Features

- Customer loan application form
- Loan eligibility check
- Max loan amount calculator
- Responsive UI
- Error handling and validation
- RESTful API integration
- API Gateway setup for routing requests

## Technologies Used

### Frontend

- **Angular 14**
- **TypeScript**
- **HTML**
- **CSS**
- **Node.js**

### Backend

- **Java Spring Boot**
- **Maven**
- **MySQL Database**

### Tools

- **Postman** (for API testing and debugging)
- **Render** (for deployment)
- **Spring Cloud Gateway** (for routing requests)

## Request and Response Flow

- **Frontend (Angular)**: All requests are made from the frontend (Angular application). These requests are routed to the **API Gateway**.

- **API Gateway (Spring Cloud Gateway)**: The API Gateway, running on port `8080`, forwards all incoming requests to the backend, which is running on port `8081`.

- **Backend (Spring Boot)**: The backend processes the requests (e.g., checking loan eligibility or calculating the max loan amount) and returns the appropriate response.

- **Frontend (Angular)**: The response is forwarded back to the frontend after processing by the backend via the API Gateway.

This flow ensures that all requests are handled through the API Gateway, simplifying routing and security.

### API Endpoints

- **Check Customer Eligibility:** `/api/loans/check-customer`
- **Calculate Max Loan Amount:** `/api/loans/calculate-max-loan`

### Testing

Use Postman or the frontend application to test the API endpoints. All requests will be routed through the API Gateway running on `http://localhost:8080`.
