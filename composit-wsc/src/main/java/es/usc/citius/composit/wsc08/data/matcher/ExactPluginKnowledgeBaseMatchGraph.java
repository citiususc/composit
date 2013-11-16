package es.usc.citius.composit.wsc08.data.matcher;


import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExactPluginKnowledgeBaseMatchGraph implements MatchGraph<Concept, Boolean> {
    private HierarchicalKnowledgeBase kb;

    public ExactPluginKnowledgeBaseMatchGraph(HierarchicalKnowledgeBase kb) {
        this.kb = kb;
    }

    @Override
    public Set<Concept> getElements() {
        return kb.getConcepts();
    }

    @Override
    public Map<Concept, Boolean> getTargetElementsMatchedBy(Concept source) {
        Map<Concept,Boolean> result = new HashMap<Concept, Boolean>();
        for(Concept target : kb.getSuperclasses(source)){
            // Plugin match source matches target if source equals(exact)/subclass(plugin) target
            // (Target is equal/superclass of source)
            result.put(target, true);
        }
        // exact match, source->source
        result.put(source, true);
        return result;
    }

    @Override
    public Map<Concept, Boolean> getSourceElementsThatMatch(Concept target) {
        Map<Concept,Boolean> result = new HashMap<Concept, Boolean>();
        for(Concept source : kb.getSubclasses(target)){
            // Plugin match
            result.put(source, true);
        }
        // exact match, target->target
        result.put(target, true);
        return result;
    }

    @Override
    public Map<Concept, Boolean> getTargetElementsMatchedBy(Concept source, Boolean type, TypeSelector selector) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Concept, Boolean> getSourceElementsThatMatch(Concept target, Boolean type, TypeSelector selector) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MatchTable<Concept, Boolean> partialMatch(Set<Concept> source, Set<Concept> target) {
        return fullMatch(source, target);
    }

    @Override
    public MatchTable<Concept, Boolean> fullMatch(Set<Concept> source, Set<Concept> target) {
        // Compute all matches between source and target elements
        MatchTable<Concept, Boolean> matchTable = new MatchTable<Concept, Boolean>();
        for(Concept src : source){
            Set<Concept> validTargets = new HashSet<Concept>(kb.getSuperclasses(src));
            validTargets.add(src);
            validTargets.retainAll(target);
            for(Concept tg : validTargets){
                matchTable.addMatch(src, tg, true);
            }
        }
        return matchTable;
    }

    @Override
    public Boolean match(Concept source, Concept target) {
        return (kb.equivalent(source,target) || kb.isSubclass(source, target));
    }
}
