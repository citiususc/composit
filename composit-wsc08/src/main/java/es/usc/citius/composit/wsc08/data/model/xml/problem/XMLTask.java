/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlRootElement(name="task")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLTask {
    @XmlElement(name="provided")
    private XMLProvided provided;
    @XmlElement(name="wanted")
    private XMLWanted wanted;

    public XMLProvided getProvided() {
        return provided;
    }

    public void setProvided(XMLProvided provided) {
        this.provided = provided;
    }

    public XMLWanted getWanted() {
        return wanted;
    }

    public void setWanted(XMLWanted wanted) {
        this.wanted = wanted;
    }
}
