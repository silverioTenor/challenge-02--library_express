# Library Express

A backend application for library management built in Java.

This project is part of my Java Journey, a long-term learning path focused on mastering Java and software engineering by evolving a real-world application through incremental development.

## 🎯 Project Goals
- Build a library management system.
- Apply object-oriented programming principles.
- Practice software architecture through incremental refactoring.
- Evolve the application from a simple MVP to a production-ready system.
- Learn technologies only when they solve real problems in the project.

## Features
- Book management
- Client management
- Loan management
- Business rule validation
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
            │   └── repository
            │
            └── infrastructure
                ├── config
                ├── controller
                ├── exception
                ├── repository
                └── service
```
The project currently uses plain Java, without dependency injection or application frameworks. As the project evolves, new interfaces may be introduced without coupling them to the business logic.

## 🚀 Tech Stack
- Java 21
- Maven

## Getting Started

```bash
git clone https://github.com/silverioTenor/challenge-02--library_express.git

cd challenge-02--library_express

mvn clean install

mvn exec:java
```

## 📚 Learning Purpose

Rather than building everything at once, this project evolves through iterative sprints.

This project follows an incremental evolution approach. Instead of introducing frameworks and architectural patterns from the beginning, each sprint solves a real problem found in the application. New technologies are adopted only when they provide clear value to the project's evolution.

## 📄 License

MIT
