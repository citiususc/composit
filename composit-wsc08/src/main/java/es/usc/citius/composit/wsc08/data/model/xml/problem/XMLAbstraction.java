/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlRootElement(name="abstraction")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLAbstraction {
    //@XmlElement(name="input")
    private XMLInputs input;
    //@XmlElement(name="output")
    private XMLOutputs output;

    public XMLInputs getInput() {
        return input;
    }

    public void setInput(XMLInputs input) {
        this.input = input;
    }

    public XMLOutputs getOutput() {
        return output;
    }

    public void setOutput(XMLOutputs output) {
        this.output = output;
    }
    
}
