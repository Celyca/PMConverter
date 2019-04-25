package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tasks {

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
            task.getActivityTypes().getImplementation().getImplementationTypes().setTask();

            xpdlTasks.add(task);
        });

        return xpdlTasks;
    }








/*
    Task act = (Task) bpmnInstance.getModelElementById("_1c347d0d-750b-4c09-980d-6877caae409b");

    ModelElementType boundsType = bpmnInstance.getModel().getType(Bounds.class);
    Collection<ModelElementInstance> asd = act.getDiagramElement().getChildElementsByType(boundsType);



    Collection<Bounds> elementsa = new ArrayList<Bounds>();
        asd.forEach(x -> {
        Bounds a = (Bounds) x;
        elementsa.add(a);
        System.out.println(a.getX() + " " + a.getY());
    });
    */
}
