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
@XmlRootElement(name="serviceDesc")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLServiceDesc extends XMLServiceNode {
    @XmlElement(name="abstraction")
    private XMLAbstraction abstraction;
    @XmlElement(name="realizations")
    private XMLRealizations realizations;

    public XMLAbstraction getAbstraction() {
        return abstraction;
    }

    public void setAbstraction(XMLAbstraction abstraction) {
        this.abstraction = abstraction;
    }

    public XMLRealizations getRealizations() {
        return realizations;
    }

    public void setRealizations(XMLRealizations realizations) {
        this.realizations = realizations;
    }

}
