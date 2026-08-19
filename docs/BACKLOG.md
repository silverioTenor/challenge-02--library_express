# 📚 Backlog

## Library Express — Agile Backlog

Java study project, evolved incrementally through real sprints.
PO/Scrum Master: Claude · Dev: Silvério

**Note on language:** this document, `README.md`, and every ADR under `docs/adr/` are written in English. Portuguese was previously used as the internal planning language for `BACKLOG.md`/`VISION.md`; that split has been retired in favor of a single language across the whole project. `VISION.md` itself has also been retired — see `docs/adr/0001-keep-library-express-framework-free.md` for the full rationale.

**Note on detail level:** completed epics are recorded here only as a summary (status, points, sprint). Full detail (Gherkin, tasks, implementation deviations) lives in Git history and in issue closing comments on GitHub Projects — it is not duplicated here, to avoid two diverging sources of truth.

---

## Epics

| ID | Epic | Status |
|---|---|---|
| E0 | Initial organization and cleanup | ✅ Done (Sprint 01) |
| E1 | Foundation — decouple I/O from Services, centralize interaction in the CLI | ✅ Done |
| E2 | MVP — Loan lifecycle | ✅ Done (Sprint 2) |
| E3 | Manual dependency inversion + repository standardization + TD01 | ✅ Done (Sprint 3) |
| E4 | Automated test foundation — JUnit 5 + Mockito | ✅ Done (Sprint 4), with a note — see TD06 |
| E5 | Containerization (Docker) | ⛔ Discontinued — scope absorbed by E6 |
| E6 | Real Persistence (JDBC/PostgreSQL) + Docker Containerization | ✅ Done (Sprint 5) |
| E7 | Real CI — automated tests running as a pipeline gate | 🔵 Refined, ready for execution (Sprint 6) — resolves TD06 |
| E8 | Customer Reputation + automatic loan-status Job | ⏳ Backlog |
| E9 | CD — delivery pipeline + minimal API (Marco 2 — Go Live, on AWS, with E6 already in place) | ⏳ Backlog |
| E10 | Notifications (loan created / completed / overdue) | ⏳ Backlog (post-Marco 2, exercises the full CI/CD cycle) |

E7 and E9 are not the same thing. E9 delivers the automated build + deploy — we call it "CD," not "CI/CD," because without tests running as a gate there is no verified integration, only automated delivery. E7 is when that becomes real CI: tests (E4) start running on every push, as a pipeline gate, before E9 exists. E7 is also where technical debt TD06 (infrastructure tests via Testcontainers) gets resolved, building on the JDBC layer already delivered in E6.

### Renumbering note (historical)

The epics from E8 onward were deliberately reordered and renumbered:

- The Spring Boot migration epic, originally numbered E9, is removed from the Library Express roadmap entirely — not deferred, descoped. Spring adoption moves to the next project (Internet Banking), built Spring-first from day one. Full rationale: ADR 0001.
- Customer Reputation, originally E10 and unordered ("no defined priority"), is now E8, moved ahead of Marco 2 — CI (E7) should be mature before a new feature epic ships, and Reputation no longer waits behind Go Live.
- CD / Go Live, originally E8, is renumbered to E9 to make room for E8 above. Its scope is unchanged.
- Notifications is a new epic (E10), scoped separately — see below.

E8 (Reputation) originated from a discussion about the return flow: when a loan is overdue, the customer loses reputation score; after 3 late returns the customer is "flagged" (concept still to be refined); after 5, the customer is blocked for a defined period. This is not technical debt — it is new scope. No fines or money are involved (payments are out of scope for this project entirely, now that the long-term vision document that used to sequence them has been retired — see ADR 0001).

Automatic overdue detection is resolved as: E8 introduces a scheduler-driven background Job. The business rule stays in the domain/application layer (Java), with concurrency safety handled through explicit locking rather than delegating the rule to the database — see ADR 0002. On a successful status transition, the Job calls a domain-level notification port; E8 ships a no-op adapter for that port, and E10 (Notifications) later plugs in the real one — see ADR 0003. Structured logging (SLF4J + Logback, TD07) is absorbed into E8 as well, since the Job is the first component in the system that runs unattended — see ADR 0004.

E10 (Notifications) is new scope: notify the customer by email when a loan is created, completed, or becomes overdue. Deliberately scheduled after Marco 2 (E9) — it is meant to simulate adding a feature to an already-deployed system through the full CI/CD pipeline, not to ship alongside Go Live. It implements the real adapter for the notification port introduced in E8, without touching the Job or the domain rule.

---

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
2. Real persistence (JDBC/PostgreSQL) + Docker containerization (E6) ✅ done
3. Real CI — tests as a pipeline gate, including infrastructure tests via Testcontainers/TD06 (E7) 🔵 current
4. Customer Reputation + automatic status Job (E8)
5. Marco 2 — Go Live (E9, packaging CD + a minimal API, with Docker and JDBC already in place)
6. Notifications (E10), exercising the full CI/CD cycle against an already-deployed system

**E5 + E6 merge (decision on record):** Epic E5 (standalone Docker) was discontinued as its own block. Rationale: containerization only generates real business value once it's wired to real persistence — "containerize a CLI with an in-memory repository" is a weak portfolio narrative compared to "containerize an application with real PostgreSQL, HikariCP, and versioned migrations." E5 remains visible in the Epics table (not removed from the map), marked as discontinued, to preserve historical traceability. All containerization scope was absorbed by E6, which took on the name Real Persistence (JDBC/PostgreSQL) + Docker Containerization.

Database chosen: PostgreSQL (via pure JDBC, no ORM), aligned with AWS's RDS free tier.

**🚀 Marco 2 — Go Live**
First real deployment to production — on AWS (free tier: ECS/Fargate or Elastic Beanstalk with Docker; replaces the original Heroku plan, which carries little relevance in the target job market). Delivered together: the CD pipeline (E9), real persistence via JDBC + a Docker image (E6, merged scope), and a minimal API without a framework (`com.sun.net.httpserver.HttpServer`, no Spring — this project stays framework-free for its entire lifecycle, see ADR 0001). The deployment only "counts" once there is a real HTTP service receiving traffic, backed by real persisted data.

Why does Go Live come after tests/Docker/persistence/CI? The sequence tells a strong portfolio narrative: tested → containerized → persisted → automated → only then went to production — the way real teams operate.

Why raw tests and raw persistence before a framework? `@SpringBootTest`/Mockito and Spring Data JPA are abstractions over plain JUnit and plain JDBC. Doing the manual path first is deliberate: it forces understanding the mechanism underneath before a framework's convenience hides it. That abstraction is deliberately exercised in the next project (Internet Banking, Spring Boot from day one) rather than inside Library Express — see ADR 0001 for why the Spring migration was removed from this project's own roadmap instead of just being deferred.

### ⚙️ Phase 3 — Professional Software Engineering

Goal: deepen engineering practices on a system that has been in production since Marco 2 — security, observability, performance, scalability, documentation.
Scope: not yet formalized into epics (future backlog). Given the project's terminal roadmap now ends at E10 (Notifications) with only maintenance-level adjustments afterward, Phase 3 in its original broad sense will not be pursued inside Library Express — see ADR 0001.

---

## Principles

- The domain always comes first.
- New technology is introduced only when it solves a real problem.
- Every Sprint must produce a functional delivery.
- The architecture evolves alongside the system.
- Learning happens through practice.

**Working rule:** one epic at a time, refined in full detail (BDD + tasks) only once it enters execution. Future epics stay as titles only until their turn comes (just-in-time backlog grooming).

**Process rule (from E4 onward):** before generating any formal backlog artifact (epic breakdown, User Story, tasks) for a new implementation decision or architecture change, alignment with the Dev must be debated and closed in conversation first. Formal Markdown generation (points, Gherkin, tasks, commits) only happens after alignment — never before. This avoids rework from scope drift discovered after the fact.

---

## Technical Debt

| ID | Description | Points | Status |
|---|---|---|---|
| TD01 | `equals`/`hashCode` contract for Book, Customer, and Loan | 3 | ✅ Resolved (US-304, E3) |
| TD05 | Fat JAR packaging (`maven-shade-plugin`, manifest with `Main-Class`) — frozen since E3, originally scheduled to resolve only in the (old) Go Live epic. Decision revised: the need for Docker moves the production justification for a single executable artifact earlier — freezing until Go Live no longer made sense. | 3 | ✅ Resolved (US-503, E6) |
| TD06 | Infrastructure layer (in-memory repositories) with no automated test coverage since E4 closed. Intentional deferral: contract and concurrency tests (originally US-404) rewritten with Testcontainers against a real database (Postgres), after E6 (JDBC) delivered the definitive implementation — avoids duplicated effort on an in-memory implementation that would be replaced. | 8 | 🔵 Refined — allocated to US-701 (E7) |
| TD07 | Structured logging (SLF4J + Logback). No component in the system has run unattended before the E8 Job; ad hoc console output is no longer sufficient. Full observability/tracing stays out of scope — see ADR 0004. | — (to be estimated during E8 refinement) | 🟡 Accepted, absorbed into E8 |

---

## 🔵 Epic E7 — Real CI (Continuous Integration Gate)

**Sprint:** 6
**Total points:** 18 (8 + 5 + 3 + 2)
**Status:** 🔵 Refined, ready for execution

### Decisions on record for this epic

- TD06 (infrastructure tests, formerly US-404) is resolved in this epic via US-701, using Testcontainers against a real PostgreSQL container — validating JDBC repositories and Flyway migrations together, not mocks. This was intentionally deferred until E6 delivered real persistence.
- Coverage quality gate is enforced **per module** (domain / application / infrastructure) rather than as a single global threshold, reflecting that each layer has a different testability profile — domain (pure business logic) is held to the strictest bar.
- CI build matrix kept simple: single JDK version (21). No multi-version matrix at this stage.
- Branch protection on `main` (US-704) is only enabled once the CI workflow (US-702) exists and is verified stable — protecting a non-existent or flaky check would block the team, not help it.

### Epic goal

Establish an automated Continuous Integration pipeline that blocks any merge into `main` on build failure, test failure, or insufficient coverage — turning quality into an enforced gate rather than manual discipline.

### Business value

A CI pipeline with an enforced coverage gate is table-stakes in international senior backend hiring processes — it signals engineering maturity beyond "tests exist" to "tests are enforced." This is also the epic that formally resolves TD06, closing the last piece of test-coverage debt carried since E4, before the system takes on new business scope (E8) and goes to production (E9).

### Definition of Done — Epic E7

- [ ] Testcontainers-based integration tests validate `BookRepository` and Flyway migrations against a real PostgreSQL container (US-701), resolving TD06
- [ ] GitHub Actions workflow builds, runs unit tests, and runs integration tests on every push/PR (US-702)
- [ ] JaCoCo `check` goal enforces per-module coverage thresholds and fails the build below them (US-703)
- [ ] `main` requires a passing CI check before merge, with direct pushes blocked (US-704)
- [ ] All 4 User Stories in Done status
- [ ] TD06 formally resolved

---

### US-701 — Infrastructure Tests with Testcontainers (resolves TD06)

**Points:** 8
**Depends on:** — (unblocked by E6's JDBC delivery)

**Story:** As a developer, I need integration tests running against a real PostgreSQL container, so the JDBC repositories and Flyway migrations delivered in E6 are validated against real database behavior instead of mocks.

**Scenarios (BDD):**

```gherkin
Feature: Infrastructure layer integration testing with real PostgreSQL

  Scenario: BookRepository persists and retrieves a book against a real database
    Given a PostgreSQL 17-alpine container is running via Testcontainers
    And Flyway migrations have been applied successfully to the container
    When a Book entity is persisted through BookDbRepository
    Then the retrieved Book must match the original entity by identity and attributes

  Scenario: Flyway migrations run cleanly on a fresh database
    Given an empty PostgreSQL 17-alpine container
    When the application starts against this container
    Then all Flyway migration scripts execute without error
    And the schema_version table reflects the latest migration as applied

  Scenario: BookRepository enforces unique constraint at database level
    Given a book already persisted with a given ISBN
    When a second book with the same ISBN is persisted
    Then a data integrity violation exception is thrown by the repository

  Scenario: HikariCP connection pool recovers from a dropped connection
    Given an active HikariCP connection pool against the Testcontainers instance
    When the underlying connection is forcibly closed
    Then a subsequent repository call successfully acquires a new connection
    And completes the operation without manual intervention
```

**Tasks:**

- Add Testcontainers dependencies (`testcontainers`, `testcontainers-postgresql`, `testcontainers-junit-jupiter`) to the `infrastructure` module
- Extend the `@IntegrationTest` annotation convention (from E4) to this package
- Implement `PostgresTestContainerConfig` — a reusable base class with a singleton container shared across test classes (avoids per-test container startup overhead)
- Write `BookDbRepositoryIntegrationTest` covering the four Gherkin scenarios above
- Validate Flyway migrations run within the Testcontainers setup itself (not mocked)
- Cover the unique-constraint violation scenario (ISBN), asserting the translated domain exception
- Cover HikariCP recovery from a dropped connection
- Update the TD06 record: status "Refined, allocated to US-701" → "Resolved — US-701 (E7)"
- Document the infrastructure testing strategy in the README (why Testcontainers instead of mocks at this layer)

**Commits:**

```
test(book-repository): US-701 add testcontainers postgres for integration tests
feat(test-config): US-701 create reusable postgres testcontainer base config
test(book-repository): US-701 cover persistence and retrieval via real jdbc
test(flyway): US-701 validate clean migration run on empty database
test(book-repository): US-701 cover unique isbn constraint violation
test(hikaricp): US-701 cover pool recovery after dropped connection
docs(td06): US-701 resolve td06, recording the updated decision in the backlog
docs(readme): US-701 document infrastructure testing strategy
```

---

### US-702 — CI Pipeline Base (GitHub Actions)

**Points:** 5
**Depends on:** US-701

**Story:** As a developer, I need every push and pull request to `main` to automatically build the project and run its full test suite, so integration issues are caught before merge instead of after.

**Scenarios (BDD):**

```gherkin
Feature: Continuous Integration pipeline

  Scenario: Pipeline triggers on pull request to main
    Given a pull request is opened targeting the main branch
    When the CI workflow is triggered
    Then the workflow executes build, unit tests, and integration tests in sequence

  Scenario: Pipeline fails fast on compilation error
    Given a pull request contains a compilation error
    When the CI workflow runs
    Then the build step fails
    And subsequent test steps are skipped

  Scenario: Pipeline reports test results as a PR check
    Given a pull request has completed the CI workflow
    When the workflow finishes
    Then a check status (success or failure) is visible directly on the pull request

  Scenario: Pipeline caches Maven dependencies between runs
    Given a previous successful pipeline run
    When a new pipeline run starts on the same branch
    Then Maven dependencies are restored from cache
    And overall pipeline duration is reduced compared to a cold run
```

**Tasks:**

- Create workflow `.github/workflows/ci.yml`
- Configure trigger on `pull_request` to `main` and `push` to feature branches
- Configure the build step (`mvn -B compile`)
- Configure the unit test step, isolated via the `@UnitTest` group (from E4)
- Configure the integration test step, isolated via the `@IntegrationTest` group, running Testcontainers on the runner (`mvn -B verify`)
- Configure Maven dependency caching (`actions/setup-java` built-in cache)
- Validate that a failure in any step halts the workflow (fail-fast)
- Validate the full pipeline by opening a draft PR

**Commits:**

```
ci(github-actions): US-702 create base continuous integration workflow
ci(github-actions): US-702 configure triggers for pull request and push
ci(github-actions): US-702 separate unit and integration test execution
ci(github-actions): US-702 add maven dependency caching
```

---

### US-703 — Per-Module JaCoCo Coverage Quality Gate

**Points:** 3
**Depends on:** US-702

**Story:** As a Product Owner, I need the build to fail automatically when a module's test coverage drops below its defined threshold, so coverage regressions are caught by the pipeline instead of by manual review.

**Confirmed thresholds:**

| Module | Line | Branch |
|---|---|---|
| domain | 95% | 90% |
| application | 90% | 85% |
| infrastructure | 75% | 65% |

**Scenarios (BDD):**

```gherkin
Feature: Per-module coverage quality gate

  Scenario: Build fails when domain module coverage drops below threshold
    Given the domain module has line coverage below 95% or branch coverage below 90%
    When the JaCoCo check goal runs during the build
    Then the build fails with a coverage violation report

  Scenario: Build fails when application module coverage drops below threshold
    Given the application module has line coverage below 90% or branch coverage below 85%
    When the JaCoCo check goal runs during the build
    Then the build fails with a coverage violation report

  Scenario: Build fails when infrastructure module coverage drops below threshold
    Given the infrastructure module has line coverage below 75% or branch coverage below 65%
    When the JaCoCo check goal runs during the build
    Then the build fails with a coverage violation report

  Scenario: Build succeeds when all modules meet their individual thresholds
    Given each module meets or exceeds its configured coverage threshold
    When the JaCoCo check goal runs during the build
    Then the build proceeds without a coverage-related failure

  Scenario: Aggregate report reflects a per-module breakdown
    Given all module test suites have executed
    When the report-aggregate module generates the consolidated report
    Then the report displays coverage figures separated by module
```

**Tasks:**

- Configure the `jacoco-maven-plugin` `check` goal individually in each module's `pom.xml` (domain, application, infrastructure)
- Define `limit` rules (COVEREDRATIO LINE/BRANCH) per module, using the thresholds table above
- Bind the `check` goal to the `verify` phase, so it runs in the same CI step as US-702
- Adjust `coverage-report`'s `report-aggregate` output to display coverage segmented by module, not only a consolidated total
- Validate locally that a deliberate coverage drop in each module fails the build
- Document the thresholds and their rationale in the README (why domain is held to a stricter bar than infrastructure)

**Commits:**

```
build(jacoco): US-703 configure check goal per module with individual thresholds
build(jacoco): US-703 define coverage limits for domain module
build(jacoco): US-703 define coverage limits for application module
build(jacoco): US-703 define coverage limits for infrastructure module
build(jacoco): US-703 adjust report-aggregate to display per-module coverage
docs(readme): US-703 document coverage thresholds and rationale
```

---

### US-704 — Branch Protection on main

**Points:** 2
**Depends on:** US-703

**Story:** As a Product Owner, I need `main` protected against direct pushes and unverified merges, so the CI gate built in this epic is actually enforced, not just informational.

**Scenarios (BDD):**

```gherkin
Feature: Branch protection on main

  Scenario: Direct push to main is blocked
    Given a developer attempts to push directly to the main branch
    When the push is executed
    Then the push is rejected by branch protection rules

  Scenario: Merge is blocked when CI check fails
    Given a pull request targeting main has a failing CI status check
    When a merge is attempted
    Then GitHub blocks the merge until the check passes

  Scenario: Merge is allowed when all required checks pass
    Given a pull request targeting main has all required CI checks passing
    When a merge is attempted
    Then the merge is permitted
```

**Tasks:**

- Configure branch protection rule for `main` in GitHub (Settings → Branches)
- Mark the US-702 workflow as a required status check
- Disable direct pushes (require a pull request before merging)
- Validate the negative case: attempt a merge with a failing CI check and confirm it is blocked
- Validate the positive case: merge with a passing CI check is permitted
- Document the rule in the README (contribution/workflow section)

**Commits:**

```
chore(github): US-704 configure branch protection rule on main
docs(readme): US-704 document branch protection rule and contribution workflow
```

---

## History of Completed Epics

### E0 — Initial organization and cleanup
✅ Done · Iteration 1 (Jul 07–Jul 20)

### E1 — Foundation
✅ Done
Goal: prepare the application's base to support new interfaces without changing business rules — Services decoupled from I/O, interaction centralized in the CLI.

### E2 — MVP: Loan lifecycle
✅ Done · Sprint 2 · 17 points (US-201 to US-207)
Delivered the full flow via CLI: customer/book registration, loan, and return, respecting business rules (availability, active loan limits). Marco 1 (MVP) reached. Tag `v0.1.1`.
Full detail (Gherkin, tasks, implementation deviations): see Issues #13–#18 on GitHub Projects.

### E3 — Manual dependency inversion + repository standardization
✅ Done · Sprint 3 · 19 points

| US | Description | Points | Status |
|---|---|---|---|
| US-301 | Inject `InMemoryBookRepository` via constructor into Book usecases | 3 | ✅ Done |
| US-302 | Inject `InMemoryLoanRepository` via constructor into Loan usecases | 3 | ✅ Done |
| US-303 | Composition Root for manual usecase wiring | 5 | ✅ Done |
| US-304 | Fix the `equals`/`hashCode` contract for Book, Customer, and Loan (TD01) | 3 | ✅ Done |
| US-305 | Standardize repository naming (remove `I` prefix), convert enum→class, reorganize folders for JDBC | 5 | ✅ Done |

Full detail: see the corresponding Issues on GitHub Projects.

### E4 — Automated Test Foundation (JUnit 5 + Mockito)
✅ Done · Sprint 4 · 15 points (2 + 5 + 8) — revised down from 20 points after scope reallocation

| US | Description | Points | Status |
|---|---|---|---|
| US-401 | Test environment setup: JUnit 5, Mockito, coverage-report (JaCoCo aggregate) | 2 | ✅ Done |
| US-402 | Domain layer tests: Value Objects and entities, zero mock/infra dependency | 5 | ✅ Done |
| US-403 | Application layer tests via Mockito: Book/Loan usecases and validators, fully replacing the manual Fakes | 8 | ✅ Done |

Scope change on record: manual Fakes (`FakeBookRepository`, `FakeLoanRepository`, `FakeCustomerRepository`) were replaced by native Mockito (`@Mock` + `@InjectMocks`). Technical justification recorded as an ADR (US-401 task).

US-404 (Infrastructure layer tests, 5 original points) was removed from E4 and reallocated to E7, for execution with Testcontainers + a real database, aligned with JDBC's arrival in E6 — see TD06.

Full detail (Gherkin, tasks, commits): see the corresponding Issues on GitHub Projects.

### E6 — Real Persistence (JDBC/PostgreSQL) + Docker Containerization
✅ Done · Sprint 5 · 18 points (2 + 8 + 3 + 5)

| US | Description | Points | Status |
|---|---|---|---|
| US-501 | Local development environment with Docker Compose (Postgres) | 2 | ✅ Done |
| US-502 | JDBC Persistence with HikariCP and Flyway | 8 | ✅ Done |
| US-503 | Fat JAR Packaging (resolved TD05) | 3 | ✅ Done |
| US-504 | Application Containerization via Docker Multi-stage + Compose | 5 | ✅ Done |

E5 (standalone Docker) was formally discontinued in favor of this merged scope. TD05 (Fat JAR packaging) resolved via US-503. Full detail (Gherkin, tasks, commits): see the corresponding Issues on GitHub Projects.

---

## Commit Convention

Follows Conventional Commits, single-line commits (no body/footer — terminal-driven workflow), with the US ID right after the colon:

```
<type>(<scope>): <ID> <description in the imperative, lowercase, no trailing period>
```

No Pull Request flow yet until E7's branch protection (US-704) lands; commits go straight to `develop` — no auto-close on Issues. When a US is completed, close its Issue manually on the board.

Multiple commits on the same US: all repeat the same ID (`US-XXX`) at the start of the description.

## Versioning Convention

Follows SemVer (`MAJOR.MINOR.PATCH`):

- `0.y.z` while the project is in early development — internal contracts (architecture, persistence, framework) may still change without notice.
- `1.0.0` is reserved for when the system stabilizes (around Phase 3).
- `alpha`/`beta`/`rc` suffixes only make sense from Marco 2 onward (once the REST API exists).
- `SNAPSHOT` in `pom.xml` during ongoing development; tags/releases use the clean version.

**Tags:**

| Tag | Milestone | Date |
|---|---|---|
| v0.1.1 | Marco 1 — MVP (Epic E2 done) | see Git history |

## Board Conventions

- **Points:** simplified Fibonacci scale (1, 2, 3, 5, 8)
- **Status:** 🔲 To Do · 🟡 In Progress · 🔵 In Review · ✅ Done
- **Story numbering:** `US-{sprint}{sequential}` (e.g., US-401 → Sprint 4, item 1)
- **Technical debt numbering:** `TD-{sequential}`, not tied to a fixed sprint until prioritized
- **BDD scenarios:** Gherkin format (Given/When/Then), used as the formal acceptance criteria for each story

---

**Last update:** Epic E6 (Real Persistence JDBC/PostgreSQL + Docker Containerization) closed — 18 points (2+8+3+5), Sprint 5, US-501 to US-504, all Done. Epic E7 (Real CI) refined and ready for execution — 18 points (8+5+3+2), Sprint 6, US-701 to US-704, resolving TD06. Coverage quality gate defined per module (domain 95%/90%, application 90%/85%, infrastructure 75%/65%).