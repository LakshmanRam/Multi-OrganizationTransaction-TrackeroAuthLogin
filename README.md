# Multi-OrganizationTransaction-TrackeroAuthLogin
Here’s a **complete documentation** for your Spring Boot “MultiTransactionTracker” project including setup, Google OAuth credentials, JWT configuration, and endpoint testing.

---

# **MultiTransactionTracker – Execution & Testing Guide**

## **1. Prerequisites**

* JDK 17+
* Maven or Gradle
* PostgreSQL (optional, H2 database used for dev)
* Internet connection for Google OAuth
* Postman or similar API testing tool

---

## **2. Clone the Project**

```bash
git clone https://github.com/<your-username>/MultiTransactionTracker.git
cd MultiTransactionTracker
```

---

## **3. Configure Google OAuth2 Credentials**

1. Go to [Google Cloud Console](https://console.cloud.google.com/).
2. Navigate to **APIs & Services → Credentials → Create Credentials → OAuth client ID**.
3. Choose **Web Application**.
4. Set **Authorized redirect URI**:

   ```
   http://localhost:8081/login/oauth2/code/google
   ```
5. Copy the **Client ID** and **Client Secret**.

---

### **4. Set Environment Variables**

For security, never commit your secrets to GitHub.

**Linux / Mac**

```bash
export GOOGLE_CLIENT_ID=<your-client-id>
export GOOGLE_CLIENT_SECRET=<your-client-secret>
export JWT_SECRET=<your-256-bit-secret>
```

**Windows (CMD)**

```cmd
set GOOGLE_CLIENT_ID=<your-client-id>
set GOOGLE_CLIENT_SECRET=<your-client-secret>
set JWT_SECRET=<your-256-bit-secret>
```

**Windows (PowerShell)**

```powershell
$env:GOOGLE_CLIENT_ID="<your-client-id>"
$env:GOOGLE_CLIENT_SECRET="<your-client-secret>"
$env:JWT_SECRET="<your-256-bit-secret>"
```

---

### **5. Configure application.properties**

```properties
spring.application.name=MultiTransactionTracker
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:test

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8081/login/oauth2/code/google

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-ms=86400000
server.port=8081
```

---

## **6. Run the Application**

```bash
mvn spring-boot:run
```

* The server will start on `http://localhost:8081`.

---

## **7. Test Authentication**

1. Open browser:

```
http://localhost:8081/oauth2/authorization/google
```

2. Google login form appears.
3. After login, the backend returns a JSON with JWT token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "example@gmail.com",
    "name": "John Doe"
  }
}
```

4. Copy the `token` for testing authenticated endpoints.

---

## **8. REST API Endpoints**

All requests require **Authorization header**:

```
Authorization: Bearer <JWT_TOKEN>
```

### **8.1 Organizations**

| Method | URL                       | Body                                               | Description                                   |
| ------ | ------------------------- | -------------------------------------------------- | --------------------------------------------- |
| POST   | `/api/organizations`      | `{ "name": "Org1", "description": "Description" }` | Create organization                           |
| GET    | `/api/organizations`      | –                                                  | List all organizations for the logged-in user |
| GET    | `/api/organizations/{id}` | –                                                  | Get organization by ID (owned by user)        |
| PUT    | `/api/organizations/{id}` | `{ "name": "Updated", "description": "Updated" }`  | Update organization                           |
| DELETE | `/api/organizations/{id}` | –                                                  | Delete organization                           |

---

### **8.2 Transactions**

| Method | URL                                       | Body                                                                                      | Description                        |
| ------ | ----------------------------------------- | ----------------------------------------------------------------------------------------- | ---------------------------------- |
| POST   | `/api/organizations/{orgId}/transactions` | `{ "type": "SALE", "amount": 100.5, "item": "Item1", "date": "2025-09-02T12:00:00Z" }`    | Create transaction                 |
| GET    | `/api/organizations/{orgId}/transactions` | –                                                                                         | List transactions for organization |
| PUT    | `/api/transactions/{txId}`                | `{ "type": "PURCHASE", "amount": 50.0, "item": "Item2", "date": "2025-09-02T12:00:00Z" }` | Update transaction                 |
| DELETE | `/api/transactions/{txId}`                | –                                                                                         | Delete transaction                 |

---

## **9. Testing with Postman**

1. Set **Authorization → Bearer Token** with JWT.
2. Send requests to the endpoints above with JSON bodies.
3. Validate responses.

---

## **10. Notes / Best Practices**

* Never commit `application.properties` with real secrets.
* JWT expiration is set in `app.jwt.expiration-ms` (default 24h).
* Users can only access organizations and transactions they own.
* H2 database is in-memory; data will reset on restart. For persistence, configure PostgreSQL.

---

If you want, I can also **create a ready Postman collection** with all endpoints and sample JSON for immediate testing.

Do you want me to do that?
