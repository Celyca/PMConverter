package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
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

public class XPDLXOR {

    public static List<Activity> generateXOR(WorkflowProcess wfp, Process bpmnProcess, ModelElementType elementType) {
        Collection<ExclusiveGateway> xorElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(elementType);

        elements.forEach(x -> xorElements.add((ExclusiveGateway)x));

        List<Activity> xpdlXOR = new ArrayList<>();
        xorElements.forEach(x -> {
            Activity xor = (Activity) wfp.getActivities().generateNewElement();

            xor.setId(x.getId());
            xor.setName(x.getName());
            xor.getActivityTypes().setRoute();
            xor.getActivityTypes().getRoute().setGatewayTypeExclusive();

            //---------------------------------------------------------------------------------------------

            TransitionRestriction tre = Transitions.checkTransition(x, xor);
            if (tre != null) {
                xor.getTransitionRestrictions().add(tre);
            }


            //---------------------------------------------------------------------------------------------

            NodeGraphicsInfo ngi = GraphicsInfo.generateGraphics(x, xor);
            xor.getNodeGraphicsInfos().add(ngi);

            xpdlXOR.add(xor);
        });

        return xpdlXOR;
    }
}
