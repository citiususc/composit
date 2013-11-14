/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.composit.wsc08.data.model.xml.problem;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlSeeAlso({XMLParallel.class, XMLSequence.class, XMLServiceDesc.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class XMLServiceNode {
    @XmlElementRef
    private ArrayList<XMLServiceNode> nodes;

    public ArrayList<XMLServiceNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<XMLServiceNode> nodes) {
        this.nodes = nodes;
    }

    
}
