package es.usc.citius.composit.core.model.impl;

import java.io.Serializable;
import java.net.URI;

public class Resource implements Serializable{
    // Name cannot be null
    private final String id;
    private final URI uri = null;

    public Resource(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;

        if (!id.equals(resource.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public URI getUri() {
        return uri;
    }
}
