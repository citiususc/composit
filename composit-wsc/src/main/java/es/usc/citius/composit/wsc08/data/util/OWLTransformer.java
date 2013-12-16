/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC) http://citius.usc.es.
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

package es.usc.citius.composit.wsc08.data.util;


import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.Instance;
import es.usc.citius.composit.wsc08.data.knowledge.WSCXMLKnowledgeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class OWLTransformer {
    private static final String eol = System.getProperty("line.separator");
    private static final Logger log = LoggerFactory.getLogger(OWLTransformer.class);

    public static String convertKbToOwl(WSCXMLKnowledgeBase kb, String baseUri) {
        StringBuffer output = new StringBuffer();
        output.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + eol +
                        "<rdf:RDF" + eol +
                        "\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + eol +
                        "\txmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + eol +
                        "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" + eol +
                        "\txmlns:owl=\"http://www.w3.org/2002/07/owl#\"" + eol +
                        "\txml:base=\"" + baseUri + "\">" + eol +
                        "\t<owl:Ontology rdf:about=\"\" />" + eol
        );

        StringBuffer strData = new StringBuffer();
        int totalConcepts = kb.getConcepts().size();
        int counter = 0;
        for (Concept concept : kb.getConcepts()) {
            Set<Concept> superclasses = kb.getSuperclasses(concept);
            if (superclasses != null && !superclasses.isEmpty()) {
                strData.append("<owl:Class rdf:ID=\"" + concept + "\">" + eol);
                for (Concept superclass : superclasses) {
                    strData.append("\t<rdfs:subClassOf rdf:resource=\"#" + superclass.getID() + "\" />" + eol);
                }
                strData.append("</owl:Class>" + eol);
            } else {
                strData.append("<owl:Class rdf:ID=\"" + concept.getID() + "\" />" + eol);
            }
            counter++;
            log.debug("Concept {} processed ({}/{})", concept, counter, totalConcepts);
        }

        counter = 0;
        for (Concept concept : kb.getConcepts()) {
            // Write instances
            if (concept.getInstances() != null && !concept.getInstances().isEmpty()) {
                for (Instance instance : concept.getInstances()) {
                    strData.append(
                            "<owl:Thing rdf:ID=\"" + instance.getID() + "\">" + eol +
                                    "\t<rdf:type rdf:resource=\"#" + concept.getID() + "\" />" + eol +
                                    "</owl:Thing>" + eol
                    );
                }
            }
            counter++;
        }
        return output.append(strData).append("</rdf:RDF>").toString();
    }
}

