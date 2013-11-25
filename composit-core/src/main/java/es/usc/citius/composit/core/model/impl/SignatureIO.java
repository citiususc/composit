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

package es.usc.citius.composit.core.model.impl;

import com.google.common.collect.ImmutableSet;
import es.usc.citius.composit.core.model.Signature;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class SignatureIO<E> implements Signature<E>, Serializable{
    private final Set<E> inputs;
    private final Set<E> outputs;

    public SignatureIO(Collection<E> inputs, Collection<E> outputs) {
        this.inputs = ImmutableSet.copyOf(inputs);
        this.outputs = ImmutableSet.copyOf(outputs);
    }

    public SignatureIO() {
        this.inputs = ImmutableSet.of();
        this.outputs = ImmutableSet.of();
    }

    @Override
    public Set<E> getInputs() {
        return Collections.unmodifiableSet(inputs);
    }

    @Override
    public Set<E> getOutputs() {
        return Collections.unmodifiableSet(outputs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignatureIO that = (SignatureIO) o;

        if (!inputs.equals(that.inputs)) return false;
        if (!outputs.equals(that.outputs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inputs.hashCode();
        result = 31 * result + outputs.hashCode();
        return result;
    }

    public static <E> SignatureIO<E> empty(){
        return new SignatureIO<E>();
    }
}
