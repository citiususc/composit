package es.usc.citius.composit.core.model.impl;


import es.usc.citius.composit.core.model.Signature;

import java.util.Collections;
import java.util.Set;

public class Source<E> extends DummyOperation<E> {
    public Source(Set<E> outputs) {
        super("Source", new SignatureIO<E>(Collections.<E>emptySet(), outputs));
    }
}
