package es.usc.citius.composit.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
import es.usc.citius.composit.blueprints.NetworkGraphMaker;
import es.usc.citius.composit.cli.CompositCli;
import es.usc.citius.composit.cli.NetworkConfig;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.wsc08.data.WSCTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@Parameters(commandDescription = "Generate a match network")
public class NetworkCommand implements CliCommand {

    @Parameter(names = {"-o", "--export"}, description = "Export match network to the specified file (GraphML format)")
    private String exportFile = null;

    @ParametersDelegate
    private NetworkConfig config = new NetworkConfig();

    @Override
    public void invoke(CompositCli contextCli) throws Exception {
        // Print system information
        contextCli.println("Generating match graph for: " + this.config.getTest().toString());

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

        ServiceMatchNetwork<Concept, Boolean> network = composit.generateNetwork(dataset.getRequest());

        contextCli.println("Service Match Network generated: ");

        // Print network info
        int level = 0;
        for(Set<Operation<Concept>> op : network.getLeveledList()){
            contextCli.println("\t- Level " + level++ + " (" + op.size() + " ops): " + op);
        }

        // Export?
        if (exportFile != null && !exportFile.isEmpty()){
            // Try to generate the graph with blueprints
            contextCli.println("Exporting network to " + exportFile + "...");
            GraphMLWriter.outputGraph(NetworkGraphMaker.create(network), new FileOutputStream(new File(exportFile)));
            contextCli.println("Graph exported");
        }
    }

    @Override
    public String getCommandName() {
        return "network";
    }
}
