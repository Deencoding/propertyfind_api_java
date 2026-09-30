# Property Find API

A backend RESTful API for managing property listings, user authentication, and notifications.  
This API allows users to search for properties, register as home seekers or providers, and interact securely using JWT authentication.

---

## Features

- User registration and login for Home Seekers and Providers
- JWT-based authentication for secure endpoints
- CRUD operations for property listings
- Admin role for managing users and listings

---

## Technologies Used

- Java 21
- Spring Boot 3.5
- Spring Security with JWT
- PostgreSQL
- JPA / Hibernate
- JDBC
- Maven

---

## Getting Started

### Prerequisites

- Java 21 or higher
- PostgreSQL
- Maven

### Installation

1. Clone the repository:

```bash
git clone https://github.com/Deencoding/propertyfind_api_java.git
```

2. Configure the database in application.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/propertyfind
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```

3. Build and run the project:

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at: http://localhost:8080

## API Endpoints

### Authentication

- POST /auth/register – Register a new user
- POST /auth/login – Login and receive a JWT token

### Properties

- GET /properties – Retrieve all properties
- POST /properties – Create a new property (requires JWT)
- GET /properties/{id} – Retrieve a property by ID
- PUT /properties/{id} – Update a property (requires JWT)
- DELETE /properties/{id} – Delete a property (requires JWT)

### Users

- GET /users – Retrieve all users (Admin only)
- GET /users/{id} – Retrieve a user by ID

### Reviews and ratings

All review endpoints require JWT authentication.

- POST /api/reviews/property/{propertyId} – Add one review per user per property
- GET /api/reviews/property/{propertyId} – List reviews, newest first
- GET /api/reviews/property/{propertyId}/rating – Get `averageRating` (rounded to two decimals) and `reviewCount`; both are zero when there are no reviews
- PUT /api/reviews/{reviewId} – Replace the rating and optional comment (review author or admin)
- DELETE /api/reviews/{reviewId} – Delete a review (review author or admin)

Create and update accept a required integer `rating` from 1 to 5 and an optional `comment` of up to 1000 characters:

```json
{"rating": 5, "comment": "Great place"}
```

Invalid input returns 400, unauthorized edits return 403, missing properties or reviews return 404, and duplicate reviews return 409. Omitting the comment on update clears it. Rating summaries are calculated from current reviews, so edits and deletions are reflected immediately.

### Property availability

New listings start as `AVAILABLE`. The listing owner or an admin can change the status:

```http
PATCH /api/properties/10/status
Content-Type: application/json
Authorization: Bearer <token>

{"status": "RENTED"}
```

Supported statuses are `AVAILABLE`, `RENTED`, and `ARCHIVED`. Any status can be changed back to `AVAILABLE`. Responses include `status` and the legacy `available` boolean, which is true only for `AVAILABLE`. Legacy updates with `available: false` map to `RENTED`, while an already archived listing stays archived; `available: true` restores availability.

Search by status using `POST /api/properties/search` with `{"status":"AVAILABLE"}`. If both status and availability filters are supplied, both apply. Unfiltered listing endpoints continue to include all statuses; archiving preserves the listing, reviews, and booking history. Rented and archived properties cannot receive new bookings or have bookings approved or rescheduled. Existing bookings are retained and can still be cancelled or completed.

For an existing database, run `src/main/resources/db/migrations/001_property_status.sql` once before deploying this version. It maps existing `available=true` rows to `AVAILABLE` and false rows to `RENTED`. The migration is manual; it is not run automatically at startup. Fresh databases use the updated `schema.sql`.

### Viewing bookings

Booking endpoints require JWT authentication:

- `POST /api/bookings`: request a viewing with `propertyId`, future `scheduledDate`, and optional `message` (maximum 1000 characters).
- `PUT /api/bookings/{id}/approve` or `/reject`: property provider handles a pending request.
- `PUT /api/bookings/{id}/reschedule`: provider supplies a new future `scheduledDate`; the pending/approved status is preserved.
- `PUT /api/bookings/{id}/cancel`: seeker or provider cancels an active booking.
- `PUT /api/bookings/{id}/complete`: provider completes an approved viewing after its one-hour slot ends.
- `GET /api/bookings/{id}`: seeker or provider views the booking.
- `GET /api/bookings/seeker` and `/provider`: current user's history, newest first, including terminal bookings.

Viewings last one hour. Pending and approved requests reserve their slot; overlapping appointments for the property or either participant return 409. Adjacent slots are allowed. Cancelled, rejected, and completed bookings cannot be reopened. Providers cannot request viewings of their own properties. Dates use the application's local time zone, consistent with existing timestamp fields.

Booking mutations use a PostgreSQL transaction advisory lock to serialize conflict checks and writes across application instances. This intentionally favors correctness over write throughput.

### Database

Main entities:

- UserEntity – Stores user details, roles, and registration date
- PropertyEntity – Stores property information such as name, location, price, and description


### Future Improvements

- Implement property search filters
- Add image upload support
- Add pagination for property listings
- Add email notifications



