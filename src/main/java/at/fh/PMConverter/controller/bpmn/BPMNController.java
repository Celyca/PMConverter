package at.fh.PMConverter.controller.bpmn;

import at.fh.PMConverter.controller.bpmn.element.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.WorkflowProcess;
import org.enhydra.jxpdl.elements.WorkflowProcesses;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNController {

    private Package xpdlInstance = null;
    private BpmnModelInstance bpmnInstance = null;
    private static BPMNController theInstance;

    public static BPMNController getInstance() {
        if (theInstance == null)
            theInstance = new BPMNController();

        return theInstance;
    }

    public void convertToXpdl(Package xpdlModelInstance) throws Exception {
        xpdlInstance = xpdlModelInstance;

        //New BPMN Instance
        bpmnInstance = BPMNHandler.newBpmnInstance(xpdlInstance);

        Collection<WorkflowProcess> wfpElements = getWFPElements();

        wfpElements.forEach(this::convertWFP);

        //---------------------------------------------------------------------------------------------

        Collection<Participant> pools = BPMNPool.generatePool(bpmnInstance, xpdlInstance);
        if (pools != null) {
            Collaboration collaboration = bpmnInstance.newInstance(Collaboration.class);
            pools.forEach(collaboration::addChildElement);
            bpmnInstance.getDefinitions().addChildElement(collaboration);
        }

        //---------------------------------------------------------------------------------------------

        BPMNLane.generateLaneSet();

        //---------------------------------------------------------------------------------------------

        BPMNHandler.validateBpmnInstance(bpmnInstance);
    }

    // Get all <WorkflowProcess> elements
    private Collection<WorkflowProcess> getWFPElements() {

        WorkflowProcesses wfps = xpdlInstance.getWorkflowProcesses();

        Collection<WorkflowProcess> wfpElements = new ArrayList<>();
        wfps.toElements().forEach(x -> wfpElements.add((WorkflowProcess) x));

        return wfpElements;
    }

    private void convertWFP(WorkflowProcess wfp){

        Diagram diagramInstance = new Diagram();

        // Generate WFP
        Process process = BPMNProcess.generateProcess(bpmnInstance, wfp);

        BpmnDiagram diagram = bpmnInstance.newInstance(BpmnDiagram.class);
        BpmnPlane plane = bpmnInstance.newInstance(BpmnPlane.class);
        diagram.addChildElement(plane);

        // Generate Activities
        Collection<FlowNode> activities = BPMNActivity.generateActivity(wfp, process, diagramInstance);
        activities.forEach(process::addChildElement);

        diagramInstance.getShapes().forEach(plane::addChildElement);

        bpmnInstance.getDefinitions().addChildElement(process);
        bpmnInstance.getDefinitions().addChildElement(diagram);

        // Generate Transition
        Collection<SequenceFlow> transitions = BPMNTransition.generateTransition(wfp, process, diagramInstance);
        transitions.forEach(process::addChildElement);
        diagramInstance.getEdges().forEach(plane::addChildElement);

    }

    public Package getXpdlInstance() {
        return xpdlInstance;
    }

    public BpmnModelInstance getBpmnInstance() {
        return bpmnInstance;
    }
}