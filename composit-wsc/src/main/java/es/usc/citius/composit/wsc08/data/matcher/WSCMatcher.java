package es.usc.citius.composit.wsc08.data.matcher;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchFunction;

/**
 * Simple boolean semantic mather for the Web Service Challenge
 * concepts. Concept A matches Concept B if A is equivalent or subclass of B
 * (plugin and exact match).
 *
 */
public class WSCMatcher implements MatchFunction<Concept, Boolean> {
    private HierarchicalKnowledgeBase kb;

    public WSCMatcher(HierarchicalKnowledgeBase kb) {
        this.kb = kb;
    }

    @Override
    public Boolean match(Concept source, Concept target) {
        return (kb.equivalent(source, target) || kb.isSubclass(source, target));
    }
}
