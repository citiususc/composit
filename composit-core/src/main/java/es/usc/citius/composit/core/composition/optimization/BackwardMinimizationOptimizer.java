package es.usc.citius.composit.core.composition.optimization;


import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.composition.network.HashServiceMatchNetwork;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BackwardMinimizationOptimizer<E, T extends Comparable<T>> implements NetworkOptimizer<E,T> {
    private static final Logger log = LoggerFactory.getLogger(BackwardMinimizationOptimizer.class);

    @Override
    public ServiceMatchNetwork<E, T> optimize(ServiceMatchNetwork<E, T> network) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Set<E> newInputs = new HashSet<E>();
        List<Set<Operation<E>>> optimized = new ArrayList<Set<Operation<E>>>(network.numberOfLevels());
        for(int i=network.numberOfLevels()-1;i>=0;i--){
            Set<Operation<E>> current = network.getOperationsAtLevel(i);
            Set<Operation<E>> optimizedSet = new HashSet<Operation<E>>();
            Set<E> futureInputs = new HashSet<E>();
            // Find all services that produces at least one of the required inputs. If new inputs is
            // empty, then select all
            for(Operation<E> op : current){
                if (newInputs.isEmpty()){
                    futureInputs.addAll(op.getSignature().getInputs());
                    optimizedSet.add(op);
                } else {
                    boolean used;
                    next:
                    for(E output : op.getSignature().getOutputs()){
                        for(E input : newInputs){
                            used = network.match(output, input) != null;
                            if (used){
                                optimizedSet.add(op);
                                // Update new inputs
                                futureInputs.addAll(op.getSignature().getInputs());
                                break next;
                            }
                        }
                    }
                }
            }
            newInputs.addAll(futureInputs);
            optimized.add(optimizedSet);
        }
        Collections.reverse(optimized);
        log.debug("Operation minimization finalized in {}", stopwatch.toString());
        // Build leveled services
        stopwatch.reset().start();
        LeveledServices<E> layers = new HashLeveledServices<E>(optimized);
        stopwatch.stop();
        log.debug("Layers builded in {}", stopwatch.toString());
        // Create a new match network
        stopwatch.reset().start();
        ServiceMatchNetwork<E, T> optimizedNetwork = new HashServiceMatchNetwork<E, T>(layers, network);
        stopwatch.stop();
        log.debug("Optimized match network created in {}", stopwatch.toString());
        // Create a new optimized service match network
        return optimizedNetwork;
    }
}
