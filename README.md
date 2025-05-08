# Secure Echo Service

A secure RESTful microservice with JWT-based authentication and role-based authorization that provides various echo services based on client permissions.

## Core Features

- JWT authentication using pre-shared API keys (clientId/apiKey pair)
- Role-based access control (RBAC) with USER_ROLE and ADMIN_ROLE permissions
- Protected endpoints with role-specific authorization
- Public endpoints accessible without authentication
- Secure credential management via environment variables and properties
- Configurable JWT expiration and secret management

## Technology Stack

- Java 21
- Spring Boot 3.2
- Spring Security 6
- Auth0 Java JWT Library (v4.4.0)
- Gradle build system
- SLF4J for structured logging
- Spring Web for RESTful endpoints

## Configuration & Secret Management

The application uses Spring's configuration system to manage credentials and settings:

### JWT Configuration

- `jwt.secret`: The secret key used to sign JWTs (critical security parameter)
- `jwt.expiration`: Token validity period in milliseconds

### Client Credentials

Client credentials are configured through the `ClientProperties` class:

- User client: API key for standard user access
- Admin client: API key for administrative access

### Setting Up for Development

1. Create an `application-dev.properties` or `application-dev.yml` file with your development settings:

   ```properties
   # JWT Configuration
   jwt.secret=your_secure_random_string_here
   jwt.expiration=3600000
   
   # Client Configurations
   app.clients[0].id=user-client
   app.clients[0].api-key=your_user_client_key_here
   app.clients[0].roles=USER_ROLE
   
   app.clients[1].id=admin-client
   app.clients[1].api-key=your_admin_client_key_here
   app.clients[1].roles=ADMIN_ROLE,USER_ROLE
   ```

2. Activate the profile with `spring.profiles.active=dev`

### Production Security Recommendations

For production environments:

1. Use a secure random generator to create your JWT secret
2. Store credentials in environment variables or a secure vault
3. Consider using a dedicated secrets manager like HashiCorp Vault, AWS Secrets Manager, or Kubernetes Secrets
4. Rotate client API keys regularly
5. Use HTTPS for all communications
6. Configure appropriate JWT expiration times

## API Endpoints

### Public Endpoints

- `GET /api/public/hello` - Public greeting endpoint
- `GET /api/public/info` - Service information

### Authentication Endpoint

- `POST /api/auth/token` - Obtain a JWT token using client credentials
  - Request body: `{"clientId": "user-client", "apiKey": "your-api-key"}`
  - Response: `{"token": "your.jwt.token", "expiresIn": 3600}`

### Protected Endpoints

#### User Role (requires USER_ROLE)
- `POST /api/echo/user/standard` - Standard echo service
  - Request: `{"message": "Hello, world!"}`
  - Response: `{"echo": "Hello, world!", "service": "User Echo Service"}`
- `POST /api/echo/user/uppercase` - Uppercase transformation service
  - Request: `{"message": "Hello, world!"}`
  - Response: `{"echo": "HELLO, WORLD!", "service": "User Echo Service (Uppercase)"}`
- `GET /api/echo/user/info` - User authentication information

#### Admin Role (requires ADMIN_ROLE)
- `POST /api/echo/admin/reverse` - Reverse echo service
  - Request: `{"message": "Hello, world!"}`
  - Response: `{"echo": "!dlrow ,olleH", "service": "Admin Echo Service (Reverse)"}`
- `GET /api/echo/admin/info` - Admin authentication information

## Authentication Flow

1. **Obtain a JWT token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/token \
     -H "Content-Type: application/json" \
     -d '{"clientId": "user-client", "apiKey": "your-api-key"}'
   ```

2. **Use the token to access protected resources:**
   ```bash
   curl -X POST http://localhost:8080/api/echo/user/standard \
     -H "Authorization: Bearer your.jwt.token" \
     -H "Content-Type: application/json" \
     -d '{"message": "Hello, world!"}'
   ```

## Security Notes

- The JWT implementation uses HMAC256 for signing tokens
- Tokens contain client ID and role information as claims
- The application validates tokens on each request to protected endpoints
- Invalid or expired tokens result in 401 Unauthorized responses
- Requests to endpoints without sufficient role permissions result in 403 Forbidden responses

## Logging

The application includes comprehensive logging for:
- Authentication attempts (success/failure)
- Token generation and validation
- Client configuration at startup
- Request processing for security auditing

Logs are structured for easy integration with log management systems.
