# SilverCare Web Application (Frontend)

Traditional Java Web Application with Servlets and JSPs for SilverCare elderly care services platform.

## Overview

- **Type**: Traditional Web Application (WAR)
- **Port**: 8081 (when deployed to Tomcat)
- **API Calls**: `http://localhost:8080/api/*`
- **Packaging**: WAR (deploy to Tomcat)

## Technology Stack

- Jakarta Servlets 6.0
- JSP 3.1 + JSTL 3.0
- **JAX-RS Client** (jakarta.ws.rs) for API calls
- Jersey Client (JAX-RS implementation)
- GSON for JSON parsing
- jBCrypt for password validation

## Project Structure

```
silvercare-web/
├── src/main/java/com/silvercare/
│   ├── servlet/                      (Servlets - Controllers)
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── HomeServlet.java
│   │   ├── ServiceServlet.java
│   │   ├── BookingServlet.java
│   │   ├── FeedbackServlet.java
│   │   ├── CartServlet.java
│   │   ├── AdminServlet.java
│   │   ├── AdminDashboardServlet.java
│   │   ├── UserServlet.java
│   │   └── ... (16 servlets total)
│   ├── web/                          (Filters, Helpers)
│   │   ├── AdminFilter.java
│   │   ├── HolidayDiscountHelper.java
│   │   └── WebConfig.java
│   ├── model/                        (DTOs)
│   │   ├── User.java
│   │   ├── Booking.java
│   │   ├── Service.java
│   │   └── ...
│   └── util/
│       └── ApiClient.java            (JAX-RS Client utility)
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml
│   ├── FrontEnd/                     (Customer JSPs, CSS, images)
│   │   ├── home.jsp
│   │   ├── login.jsp
│   │   ├── register.jsp
│   │   ├── services.jsp
│   │   ├── bookingForm.jsp
│   │   ├── cart.jsp
│   │   ├── styles.css
│   │   └── images/
│   ├── Admin/                        (Admin JSPs)
│   │   ├── dashboard.jsp
│   │   ├── manageServices.jsp
│   │   └── ...
│   └── backend/                      (Backend JSPs)
│       ├── addToCart.jsp
│       ├── saveBooking.jsp
│       └── ...
└── pom.xml
```

## How It Works

### Architecture Flow

```
User Browser
    ↓
JSP (View)
    ↓
Servlet (Controller)
    ↓
ApiClient (JAX-RS Client)
    ↓
HTTP Request to REST API (http://localhost:8080/api/*)
    ↓
REST API Response
    ↓
Servlet processes response
    ↓
Forward to JSP
    ↓
Rendered HTML to User
```

### Example: Login Flow

1. User visits `/login` → `LoginServlet` → `FrontEnd/login.jsp`
2. User submits form → `LoginServlet.doPost()`
3. Servlet uses JAX-RS Client to call API:
   ```java
   Client client = ClientBuilder.newClient();
   WebTarget target = client.target("http://localhost:8080/api/users/login");
   Response response = target.request(MediaType.APPLICATION_JSON)
       .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
   ```
4. API validates credentials and returns user data
5. Servlet stores user in session
6. Servlet redirects to dashboard

## Running the Application

### Prerequisites
- Java 21
- Maven 3.6+
- Apache Tomcat 10+ (Jakarta EE 10 compatible)
- **silvercare-api must be running on port 8080**

### Build WAR file

```bash
cd silvercare-web
mvn clean package
```

This creates `target/silvercare-web.war`

### Deploy to Tomcat

#### Option 1: Copy WAR to Tomcat
```bash
copy target\silvercare-web.war C:\path\to\tomcat\webapps\
```

Configure Tomcat to run on port 8081 (edit `server.xml`):
```xml
<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

Then start Tomcat:
```bash
C:\path\to\tomcat\bin\startup.bat
```

#### Option 2: Use Maven Tomcat Plugin

Add to `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8081</port>
        <path>/</path>
    </configuration>
</plugin>
```

Then run:
```bash
mvn tomcat7:run
```

The web application will be available at `http://localhost:8081`

## Configuration

### API Base URL

The web application calls the REST API at `http://localhost:8080/api`

To change this, edit `src/main/java/com/silvercare/util/ApiClient.java`:

```java
private static final String API_BASE_URL = "http://localhost:8080/api";
```

Or set system property:
```bash
-Dapi.base.url=http://your-api-server:8080/api
```

## Key Files

### Servlets (Controllers)

- **LoginServlet.java** - Handles login
- **RegisterServlet.java** - Handles registration
- **HomeServlet.java** - Homepage with service categories
- **ServiceServlet.java** - Service catalog and details
- **BookingServlet.java** - Booking creation and management
- **FeedbackServlet.java** - Feedback submission
- **CartServlet.java** - Shopping cart
- **AdminServlet.java** - Admin operations
- **UserServlet.java** - User profile management

### JSPs (Views)

**Customer Pages:**
- `FrontEnd/home.jsp` - Homepage
- `FrontEnd/login.jsp` - Login page
- `FrontEnd/register.jsp` - Registration page
- `FrontEnd/services.jsp` - Service catalog
- `FrontEnd/bookingForm.jsp` - Booking form
- `FrontEnd/cart.jsp` - Shopping cart
- `FrontEnd/checkout.jsp` - Checkout page
- `FrontEnd/bookings.jsp` - My bookings
- `FrontEnd/profile.jsp` - User profile

**Admin Pages:**
- `Admin/dashboard.jsp` - Admin dashboard
- `Admin/manageServices.jsp` - Service management
- `Admin/viewBookings.jsp` - View all bookings
- `Admin/viewFeedback.jsp` - View feedback

### Utilities

- **ApiClient.java** - JAX-RS Client for calling REST API
- **HolidayDiscountHelper.java** - Discount calculations
- **AdminFilter.java** - Admin authentication filter

## JAX-RS Client Usage

### Example: GET Request

```java
Client client = ClientBuilder.newClient();
try {
    WebTarget target = client.target("http://localhost:8080/api/services");
    Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
    Response response = invoker.get();
    
    if (response.getStatus() == 200) {
        Map<String, Object> data = response.readEntity(
            new GenericType<Map<String, Object>>() {}
        );
        // Process data
    }
} finally {
    client.close();
}
```

### Example: POST Request

```java
Client client = ClientBuilder.newClient();
try {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", email);
    credentials.put("password", password);
    
    WebTarget target = client.target("http://localhost:8080/api/users/login");
    Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
    Response response = invoker.post(
        Entity.entity(credentials, MediaType.APPLICATION_JSON)
    );
    
    if (response.getStatus() == 200) {
        User user = response.readEntity(User.class);
        // Process user
    }
} finally {
    client.close();
}
```

## Testing

### Prerequisites
1. **Start the API first**:
   ```bash
   cd silvercare-api
   mvn spring-boot:run
   ```

2. **Start the web app**:
   ```bash
   cd silvercare-web
   mvn clean package
   # Deploy to Tomcat
   ```

### Test the Application

1. **Access the application**:
   - Open browser: `http://localhost:8081`

2. **Test login**:
   - Email: `john.doe@email.com`
   - Password: `customer123`

3. **Test admin login**:
   - Username: `admin`
   - Password: `admin123`

4. **Verify API calls**:
   - Open browser DevTools (F12)
   - Go to Network tab
   - Perform actions (login, view services, etc.)
   - Verify calls to `http://localhost:8080/api/*`

## Important Notes

### No Direct Database Access
This application **does NOT** connect to the database directly. All data operations go through the REST API.

### No Business Logic
Business logic is in the REST API. Servlets only:
- Handle HTTP requests
- Call the REST API using JAX-RS Client
- Render JSPs
- Manage sessions

### Dependencies
- **Must have**: silvercare-api running on port 8080
- **Cannot run standalone**: This app needs the API to function

### Session Management
- Sessions are managed by the web app
- User data is fetched from API and stored in session
- Logout clears the session

## Troubleshooting

### "Connection refused" errors
- Make sure silvercare-api is running on port 8080
- Check `ApiClient.java` has correct API_BASE_URL
- Verify firewall settings

### "404 Not Found" on API calls
- Verify API endpoints in REST controllers
- Check CORS configuration in API
- Ensure API context path is `/api`

### Session issues
- Sessions are managed by the web app
- User data is fetched from API and stored in session
- Logout clears the session

### JSP not found
- Verify JSP path in servlet forward
- Check JSPs are in `src/main/webapp/`
- Ensure WAR is properly deployed

## Deployment

### For Production

1. **Update API URL**:
   Edit `ApiClient.java`:
   ```java
   private static final String API_BASE_URL = "https://your-api-domain.com/api";
   ```

2. **Build WAR**:
   ```bash
   mvn clean package
   ```

3. **Deploy to Tomcat**:
   ```bash
   copy target\silvercare-web.war to production Tomcat
   ```

4. **Configure Tomcat**:
   - Set port (80 or 443 with SSL)
   - Configure SSL certificate
   - Set up reverse proxy if needed

## Features

### Customer Features
- User registration and login
- Browse service catalog
- Add services to cart
- Book services with payment
- View booking history
- Submit feedback and ratings
- Manage profile

### Admin Features
- Admin dashboard with statistics
- Manage services (CRUD)
- View all bookings
- View all feedback
- Manage service categories

### Advanced Features
- Shopping cart functionality
- Stripe payment integration
- Tutorial system for new users
- Holiday discount calculations
- Profile picture upload
- Session management
- Admin authentication filter

## Architecture

This is a **traditional web application** using the MVC pattern:

- **Model**: DTOs (User, Booking, Service, etc.)
- **View**: JSPs (home.jsp, login.jsp, etc.)
- **Controller**: Servlets (LoginServlet, BookingServlet, etc.)

Data is fetched from the REST API using JAX-RS Client.

## Related Projects

- **silvercare-api** - Backend REST API that this application consumes

## Notes

- This is a **traditional web application** - no Spring Boot
- Uses Servlets and JSPs for MVC pattern
- All data comes from REST API calls
- Packaging is WAR (not JAR)
- Requires servlet container (Tomcat, Jetty, etc.)
- Follows Jakarta EE 10 specifications

## License

Educational project for JAD Assignment 2
