# Library Express
A backend application for library management built in Java.

> **Note on language:** every document in this project — this README, `docs/BACKLOG.md`, and everything under `docs/adr/` — is written in English. Portuguese was previously used as the working language for planning documents (`BACKLOG.md`, `VISION.md`); that split has been retired in favor of a single language across the whole project.

This project is part of my **Java Journey**, a long-term learning path focused on mastering Java and software engineering by evolving a real-world application through incremental development, guided by a real agile process (epics, sprints, user stories, tasks).

## 🎯 Project Goals
- Build a library management system.
- Apply object-oriented programming principles.
- Practice software architecture through incremental refactoring.
- Evolve the application from a simple MVP to a production-ready system.
- Learn technologies only when they solve real problems in the project.

## Features
- Book management
- Customer management
- Loan management (creation, search)
- Business rule validation (book availability, active loan limits)
- Custom exception handling

## 🏗️ Current Architecture

The project is organized as a **Maven multi-module** build. Each layer of the Clean Architecture is its own Maven module, with its own `pom.xml` and its own `src/main` / `src/test` — not just a package convention. This isolates test scope per layer (domain tests never see infrastructure dependencies, for example) and keeps compile-time boundaries between layers enforced by Maven itself, not just by discipline.

```
library_express--api
├── pom.xml                    (parent/aggregator — packaging: pom)
│
├── domain
│   ├── pom.xml
│   └── src
│       ├── main/java/org.libraryexpress.domain
│       │   ├── book
│       │   ├── customer
│       │   ├── loan
│       │   └── core            (shared entities, enums, helpers, repository/validator interfaces)
│       └── test/java/org.libraryexpress.domain   (JUnit 5 — entity/contract tests)
│
├── application
│   ├── pom.xml
│   └── src
│       ├── main/java/org.libraryexpress.application
│       │   ├── book             (dto, mapper, usecase, validator)
│       │   ├── customer         (dto, mapper, usecase)
│       │   └── loan             (dto, usecase, validator)
│       └── test/java/org.libraryexpress.application   (JUnit 5 + Mockito — usecase tests, mocked repositories)
│
├── infrastructure
│   ├── pom.xml
│   └── src
│       ├── main/java/org.libraryexpress.infrastructure
│       │   ├── cli               (current entrypoint/UI)
│       │   ├── config
│       │   ├── exception
│       │   └── repository        (in-memory implementations)
│       └── test/java/org.libraryexpress.infrastructure
│
└── coverage-report
    └── pom.xml                (packaging: pom — no src; aggregates JaCoCo reports from the 3 modules above)
```

Dependency direction between modules: `infrastructure → application → domain`. `domain` has no dependency on the other two — it's pure business logic and contracts. `coverage-report` depends on all three, but only to aggregate their test coverage data — it contains no production code.

It currently uses plain Java, without dependency injection or application frameworks. This is deliberate: new technologies (a real database, containerization, etc.) are introduced only when they solve a real problem the project has reached — not upfront. Unlike earlier drafts of the roadmap, this is not a temporary state pending a future Spring Boot migration: Library Express stays framework-free for its entire lifecycle by design. The framework step of the author's learning path happens in a separate, Spring-first project instead. Full rationale: [ADR 0001](./docs/adr/0001-keep-library-express-framework-free.md).

Architectural decisions (like the module split above, and the framework-free scope) are recorded as ADRs in [`docs/adr/`](./docs/adr/). A high-level C4 view of the modules lives in [`docs/architecture/`](./docs/architecture/).

## 🚀 Tech Stack
- Java 21
- Maven (multi-module)
- JUnit 5 (Jupiter)
- Mockito
- MapStruct (DTO ↔ entity mapping)
- JaCoCo (test coverage)

## Getting Started
```bash
git clone https://github.com/silverioTenor/library_express--api.git
cd library_express--api
mvn clean install
```

Run the CLI:
```bash
mvn exec:java
```
The entrypoint lives in the `infrastructure` module. `exec-maven-plugin` is skipped by default (`exec.skip=true` in the parent POM) and only re-enabled (`exec.skip=false`) in `infrastructure` — so even though the reactor visits all three modules, it only actually executes there. No `-pl` needed.

## ✅ Running Tests
```bash
mvn test
```
Runs tests across all modules. To run a single module's tests only:
```bash
mvn test -pl domain
mvn test -pl application
```

## 📊 Test Coverage
Each module (`domain`, `application`, `infrastructure`) generates its own JaCoCo report on `mvn test`, at `<module>/target/site/jacoco/index.html`.

For a single consolidated report across all modules:
```bash
mvn clean verify
```
The aggregated report is generated by the `coverage-report` module at `coverage-report/target/site/jacoco-aggregate/index.html`. It has no source code of its own — it exists solely to depend on the three modules and run JaCoCo's `report-aggregate` goal after they've all been tested.

## 📚 Learning Purpose & Agile Process
Rather than building everything at once, this project evolves through iterative sprints, planned and tracked as a real agile backlog — epics, sprints, user stories (with BDD-style acceptance criteria), and tasks.

Instead of introducing frameworks and architectural patterns from the beginning, each sprint solves a real problem found in the application. New technologies are adopted only when they provide clear value to the project's evolution.

- **[docs/BACKLOG.md](./docs/BACKLOG.md)** — active engineering roadmap: current epic, sprint backlog, BDD acceptance criteria, technical debt, commit conventions.
- **[docs/adr/](./docs/adr/)** — Architecture Decision Records, including the decision to keep this project framework-free for its full lifecycle and to retire the long-term vision document below.
- **[docs/architecture/](./docs/architecture/)** — C4 diagrams and structural documentation.

A long-term product vision document (`VISION.md`) used to live alongside the backlog, describing aspirational, non-executable future expansions (a self-service platform, a marketplace, payments, subscriptions, microservices). It has been retired — Library Express's scope is bounded to what's tracked in `BACKLOG.md`. See [`docs/VISION.md`](./docs/VISION.md) for the retirement note and [ADR 0001](./docs/adr/0001-keep-library-express-framework-free.md) for the full rationale.

## 📄 License
MIT