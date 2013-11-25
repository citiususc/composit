/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
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

package es.usc.citius.composit.core.composition;

import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Verifier {

    public static <E,T extends Comparable<T>> boolean satisfies(LeveledServices<E> network, SetMatchFunction<E,T> matcher){
        // Check if the network is well-formed and satisfies the request
        Set<E> available = new HashSet<E>(network.getSource().getSignature().getOutputs());
        for(int level=1; level < network.numberOfLevels()-1; level++){
            Set<Operation<E>> operations = network.getOperationsAtLevel(level);
            // Check invokability
            for(Operation<E> op : operations){
                if (!matcher.fullMatch(available, op.getSignature().getInputs()).getTargetElements().equals(op.getSignature().getInputs())){
                    // This operation cannot be executed
                    return false;
                }
            }
            available.addAll(Operations.outputs(operations));
            if (matcher.fullMatch(available, network.getSink().getSignature().getInputs()).getTargetElements().equals(network.getSink().getSignature().getInputs())){
                return true;
            }
        }
        return false;
    }

}
