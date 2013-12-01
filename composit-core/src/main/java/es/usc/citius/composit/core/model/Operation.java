/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC) http://citius.usc.es.
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


/**
 * Generic IO(PE) specification of a service operation. A service operation is basically
 * a {@link Signature} with the information about inputs, outputs, the PE part (preconditions and effects),
 * and the {@link Service} owner. NOTE: Preconditions/Effects are not supported in the current version of
 * the API.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @param <E> input/output type.
 */
public interface Operation<E> extends Resource {

    /**
     * Get the service of this operation. The service can be null (for example, dummy services
     * has no services associated).
     *
     * @return Service owner.
     */
    Service<E> getServiceOwner();

    /**
     * Signature (inputs/outputs) of the operation.
     * @return Signature with the inputs and outputs.
     */
    Signature<E> getSignature();
    //TODO; implement precondition / effects methods
}
