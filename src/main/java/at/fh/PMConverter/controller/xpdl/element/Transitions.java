package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.TransitionRef;
import org.enhydra.jxpdl.elements.TransitionRestriction;

import java.util.Collection;

public class Transitions {

    public static TransitionRestriction checkTransition(FlowNode flowNode, Activity activity) {

        Collection<SequenceFlow> incoming = flowNode.getIncoming();
        Collection<SequenceFlow> outgoing = flowNode.getOutgoing();

        if (outgoing.size() > 1 || incoming.size() > 1) {

            TransitionRestriction tre = (TransitionRestriction) activity.getTransitionRestrictions().generateNewElement();

            if (outgoing.size() > 1) {

                outgoing.forEach(x -> {
                    TransitionRef tref = (TransitionRef) tre.getSplit().getTransitionRefs().generateNewElement();
                    tref.setId(x.getId());
                    tre.getSplit().getTransitionRefs().add(tref);
                });

                tre.getSplit().setTypeExclusive();
                return tre;
            } else if (incoming.size() > 1) {
                tre.getJoin().setTypeParallel();
                return tre;
            }
        }
        return null;
    }
}
