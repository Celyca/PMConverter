package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.NodeGraphicsInfo;
import org.enhydra.jxpdl.elements.TransitionRestriction;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLStart {

    public static List<Activity> generateStart(WorkflowProcess wfp, Process bpmnProcess, ModelElementType taskType) {
        Collection<StartEvent> startElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(taskType);

        elements.forEach(x -> startElements.add((StartEvent)x));

        List<Activity> xpdlStart = new ArrayList<>();
        startElements.forEach(x -> {
            Activity start = (Activity) wfp.getActivities().generateNewElement();

            start.setId(x.getId());
            start.setName(x.getName());
            start.getActivityTypes().setEvent();
            start.getActivityTypes().getEvent().getEventTypes().setStartEvent();

            //---------------------------------------------------------------------------------------------

            TransitionRestriction tre = Transitions.checkTransition(x, start);
            if (tre != null) {
                start.getTransitionRestrictions().add(tre);
            }

            //---------------------------------------------------------------------------------------------

            NodeGraphicsInfo ngi = GraphicsInfo.generateGraphics(x, start);
            start.getNodeGraphicsInfos().add(ngi);

            xpdlStart.add(start);
        });

        return xpdlStart;
    }
}
