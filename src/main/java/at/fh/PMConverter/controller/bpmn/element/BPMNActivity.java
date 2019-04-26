package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.enhydra.jxpdl.elements.Activities;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNActivity {

    public static Collection<FlowNode> generateActivity(WorkflowProcess wfp, Process process, Diagram diagram) {

        Activities activities = wfp.getActivities();

        Collection<Activity> activityElements = new ArrayList<>();
        activities.toElements().forEach(x -> activityElements.add((Activity) x));


        Collection<FlowNode> bpmnActivityElements = new ArrayList<>();
        activityElements.forEach(x -> {
            int type = x.getActivityType();

            //---------------------------------------------------------------------------------------------
            /*
                6: Start
                7: End
                2: Task
                0: Gateway
            */
            //---------------------------------------------------------------------------------------------
            FlowNode activity = null;
            if (type == 2) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(Task.class);
                diagram.getShapes().add(Diagram.generateShape(x, activity));
                bpmnActivityElements.add(setValue(activity, x));
                //---------------------------------------------------------------------------------------------
            } else if (type == 6) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(StartEvent.class);
                diagram.getShapes().add(Diagram.generateShape(x, activity));
                bpmnActivityElements.add(setValue(activity, x));
                //---------------------------------------------------------------------------------------------
            } else if (type == 7) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(EndEvent.class);
                diagram.getShapes().add(Diagram.generateShape(x, activity));
                bpmnActivityElements.add(setValue(activity, x));
                //---------------------------------------------------------------------------------------------
            } else if (type == 0 && x.getActivityTypes().getRoute().getGatewayType().toLowerCase().equals("exclusive")) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(ExclusiveGateway.class);
                diagram.getShapes().add(Diagram.generateShape(x, activity));
                bpmnActivityElements.add(setValue(activity, x));
                //---------------------------------------------------------------------------------------------
            } else if (type == 0 && x.getActivityTypes().getRoute().getGatewayType().toLowerCase().equals("parallel")) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(ParallelGateway.class);
                diagram.getShapes().add(Diagram.generateShape(x, activity));
                bpmnActivityElements.add(setValue(activity, x));

            }


        });
        return bpmnActivityElements;
    }

    private static FlowNode setValue(FlowNode node, Activity activity) {
        node.setId("_" + activity.getId());
        node.setName(activity.getName());
        return node;
    }
}
