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
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.model.Operation;
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
        Random r = new Random();
        Set<Operation<Concept>> selected = new HashSet<Operation<Concept>>();
        Set<Concept> inputs = new HashSet<Concept>();
        Set<String> availableOps = dataset.getServiceProvider().listOperations();
        String randomService = randomElement(availableOps, r);
        cli.println("Random operation selected: " + randomService);
        Operation<Concept> operation = dataset.getServiceProvider().getOperation(randomService);
        inputs.addAll(operation.getSignature().getInputs());
        // Generate all possible levels
        InputDiscoverer<Concept> discoverer = dataset.getDefaultCompositionProblem().getInputDiscoverer();
        ForwardServiceDiscoverer<Concept, Boolean> fs = new ForwardServiceDiscoverer<Concept, Boolean>(dataset.getDefaultCompositionProblem().getInputDiscoverer(),
                dataset.getDefaultCompositionProblem().getMatchGraph());
        ServiceMatchNetwork<Concept, Boolean> result = fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
        int levels = result.numberOfLevels()-2;
        cli.println("Number of levels: " + levels);
        selected.add(operation);

        while(levels < minLevels){
            // Select a new random operation for a random level
            Set<Operation<Concept>> operations = findNewOperations(discoverer, result, selected);
            if (operations.isEmpty()) break;

            Operation<Concept> op = randomElement(operations, r);
            selected.add(op);
            // Make it executable
            inputs.addAll(op.getSignature().getInputs());
            // Repeat the search
            result = fs.search(new SignatureIO<Concept>(inputs, Collections.<Concept>emptySet()));
            levels = result.numberOfLevels()-2;
        }
        cli.println(result.getLeveledList());
        // Now, search for an optimal composition. Select a random op from each level
        Set<Concept> requiredOutputs = new HashSet<Concept>();
        for(int i=1; i < result.numberOfLevels()-1; i++){
            Set<Operation<Concept>> ops = result.getOperationsAtLevel(i);
            Operation<Concept> op = randomElement(ops, r);
            // Select a random output
            Concept output = randomElement(op.getSignature().getOutputs(), r);
            if (r.nextBoolean()) requiredOutputs.add(output);
        }
        // Generate optimal composition
        cli.println("Selected inputs: " + inputs);
        cli.println("Selected outputs: " + requiredOutputs);
        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());
        composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        composit.search(new SignatureIO<Concept>(inputs, requiredOutputs));
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
