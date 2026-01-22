package org.example.utils;

import java.util.Objects;
import java.util.UUID;

public abstract class EntityWithId {
    private final String id;

    public EntityWithId() {
        this.id = UUID.randomUUID().toString();
    }

    public EntityWithId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EntityWithId entity = (EntityWithId) obj;
        return Objects.equals(id, entity.id);
    }
}
