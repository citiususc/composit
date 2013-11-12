/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlRootElement(name="realizations")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLRealizations {
    @XmlElement(name="service")
    private ArrayList<XMLService> services;

    public ArrayList<XMLService> getServices() {
        return services;
    }

    public void setServices(ArrayList<XMLService> services) {
        this.services = services;
    }


}
