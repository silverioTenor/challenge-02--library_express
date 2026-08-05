package org.libraryexpress.domain.customer.entity;

import org.libraryexpress.domain.core.util.RandomGenerator;
import org.libraryexpress.domain.customer.valueobject.Email;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer implements Comparable<Customer> {

    private final String id;

    private final String name;

    private Email email;

    private Customer(String id, String name, Email email) {
        this.id = id != null ? id : RandomGenerator.UUID();
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public void changeEmail(String newEmail) {
        this.email = new Email(newEmail);
    }

    @Override
    public String toString() {
        return "{\n" +
                " Id: " + id + ",\n" +
                " name: " + name + ",\n" +
                " email: " + email + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Customer otherCustomer) {
        return Objects.compare(this.name, otherCustomer.getName(), String::compareTo);
    }

    public static class Builder {

        private String id;

        private String name;

        private String email;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Customer build() {
            return new Customer(id, name, new Email(email));
        }
    }
}
