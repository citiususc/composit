package es.usc.citius.composit.core.matcher.logic;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchFunction;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class LogicMatcher implements MatchFunction<Concept, LogicMatchType> {
    private HierarchicalKnowledgeBase kb;

    public LogicMatcher(HierarchicalKnowledgeBase kb){
        this.kb = kb;
    }

    public LogicMatcher(HierarchicalKnowledgeBase kb, LogicMatchType... acceptedMatchTypes){
        this.kb = kb;
    }

    @Override
    public LogicMatchType match(Concept source, Concept target) {
        if (kb.equivalent(source, target)){
            return LogicMatchType.EXACT;
        } else if (kb.isSubclass(source, target)){
            return LogicMatchType.PLUGIN;
        } else if (kb.isSuperclass(source, target)){
            return LogicMatchType.SUBSUMES;
        }
        return LogicMatchType.FAIL;
    }
}
