package es.usc.citius.composit.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import es.usc.citius.composit.cli.command.CliCommand;
import es.usc.citius.composit.cli.command.CompositionCommand;
import es.usc.citius.composit.cli.command.HelpCommand;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositCli {

    private static final String nl = System.getProperty("line.separator");

    // Command binding
    private Map<String, CliCommand> bindings = new HashMap<String, CliCommand>();

    @Parameter(names = "--debug", description = "Change log level to Debug mode")
    private boolean debug = false;

    @Parameter(names = "--help", help = true, description = "Print general command usage options")
    private boolean showHelp = false;

    @Parameter(names = {"-v", "--version"}, description = "Print ComposIT version")
    private boolean version = false;

    public static void main(String[] args) throws IOException {
        new CompositCli().run(args);
    }

    private void setLogbackLevel(Level level){
        ((Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)).setLevel(level);
    }

    private void run(String[] args) throws IOException {
        header();
        systemInfo();
        System.out.println("Arguments: " + Arrays.toString(args));
        separator();
        System.out.println();

        // Configure cli with the available commands
        JCommander cli = new JCommander(this);
        cli.setProgramName("Composit");

        // Add command bindings
        CompositionCommand compose = new CompositionCommand();
        HelpCommand help = new HelpCommand();
        bindings.put(compose.getCommandName(), compose);
        bindings.put(help.getCommandName(), help);

        // Add all available commands to JCommander
        for(CliCommand cmd : bindings.values()){
            cli.addCommand(cmd.getCommandName(), cmd);
        }

        if (args.length == 0){
            cli.usage();
            System.exit(0);
        }
        // Parse options
        try {
            cli.parse(args);
        }catch(Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("To see the commands and options available, use --help");
            System.exit(-1);
        }

        // Print help?
        if (showHelp){
            cli.usage();
            System.exit(0);
        }

        // Configure log level
        Level level = (debug)? Level.DEBUG : Level.INFO;
        setLogbackLevel(level);

        // Run command
        bindings.get(cli.getParsedCommand()).invoke(cli, this);
    }

    public void separator(){
        System.out.println("========================================================================");
    }

    public void header(){
        String header =
                "   _____                                _____ _______  " + nl +
                "  / ____|                              |_   _|__   __| " + nl +
                " | |     ___  _ __ ___  _ __   ___  ___  | |    | |    " + nl +
                " | |    / _ \\| '_ ` _ \\| '_ \\ / _ \\/ __| | |    | |" + nl +
                " | |___| (_) | | | | | | |_) | (_) \\__ \\_| |_   | |  " + nl +
                "  \\_____\\___/|_| |_| |_| .__/ \\___/|___/_____|  |_| " + nl +
                "                       | |                             " + nl +
                "                       |_|                             " + nl;
        System.out.println(header);
        System.out.println("  ComposIT :: Semantic Web Service Composition API" + nl);
        System.out.println();
        System.out.println(getLicense());
        System.out.println();
    }

    public void systemInfo(){
        System.out.println(getSystemInfo());
        System.out.println();
    }

    private String getSystemInfo(){
        String info =
                "Java: " + System.getProperty("java.version") + nl +
                "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + nl +
                "Free/Total/Max memory: " + byteCount(Runtime.getRuntime().freeMemory(), true) + "/" + byteCount(Runtime.getRuntime().totalMemory(), true) + "/" + byteCount(Runtime.getRuntime().maxMemory(), true);
                //"Available processors: " + Runtime.getRuntime().availableProcessors() + nl +
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

    public String getLicense(){
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
