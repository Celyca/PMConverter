package at.fh.PMConverter.controller.bpmn.element;

import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.xml.ModelInstance;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.Pool;
import org.enhydra.jxpdl.elements.Pools;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNPool {

    public static Collection<Participant> generatePool(ModelInstance bpmnInstance, Package xpdlInstance) {

        Pools pools = xpdlInstance.getPools();

        Collection<Pool> poolElements = new ArrayList<>();
        pools.toElements().forEach(x -> poolElements.add((Pool) x));


        Collection<Participant> poolBpmnElements = new ArrayList<>();
        poolElements.forEach(y -> {
            Participant participant = bpmnInstance.newInstance(Participant.class);
            participant.setName(y.getName());
            participant.setId("_" + y.getId());
            participant.setProcess(bpmnInstance.getModelElementById("_" + y.getProcess()));

            poolBpmnElements.add(participant);
        });

        return poolBpmnElements;
    }
}
