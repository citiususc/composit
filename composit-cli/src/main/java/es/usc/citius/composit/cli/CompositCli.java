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

package es.usc.citius.composit.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import es.usc.citius.composit.cli.command.CliCommand;
import es.usc.citius.composit.cli.command.CompositionCommand;
import es.usc.citius.composit.cli.command.HelpCommand;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

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

    private JCommander cli;

    public static void main(String[] args) throws IOException {
        AnsiConsole.systemInstall();
        new CompositCli().run(args);
    }

    public CompositCli() {
        // Configure cli with the available commands
        this.cli = new JCommander(this);
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
    }

    private void setLogbackLevel(Level level){
        ((Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)).setLevel(level);
    }

    private void run(String[] args) throws IOException {
        header(args);

        // If no arguments are provided, show help and return
        if (args.length == 0){
            cli.usage();
            return;
        }

        // Parse options
        try {
            cli.parse(args);
        }catch(Exception e){
            System.err.println(e.getMessage());
            System.err.println("To see the commands and options available, use --help");
            System.exit(-1);
        }

        // Configure log level
        setLogbackLevel((debug)? Level.DEBUG : Level.INFO);

        // Print help?
        if (showHelp){
            cli.usage();
            return;
        }

        // Run command and exit
        try {
            bindings.get(cli.getParsedCommand()).invoke(cli, this);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public void separator(){
        System.out.println(Strings.repeat("=", 80));
    }

    public void header(String[] args) throws IOException {
        InputStream logoRsc = this.getClass().getClassLoader().getResourceAsStream("logo.txt");
        //InputStream licenseRsc = this.getClass().getClassLoader().getResourceAsStream("LICENSE.txt");
        String logo = CharStreams.toString(new InputStreamReader(logoRsc));
        //String license = CharStreams.toString(new InputStreamReader(licenseRsc));

        System.out.println(ansi().render("@|yellow " + logo + "|@"));
        System.out.println();
        System.out.println(ansi().render("\t  @|yellow ComposIT|@ " + ":: Semantic Web Service Composition API"));
        System.out.println();
        System.out.println();
        System.out.println(ansi().render("This software is licensed under @|green Apache 2.0|@ license:"));
        System.out.println();
        System.out.println(getLicense());
        System.out.println();
        systemInfo();
        System.out.println("Arguments: " + Arrays.toString(args));
        separator();
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
        return
                "\tCopyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS)\n" +
                "\tUniversity of Santiago de Compostela (USC) http://citius.usc.es.\n" +
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
