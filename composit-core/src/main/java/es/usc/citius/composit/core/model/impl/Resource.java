package es.usc.citius.composit.core.model.impl;

import java.io.Serializable;
import java.net.URI;

public class Resource implements Serializable{
    // ID cannot be null. The ID is used to identify unique instances of resource.
    private final String id;
    private final URI uri;

    public Resource(String id) {
        this.id = id;
        this.uri = null;
    }

    public Resource(String id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (!id.equals(resource.id)) return false;
        if (uri != null ? !uri.equals(resource.uri) : resource.uri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return id;
    }
}
