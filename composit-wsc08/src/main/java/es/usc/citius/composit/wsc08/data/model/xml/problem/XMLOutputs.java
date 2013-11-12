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
public class XMLOutputs {
    @XmlElement(name="concept")
    private ArrayList<XMLConcept> outputs;

    public ArrayList<XMLConcept> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<XMLConcept> outputs) {
        this.outputs = outputs;
    }

}
