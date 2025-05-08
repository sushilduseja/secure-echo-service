# Secure Echo Service

A secure echo service with JWT-based authentication and role-based authorization.

## Core Features

- JWT authentication using pre-shared API keys (no traditional passwords)
- Role-based access control (RBAC) with USER and ADMIN roles
- Protected endpoints requiring specific permissions
- Public endpoints accessible without authentication

## Technology Stack

- Java 21
- Spring Boot 3.2
- Spring Security 6
- Auth0 Java JWT Library (v4.4.0)
- Gradle build system
- SLF4J for logging
- Spring Web for RESTful endpoints

## API Endpoints

### Public Endpoints

- `GET /api/public/hello` - Public greeting endpoint
- `GET /api/public/info` - Service information

### Authentication Endpoints

- `POST /api/auth/token` - Get JWT token using client credentials

### Protected Endpoints

#### User Role Required
- `POST /api/echo/user/standard` - Standard echo service
- `POST /api/echo/user/uppercase` - Uppercase echo service
- `GET /api/echo/user/info` - User authentication info

#### Admin Role Required
- `POST /api/echo/admin/reverse` - Reverse echo service
- `GET /api/echo/admin/info` - Admin authentication info

