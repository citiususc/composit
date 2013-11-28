package es.usc.citius.composit.cli;

import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.wsc08.data.WSCTest;
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

    @Option(name="-dataset", usage="Select WSC'08 dataset between 1 and 8.")
    private int dataset = 1;

    @Option(name="-opt-backmin", usage="Use backward minimization.")
    private boolean backwardMinimizationOpt = true;

    @Option(name="-opt-dominance", usage="Use functional dominance optimization.")
    private boolean functionalDominanceOpt = true;

    @Option(name="-opt-eqnode", usage="Use node equivalence detection.")
    private boolean nodeEquivalenceOpt = false;

    @Option(name="-smatch", usage="Select semantic match type")
    private LogicMatchType matchType = LogicMatchType.EXACT;

    @Option(name="-debug", usage="Dump debug information.")
    private boolean debug = false;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        new CompositClient().run(args);
    }

    private void run(String[] args) {
        header();
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

    private void search() throws IOException {
        WSCTest.Dataset dataset = WSCTest.TESTSET_2008_01.dataset();
        ComposIT.search(dataset.getDefaultCompositionProblem(), dataset.getRequest());
    }

    private void header(){
        String header =
                "   _____                                _____ _______   \n" +
                "  / ____|                              |_   _|__   __|  \n" +
                " | |     ___  _ __ ___  _ __   ___  ___  | |    | |     \n" +
                " | |    / _ \\| '_ ` _ \\| '_ \\ / _ \\/ __| | |    | | \n" +
                " | |___| (_) | | | | | | |_) | (_) \\__ \\_| |_   | |   \n" +
                "  \\_____\\___/|_| |_| |_| .__/ \\___/|___/_____|  |_|  \n" +
                "                       | |                              \n" +
                "                       |_|                              \n";
        System.out.println(header);
        System.out.println("ComposIT :: Semantic Web Service Composition API");
        System.out.println();
        System.out.println(license());
        System.out.println();
    }

    private String license(){
        return  "This software is licensed under Apache 2.0 license:\n" +
                "\n" +
                "\tCopyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS)\n" +
                "\tUniversity of Santiago de Compostela (USC).\n" +
                "\n" +
                "\tLicensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "\tyou may not use this file except in compliance with the License.\n" +
                "\tYou may obtain a copy of the License at\n" +
                "\n" +
                "\t    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "\tUnless required by applicable law or agreed to in writing, software\n" +
                "\tdistributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "\tWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "\tSee the License for the specific language governing permissions and\n" +
                "\tlimitations under the License.";
    }
}
