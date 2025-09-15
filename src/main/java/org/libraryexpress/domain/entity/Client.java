package org.libraryexpress.domain.entity;

import org.libraryexpress.helper.Generator;

public class Client {

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

    public static class Builder {

        private String name;

        private String email;

        Builder name(String name) {
            return this;
        }

        Builder email(String email) {
            return this;
        }

        Client build() {
            return new Client(name, email);
        }
    }
}
