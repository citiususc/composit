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

package es.usc.citius.composit.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import es.usc.citius.composit.cli.CompositCli;
import es.usc.citius.composit.core.composition.InputDiscoverer;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.composition.search.CompositionProblem;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.wsc08.data.WSCTest;

import java.util.*;

@Parameters(commandDescription = "Generate random queries for a dataset")
public class RandomQueryCommand implements CliCommand {

    @Parameter(names = {"-d", "--dataset"}, description = "Select a WSC'08 dataset.", required = true)
    private WSCTest test;

    @Parameter(names = {"-l", "--min-levels"}, description = "Minimum number of levels in the match graph")
    private int minLevels = 5;

    /*
    @Parameter(names = {"-s", "--min-ops"}, description = "Minimum number of operations. Set to 0 to any number")
    private int minOps = 0;
    */

    private CompositCli cli;

    @Override
    public void invoke(CompositCli contextCli) throws Exception {
        this.cli = contextCli;
        WSCTest.Dataset dataset = test.dataset();
        backwardSearch(dataset);
    }

    private void backwardSearch(WSCTest.Dataset dataset){
        // Start from the outputs
        Random r = new Random();
        int totalOutputs = r.nextInt(9)+1;
        Set<Concept> initialOutputs = new HashSet<Concept>();
        for(int i=0; i < totalOutputs; i++){
            initialOutputs.add(randomElement(dataset.getMatchGraph().getElements(), r));
        }
        //initialOutputs.clear();
        //initialOutputs.add(dataset.getKb().getConcept("con896546722"));
        Set<Concept> selectedOutputs = new HashSet<Concept>(initialOutputs);
        cli.println(totalOutputs + " wanted outputs.");
        // Start backwards
        InputDiscoverer<Concept> discoverer = dataset.getDefaultCompositionProblem().getInputDiscoverer();
        Set<Operation<Concept>> selected = new HashSet<Operation<Concept>>();
        List<Set<Operation<Concept>>> levels = new LinkedList<Set<Operation<Concept>>>();
        Set<Concept> unsolved = new HashSet<Concept>();

        do {
            Set<Operation<Concept>> levelOps = new HashSet<Operation<Concept>>();
            for(Concept output : selectedOutputs){
                Set<Operation<Concept>> ops = discoverer.findOperationsConsuming(output);
                ops.removeAll(selected);
                if (ops.isEmpty()){
                    unsolved.add(output);
                } else {
                    // Select one randomly
                    Operation<Concept> op = randomElement(ops, r);
                    ops = new HashSet<Operation<Concept>>(Arrays.asList(op));
                    selected.addAll(ops);
                    levelOps.addAll(ops);
                }
            }
            if (levelOps.isEmpty()){
                break;
            }
            levels.add(levelOps);
            cli.println(levelOps.size() + " added");
            // Get the new inputs
            selectedOutputs = Operations.inputs(levelOps);
            selectedOutputs.removeAll(unsolved);
        } while (true);
        cli.println(unsolved.size() + " unsolved inputs.");
        /*
        ForwardServiceDiscoverer<Concept, Boolean> fs = new ForwardServiceDiscoverer<Concept, Boolean>(dataset.getDefaultCompositionProblem().getInputDiscoverer(),
                dataset.getDefaultCompositionProblem().getMatchGraph());
        */

        // Use the last outputs as inputs
        Set<Concept> selectedInputs = selectedOutputs;
        cli.println("Forward search with " + selectedInputs.size() + " inputs and " + initialOutputs.size() + " outputs.");
        //fs.search(new SignatureIO<Concept>(unsolved, Collections.<Concept>emptySet()));
        forwardSearch(selectedInputs, initialOutputs, discoverer, dataset.getMatchGraph(), r);
    }

    private void forwardSearch(Set<Concept> availableInputs, Set<Concept> wantedOutputs, final InputDiscoverer<Concept> discoverer, final MatchGraph<Concept, Boolean> matcher, Random r){
        ForwardServiceDiscoverer<Concept, Boolean> fs = new ForwardServiceDiscoverer<Concept, Boolean>(discoverer,
                matcher);
        // Inputs and outputs of the random request
        Set<Concept> inputs = new HashSet<Concept>(availableInputs);
        // Generate the reachability set of operations
        fs.setRelaxedMatchCondition(true);
        ServiceMatchNetwork<Concept, Boolean> reachabilityGraph =
                fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
        fs.setRelaxedMatchCondition(false);
        Set<Operation<Concept>> reachableOps =
                Sets.newHashSet(reachabilityGraph.getOperations());

        int levels;
        cli.println("Starting...");
        ServiceMatchNetwork<Concept, Boolean> graph;

        // Extend the initial graph
        do {
            fs = new ForwardServiceDiscoverer<Concept, Boolean>(discoverer,
                    matcher);
            graph = fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
            levels = graph.numberOfLevels()-2;
            Map<Operation<Concept>, Set<Concept>> unmatchedInputMap = fs.getUnmatchedInputMap();

            int minSize = Integer.MAX_VALUE;
            Operation<Concept> op = null;
            // Find the operation with less unmatched inputs (different from 0)
            for(Map.Entry<Operation<Concept>, Set<Concept>> entry : unmatchedInputMap.entrySet()){
                int currentSize = entry.getValue().size();
                if (currentSize != 0 && currentSize < minSize){
                    minSize = currentSize;
                    op = entry.getKey();
                }
            }
            Set<Concept> newInputs = new HashSet<Concept>();
            if (op == null){
                // Select a random operation
                Set<Operation<Concept>> invokableOps =
                        Sets.newHashSet(graph.getOperations());
                reachableOps.removeAll(invokableOps);
                if (reachableOps.isEmpty()) break;
                op = randomElement(reachableOps, r);
                Set<Concept> generatedOutputs = Operations.outputs(invokableOps);
                Set<Concept> matched = matcher.partialMatch(generatedOutputs, op.getSignature().getInputs()).getTargetElements();
                newInputs.addAll(new HashSet<Concept>(Sets.difference(op.getSignature().getInputs(), matched)));
            } else {
                newInputs.addAll(unmatchedInputMap.get(op));
            }

            // Compute the minimum number of inputs that we need to add in order to make the
            // operation invokable:
            inputs.addAll(newInputs);
        } while (levels < minLevels);


        wantedOutputs = Operations.outputs(graph.getLeveledList().get(graph.numberOfLevels()-2));
        // Generate optimal composition
        cli.println("Total inputs: " + inputs.size());
        cli.println("Total outputs: " + wantedOutputs.size());
        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(new CompositionProblem<Concept, Boolean>() {
            @Override
            public MatchGraph<Concept, Boolean> getMatchGraph() {
                return matcher;
            }

            @Override
            public InputDiscoverer<Concept> getInputDiscoverer() {
                return discoverer;
            }
        });
        composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        composit.search(new SignatureIO<Concept>(inputs, wantedOutputs));
    }

    private void forwardSearch(WSCTest.Dataset dataset){
        Random r = new Random();
        MatchGraph<Concept, Boolean> matcher = dataset.getDefaultCompositionProblem().getMatchGraph();
        InputDiscoverer<Concept> discoverer = dataset.getDefaultCompositionProblem().getInputDiscoverer();
        ForwardServiceDiscoverer<Concept, Boolean> fs = new ForwardServiceDiscoverer<Concept, Boolean>(dataset.getDefaultCompositionProblem().getInputDiscoverer(),
                dataset.getDefaultCompositionProblem().getMatchGraph());
        // Selected operations
        Set<Operation<Concept>> selected = new HashSet<Operation<Concept>>();
        // Inputs and outputs of the random request
        Set<Concept> inputs = new HashSet<Concept>();
        Set<String> availableOps = dataset.getServiceProvider().listOperations();
        // Select a service that will be placed in the first layer:
        String randomService = randomElement(availableOps, r);
        cli.println("Random operation selected: " + randomService);
        Operation<Concept> operation = dataset.getServiceProvider().getOperation(randomService);
        selected.add(operation);
        // Make it invokable
        inputs.addAll(operation.getSignature().getInputs());
        // Generate the reachability set of operations
        fs.setRelaxedMatchCondition(true);
        ServiceMatchNetwork<Concept, Boolean> reachabilityGraph =
                fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
        fs.setRelaxedMatchCondition(false);
        Set<Operation<Concept>> reachableOps =
                Sets.newHashSet(reachabilityGraph.getOperations());

        int levels;
        cli.println("Starting...");
        ServiceMatchNetwork<Concept, Boolean> graph;

        do {
            // Generate the match network, assuming that the selected operation is invokable
            graph = fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
            levels = graph.numberOfLevels()-2;
            Map<Operation<Concept>, Set<Concept>> unmatchedInputMap = fs.getUnmatchedInputMap();

            int minSize = Integer.MAX_VALUE;
            Operation<Concept> op = null;
            // Find the operation with less unmatched inputs (different from 0)
            for(Map.Entry<Operation<Concept>, Set<Concept>> entry : unmatchedInputMap.entrySet()){
                int currentSize = entry.getValue().size();
                if (currentSize != 0 && currentSize < minSize){
                    minSize = currentSize;
                    op = entry.getKey();
                }
            }
            Set<Concept> newInputs = new HashSet<Concept>();
            if (op == null){
                // Select a random operation
                Set<Operation<Concept>> invokableOps =
                        Sets.newHashSet(graph.getOperations());
                reachableOps.removeAll(invokableOps);
                if (reachableOps.isEmpty()) break;
                op = randomElement(reachableOps, r);
                Set<Concept> generatedOutputs = Operations.outputs(invokableOps);
                Set<Concept> matched = matcher.partialMatch(generatedOutputs, op.getSignature().getInputs()).getTargetElements();
                newInputs.addAll(new HashSet<Concept>(Sets.difference(op.getSignature().getInputs(), matched)));
            } else {
                newInputs.addAll(unmatchedInputMap.get(op));
            }

            // Compute the minimum number of inputs that we need to add in order to make the
            // operation invokable:
            inputs.addAll(newInputs);
        } while (levels < minLevels);

        // Select N random outputs
        Set<Concept> availableOutputs = Operations.outputs(graph.getOperations());
        Set<Concept> outputs = new HashSet<Concept>();

        float p = 2f;
        int total = Math.round((p/100f) * availableOutputs.size()) + 1;
        cli.println("Selecting " + total + " inputs...");
        for(int i=0; i < total; i++){
            outputs.add(randomElement(availableOutputs, r));
        }

        // Generate optimal composition
        cli.println("Total inputs: " + inputs.size());
        cli.println("Total outputs: " + outputs.size());

        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());
        composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        composit.search(new SignatureIO<Concept>(inputs, outputs));

    }

    private static <E> Set<E> randomSetWithRepetition(Collection<E> elements, float percentage, Random r){
        int total = Math.round((percentage/100f) * elements.size()) + 1;
        Set<E> set = new HashSet<E>();
        for(int i=0; i < total; i++){
            set.add(randomElement(elements, r));
        }
        return set;
    }

    private static <E> E randomElement(Iterator<E> it, Random r){
        List<E> list = ImmutableList.copyOf(it);
        if (list.size()==1) return list.iterator().next();
        // Generate random index
        return list.get(r.nextInt(list.size()));
    }

    private static <E> E randomElement(Collection<E> c, Random r){
        if (c.size()==1) return c.iterator().next();
        // Generate random index
        List<E> list = ImmutableList.copyOf(c);
        return list.get(r.nextInt(list.size()));
    }


    private Set<Operation<Concept>> findNewOperations(InputDiscoverer<Concept> discoverer, LeveledServices<Concept> layers, Set<Operation<Concept>> selectedOps){
        Random r = new Random();
        ArrayList<Integer> validLevels = new ArrayList<Integer>();
        for(int i=1;i<layers.numberOfLevels(); i++){
            validLevels.add(i);
        }
        Set<Operation<Concept>> operations;
        do{
            Collections.shuffle(validLevels, r);
            int selectedLevel = validLevels.get(0);
            cli.println("Selected level: " + selectedLevel);
            operations = discoverer.findOperationsConsumingSome(generatedOutputsBeforeLevel(selectedLevel, layers));
            operations.removeAll(selectedOps);
            operations.removeAll(layers.getOperationsBeforeLevel(selectedLevel));
            // No more new ops in the selected level, remove
            if (operations.isEmpty()){
                validLevels.remove(validLevels.indexOf(selectedLevel));
            }
        }while(!validLevels.isEmpty() && operations.isEmpty());
        return operations;
    }

    private Set<Concept> generatedOutputsBeforeLevel(int level, LeveledServices<Concept> layers){
        Set<Concept> outputs = new HashSet<Concept>();
        for(Operation<Concept> op : layers.getOperationsBeforeLevel(level)){
            outputs.addAll(op.getSignature().getOutputs());
        }
        return outputs;
    }


    @Override
    public String getCommandName() {
        return "rquery";
    }
}
