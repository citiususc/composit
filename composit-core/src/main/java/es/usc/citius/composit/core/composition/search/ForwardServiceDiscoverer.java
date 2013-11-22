package es.usc.citius.composit.core.composition.search;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;
import es.usc.citius.composit.core.model.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ForwardServiceDiscoverer<E> {
    private static final Logger log = LoggerFactory.getLogger(ForwardServiceDiscoverer.class);
    private DiscoveryIO<E> discovery;
    private SetMatchFunction<E, ?> matcher;

    public ForwardServiceDiscoverer(DiscoveryIO<E> discovery, SetMatchFunction<E, ?> matcher) {
        this.discovery = discovery;
        this.matcher = matcher;
    }


    public LeveledServices<E> search(Signature<E> signature){
        Set<E> availableInputs = new HashSet<E>(signature.getInputs());
        Set<E> newOutputs = new HashSet<E>(signature.getInputs());
        Set<E> unmatchedOutputs = new HashSet<E>(signature.getOutputs());
        Set<Operation<E>> usedServices = new HashSet<Operation<E>>();
        Map<Operation<E>, Set<E>> unmatchedInputMap = new HashMap<Operation<E>, Set<E>>();

        boolean checkExpectedOutputs = !signature.getOutputs().isEmpty();
        boolean stop;

        do {
            Stopwatch w = Stopwatch.createStarted();
            HashSet<Operation<E>> candidates = new HashSet<Operation<E>>();
            for(E newConcept : newOutputs){
                candidates.addAll(discovery.discoverOperationsForInput(newConcept));
            }
            log.debug("Services retrieved from index in " + w.stop().toString() + " (" + candidates.size() + " candidates)");
            w.reset().start();
            // Remove services that cannot be invoked with the available inputs
            for(Iterator<Operation<E>> it=candidates.iterator(); it.hasNext();){
                Operation<E> candidate = it.next();
                // Retrieve the unmatched inputs for this operation
                Set<E> unmatchedInputs = unmatchedInputMap.get(candidate);
                if (unmatchedInputs == null){
                    unmatchedInputs = candidate.getSignature().getInputs();
                }
                // Check if the new concepts match some unmatched inputs
                //Set<Resource> matched = matcher.matched(newOutputs, unmatchedInputs);
                Set<E> matched = matcher.partialMatch(newOutputs, unmatchedInputs).getTargetElements();
                // Update the unmatchedInputs
                unmatchedInputs = Sets.newHashSet(Sets.difference(unmatchedInputs, matched));
                unmatchedInputMap.put(candidate, unmatchedInputs);
                // If there are no unmatched inputs, the service is invokable!
                if (!unmatchedInputs.isEmpty()){
                    it.remove();
                } else {
                    // Invokable operation, check if it was used previously
                    boolean isNew = usedServices.add(candidate);
                    if (!isNew) it.remove();
                }
            }
            log.debug("Candidates selected in " + w.stop().toString() + " (" + candidates.size() + " candidates)");

            // Collect the new outputs of the new candidates
            Set<E> nextOutputs = Operations.outputs(candidates);

            // Check unmatched outputs
            Set<E> matchedOutputs = matcher.partialMatch(Sets.union(newOutputs, nextOutputs), unmatchedOutputs).getTargetElements();
            //Set<Resource> matchedOutputs = matcher.matched(newOutputs, unmatchedOutputs);
            // Update the unmatched outputs
            unmatchedOutputs = Sets.newHashSet(Sets.difference(unmatchedOutputs, matchedOutputs));

            // Update for the next iteration
            availableInputs.addAll(newOutputs);
            newOutputs = nextOutputs;
            //log.debug("Available Inputs {}, New Outputs {}", availableInputs.size(), newOutputs.size());
            // Stop condition. Stop if there are no more candidates and/or expected outputs are satisfied.
            stop = (checkExpectedOutputs) ? candidates.isEmpty() || unmatchedOutputs.isEmpty() : candidates.isEmpty();
        } while(!stop);
        return null;
    }
}
