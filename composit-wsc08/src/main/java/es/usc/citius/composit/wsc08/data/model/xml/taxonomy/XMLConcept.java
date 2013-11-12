/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.taxonomy;


import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLConcept {
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

    public ArrayList<XMLInstance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<XMLInstance> instances) {
        this.instances = instances;
    }

    public String getName() {
        return name;
    }

}
