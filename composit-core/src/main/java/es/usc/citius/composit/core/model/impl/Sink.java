package es.usc.citius.composit.core.model.impl;


import es.usc.citius.composit.core.model.Signature;

import java.util.Collections;
import java.util.Set;

public class Sink<E> extends DummyOperation<E> {

    public Sink(Set<E> inputs) {
        super("Sink", new SignatureIO<E>(inputs, Collections.<E>emptySet()));
    }
}
