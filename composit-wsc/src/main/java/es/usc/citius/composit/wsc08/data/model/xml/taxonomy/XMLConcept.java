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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.taxonomy;


import com.google.common.collect.ImmutableSet;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.Set;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLConcept implements Concept {
    @XmlAttribute(name="name")
    private String name;
    @XmlElement(name="instance")
    private ArrayList<XMLInstance> instances;
    @XmlElement(name="concept")
    private ArrayList<XMLConcept> concepts;

    public ArrayList<XMLConcept> getConcepts() {
        return concepts;
    }

    public void setConcepts(ArrayList<XMLConcept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public Set<XMLInstance> getInstances() {
        return ImmutableSet.copyOf(instances);
    }

    @Override
    public Set<XMLConcept> getDirectSubclasses() {
        return ImmutableSet.copyOf(concepts);
    }

    public void setInstances(ArrayList<XMLInstance> instances) {
        this.instances = instances;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return name;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XMLConcept that = (XMLConcept) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
