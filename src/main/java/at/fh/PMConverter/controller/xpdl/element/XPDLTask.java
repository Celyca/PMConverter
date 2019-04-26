package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.NodeGraphicsInfo;
import org.enhydra.jxpdl.elements.TransitionRestriction;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLTask {

    public static List<Activity> generateTasks(WorkflowProcess wfp, Process bpmnProcess, ModelElementType taskType) {

        Collection<Task> taskElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(taskType);

        elements.forEach(x -> taskElements.add((Task)x));

        List<Activity> xpdlTasks = new ArrayList<>();
        taskElements.forEach(x -> {
            Activity task = (Activity) wfp.getActivities().generateNewElement();

            task.setId(x.getId());
            task.setName(x.getName());
            task.getActivityTypes().setImplementation();
            task.getActivityTypes().getImplementation().getImplementationTypes().setNo();

            //---------------------------------------------------------------------------------------------

            TransitionRestriction tre = Transitions.checkTransition(x, task);
            if (tre != null) {
                task.getTransitionRestrictions().add(tre);
            }

            //---------------------------------------------------------------------------------------------

            NodeGraphicsInfo ngi = GraphicsInfo.generateGraphics(x, task);
            task.getNodeGraphicsInfos().add(ngi);

            xpdlTasks.add(task);
        });

        return xpdlTasks;
    }



}
