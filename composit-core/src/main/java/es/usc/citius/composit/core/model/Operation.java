package es.usc.citius.composit.core.model;


/**
 * Generic IOPE specification of a service operation. A service operation is basically
 * a {@link Signature} with the information about inputs, outputs, preconditions and effects,
 * and the {@link Service} owner.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @param <E> data type.
 */
public interface Operation<E> extends Resource {
    Service<E> getServiceOwner();
    Signature<E> getSignature();
    //TODO; implement precondition / effects methods
}
