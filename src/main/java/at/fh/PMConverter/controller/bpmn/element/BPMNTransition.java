package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import javafx.util.Pair;
import org.camunda.bpm.model.bpmn.impl.instance.Incoming;
import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.enhydra.jxpdl.elements.Transition;
import org.enhydra.jxpdl.elements.Transitions;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNTransition {
    public static Collection<Pair<SequenceFlow, BpmnEdge>> generateTransition(WorkflowProcess wfp, Process process) {

        Transitions transition = wfp.getTransitions();

        Collection<Transition> transitionElements = new ArrayList<>();
        transition.toElements().forEach(x -> transitionElements.add((Transition) x));


        Collection<Pair<SequenceFlow, BpmnEdge>> bpmnTransitionElements = new ArrayList<>();
        transitionElements.forEach(x -> {
            SequenceFlow sequenceFlow = BPMNController.getInstance().getBpmnInstance().newInstance(SequenceFlow.class);

            sequenceFlow.setId("_" + x.getId());
            sequenceFlow.setName(x.getName());

            try {
                if (!x.getFrom().equals("")) {
                    FlowNode outgoingNode = BPMNController.getInstance().getBpmnInstance().getModelElementById("_" + x.getFrom());
                    Outgoing outgoing = BPMNController.getInstance().getBpmnInstance().newInstance(Outgoing.class);
                    outgoing.getDomElement().setTextContent(sequenceFlow.getId());
                    outgoingNode.addChildElement(outgoing);
                    sequenceFlow.setSource(outgoingNode);
                }

                if (!x.getTo().equals("")) {
                    FlowNode incomingNode = BPMNController.getInstance().getBpmnInstance().getModelElementById("_" + x.getTo());
                    Incoming incoming = BPMNController.getInstance().getBpmnInstance().newInstance(Incoming.class);
                    incoming.getDomElement().setTextContent(sequenceFlow.getId());
                    incomingNode.addChildElement(incoming);
                    sequenceFlow.setTarget(incomingNode);
                }
                Pair<SequenceFlow, BpmnEdge> pair = new Pair<>(sequenceFlow, Diagram.generateEdge(x, sequenceFlow));

                bpmnTransitionElements.add(pair);
            } catch (Exception e) {
            }
        });
        return bpmnTransitionElements;
    }
}
