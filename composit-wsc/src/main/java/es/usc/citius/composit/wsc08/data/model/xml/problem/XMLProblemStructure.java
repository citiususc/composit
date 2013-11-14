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
@XmlRootElement(name="problemStructure")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLProblemStructure {
    @XmlElement(name="task")
    private XMLTask task;
    @XmlElement(name="solutions")
    private XMLSolutions solutions;

    public XMLSolutions getSolutions() {
        return solutions;
    }

    public void setSolutions(XMLSolutions solutions) {
        this.solutions = solutions;
    }

    public XMLTask getTask() {
        return task;
    }

    public void setTask(XMLTask task) {
        this.task = task;
    }

}
