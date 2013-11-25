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

package es.usc.citius.composit.wsc08.data.model.xml.taxonomy;



import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB Class.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@XmlRootElement(name="taxonomy")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLTaxonomy {
    @XmlElement(name="concept")
    private XMLConcept concept;

    public XMLConcept getConcept() {
        return concept;
    }

    public void setConcept(XMLConcept concept) {
        this.concept = concept;
    }

    public XMLConcept searchConceptForInstance(String _instance){
        XMLConcept con = searchConceptForInstance(this.concept, _instance);
        if (con == null){
            throw new RuntimeException("searchConceptForInstance: Invalid concept for instance " + _instance);
        } else {
            return con;
        }
    }


    private XMLConcept searchConceptForInstance(XMLConcept _root, String _instance){
        if (isInstanceOfConcept(_root, _instance)){
            return _root;
        } else {
            XMLConcept found;
            if (_root != null && _root.getConcepts() != null){
                for(XMLConcept con : _root.getConcepts()){
                   found = searchConceptForInstance(con, _instance);
                   if (found != null) return found;
                }
            }
            return null;
        }

    }


    private boolean isInstanceOfConcept(XMLConcept _concept, String _instance){
        for(XMLInstance instance : _concept.getInstances()){
            if (instance.getName().equals(_instance)){
                return true;
            }
        }
        return false;
    }


}
