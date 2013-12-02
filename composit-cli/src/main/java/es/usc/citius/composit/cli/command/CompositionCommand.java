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
import com.beust.jcommander.ParametersDelegate;
import com.google.common.base.Stopwatch;
import es.usc.citius.composit.cli.CompositCli;
import es.usc.citius.composit.cli.NetworkConfig;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.wsc08.data.WSCTest;

import java.util.concurrent.TimeUnit;

@Parameters(commandDescription = "Search for an optimal composition")
public class CompositionCommand implements CliCommand {

    @ParametersDelegate
    private NetworkConfig config = new NetworkConfig();

    @Parameter(names = {"-oeq", "--opt-node-eq"}, description = "Use node equivalence detection during search node expansion")
    private boolean nodeEquivalence = false;

    @Parameter(names = {"-b", "--benchmark-cycles"}, description = "Activate benchmark mode using N cycles")
    private int benchmarkCycles = 0;

    @Parameter(names = {"-sm", "--semantic-match"}, description = "Select semantic match type allowed [ SUBSUMES | PLUGIN | EXACT ]")
    private LogicMatchType matchType = LogicMatchType.PLUGIN;

    private CompositCli cli;

    @Override
    public void invoke(CompositCli contextCli) throws Exception {
        // Print system information
        System.out.println("> Running composition on: " + this.config.getTest().toString());

        // Load dataset
        WSCTest.Dataset dataset = config.getTest().dataset();

        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());

        // Configure search
        if (config.isBackwardOptimization()){
            composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        }
        if (config.isFunctionalDominance()){
            composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        }

        if (benchmarkCycles > 0){
            benchmark(composit, dataset, benchmarkCycles);
        } else {
            composit.search(dataset.getRequest());
        }

    }

    private void benchmark(ComposIT<Concept, Boolean> composit, WSCTest.Dataset dataset, int cycles){
        // Compute benchmark
        Stopwatch watch = Stopwatch.createUnstarted();
        long minMS = Long.MAX_VALUE;
        for(int i=0; i<cycles; i++){
            System.out.println("[ComposIT Search] Starting benchmark cycle " + (i+1));
            watch.start();
            composit.search(dataset.getRequest());
            long ms = watch.stop().elapsed(TimeUnit.MILLISECONDS);
            if (ms < minMS){
                minMS = ms;
            }
            watch.reset();
        }
        System.out.println("[Benchmark Result] " + cycles + "-cycle benchmark completed. Best time: " + minMS + " ms.");
    }

    @Override
    public String getCommandName() {
        return "compose";
    }

    public boolean isNodeEquivalence() {
        return nodeEquivalence;
    }

    public int getBenchmarkCycles() {
        return benchmarkCycles;
    }

    public LogicMatchType getMatchType() {
        return matchType;
    }
}
