/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;


import es.usc.citius.composit.wsc08.data.model.xml.taxonomy.XMLConcept;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLInputs {
    @XmlElement(name="concept")
    private ArrayList<XMLConcept> inputs;

    public ArrayList<XMLConcept> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<XMLConcept> inputs) {
        this.inputs = inputs;
    }

}
