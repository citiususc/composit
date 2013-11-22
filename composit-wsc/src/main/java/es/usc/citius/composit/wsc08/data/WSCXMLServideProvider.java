package es.usc.citius.composit.wsc08.data;


import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;
import es.usc.citius.composit.core.model.impl.ResourceOperation;
import es.usc.citius.composit.core.model.impl.ResourceService;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.core.provider.ServiceDataProvider;
import es.usc.citius.composit.wsc08.data.model.xml.XMLInstance;
import es.usc.citius.composit.wsc08.data.model.xml.services.XMLService;
import es.usc.citius.composit.wsc08.data.model.xml.services.XMLServices;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * This class provides access and translates the data format of the Web Service Challenge 2008
 * into the ComposIT model. The WSC'08 format has many drawbacks: it does not use proper URIs and
 * it uses non-standard XML formats to define services and ontologies.
 */
public class WSCXMLServideProvider implements ServiceDataProvider<String> {
    // JAXB Root class
    private XMLServices services;
    // KV Map with name / class
    private Map<String, XMLService> nameIndex = new HashMap<String, XMLService>();
    // KV Map with input / operation
    private Multimap<String, XMLService> inputIndex = HashMultimap.create();
    // KV Map with output / operation
    private Multimap<String, XMLService> outputIndex = HashMultimap.create();
    // WSC'08 does not define the concept of service operations. Each service
    // has inputs and outputs. This service provider generates one operation
    // per service, using the same name as the WSC'08 service name but adding
    // the suffix Operation to the end.
    public static final String operationSuffix = "Operation";

    public WSCXMLServideProvider(InputStream serviceStream) {
        this.services = JAXB.unmarshal(serviceStream, XMLServices.class);
        index();
    }

    public WSCXMLServideProvider(File xmlServices) {
        this.services = JAXB.unmarshal(xmlServices, XMLServices.class);
        index();
    }

    private final void index() {
        for (XMLService s : services.getServices()) {
            nameIndex.put(s.getName(), s);
            for(XMLInstance input : s.getInputs().getInstances()){
                inputIndex.put(input.getID(), s);
            }
            for(XMLInstance output : s.getOutputs().getInstances()){
                outputIndex.put(output.getID(), s);
            }
        }
    }

    public Service<String> getService(String serviceID) {
        XMLService xmlService = nameIndex.get(serviceID);
        if (xmlService != null) {
            // Convert and return
            ResourceService<String> s = new ResourceService<String>(xmlService.getName());
            // Set inputs
            Set<String> inputs = new HashSet<String>();
            for (XMLInstance input : xmlService.getInputs().getInstances()) {
                inputs.add(input.getName());
            }
            // Set outputs
            Set<String> outputs = new HashSet<String>();
            for (XMLInstance output : xmlService.getOutputs().getInstances()) {
                outputs.add(output.getName());
            }
            // Create the signature
            SignatureIO<String> signature = new SignatureIO<String>(inputs, outputs);
            // Default operation.
            ResourceOperation<String> op = new ResourceOperation<String>(serviceID + operationSuffix, signature);
            s.addOperation(op);
            return s;
        }
        return null;
    }

    /**
     * Retrieve the operation model of the operation with the provided id.
     * NOTE: The operation id of service serv7211679 is serv7211679Operation
     * (adding {@link #operationSuffix} to the end of the service name.
     *
     * @param operationID unique ID of the operation used to locate the resource.
     * @return operation model.
     */
    @Override
    public Operation<String> getOperation(String operationID) {
        Service<String> service = getService(operationID.substring(0, operationID.indexOf(operationSuffix)));
        if (service != null){
            // Return the single operation
            return service.getOperations().iterator().next();
        }
        return null;
    }

    @Override
    public Iterable<Operation<String>> getOperationsWithInput(String inputInstance) {
        final Collection<XMLService> services = inputIndex.get(inputInstance);
        return new Iterable<Operation<String>>() {
            @Override
            public Iterator<Operation<String>> iterator() {
                return Iterators.transform(services.iterator(), new Function<XMLService, Operation<String>>() {
                    @Override
                    public Operation<String> apply(XMLService service) {
                        return getOperation(service.getName()+operationSuffix);
                    }
                });
            }
        };
    }

    @Override
    public Iterable<Operation<String>> getOperationsWithOutput(String outputInstance) {
        final Collection<XMLService> services = outputIndex.get(outputInstance);
        return new Iterable<Operation<String>>() {
            @Override
            public Iterator<Operation<String>> iterator() {
                return Iterators.transform(services.iterator(), new Function<XMLService, Operation<String>>() {
                    @Override
                    public Operation<String> apply(XMLService service) {
                        return getOperation(service.getName()+operationSuffix);
                    }
                });
            }
        };
    }

    @Override
    public Iterable<Operation<String>> getOperations() {
        final Set<String> operations = this.listOperations();
        return new Iterable<Operation<String>>() {
            @Override
            public Iterator<Operation<String>> iterator() {
                return new Iterator<Operation<String>>() {
                    Iterator<String> ids = operations.iterator();
                    @Override
                    public boolean hasNext() {
                        return ids.hasNext();
                    }

                    @Override
                    public Operation<String> next() {
                        return getOperation(ids.next());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override
    public Iterable<Service<String>> getServices() {
        final Set<String> services = this.listServices();
        return new Iterable<Service<String>>() {
            @Override
            public Iterator<Service<String>> iterator() {
                return new Iterator<Service<String>>() {
                    Iterator<String> ids = services.iterator();
                    @Override
                    public boolean hasNext() {
                        return ids.hasNext();
                    }

                    @Override
                    public Service<String> next() {
                        return getService(ids.next());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override
    public Set<String> listOperations() {
        Set<String> ops = new HashSet<String>();
        for(String service : listServices()){
            ops.add(service+operationSuffix);
        }
        return ops;
    }

    @Override
    public Set<String> listServices() {
        return Collections.unmodifiableSet(nameIndex.keySet());
    }
}
