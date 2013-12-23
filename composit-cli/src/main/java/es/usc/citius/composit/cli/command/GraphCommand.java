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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
import es.usc.citius.composit.blueprints.BlueprintsUtils;
import es.usc.citius.composit.cli.CompositCli;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.provider.MemoryIndexServiceProvider;
import es.usc.citius.composit.core.provider.ServiceProvider;
import es.usc.citius.composit.wsc08.data.WSCTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@Parameters(commandDescription = "Generate different service graphs from a dataset")
public class GraphCommand implements CliCommand {

    @Parameter(names = {"-d", "--dataset"}, description = "Select a WSC'08 dataset.", required = true)
    private WSCTest test;

    @Parameter(names = {"-o", "--export"}, description = "Export match network to the specified file (GraphML format)")
    private String exportFile = null;

    @Override
    public void invoke(CompositCli contextCli) throws Exception {
        // Export?
        if (exportFile != null && !exportFile.isEmpty()){
            // Try to generate the graph with blueprints
            contextCli.println("Exporting graph to " + exportFile + "...");
            GraphMLWriter.outputGraph(buildGraph(), new FileOutputStream(new File(exportFile)));
            contextCli.println("Graph exported");
        }
    }

    @Override
    public String getCommandName() {
        return "graph";
    }

    public static Multimap<Operation<Concept>, Operation<Concept>> buildOperationGraph(ServiceProvider<Concept> provider, SetMatchFunction<Concept, ?> matcher){
        Multimap<Operation<Concept>, Operation<Concept>> mmap = HashMultimap.create();
        // Find match between all services
        for(Operation<Concept> source : provider.getOperations()){
            for(Operation<Concept> target : provider.getOperations()){
                if (source.getID().equals(target.getID())) continue;
                // Output -> Input match?
                if (!matcher.partialMatch(source.getSignature().getOutputs(),
                        target.getSignature().getInputs()).getMatchTable().isEmpty()){
                    mmap.get(source).add(target);
                }
            }
        }
        return mmap;
    }

    private Graph buildGraph() throws IOException {
        // Open dataset
        WSCTest.Dataset dataset = test.dataset();
        ServiceProvider<Concept> provider = new MemoryIndexServiceProvider<Concept>(dataset.getServiceProvider());
        SetMatchFunction<Concept, Boolean> matcher = dataset.getMatchGraph();
        Multimap<Operation<Concept>, Operation<Concept>> mmap = buildOperationGraph(provider, matcher);
        return BlueprintsUtils.mapToGraph(mmap.asMap());
    }
}
