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
import es.usc.citius.composit.cli.CompositCli;
import es.usc.citius.composit.core.composition.InputDiscoverer;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.composition.search.State;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.wsc08.data.WSCTest;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.node.HeuristicNode;

import java.util.*;

@Parameters(commandDescription = "Generate random queries for a dataset")
public class RandomQueryCommand implements CliCommand {

    @Parameter(names = {"-d", "--dataset"}, description = "Select a WSC'08 dataset.", required = true)
    private WSCTest test;

    private int minLevels = 5;

    @Parameter(names = {"-min", "--min-ops"}, description = "Minimum number of operations in the match graph")
    private int minOps = 10;

    @Parameter(names = {"-max", "--max-ops"}, description = "Maximum number of operations in the match graph")
    private int maxOps = 100;

    private CompositCli cli;

    @Override
    public void invoke(CompositCli contextCli) throws Exception {
        this.cli = contextCli;
        WSCTest.Dataset dataset = test.dataset();
        iterativeForwardSearch(dataset);
    }

    private void kbOptimizeRequest(Set<Concept> inputs, Set<Concept> outputs, HierarchicalKnowledgeBase kb){
        for(Concept x : inputs){
            Set<Concept> xs = kb.getSubclasses(x);
            for(Concept y : inputs){
                if (x.equals(y)) continue;
                Set<Concept> ys = kb.getSubclasses(y);
                // Check some element in common
            }
        }

    }

    private void iterativeForwardSearch(WSCTest.Dataset dataset) throws Exception {
        // Select a random input
        Random r = new Random();
        Set<Concept> inputs = new HashSet<Concept>();
        InputDiscoverer<Concept> discoverer = dataset.getDefaultCompositionProblem().getInputDiscoverer();
        MatchGraph<Concept, Boolean> matcher = dataset.getDefaultCompositionProblem().getMatchGraph();
        // Starting point
        String startingOp = randomElement(dataset.getServiceProvider().listOperations(), r);
        inputs.addAll(dataset.getServiceProvider().getOperation(startingOp).getSignature().getInputs());
        // Random max ops
        int diff = (maxOps-minOps) > 0 ? maxOps - minOps : 1;
        int max = r.nextInt(diff)+minOps;
        cli.println("Max operations selected: " + max);
        ServiceMatchNetwork<Concept, Boolean> result;
        do {
            ForwardServiceDiscoverer<Concept, Boolean> fs =
                    new ForwardServiceDiscoverer<Concept, Boolean>(discoverer, matcher);
            result = fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
            // Select a random op and provide its inputs:
            Operation<Concept> op = randomElement(fs.getUnmatchedInputMap().keySet(), r);
            inputs.addAll(fs.getUnmatchedInputMap().get(op));
        } while (result.listOperations().size() < max);

        cli.println("Request inputs: " + inputs.size());
        cli.println("Unresolved inputs before optimization: " + unresolvedInputs(result).size());
        // Optimize network
        result = new BackwardMinimizationOptimizer<Concept, Boolean>().optimize(result);
        result = new FunctionalDominanceOptimizer<Concept, Boolean>().optimize(result);
        // Optimize inputs
        Set<Concept> optInputs = unresolvedInputs(result);
        cli.println("Unresolved inputs after optimization: " + optInputs.size());

        if (optInputs.size() < inputs.size()){
            inputs = optInputs;
        }

        // TODO: Select a random number of outputs from the last layer
        // Select outputs (one from each service in the last level)
        int randomSizeOutputs = ((r.nextInt(Operations.outputs(result.getOperations()).size())) % 5) +1;
        cli.println("Selecting " + randomSizeOutputs + " outputs...");
        Set<Concept> outputs = randomOutputs(result, randomSizeOutputs, r);

        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());
        composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        Algorithms.Search<State<Concept>,HeuristicNode<State<Concept>,Double>>.Result composition
                = composit.search(new SignatureIO<Concept>(inputs, outputs));
        List<State<Concept>> solution = composition.getOptimalPath();
        // Clean request
        inputs = cleanInputs(solution, matcher);
        List<State<Concept>> solution2 = composit.search(new SignatureIO<Concept>(inputs, outputs)).getOptimalPath();
        int solutionSize = composition.getGoalNode().getCost().intValue();
        int solutionLength = solution.size()-2;
        //if (solution.equals(solution2)){
            cli.println("Random query found.");
            cli.println(" - Inputs: " + inputs);
            cli.println(" - Outputs: " + outputs);
            cli.println(" - Optimal solution size/length: " + solutionLength + "/" + solutionSize);
        /*} else {
            throw new Exception("Query minimization failed");
        }*/
    }

    private Set<Concept> randomOutputs(ServiceMatchNetwork<Concept, Boolean> network, int total, Random r){
        Set<Concept> outputs = new HashSet<Concept>();
        while(outputs.size() < total){
            double initialProbability = 0.9d;
            double p = initialProbability;
            for(int level=network.numberOfLevels()-2; level>0; level--){
                Set<Operation<Concept>> ops = network.getOperationsAtLevel(level);
                for(Operation<Concept> o : ops){
                    Concept output = randomElement(o.getSignature().getOutputs(), r);
                    // add an output with more probability if it is far from the first level
                    if (r.nextDouble() < p){
                        outputs.add(output);
                    }
                    if (outputs.size() == total){
                        return outputs;
                    }
                }
                // Decrement probability initP / x
                p = initialProbability / (double)(network.numberOfLevels()-level);
            }
        }
        return outputs;
    }

    private Set<Concept> unresolvedInputs(ServiceMatchNetwork<Concept, Boolean> network){
        Set<Concept> unresolvedInputs = new HashSet<Concept>();
        for(int level = network.numberOfLevels()-1; level > 0; level--){
            Set<Operation<Concept>> ops = network.getOperationsAtLevel(level);
            Set<Concept> levelOutputs = Operations.outputs(ops);
            Set<Concept> resolved = network.partialMatch(levelOutputs, unresolvedInputs).getTargetElements();
            unresolvedInputs.removeAll(resolved);
            unresolvedInputs.addAll(Operations.inputs(ops));
        }
        return unresolvedInputs;
    }

    private Set<Concept> cleanInputs(List<State<Concept>> solution, SetMatchFunction<Concept, Boolean> matcher){
        Set<Concept> unresolvedInputs = new HashSet<Concept>();
        for(int level = 0; level < solution.size()-1; level++){
            Set<Operation<Concept>> ops = solution.get(level).getStateOperations();
            Set<Concept> levelOutputs = Operations.outputs(ops);
            Set<Concept> resolved = matcher.partialMatch(levelOutputs, unresolvedInputs).getTargetElements();
            unresolvedInputs.removeAll(resolved);
            unresolvedInputs.addAll(Operations.inputs(ops));
        }
        return unresolvedInputs;
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

    @Override
    public String getCommandName() {
        return "rquery";
    }
}
