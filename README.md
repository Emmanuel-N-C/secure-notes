# Secure Notes Service

A secure REST API for managing encrypted notes using **AES encryption** and **token-based authentication**, built with **Spring Boot 3.5.7** and **Java 17**.

---

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **H2 In-Memory Database**
- **Spring Data JPA**
- **Maven**
- **MockMvc** (Testing)

---

## Design Choices

### Architecture
- **Layered structure:**  
  `Controller → Service → Repository`
- **DTOs:**  
  `NoteRequest`, `NoteResponse` to decouple API from internal entities.
- **Constructor injection:**  
  Promotes immutability, improves testability.
- **Service abstraction:**  
  Interface-based service layer for flexibility.

### Why this matters
Clean separation of concerns makes the code:
- Maintainable  
- Testable  
- Easily extendable  
- Secure  

---

## Security Measures

### 1️⃣ Token-Based Authentication
- Static Bearer token loaded from environment variables.
- Custom filter (`TokenAuthFilter`) validates: Authorization: Bearer <token>
- All `/notes/**` endpoints require authentication.
- Invalid or missing token → `401 Unauthorized`.

### 2️⃣ AES-256 Content Encryption
- Note **content is encrypted before being stored**.
- AES 256-bit key (32-byte hex string).
- Encrypted data saved as Base64.
- Decrypted automatically on retrieval.

### 3️⃣ Input Validation
- `@NotBlank` on title and content.
- Invalid input → `400 Bad Request`.

### 4️⃣ Stateless Sessions
- No server-side sessions.
- CSRF disabled (standard for stateless APIs).

---

## Setup Instructions

### Prerequisites
- Java **17+**
- Maven **3.6+**  
(or use included `mvnw` wrapper)

---

## Quick Start & Important notes

### Navigate to the project root
```sh
cd secure-notes/secure-notes
Build the project: ./mvnw clean install
Run the application: ./mvnw spring-boot:run

### Environment Configuration (IMPORTANT)

This project uses environment variables for security.
Create a .env file in the project root OR export the variables manually.

.env Example
APP_SECURITY_TOKEN=Your_Value
APP_ENCRYPTION_SECRET=Your_Value(32 hexadecimal characters, Characters allowed: 0–9 and a–f)

What these do
Variable Description
APP_SECURITY_TOKEN	Used for verifying API requests (Authorization: Bearer <token>)
APP_ENCRYPTION_SECRET	32-character AES-256 key for encrypting/decrypting note content


Testing

Run all tests: ./mvnw test

The test suite covers:

- Successful note creation

- Unauthorized access handling

- Validation failures

- Health endpoint

- Fetching all notes

IntelliJ Users — Important Note

To avoid Lombok or test-runner issues:

Enable: Settings → Build Tools → Maven → Runner → ✔ Delegate IDE build/run actions to Maven

Also ensure:
Settings → Build/Execution/Deployment → Compiler → Annotation Processors → ✔ Enable



