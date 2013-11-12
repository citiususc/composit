package es.usc.citius.composit.core.knowledge;

import java.util.Set;


/**
 * Basic implementation of a hierarchical knowledge base. It provides basic methods
 * to test the relation between concepts. This interface is not intended to replace
 * a real knowledge base system. Use this interface to implement basic superclass/subclass
 * inference reasoning with non standard models (for example, WSC'08 taxonomy format).
 */
public interface HierarchicalKnowledgeBase {

    /**
     * Get the concept with the provided id.
     * @param id Concept ID.
     * @return Concept
     */
    Concept getConcept(String id);

    /**
     * Get the instance with the provided id.
     * @param id Instance ID.
     * @return Instance
     */
    Instance getInstance(String id);
	/**
     * Get a collection with the subclasses of the provided concept
     * @param concept Concept provided
     * @return collection of subclasses of the concept
     */
    Set<? extends Concept> getSubclasses(Concept concept);
    
    /**
     * Returns a collection containing the superclasses of the provided concept
     * @param concept Concept provided
     * @return collection of subclasses of the concept
     */
    Set<? extends Concept> getSuperclasses(Concept concept);
    
    /**
     * Check if concept x is equivalent to concept y
     * @param x Concept x
     * @param y Concept y
     * @return True if x is equivalent to y
     */
    boolean equivalent(Concept x, Concept y);
    
    /**
     * Check if concept x is subclass of concept y
     * @param x Concept x
     * @param y Concept y
     * @return True if x subclass of y, false in other case.
     */
    boolean isSubclass(Concept x, Concept y);
    
    /**
     * Check if concept x is a subclass of y, false in other case
     * @param x Concept x
     * @param y Concept y
     * @return True if x is a superclass of y
     */
    boolean isSuperclass(Concept x, Concept y);
    
    /**
     * Returns the concept Resource from which this Resource is instance. If
     * the Resource is not an instance, the function returns the same Resource.
     * @param instance Concept or Instance Resource
     * @return Concept parent of the instance.
     */
    Concept resolveInstance(Instance instance);
    
    /**
     * Return all instances of the provided concept.
     * @return {@link Set} with the instances of the concept.
     * If there are no instances, a empty set is returned.
     */
    Set<? extends Instance> getInstances(Concept concept);

    /**
     * Return all concepts managed by this KB.
     * @return {@link Set} with all available concepts.
     */
    Set<? extends Concept> getConcepts();

    /**
     * Return all instances managed by this KB.
     * @return {@link Set} with all available instances.
     */
    Set<? extends Instance> getInstances();
}
