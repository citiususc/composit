package es.usc.citius.composit.core.knowledge;

import es.usc.citius.composit.core.model.Resource;

import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface Concept extends Resource {
    Set<? extends Instance> getInstances();
    Set<? extends Concept> getDirectSubclasses();
}
