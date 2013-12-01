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
import com.beust.jcommander.internal.Lists;
import es.usc.citius.composit.cli.CompositCli;

import java.util.List;

@Parameters(commandDescription = "Print command help")
public class HelpCommand implements CliCommand {

    @Parameter
    private List<String> command = Lists.newArrayList();

    @Override
    public void invoke(CompositCli contextCli) {
        for(String cmd : command){
            System.out.println("Printing " + cmd + " documentation:");
            contextCli.getCli().usage(cmd);
            System.out.println();
        }
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}
