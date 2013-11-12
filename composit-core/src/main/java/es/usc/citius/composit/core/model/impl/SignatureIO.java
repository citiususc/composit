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
}
