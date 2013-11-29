package es.usc.citius.composit.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositClient {

    private static final String nl = System.getProperty("line.separator");

    @Option(name="-dataset", usage="Select WSC'08 dataset between 1 and 8.")
    private int dataset = 1;

    @Option(name="-opt-backmin", usage="Use backward minimization.")
    private boolean backwardMinimizationOpt = true;

    @Option(name="-opt-dominance", usage="Use functional dominance optimization.")
    private boolean functionalDominanceOpt = true;

    @Option(name="-opt-eqnode", usage="Use node equivalence detection.")
    private boolean nodeEquivalenceOpt = false;

    @Option(name="-smatch", usage="Select semantic match type (subsumes > plugin > exact)")
    private LogicMatchType matchType = LogicMatchType.PLUGIN;

    @Option(name="-benchmark-cycles", usage="Use this option with a number N > 0 to run N benchmark cycles")
    private int benchmarkCycles = 0;

    @Option(name="-debug", usage="Dump debug information.")
    private boolean debug = false;

    public static void main(String[] args) throws IOException {
        new CompositClient().run(args);
    }

    private void setLogbackLevel(Level level){
        ((Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)).setLevel(level);
    }

    private void run(String[] args) throws IOException {
        header();
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

        // Configure all according to the options
        WSCTest.Dataset wscDataset;

        switch(dataset){
            case 1:
                wscDataset = WSCTest.TESTSET_2008_01.dataset();
                break;
            case 2:
                wscDataset = WSCTest.TESTSET_2008_02.dataset();
                break;
            case 3:
                wscDataset = WSCTest.TESTSET_2008_03.dataset();
                break;
            case 4:
                wscDataset = WSCTest.TESTSET_2008_04.dataset();
                break;
            case 5:
                wscDataset = WSCTest.TESTSET_2008_05.dataset();
                break;
            case 6:
                wscDataset = WSCTest.TESTSET_2008_06.dataset();
                break;
            case 7:
                wscDataset = WSCTest.TESTSET_2008_07.dataset();
                break;
            case 8:
                wscDataset = WSCTest.TESTSET_2008_08.dataset();
                break;
            default:
                wscDataset = WSCTest.TESTSET_2008_01.dataset();
                System.err.println("Unrecognized dataset. Using WSC'08 Dataset 01");
        }

        setLogbackLevel(Level.INFO);

        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(wscDataset.getDefaultCompositionProblem());

        // Configure search
        if (backwardMinimizationOpt){
            composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        }
        if (functionalDominanceOpt){
            composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        }
        if (debug){
            setLogbackLevel(Level.DEBUG);
        }

        // Compute benchmark
        long minMS = Long.MAX_VALUE;
        for(int i=0; i<benchmarkCycles; i++){
            Stopwatch benchWatch = Stopwatch.createStarted();
            composit.search(wscDataset.getRequest());
            long ms = benchWatch.stop().elapsed(TimeUnit.MILLISECONDS);
            if (ms < minMS){
                minMS = ms;
            }
        }
        if (benchmarkCycles > 0){
            System.out.println(" > " + benchmarkCycles + "-cycle benchmark completed. Best time: " + minMS + " ms.");
        }

    }

    private void header(){
        String header =
                "   _____                                _____ _______  " + nl +
                "  / ____|                              |_   _|__   __| " + nl +
                " | |     ___  _ __ ___  _ __   ___  ___  | |    | |    " + nl +
                " | |    / _ \\| '_ ` _ \\| '_ \\ / _ \\/ __| | |    | |" + nl +
                " | |___| (_) | | | | | | |_) | (_) \\__ \\_| |_   | |  " + nl +
                "  \\_____\\___/|_| |_| |_| .__/ \\___/|___/_____|  |_| " + nl +
                "                       | |                             " + nl +
                "                       |_|                             ";
        System.out.println(header);
        System.out.println("  ComposIT :: Semantic Web Service Composition API" + nl);
        System.out.println();
        System.out.println(license());
        System.out.println();
        System.out.println(systemInfo());
        System.out.println();
    }

    private String systemInfo(){
        String info =
                "Running on: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + nl +
                "Available processors: " + Runtime.getRuntime().availableProcessors() + nl +
                "Free/Total/Max memory: " + byteCount(Runtime.getRuntime().freeMemory(), true) + "/" + byteCount(Runtime.getRuntime().totalMemory(), true) + "/" + byteCount(Runtime.getRuntime().maxMemory(), true);
        return info;
    }

    /**
     * Util method to print formatted byte size converted.
     * Code from http://stackoverflow.com/a/3758880
     */
    public static String byteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
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
