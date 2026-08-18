-- ============================================================================
-- SQL Migration: V1__create_initial_schema.sql
-- Database Target: PostgreSQL 17+
-- Domain Scope: Customer, Book, and Loan Aggregates
-- ============================================================================

-- 1. Create the customer aggregate table
CREATE TABLE tb_customer (
     id VARCHAR(36) PRIMARY KEY,
     name VARCHAR(150) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Create the book aggregate table
CREATE TABLE tb_book (
     isbn VARCHAR(17) PRIMARY KEY,
     title VARCHAR(200) NOT NULL,
     author VARCHAR(150) NOT NULL,
     year_published INT NOT NULL,
     status VARCHAR(20) NOT NULL
);

-- 3. Create the loan aggregate table
CREATE TABLE tb_loan (
     id VARCHAR(36) PRIMARY KEY,
     isbn VARCHAR(17) NOT NULL,
     customer_id VARCHAR(36) NOT NULL,
     status VARCHAR(20) NOT NULL,
     start_date DATE NOT NULL,
     end_date DATE NOT NULL,
     CONSTRAINT fk_loan_book FOREIGN KEY (isbn) REFERENCES tb_book(isbn),
     CONSTRAINT fk_loan_customer FOREIGN KEY (customer_id) REFERENCES tb_customer(id)
);

-- 4. Create performance indexes for relational foreign key lookups
CREATE INDEX idx_loan_isbn ON tb_loan(isbn);
CREATE INDEX idx_loan_customer_id ON tb_loan(customer_id);
