/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.composit.core.model.impl;

import es.usc.citius.composit.core.model.Resource;

import java.io.Serializable;
import java.net.URI;

/**
 * Serializable resource, identifiable by id.
 */
public class ResourceComponent implements Resource, Serializable{
    // ID cannot be null. The ID is used to identify unique instances of resource.
    private final String id;
    private final URI uri;

    public ResourceComponent(String id) {
        this.id = id;
        this.uri = null;
    }

    public ResourceComponent(String id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceComponent resource = (ResourceComponent) o;

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

    public String getID() {
        return id;
    }

    public URI getURI() {
        return uri;
    }

    @Override
    public String toString() {
        return id;
    }
}
