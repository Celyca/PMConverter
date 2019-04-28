package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.NodeGraphicsInfo;
import org.enhydra.jxpdl.elements.TransitionRestriction;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLAND {

    public static List<Activity> generateAND(WorkflowProcess wfp, Process bpmnProcess, ModelElementType elementType) {
        Collection<ParallelGateway> andElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(elementType);

        elements.forEach(x -> andElements.add((ParallelGateway)x));

        List<Activity> xpdlAND = new ArrayList<>();
        andElements.forEach(x -> {
            Activity and = (Activity) wfp.getActivities().generateNewElement();

            and.setId(x.getId());
            and.setName(x.getName());
            and.getActivityTypes().setRoute();
            and.getActivityTypes().getRoute().setGatewayTypeParallel();

            //---------------------------------------------------------------------------------------------

            TransitionRestriction tre = Transitions.checkTransition(x, and);
            if (tre != null) {
                and.getTransitionRestrictions().add(tre);
            }

            //---------------------------------------------------------------------------------------------

            NodeGraphicsInfo ngi = GraphicsInfo.generateGraphics(x, and);
            and.getNodeGraphicsInfos().add(ngi);

            xpdlAND.add(and);
        });

        return xpdlAND;
    }
}
