package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
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

public class XPDLEnd {


    public static List<Activity> generateEnd(WorkflowProcess wfp, Process bpmnProcess, ModelElementType taskType) {
        Collection<EndEvent> endElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(taskType);

        elements.forEach(x -> endElements.add((EndEvent)x));

        List<Activity> xpdlEnd = new ArrayList<>();
        endElements.forEach(x -> {
            Activity end = (Activity) wfp.getActivities().generateNewElement();

            end.setId(x.getId());
            end.setName(x.getName());
            end.getActivityTypes().setEvent();
            end.getActivityTypes().getEvent().getEventTypes().setEndEvent();

            //---------------------------------------------------------------------------------------------

            TransitionRestriction tre = Transitions.checkTransition(x, end);
            if (tre != null) {
                end.getTransitionRestrictions().add(tre);
            }


            //---------------------------------------------------------------------------------------------

            NodeGraphicsInfo ngi = GraphicsInfo.generateGraphics(x, end);
            end.getNodeGraphicsInfos().add(ngi);

            xpdlEnd.add(end);
        });

        return xpdlEnd;
    }
}
