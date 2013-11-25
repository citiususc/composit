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

package es.usc.citius.composit.wsc08.data.knowledge;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.knowledge.Instance;
import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;
import es.usc.citius.composit.wsc08.data.model.xml.taxonomy.XMLConcept;
import es.usc.citius.composit.wsc08.data.model.xml.taxonomy.XMLTaxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCXMLKnowledgeBase implements HierarchicalKnowledgeBase, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(WSCXMLKnowledgeBase.class);

    // Do not serialize this! only used for initialization.
    private transient XMLTaxonomy taxonomy;
    // Concept -> subclasses
    private SetMultimap<Concept, Concept> subclasses = HashMultimap.create();
    // Concept -> superclasses
    private SetMultimap<Concept, Concept> superclasses = HashMultimap.create();
    // Instance -> concept map
    private Map<Instance, Concept> instanceConcept = new HashMap<Instance, Concept>();
    // Concept ID -> Concept
    private Map<String, Concept> conceptID = new HashMap<String, Concept>();
    // Instance ID -> Instance
    private Map<String, Instance> instanceID = new HashMap<String, Instance>();

    public WSCXMLKnowledgeBase(InputStream xmlTaxonomyStream) {
        this.taxonomy = JAXB.unmarshal(xmlTaxonomyStream, XMLTaxonomy.class);
        initialize();
    }

    public WSCXMLKnowledgeBase(File xmlTaxonomyFile) {
        this.taxonomy = JAXB.unmarshal(xmlTaxonomyFile, XMLTaxonomy.class);
        initialize();
    }

    public WSCXMLKnowledgeBase(){}

    private void initialize() {
        // Load all superclasses, subclasses and instances
        logger.debug("Initializing WSCXMLKnowledgeBase (root concept {})", taxonomy.getConcept().getID());
        Stopwatch stopwatch = Stopwatch.createStarted();
        populate(taxonomy.getConcept());
        logger.debug("Knowledge base created in {}", stopwatch.stop().toString());

    }

    private final Set<Concept> populate(XMLConcept root) {

        if (root == null) {
            return new HashSet<Concept>();
        }
        logger.trace("Processing concept {}", root.getName());
        conceptID.put(root.getID(), root);
        Set<Concept> rootSubclasses = this.subclasses.get(root);
        // Fill instances
        if (root.getInstances() != null) {
            for (XMLInstance instance : root.getInstances()) {
                instanceConcept.put(instance, root);
                instanceID.put(instance.getID(), instance);
            }
        }

        // Add each subclass to the map
        if (root.getConcepts() != null) {
            for (XMLConcept subclass : root.getConcepts()) {
                // Add this
                rootSubclasses.add(subclass);
                // Update superclasses iteratively. Note that WSC does not support
                // multiple inheritance, so each concept has only one single
                // parent.

                // Add root and all their parents as superclasses
                superclasses.get(subclass).add(root);
                // All superclasses
                Set<Concept> indirectSuperclasses = superclasses.get(root);
                if (indirectSuperclasses != null) {
                    superclasses.get(subclass).addAll(indirectSuperclasses);
                }
                // Add indirect subclasses, repeat recursively
                rootSubclasses.addAll(populate(subclass));
            }
        }
        return rootSubclasses;
    }


    @Override
    public Concept getConcept(String id) {
        return conceptID.get(id);
    }

    @Override
    public Instance getInstance(String id) {
        return instanceID.get(id);
    }

    @Override
    public Set<Concept> getSubclasses(Concept concept) {
        return ImmutableSet.copyOf(this.subclasses.get(concept));
    }

    @Override
    public Set<Concept> getSuperclasses(Concept concept) {
        return ImmutableSet.copyOf(this.superclasses.get(concept));
    }

    public boolean equivalent(Concept x, Concept y) {
        return x.equals(y);
    }

    public boolean isSubclass(Concept x, Concept y) {
        return this.subclasses.get(y).contains(x);
    }

    public boolean isSuperclass(Concept x, Concept y) {
        return this.superclasses.get(y).contains(x);
    }

    @Override
    public Concept resolveInstance(Instance instance) {
        return instanceConcept.get(instance);
    }


    public Set<Instance> getInstances(Concept concept) {
        return ImmutableSet.copyOf(concept.getInstances());
    }

    public Set<Concept> getConcepts() {
        return ImmutableSet.copyOf(conceptID.values());
        //return ImmutableSet.copyOf(Sets.union(this.subclasses.keySet(), this.superclasses.keySet()));
    }

    public Set<Instance> getInstances() {
        return ImmutableSet.copyOf(instanceID.values());
    }
}
