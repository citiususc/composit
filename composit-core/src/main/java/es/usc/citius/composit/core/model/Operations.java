package es.usc.citius.composit.core.model;

import java.util.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Operations {

    private Operations(){
        throw new AssertionError("Use the static methods of this class instead.");
    }

	/* Static functions to manipulate operations */

    public static final <E> Set<Operation<E>> extract(Iterable<Service> services){
        Set<Operation<E>> operations = new HashSet<Operation<E>>();
        for(Service s : services){
            operations.addAll(s.getOperations());
        }
        return operations;
    }

    public static final <E> Set<E> inputs(Iterable<Operation<E>> processes){
        Set<E> inputs = new HashSet<E>();
        for(Operation<E> o : processes){
            inputs.addAll(o.getSignature().getInputs());
        }
        return inputs;
    }

    public static final <E> Set<E> outputs(Iterable<Operation<E>> processes){
        Set<E> outputs = new HashSet<E>();
        for(Operation<E> o : processes){
            outputs.addAll(o.getSignature().getOutputs());
        }
        return outputs;
    }

    public static final <E> Collection<Signature> signatures(Collection<Operation<E>> processes){
        Set<Signature> signatures = new HashSet<Signature>();
        for(Operation<E> o : processes){
            signatures.add(o.getSignature());
        }
        return signatures;
    }

    public static final <E> Map<Signature, Operation<E>> signatureOperation(Collection<Operation<E>> processes){
        Map<Signature, Operation<E>> mapping = new HashMap<Signature, Operation<E>>();
        for(Operation<E> o : processes){
            mapping.put(o.getSignature(), o);
        }
        return mapping;
    }

    public static final <E> Collection<Service> owners(Collection<Operation<E>> processes){
        Set<Service> owners = new HashSet<Service>();
        for(Operation<E> o : processes){
            owners.add(o.getServiceOwner());
        }
        return owners;
    }
}
