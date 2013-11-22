package es.usc.citius.composit.core.provider;


import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.Set;

public interface ServiceDataProvider<E> {
    /**
     * Locates and imports a service from the current service
     * provider.
     * @param serviceID unique ID of the service used to locate the resource.
     * @return Imported service model.
     */
    Service<E> getService(String serviceID);

    /**
     * Locates and imports an operation from the current service provider.
     * @param operationID unique ID of the operation used to locate the resource.
     * @return Imported service model.
     */
    Operation<E> getOperation(String operationID);

    /**
     * Returns an iterable with all operations that have the
     * selected input.
     * @param input Input to be found.
     * @return Iterable with operations
     */
    Iterable<Operation<E>> getOperationsWithInput(E input);

    /**
     * Returns an iterable with all operations that have the
     * selected output.
     * @param output Output to be found.
     * @return Iterable with operations
     */
    Iterable<Operation<E>> getOperationsWithOutput(E output);

    /**
     * Returns an iterator over the available operations.
     * @return {@link Iterable} with the translated operations.
     */
    Iterable<Operation<E>> getOperations();

    /**
     * Returns an iterator over the available services.
     * @return {@link Iterable} with the translated services.
     */
    Iterable<Service<E>> getServices();

    /**
     * List the available operation id's.
     * @return Set of operation ids. To retrieve
     * the operation model use {@link ServiceDataProvider#getOperation(String)}.
     */
    Set<String> listOperations();

    /**
     * List the available service id's.
     * @return Set of service ids. To retrieve the service model
     * use {@link ServiceDataProvider#getService(String)}.
     */
    Set<String> listServices();
}
