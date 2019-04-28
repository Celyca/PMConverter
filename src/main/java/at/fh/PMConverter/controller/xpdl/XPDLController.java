package at.fh.PMConverter.controller.xpdl;

import at.fh.PMConverter.controller.xpdl.element.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.*;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.Lane;
import org.enhydra.jxpdl.elements.Package;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLController {
    
    private Package xpdlInstance = null;
    private BpmnModelInstance bpmnInstance = null;
    public ModelElementType boundsType = null;
    private String log = "";
    private Double progress;

    private static XPDLController theInstance;

    public static XPDLController getInstance() {
        if (theInstance == null)
            theInstance = new XPDLController();

        return theInstance;
    }

    public void convertToXpdl(BpmnModelInstance bpmnModelInstance) {
        bpmnInstance = bpmnModelInstance;
        Collection<Process> processElements = null;
        try {
            // New XPDL Instance
            xpdlInstance = XPDLHandler.newXpdlInstance(bpmnInstance);
            boundsType = bpmnInstance.getModel().getType(Bounds.class);

            log = log + "\nCreate a new XPDL instance";
            progress = 0.2;

            processElements = getProcessElements();
            log = log + "\nCollect all process elements";
            progress = 0.5;
        } catch (Exception e){
            progress = 0.5;
            log = log + "\n\nFailed to create or load process elements";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
        if (processElements != null) {
            processElements.forEach(this::convertProcess);
        }

        log = log + "\nDone!";
        progress = 1.0;
    }

    // Get all <Process> elements
    private Collection<Process> getProcessElements() {
        ModelElementType processType = bpmnInstance.getModel().getType(Process.class);
        Collection<ModelElementInstance> elements = bpmnInstance.getModelElementsByType(processType);

        Collection<Process> processElements = new ArrayList<>();
        elements.forEach(x -> processElements.add((Process)x));

        return processElements;
    }

    private void convertProcess(Process process){
        try {
            // Generate WFP
            WorkflowProcess wfp = XPDLWorkflowProcess.generateWFP(xpdlInstance, process);
            log = log + "\nConvert process\nID: " + wfp.getId() + "\nName: " + wfp.getName();
            //---------------------------------------------------------------------------------------------

            // Generate Pool
            log = log + "\nGenerate pool";
            Pool pool = XPDLPool.generatePool(xpdlInstance);


            // Generate Lanes
            log = log + "\nGenerate lanes";
            List<Lane> lanes = XPDLLane.generateLanes(pool, process);
            pool.getLanes().addAll(lanes);
            pool.setProcess(wfp.getId());

            //---------------------------------------------------------------------------------------------

            //Generate Task
            log = log + "\nGenerate tasks";
            ModelElementType taskType = bpmnInstance.getModel().getType(Task.class);
            List<Activity> tasks = XPDLTask.generateTasks(wfp, process, taskType);

            //Generate UserTask
            ModelElementType userTaskType = bpmnInstance.getModel().getType(UserTask.class);
            List<Activity> userTasks = XPDLTask.generateTasks(wfp, process, userTaskType);

            //Generate ServiceTask
            ModelElementType serviceTaskType = bpmnInstance.getModel().getType(ServiceTask.class);
            List<Activity> serviceTasks = XPDLTask.generateTasks(wfp, process, serviceTaskType);

            //Add to WFP
            wfp.getActivities().addAll(tasks);
            wfp.getActivities().addAll(userTasks);
            wfp.getActivities().addAll(serviceTasks);

            //---------------------------------------------------------------------------------------------
            log = log + "\nGenerate AND-Gateway";
            //Generate AND
            ModelElementType andType = bpmnInstance.getModel().getType(ParallelGateway.class);
            List<Activity> andGateways = XPDLAND.generateStart(wfp, process, andType);

            log = log + "\nGenerate XOR-Gateway";
            //Generate XOR
            ModelElementType xorType = bpmnInstance.getModel().getType(ExclusiveGateway.class);
            List<Activity> xorGateways = XPDLXOR.generateStart(wfp, process, xorType);

            //Add to WFP
            wfp.getActivities().addAll(andGateways);
            wfp.getActivities().addAll(xorGateways);

            //---------------------------------------------------------------------------------------------

            //Generate StartEvent
            log = log + "\nGenerate start events";
            ModelElementType startType = bpmnInstance.getModel().getType(StartEvent.class);
            List<Activity> startEvents = XPDLStart.generateStart(wfp, process, startType);

            //Generate EndEvents
            log = log + "\nGenerate end events";
            ModelElementType endType = bpmnInstance.getModel().getType(EndEvent.class);
            List<Activity> endEvents = XPDLEnd.generateEnd(wfp, process, endType);

            //Add to WFP
            wfp.getActivities().addAll(startEvents);
            wfp.getActivities().addAll(endEvents);

            //---------------------------------------------------------------------------------------------

            //Generate SequenceFlow
            log = log + "\nGenerate sequence flows";
            ModelElementType sequenceFlowType = bpmnInstance.getModel().getType(SequenceFlow.class);
            List<Transition> sequenceFlows = XPDLSequenceFlow.generateSf(wfp, process, sequenceFlowType);

            //Add to WFP
            wfp.getTransitions().addAll(sequenceFlows);


            xpdlInstance.getWorkflowProcesses().add(wfp);
            xpdlInstance.getPools().add(pool);
            log = log + "\nProcess: " + wfp.getId() + "done";
        } catch (Exception e) {
            log = log + "\n\nFailed to convert process " + process.getId();
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    public String getLog() {
        return log;
    }

    public Double getProgress() {
        return progress;
    }

    public Package getXpdlInstance() {
        return xpdlInstance;
    }
}
