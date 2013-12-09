package es.usc.citius.composit.cli;

import com.beust.jcommander.Parameter;
import es.usc.citius.composit.wsc08.data.WSCTest;

import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositionConfig {
    @Parameter(names = {"-d", "--dataset"}, description = "Select an internal WSC'08 dataset.")
    private WSCTest test = null;

    @Parameter(names = {"-p", "--dataset-path"}, description = "Select a custom WSC'08 dataset folder. This option will be ignored if -d is used.")
    private String wscDatasetPath = null;

    @Parameter(names = {"-ri", "--inputs"}, description = "Select request inputs. If no inputs are specified, " +
            "then the default request inputs for the dataset will be used.")
    private Set<String> inputs = null;

    @Parameter(names = {"-ro", "--outputs"}, description = "Select request outputs. If no outputs are specified, " +
            "then the default request outputs for the dataset will be used.")
    private Set<String> outputs = null;

    @Parameter(names = {"-ob", "--opt-backward"}, arity = 1, description = "Use backward optimization over the match network")
    private boolean backwardOptimization = true;

    @Parameter(names = {"-of", "--opt-fdominance"}, arity = 1, description = "Use functional dominance optimization over the match network")
    private boolean functionalDominance = true;

    public void validate() throws Exception {
        if (this.test == null && (this.getWscDatasetPath() == null || this.getWscDatasetPath().isEmpty()) ){
            throw new Exception("Please use -d to select an embedded " +
                    "WSC'08 dataset or -p to select a WSC'08 dataset folder");
        }
    }

    public WSCTest getTest() {
        return test;
    }

    public Set<String> getInputs() {
        return inputs;
    }

    public Set<String> getOutputs() {
        return outputs;
    }

    public boolean isBackwardOptimization() {
        return backwardOptimization;
    }

    public boolean isFunctionalDominance() {
        return functionalDominance;
    }

    public String getWscDatasetPath() {
        return wscDatasetPath;
    }
}
