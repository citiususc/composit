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
@XmlRootElement(name="wanted")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLWanted {
    @XmlElement(name="instance")
    private ArrayList<XMLInstance> outputs;

    public ArrayList<XMLInstance> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<XMLInstance> outputs) {
        this.outputs = outputs;
    }
    
}
