package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import javafx.util.Pair;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.enhydra.jxpdl.elements.Activities;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNActivity {

    public static Collection<Pair<FlowNode, BpmnShape>> generateActivity(WorkflowProcess wfp, Process process) {

        Activities activities = wfp.getActivities();


        Collection<Activity> activityElements = new ArrayList<>();
        activities.toElements().forEach(x -> activityElements.add((Activity) x));


        Collection<Pair<FlowNode, BpmnShape>> bpmnActivityElements = new ArrayList<>();
        activityElements.forEach(x -> {
            int type = x.getActivityType();
            BpmnShape shape = null;

            //---------------------------------------------------------------------------------------------
            /*
                6: Start
                7: End
                2: Task
                0: Gateway
            */
            //---------------------------------------------------------------------------------------------
            FlowNode activity = null;
            System.out.println(type + " " + x.getName());
            if (type == 2 || type == 1) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(Task.class);
                FlowNode element = setValue(activity, x);
                shape = Diagram.generateShape(x, element);
                Pair<FlowNode, BpmnShape> pair = new Pair<FlowNode, BpmnShape>(element, shape);
                bpmnActivityElements.add(pair);
                //---------------------------------------------------------------------------------------------
            } else if (type == 6) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(StartEvent.class);
                FlowNode element = setValue(activity, x);
                shape = Diagram.generateShape(x, element);
                Pair<FlowNode, BpmnShape> pair = new Pair<FlowNode, BpmnShape>(element, shape);
                bpmnActivityElements.add(pair);
                //---------------------------------------------------------------------------------------------
            } else if (type == 7) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(EndEvent.class);
                FlowNode element = setValue(activity, x);
                shape = Diagram.generateShape(x, element);
                Pair<FlowNode, BpmnShape> pair = new Pair<FlowNode, BpmnShape>(element, shape);
                bpmnActivityElements.add(pair);
                //---------------------------------------------------------------------------------------------
            } else if (type == 0 && x.getActivityTypes().getRoute().getGatewayType().toLowerCase().equals("exclusive")) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(ExclusiveGateway.class);
                FlowNode element = setValue(activity, x);
                shape = Diagram.generateShape(x, element);
                Pair<FlowNode, BpmnShape> pair = new Pair<FlowNode, BpmnShape>(element, shape);
                bpmnActivityElements.add(pair);
                //---------------------------------------------------------------------------------------------
            } else if (type == 0 && x.getActivityTypes().getRoute().getGatewayType().toLowerCase().equals("parallel")) {
                activity = BPMNController.getInstance().getBpmnInstance().newInstance(ParallelGateway.class);
                FlowNode element = setValue(activity, x);
                shape = Diagram.generateShape(x, element);
                Pair<FlowNode, BpmnShape> pair = new Pair<FlowNode, BpmnShape>(element, shape);
                bpmnActivityElements.add(pair);
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
