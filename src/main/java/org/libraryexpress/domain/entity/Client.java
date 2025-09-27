package org.libraryexpress.domain.entity;

import org.libraryexpress.domain.helper.Generator;

import java.util.Objects;

public class Client implements Comparable<Client> {

    private final String ID = Generator.genUUID();

    private String name;

    private String email;

    private Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{\n" +
                " ID: " + ID + ",\n" +
                " name: " + name + ",\n" +
                " email: " + email + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client client)) return false;
        return Objects.equals(ID, client.ID) || Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public int compareTo(Client otherClient) {
        return Objects.compare(this.name, otherClient.getName(), String::compareTo);
    }

    public static class Builder {

        private String name;

        private String email;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Client build() {
            return new Client(name, email);
        }
    }
}
