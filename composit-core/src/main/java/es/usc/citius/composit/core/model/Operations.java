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

package es.usc.citius.composit.core.model;

import java.util.*;

/**
 * Util class to manipulate operations.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Operations {

    private Operations(){
        throw new AssertionError("Use the static methods of this class instead.");
    }

	/* Static functions to manipulate operations */

    /**
     * Get all the operations of the services.
     * @param services Iterable with the services.
     * @param <E> input/output type of the operations.
     * @return A set with the operations of the services.
     */
    public static final <E> Set<Operation<E>> extract(Iterable<Service> services){
        Set<Operation<E>> operations = new HashSet<Operation<E>>();
        for(Service s : services){
            operations.addAll(s.getOperations());
        }
        return operations;
    }

    /**
     * Collect all inputs of the operations.
     * @param operations Iterable with the operations.
     * @param <E> input/output type of the operations.
     * @return Set with all inputs.
     */
    public static final <E> Set<E> inputs(Iterable<Operation<E>> operations){
        Set<E> inputs = new HashSet<E>();
        for(Operation<E> o : operations){
            inputs.addAll(o.getSignature().getInputs());
        }
        return inputs;
    }

    /**
     * Collect all outputs of the operations.
     * @param operations Iterable with the operations.
     * @param <E> input/output type of the operations.
     * @return Set with all outputs.
     */
    public static final <E> Set<E> outputs(Iterable<Operation<E>> operations){
        Set<E> outputs = new HashSet<E>();
        for(Operation<E> o : operations){
            outputs.addAll(o.getSignature().getOutputs());
        }
        return outputs;
    }

    /**
     * Get all signatures of the operations.
     * @param operations Iterable with the operations.
     * @param <E> input/output type of the operations.
     * @return A Set with all signatures.
     */
    public static final <E> Set<Signature<E>> signatures(Collection<Operation<E>> operations){
        Set<Signature<E>> signatures = new HashSet<Signature<E>>();
        for(Operation<E> o : operations){
            signatures.add(o.getSignature());
        }
        return signatures;
    }

    /**
     * Generate a map between signatures and their operations.
     * @param operations Iterable with the operations.
     * @param <E> input/output type of the operations.
     * @return Map with signature => operation.
     */
    public static final <E> Map<Signature<E>, Operation<E>> signatureOperation(Iterable<Operation<E>> operations){
        Map<Signature<E>, Operation<E>> mapping = new HashMap<Signature<E>, Operation<E>>();
        for(Operation<E> o : operations){
            mapping.put(o.getSignature(), o);
        }
        return mapping;
    }

    /**
     * Get all service owners of the operations.
     * @param operations Iterable with the operations.
     * @param <E> input/output type of the operations.
     * @return Set with the service owners.
     */
    public static final <E> Set<Service<E>> owners(Iterable<Operation<E>> operations){
        Set<Service<E>> owners = new HashSet<Service<E>>();
        for(Operation<E> o : operations){
            Service<E> owner = o.getServiceOwner();
            if (owner != null) owners.add(owner);
        }
        return owners;
    }
}
