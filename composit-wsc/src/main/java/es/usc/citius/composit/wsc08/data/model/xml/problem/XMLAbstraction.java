/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
