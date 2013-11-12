/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;

import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlRootElement(name="provided")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLProvided {
    @XmlElement(name="instance")
    private ArrayList<XMLInstance> inputs;

    public ArrayList<XMLInstance> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<XMLInstance> inputs) {
        this.inputs = inputs;
    }

    
}
