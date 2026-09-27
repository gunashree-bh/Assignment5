# HDFC Life Policy Ledger

Spring Boot 3 / Java 17 REST API for HDFC Life customers, policies, claims and riders.

## Run

Requirements:
- Java 17
- Maven 3.8+

Run:
```bash
mvn spring-boot:run
```

Default profile is `dev`, using H2 in-memory database and Flyway.

Swagger:
`http://localhost:8080/swagger-ui/index.html`

## Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | /api/policies | List policies |
| GET | /api/policies/{policyNo} | Policy detail |
| GET | /api/policies?status=Active | Filter by status |
| GET | /api/policies?type=TERM | Filter by type |
| GET | /api/policies?customer=Anita%20Sharma | Filter by customer |
| GET | /api/policies/search?minPremium=20000 | Premium search |
| POST | /api/policies | Create policy |
| DELETE | /api/policies/{policyNo} | Delete policy |
| GET | /api/policies/{policyNo}/claims | Claims for policy |
| POST | /api/claims | Create claim |

## policy_riders ownership

`Policy` is the owning side of the many-to-many relationship. It declares
the `@JoinTable(name = "policy_riders")`; `Rider` uses `mappedBy = "riders"`.

## Why DTOs when open-in-view is false?

Controllers return DTOs so the response can be constructed while the service
transaction is active. With `open-in-view=false`, a lazy collection such as
`Policy.riders` may no longer be initialized when serialization happens after
the transaction has ended. Returning an entity graph can therefore cause a
`LazyInitializationException`, and it also exposes persistence details.
DTOs keep the API contract separate from the JPA model and make the response
shape explicit.
