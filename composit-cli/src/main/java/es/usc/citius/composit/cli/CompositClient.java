package es.usc.citius.composit.cli;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositClient {

    @Option(name="-dataset", usage="Select WSC'08 dataset between 1 and 8")
    private int dataset = 1;

    @Option(name="-opt-backmin", usage="Use backward minimization")
    private boolean backwardMinimizationOpt = true;

    @Option(name="-opt-dominance", usage="Use functional dominance optimization")
    private boolean functionalDominanceOpt = true;

    @Option(name="-opt-eqnode", usage="Use node equivalence detection")
    private boolean nodeEquivalenceOpt = false;

    @Option(name="-debug", usage="Dump debug information")
    private boolean debug = false;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        new CompositClient().run(args);
    }

    private void run(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
            if( arguments.isEmpty() )
                throw new CmdLineException(parser,"No argument is given");
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

        // Configure all according to the options
    }
}
