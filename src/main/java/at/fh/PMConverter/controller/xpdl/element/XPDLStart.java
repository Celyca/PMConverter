package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Start {

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

            xpdlStart.add(start);
        });

        return xpdlStart;
    }

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

            xpdlEnd.add(end);
        });

        return xpdlEnd;
    }
}
