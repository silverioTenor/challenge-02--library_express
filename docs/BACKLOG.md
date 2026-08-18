# Library Express — Agile Backlog

Java study project, evolved incrementally through real sprints.

PO/Scrum Master: Claude · Dev: Silvério

> **Note on language:** this document, `README.md`, and every ADR under `docs/adr/` are written in English. Portuguese was previously used as the internal planning language for `BACKLOG.md`/`VISION.md`; that split has been retired in favor of a single language across the whole project. `VISION.md` itself has also been retired — see [`docs/adr/0001-keep-library-express-framework-free.md`](./adr/0001-keep-library-express-framework-free.md) for the full rationale.

> **Note on detail level:** completed epics are recorded here only as a summary (status, points, sprint). Full detail (Gherkin, tasks, implementation deviations) lives in Git history and in issue closing comments on GitHub Projects — it is not duplicated here, to avoid two diverging sources of truth.

## Epics

| ID | Epic | Status |
|---|---|---|
| E0 | Initial organization and cleanup | ✅ Done (Sprint 0–1) |
| E1 | Foundation — decouple I/O from Services, centralize interaction in the CLI | ✅ Done |
| E2 | MVP — Loan lifecycle | ✅ Done (Sprint 2) |
| E3 | Manual dependency inversion + repository standardization + TD01 | ✅ Done (Sprint 3) |
| E4 | Automated test foundation — JUnit 5 + Mockito | ✅ Done (Sprint 4), with a note — see TD06 |
| E5 | Containerization (Docker) | ⛔ Discontinued — scope absorbed by E6 |
| E6 | Real Persistence (JDBC/PostgreSQL) + Docker Containerization | 🔵 In progress (Sprint 5) |
| E7 | Real CI — automated tests running as a pipeline gate | ⏳ Backlog (receives TD06 — infrastructure tests via Testcontainers) |
| E8 | Customer Reputation + automatic loan-status Job | ⏳ Backlog |
| E9 | CD — delivery pipeline + minimal API (**Marco 2 — Go Live**, on AWS, with E6 already in place) | ⏳ Backlog |
| E10 | Notifications (loan created / completed / overdue) | ⏳ Backlog (post-Marco 2, exercises the full CI/CD cycle) |

E7 and E9 are not the same thing. E9 delivers the automated build + deploy — we call it "CD," not "CI/CD," because without tests running as a gate there is no verified integration, only automated delivery. E7 is when that becomes real CI: tests (E4) start running on every push, as a pipeline gate, before E9 exists. E7 is also where technical debt TD06 (infrastructure tests via Testcontainers) gets resolved, building on the JDBC layer already delivered in E6.

### Renumbering note (this session)

The epics from E8 onward were deliberately reordered and renumbered:

- The **Spring Boot migration epic**, originally numbered E9, is **removed** from the Library Express roadmap entirely — not deferred, descoped. Spring adoption moves to the next project (Internet Banking), built Spring-first from day one. Full rationale: [ADR 0001](./adr/0001-keep-library-express-framework-free.md).
- **Customer Reputation**, originally E10 and unordered ("no defined priority"), is now **E8**, moved ahead of Marco 2 — CI (E7) should be mature before a new feature epic ships, and Reputation no longer waits behind Go Live.
- **CD / Go Live**, originally E8, is renumbered to **E9** to make room for E8 above. Its scope is unchanged.
- **Notifications** is a **new epic (E10)**, scoped after this session's discussion — see below.

E8 (Reputation) originated from a discussion during Sprint 2, about the return flow: when a loan is overdue, the customer loses reputation score; after 3 late returns the customer is "flagged" (concept still to be refined); after 5, the customer is blocked for a defined period. This is not technical debt — it is new scope. No fines or money are involved (payments are out of scope for this project entirely, now that the long-term vision document that used to sequence them has been retired — see ADR 0001).

**Automatic overdue detection**, previously an open question ("no scheduler — probably a lazy calculation on return/validation, not a background job"), is now resolved: E8 introduces a scheduler-driven background Job. The business rule stays in the domain/application layer (Java), with concurrency safety handled through explicit locking rather than delegating the rule to the database — see [ADR 0002](./adr/0002-domain-owned-overdue-status-rule-with-explicit-locking.md). On a successful status transition, the Job calls a domain-level notification port; E8 ships a no-op adapter for that port, and E10 (Notifications) later plugs in the real one — see [ADR 0003](./adr/0003-notification-port-with-noop-adapter-ahead-of-notifications-epic.md). Structured logging (SLF4J + Logback, TD07) is absorbed into E8 as well, since the Job is the first component in the system that runs unattended — see [ADR 0004](./adr/0004-slf4j-logback-without-full-observability.md). Still open for E8's refinement: `Customer` will need new fields (score, late-return count, `blockedUntil`).

**E10 (Notifications)** is new scope: notify the customer by email when a loan is created, completed, or becomes overdue. Deliberately scheduled *after* Marco 2 (E9) — it is meant to simulate adding a feature to an already-deployed system through the full CI/CD pipeline, not to ship alongside Go Live. It implements the real adapter for the notification port introduced in E8, without touching the Job or the domain rule.

## Roadmap — Phases and Milestones

### 🌱 Phase 1 — Foundation
Goal: build a solid base, consolidating the domain and eliminating architectural problems before introducing new technology.
Scope: E0, E1, E2, E3.

**🚀 Marco 1 — MVP** ✅ Reached
The system meets the essential functional requirements of a library, via CLI. Closes with E2. Tag `v0.1.1`.

### 🏗 Phase 2 — Software Maturity
Goal: raise the system's quality and reliability, driven by the project's real needs — now also calibrated to generate portfolio value in international (US/Canada) hiring processes.

Theme sequence:
1. Automated test foundation — JUnit 5 + Mockito (E4) ✅ done
2. Real persistence (JDBC/PostgreSQL) + Docker containerization (E6) 🔵 current
3. Real CI — tests as a pipeline gate, including infrastructure tests via Testcontainers/TD06 (E7)
4. Customer Reputation + automatic status Job (E8)
5. **Marco 2 — Go Live** (E9, packaging CD + a minimal API, with Docker and JDBC already in place)
6. Notifications (E10), exercising the full CI/CD cycle against an already-deployed system

E5 + E6 merge (decision on record): Epic E5 (standalone Docker) was discontinued as its own block. Rationale: containerization only generates real business value once it's wired to real persistence — "containerize a CLI with an in-memory repository" is a weak portfolio narrative compared to "containerize an application with real PostgreSQL, HikariCP, and versioned migrations." E5 remains visible in the Epics table (not removed from the map), marked as discontinued, to preserve historical traceability. All containerization scope was absorbed by E6, which took on the name Real Persistence (JDBC/PostgreSQL) + Docker Containerization.

Database chosen: PostgreSQL (via pure JDBC, no ORM), aligned with AWS's RDS free tier.

**🚀 Marco 2 — Go Live**
First real deployment to production — on AWS (free tier: ECS/Fargate or Elastic Beanstalk with Docker; replaces the original Heroku plan, which carries little relevance in the target job market). Delivered together: the CD pipeline (E9), real persistence via JDBC + a Docker image (E6, merged scope), and a minimal API without a framework (`com.sun.net.httpserver.HttpServer`, no Spring — this project stays framework-free for its entire lifecycle, see ADR 0001). The deployment only "counts" once there is a real HTTP service receiving traffic, backed by real persisted data.

Why does Go Live come after tests/Docker/persistence/CI? The sequence tells a strong portfolio narrative: tested → containerized → persisted → automated → only then went to production — the way real teams operate.

Why raw tests and raw persistence before a framework? `@SpringBootTest`/Mockito and Spring Data JPA are abstractions over plain JUnit and plain JDBC. Doing the manual path first is deliberate: it forces understanding the mechanism underneath before a framework's convenience hides it. That abstraction is deliberately exercised in the *next* project (Internet Banking, Spring Boot from day one) rather than inside Library Express — see ADR 0001 for why the Spring migration was removed from this project's own roadmap instead of just being deferred.

### ⚙️ Phase 3 — Professional Software Engineering
Goal: deepen engineering practices on a system that has been in production since Marco 2 — security, observability, performance, scalability, documentation.
Scope: not yet formalized into epics (future backlog). Given the project's terminal roadmap now ends at E10 (Notifications) with only maintenance-level adjustments afterward, Phase 3 in its original broad sense will not be pursued inside Library Express — see ADR 0001.

## Principles

- The domain always comes first.
- New technology is introduced only when it solves a real problem.
- Every Sprint must produce a functional delivery.
- The architecture evolves alongside the system.
- Learning happens through practice.

**Working rule:** one epic at a time, refined in full detail (BDD + tasks) only once it enters execution. Future epics stay as titles only until their turn comes (just-in-time backlog grooming).

**Process rule (from E4 onward):** before generating any formal backlog artifact (epic breakdown, User Story, tasks) for a new implementation decision or architecture change, alignment with the Dev must be debated and closed in conversation first. Formal Markdown generation (points, Gherkin, tasks, commits) only happens after alignment — never before. This avoids rework from scope drift discovered after the fact.

## Technical Debt

| ID | Description | Points | Status |
|---|---|---|---|
| TD01 | `equals`/`hashCode` contract for Book, Customer, and Loan | 3 | ✅ Resolved (US-304, E3) |
| TD05 | Fat JAR packaging (`maven-shade-plugin`, manifest with `Main-Class`) — frozen since E3, originally scheduled to resolve only in the (old) Go Live epic. Decision revised: the need for Docker moves the production justification for a single executable artifact earlier — freezing until Go Live no longer made sense. | 3 | ✅ Resolved (US-503, E6) |
| TD06 | Infrastructure layer (in-memory repositories) with no automated test coverage since E4 closed. Intentional deferral: contract and concurrency tests (originally US-404) will be rewritten with Testcontainers against a real database (Postgres), after E6 (JDBC) delivers the definitive implementation — avoids duplicated effort on an in-memory implementation that will be replaced. Resolution allocated to E7. | — (to be estimated during E7 refinement) | 🟡 Accepted, waiting on E6 |
| TD07 | Structured logging (SLF4J + Logback). No component in the system has run unattended before the E8 Job; ad hoc console output is no longer sufficient. Full observability/tracing stays out of scope — see [ADR 0004](./adr/0004-slf4j-logback-without-full-observability.md). | — (to be estimated during E8 refinement) | 🟡 Accepted, absorbed into E8 |

---

## 🔵 Epic E6 — Real Persistence (JDBC/PostgreSQL) + Docker Containerization

**Sprint:** 5
**Total points:** 18 (2 + 8 + 3 + 5)
**Status:** 🔵 Refined, ready for execution

### Decisions on record for this merge

- E5 discontinued as a standalone block, scope fully absorbed by E6. The row stays in the Epics table with status *Discontinued — scope absorbed by E6*, to preserve historical traceability.
- TD05 unblocked and reallocated to this epic (US-503). Updated justification: the original freeze (until the old Go Live epic) assumed there would only be a production reason for single-artifact packaging at Go Live. The need to containerize via Docker moves that reason earlier.
- Docker Compose is in scope, both for the local development environment (standalone Postgres, US-501) and for orchestrating the full application at the final stage (US-504).
- Internal execution order: **C (JDBC persistence) → A (Fat JAR) → B (Docker multi-stage)** — the application works against a real database locally before any packaging/containerization effort.
- Confirmed network scope: the application keeps running as a batch/interactive CLI inside the container, with no HTTP server — the minimal API is reserved for E9 (Go Live), even though it would technically be possible to bring it forward here.
- PostgreSQL version: `postgres:17-alpine`, tracking the latest available stable release.

### Epic goal

Migrate in-memory storage to real PostgreSQL via pure JDBC, with a managed connection pool (HikariCP) and versioned schema migrations (Flyway), delivering a 100% reproducible environment via Docker — application and database.

### Business value

Without real persistence, the system doesn't survive a restart and can't go to production (Marco 2 depends on it). Without containerization, the environment isn't reproducible across machines or deployable to AWS. The combination PostgreSQL + pure JDBC + HikariCP + Flyway + Docker multi-stage is the most commonly assessed skill set in senior backend Java technical interviews in the international market.

### Definition of Done — Epic E6

- `docker-compose.dev.yml` starts a local Postgres, ready for the JDBC repositories (US-501)
- Flyway applies versioned migrations and HikariCP manages the connection pool at startup (US-502)
- `BookDbRepository`, `LoanDbRepository`, `CustomerDbRepository` implement the domain repository interfaces via pure SQL, replacing the in-memory implementations in the Composition Root (US-502)
- A single executable Fat JAR is generated via `maven-shade-plugin`, resolving TD05 (US-503)
- A multi-stage Dockerfile builds the Fat JAR and runs it on an `eclipse-temurin:21-jre-alpine` image (US-504)
- A production/integration `docker-compose.yml` starts the application (CLI batch) + Postgres together, with the application connecting to the database via environment variables (US-504)
- All 4 User Stories in Done status

---

### US-501 — Local development environment with Docker Compose (Postgres)

**Points:** 2
**Depends on:** —

**Story:** As a developer, I need a `docker-compose.dev.yml` that starts an isolated local PostgreSQL, so I can implement and manually test the JDBC repositories without depending on a local database installation.

**Scenarios (BDD):**

```gherkin
Scenario: Postgres starts via the development Docker Compose file
  Given the docker-compose.dev.yml file at the project root
  When "docker compose -f docker-compose.dev.yml up -d" is executed
  Then a Postgres 17-alpine container should start on the configured port
  And the database should accept connections with the credentials defined in the compose file

Scenario: Development Postgres data persists across restarts
  Given the development Postgres container running with data already written
  When the container is restarted (docker compose restart)
  Then previously written data should still be available
```

**Tasks:**
- Create `docker-compose.dev.yml` with a `postgres:17-alpine` service
- Configure a named volume for data persistence across restarts
- Configure environment variables via a local `.env` (`.env.example` versioned, `.env` in `.gitignore`)
- Document the "Local development" section in the README

**Commits:**
- `build(docker): US-501 create development docker-compose with postgres 17-alpine`
- `docs(readme): US-501 document local development environment setup`

---

### US-502 — JDBC Persistence with HikariCP and Flyway

**Points:** 8
**Depends on:** US-501

**Story:** As a developer, I need to replace the in-memory repositories with real PostgreSQL implementations, using pure JDBC, HikariCP for connection pooling, and Flyway to version the schema — plugging everything into the repository interfaces already defined in the domain, without changing their contracts.

**Scenarios (BDD):**

```gherkin
Scenario: Flyway applies migrations at application startup
  Given migration scripts in src/main/resources/db/migration (V1__..., V2__...)
  When the application starts
  Then Flyway should automatically apply pending migrations
  And the resulting schema should reflect the book, customer, and loan tables

Scenario: HikariCP manages the connection pool
  Given the HikariDataSource configured at application startup
  When multiple repository operations occur in sequence
  Then connections should be reused from the pool, with no exhaustion under normal load

Scenario: BookDbRepository correctly persists and retrieves a book
  Given a Postgres database with the schema applied
  When a Book is saved via BookDbRepository.create()
  Then getByIsbn() should return the same book with all attributes intact

Scenario: The real database enforces the UNIQUE constraint on email
  Given a Customer already persisted with a given email
  When a second Customer is created with the same email
  Then the database should reject the operation via the UNIQUE constraint
  And the exception should be translated into a domain exception, not a leaked SQLException

Scenario: LoanDbRepository.update() applies a status change by id
  Given a Loan already persisted in the database
  When update() is called with the same id and a new status
  Then the persisted record should reflect the new status
  And no other row in the table should be affected

Scenario: Loan.search() correctly filters via a combined SQL query
  Given multiple Loans persisted with distinct combinations of customerId, ISBN, and status
  When search() is called with a subset of those criteria
  Then only the Loans matching ALL informed criteria should be returned

Scenario: Composition Root swaps in-memory repositories for JDBC without changing usecases
  Given AppContext configured to use the JDBC implementations
  When any Book, Customer, or Loan usecase is executed
  Then the observable behavior should be identical to the in-memory implementations
```

**Tasks:**
- Add `org.postgresql:postgresql`, `com.zaxxer:HikariCP`, `org.flywaydb:flyway-core` (+ `flyway-database-postgresql`) dependencies to the `infrastructure` module
- Create Flyway migration scripts (`V1__create_book_table.sql`, `V2__create_customer_table.sql`, `V3__create_loan_table.sql`), including a UNIQUE constraint on `customer.email`
- Configure `HikariDataSource` at application startup (conservative pool size and timeout for the free tier)
- Implement `BookDbRepository`, `CustomerDbRepository`, `LoanDbRepository` in the `infrastructure` module
- Translate SQL exceptions (e.g., constraint violations) into the existing domain exceptions
- Update `AppContext` (Composition Root) to inject the JDBC implementations in place of the in-memory ones
- Keep the in-memory implementations (future use in tests), unused in production
- Update the README with connection environment variables

**Commits:**
- `build(pom): US-502 add postgresql, hikaricp and flyway to infrastructure module`
- `build(flyway): US-502 create initial migrations for book, customer and loan`
- `feat(datasource): US-502 configure hikaricp at application startup`
- `feat(book-repository): US-502 implement bookdbrepository via pure jdbc`
- `feat(customer-repository): US-502 implement customerdbrepository via pure jdbc`
- `feat(loan-repository): US-502 implement loandbrepository via pure jdbc`
- `fix(repositories): US-502 translate sqlexception into domain exceptions`
- `refactor(composition-root): US-502 swap in-memory repositories for jdbc in appcontext`
- `docs(readme): US-502 document database connection environment variables`

---

### US-503 — Fat JAR Packaging (unblocks TD05)

**Points:** 3
**Depends on:** US-502

**Story:** As a developer, I need a single executable artifact (Fat JAR) consolidating the `domain`, `application`, and `infrastructure` modules, so the application can run outside the IDE and be packaged into a Docker image.

**Scenarios (BDD):**

```gherkin
Scenario: The Fat JAR builds successfully via Maven
  Given maven-shade-plugin configured in the infrastructure module's pom.xml
  When "mvn clean package" is executed at the root
  Then a single executable jar should be generated under infrastructure/target/

Scenario: The Fat JAR runs the application standalone
  Given the fat jar produced by the build
  When "java -jar library-express.jar" is executed
  Then the application should start correctly, applying migrations and connecting to the database
  And no ClassNotFoundException or NoClassDefFoundError should occur

Scenario: The manifest points to the correct main class
  Given the generated fat jar
  When the MANIFEST.MF manifest is inspected
  Then the Main-Class attribute should point to the Application class
```

**Tasks:**
- Configure `maven-shade-plugin` in `infrastructure/pom.xml`, consolidating classes from the sibling modules and external dependencies
- Configure `Main-Class` in the manifest
- Resolve resource merge conflicts (e.g., `META-INF/services`) via `ServicesResourceTransformer`, if they occur
- Update the TD05 record: status "Unblocked" → "Resolved — US-503 (E6)"
- Document the build and run command via `java -jar` in the README

**Commits:**
- `build(shade): US-503 configure maven-shade-plugin in the infrastructure module`
- `build(manifest): US-503 point main-class to the application class`
- `docs(td05): US-503 resolve TD05, recording the updated decision in the backlog`
- `docs(readme): US-503 document build and execution via fat jar`

---

### US-504 — Application Containerization via Docker Multi-stage + Compose

**Points:** 5
**Depends on:** US-503

**Story:** As a developer, I need a lightweight, multi-stage Docker image, and a `docker-compose.yml` that starts the application (CLI batch) and PostgreSQL together, to deliver the 100% reproducible environment Marco 2 requires.

**Scenarios (BDD):**

```gherkin
Scenario: The Docker image builds the application in a separate stage
  Given the multi-stage Dockerfile at the project root
  When "docker build" is executed
  Then the build stage should compile the submodules and produce the fat jar
  And the runtime stage should contain only the JRE and the jar, no build tooling

Scenario: The final image is lightweight and contains no Maven or full JDK
  Given the final image produced by the multi-stage build
  When the image's size and contents are inspected
  Then the image should be based on eclipse-temurin:21-jre-alpine
  And it should contain neither Maven nor the full JDK from the build stage

Scenario: The application connects to Postgres via environment variables in the container
  Given the application running in a container with connection environment variables configured
  When the container starts
  Then the application should connect to the Postgres instance the variables point to, with no hardcoded values

Scenario: Docker Compose starts the application and database together
  Given the docker-compose.yml at the project root, with the app and postgres services
  When "docker compose up" is executed
  Then both containers should start
  And the application should wait for the database to be healthy before connecting (healthcheck/depends_on)
  And the application should run correctly as a CLI batch process once startup completes
```

**Tasks:**
- Create a multi-stage Dockerfile: a build stage (Maven + Java 21) and a runtime stage (`eclipse-temurin:21-jre-alpine`)
- Parameterize the database connection via environment variables
- Create `docker-compose.yml` (production/integration, distinct from US-501's `docker-compose.dev.yml`) with `app` and `postgres:17-alpine` services
- Configure a healthcheck on the Postgres service and a health-conditioned `depends_on` on the app service
- Validate final image size and the absence of build tooling in the runtime image
- Document a "Running with Docker" section in the README

**Commits:**
- `build(docker): US-504 create multi-stage dockerfile for build and runtime`
- `build(docker): US-504 create application docker-compose with app and postgres 17-alpine`
- `feat(config): US-504 parameterize database connection via environment variables`
- `docs(readme): US-504 document build and execution via docker compose`

---

## History of Completed Epics

**E0 — Initial organization and cleanup**
✅ Done · Iteration 1 (Jul 07–Jul 20)

**E1 — Foundation**
✅ Done
Goal: prepare the application's base to support new interfaces without changing business rules — Services decoupled from I/O, interaction centralized in the CLI.

**E2 — MVP: Loan lifecycle**
✅ Done · Sprint 2 · 17 points (US-201 to US-207)
Delivered the full flow via CLI: customer/book registration, loan, and return, respecting business rules (availability, active loan limits). Marco 1 (MVP) reached. Tag `v0.1.1`.
Full detail (Gherkin, tasks, implementation deviations): see Issues #13–#18 on GitHub Projects.

**E3 — Manual dependency inversion + repository standardization**
✅ Done · Sprint 3 · 19 points

| US | Description | Points | Status |
|---|---|---|---|
| US-301 | Inject `InMemoryBookRepository` via constructor into Book usecases | 3 | ✅ Done |
| US-302 | Inject `InMemoryLoanRepository` via constructor into Loan usecases | 3 | ✅ Done |
| US-303 | Composition Root for manual usecase wiring | 5 | ✅ Done |
| US-304 | Fix the `equals`/`hashCode` contract for Book, Customer, and Loan (TD01) | 3 | ✅ Done |
| US-305 | Standardize repository naming (remove `I` prefix), convert enum→class, reorganize folders for JDBC | 5 | ✅ Done |

Full detail: see the corresponding Issues on GitHub Projects.

**E4 — Automated Test Foundation (JUnit 5 + Mockito)**
✅ Done · Sprint 4 · 15 points (2 + 5 + 8) — revised down from 20 points after scope reallocation

| US | Description | Points | Status |
|---|---|---|---|
| US-401 | Test environment setup: JUnit 5, Mockito, coverage-report (JaCoCo aggregate) | 2 | ✅ Done |
| US-402 | Domain layer tests: Value Objects and entities, zero mock/infra dependency | 5 | ✅ Done |
| US-403 | Application layer tests via Mockito: Book/Loan usecases and validators, fully replacing the manual Fakes | 8 | ✅ Done |

Scope change on record: manual Fakes (`FakeBookRepository`, `FakeLoanRepository`, `FakeCustomerRepository`) were replaced by native Mockito (`@Mock` + `@InjectMocks`). Technical justification recorded as an ADR (US-401 task).

US-404 (Infrastructure layer tests, 5 original points) was removed from E4 and reallocated to E7, for execution with Testcontainers + a real database, aligned with JDBC's arrival in E6 — see TD06.

Full detail (Gherkin, tasks, commits): see the corresponding Issues on GitHub Projects.

## Commit Convention

Follows Conventional Commits, single-line commits (no body/footer — terminal-driven workflow), with the US ID right after the colon:

```
<type>(<scope>): <ID> <description in the imperative, lowercase, no trailing period>
```

No Pull Request flow yet (see E9 in the roadmap), commits go straight to `develop` — no auto-close on Issues. When a US is completed, close its Issue manually on the board.

Multiple commits on the same US: all repeat the same ID (`US-XXX`) at the start of the description.

## Versioning Convention

Follows SemVer (`MAJOR.MINOR.PATCH`):
- `0.y.z` while the project is in early development — internal contracts (architecture, persistence, framework) may still change without notice. `1.0.0` is reserved for when the system stabilizes (around Phase 3).
- `alpha`/`beta`/`rc` suffixes only make sense from Marco 2 onward (once the REST API exists).
- `SNAPSHOT` in `pom.xml` during ongoing development; tags/releases use the clean version.

**Tags:**

| Tag | Milestone | Date |
|---|---|---|
| `v0.1.1` | Marco 1 — MVP (Epic E2 done) | see Git history |

## Board Conventions

- **Points:** simplified Fibonacci scale (1, 2, 3, 5, 8)
- **Status:** 🔲 To Do · 🟡 In Progress · 🔵 In Review · ✅ Done
- **Story numbering:** `US-{sprint}{sequential}` (e.g., `US-401` → Sprint 4, item 1)
- **Technical debt numbering:** `TD-{sequential}`, not tied to a fixed sprint until prioritized
- **BDD scenarios:** Gherkin format (Given/When/Then), used as the formal acceptance criteria for each story

---

**Last update:** Epics E8, E9, and E10 renumbered and rescoped (Customer Reputation + status Job moved ahead of Marco 2; Go Live shifted to E9; Notifications added as a new E10; the Spring Boot migration epic removed from this project entirely — see ADR 0001). `VISION.md` retired. All project documentation unified to English. Epic E6 (E5+E6 merge — Real Persistence JDBC/PostgreSQL + Docker Containerization) refined and ready for execution: 18 pts (2+8+3+5), Sprint 5, US-501 to US-504.