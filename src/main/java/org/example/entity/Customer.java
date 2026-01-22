package org.example.entity;

import org.example.utils.EntityWithId;

import java.util.Objects;

public class Customer extends EntityWithId {
    private String email;
    private String name;
    // Extra information needed

    public Customer(String email, String name) {
        super();
        this.email = email;
        this.name = name;
    }

    public Customer(String id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
