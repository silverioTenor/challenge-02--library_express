# Library Express

A backend application for library management built in Java.

> **Note on language:** this README is in English (public-facing documentation). Planning documents (`BACKLOG.md`, `VISION.md`) are in Portuguese — the working language used to think through and discuss the project's evolution. This split is intentional, not an inconsistency.


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

```
src
└── main
    └── java
        └── org.libraryexpress
            ├── domain
            │   ├── entity
            │   ├── enums
            │   ├── helper
            │   ├── repository       (interfaces)
            │   └── validator        (interfaces)
            │
            ├── application
            │   ├── book
            │   │   ├── dto
            │   │   ├── mapper
            │   │   ├── usecase
            │   │   └── validator
            │   ├── customer
            │   │   ├── dto
            │   │   ├── mapper
            │   │   └── usecase
            │   └── loan
            │       ├── dto
            │       ├── usecase
            │       └── validator
            │
            └── infrastructure
                ├── cli               (current entrypoint/UI)
                ├── config
                ├── exception
                └── repository        (in-memory implementations)
```

The project follows a Clean Architecture-inspired layering: `domain` holds entities and contracts, `application` holds use cases (business logic, framework-agnostic), and `infrastructure` holds concrete implementations (currently CLI + in-memory repositories).

It currently uses plain Java, without dependency injection or application frameworks. This is deliberate: new technologies (a web framework, a real database, etc.) are introduced only when they solve a real problem the project has reached — not upfront. As the project evolves, new interfaces may be introduced without coupling them to the business logic.

## 🚀 Tech Stack
- Java 21
- Maven
- MapStruct (DTO ↔ entity mapping)

## Getting Started

```bash
git clone https://github.com/silverioTenor/library_express--api.git

cd library_express--api

mvn clean install

mvn exec:java
```

## 📚 Learning Purpose & Agile Process

Rather than building everything at once, this project evolves through iterative sprints, planned and tracked as a real agile backlog — epics, sprints, user stories (with BDD-style acceptance criteria), and tasks.

Instead of introducing frameworks and architectural patterns from the beginning, each sprint solves a real problem found in the application. New technologies are adopted only when they provide clear value to the project's evolution.

- **[BACKLOG.md](./docs/BACKLOG.md)** — active engineering roadmap: current epic, sprint backlog, BDD acceptance criteria, technical debt, commit conventions.
- **[VISION.md](./docs/VISION.md)** — long-term product vision (not yet in execution): future expansions such as a self-service platform, marketplace, payments, and eventual microservices/event-driven evolution.

## 📄 License

MIT