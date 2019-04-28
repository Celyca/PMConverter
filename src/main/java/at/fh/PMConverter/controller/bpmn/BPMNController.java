package at.fh.PMConverter.controller.bpmn;

import at.fh.PMConverter.model.Triplet;
import at.fh.PMConverter.controller.bpmn.element.*;
import javafx.util.Pair;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.WorkflowProcess;
import org.enhydra.jxpdl.elements.WorkflowProcesses;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNController {

    private Package xpdlInstance = null;
    private BpmnModelInstance bpmnInstance = null;
    private Collection<Triplet> shapes = new ArrayList<>();
    private Collection<Triplet> edges = new ArrayList<>();
    private static BPMNController theInstance;
    private String log = "";
    private Double progress;

    public static BPMNController getInstance() {
        if (theInstance == null)
            theInstance = new BPMNController();

        return theInstance;
    }

    public void convertToBpmn(Package xpdlModelInstance) throws Exception {
        xpdlInstance = xpdlModelInstance;
        Collection<WorkflowProcess> wfpElements = null;
        try {
            log = log + "\nCreate a new XPDL instance";
            progress = 0.2;

            //New BPMN Instance
            bpmnInstance = BPMNHandler.newBpmnInstance(xpdlInstance);

            log = log + "\nCollect all process elements";
            progress = 0.5;
            wfpElements = getWFPElements();

        } catch(Exception e) {
            progress = 0.5;
            log = log + "\n\nFailed to create or load process elements";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }

        if (wfpElements != null) {
            wfpElements.forEach(this::convertWFP);
        }

        //---------------------------------------------------------------------------------------------

        try {
            log = log + "\nGenerate pools";
            Collection<Participant> pools = BPMNPool.generatePool(bpmnInstance, xpdlInstance);
            if (pools != null) {
                Collaboration collaboration = bpmnInstance.newInstance(Collaboration.class);
                pools.forEach(collaboration::addChildElement);
                bpmnInstance.getDefinitions().addChildElement(collaboration);
            }
        } catch (Exception e) {
            log = log + "\n\nFailed to convert pools";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }


        //---------------------------------------------------------------------------------------------

        try {
            log = log + "\nAdd shapes";
            shapes.forEach(x -> {
                if (x.getPlane() != null && x.getShape() != null && x.getNode() != null) {
                    BpmnShape shape = x.getShape();
                    shape.setBpmnElement(x.getNode());
                    x.getPlane().addChildElement(shape);
                }
            });

            log = log + "\nAdd Edges";
            edges.forEach(x -> {
                if (x.getPlane() != null && x.getEdge() != null && x.getFlow() != null) {
                    BpmnEdge edge = x.getEdge();
                    SequenceFlow flow = x.getFlow();
                    edge.setBpmnElement(flow);
                    if (edge.getWaypoints().isEmpty()) {
                        Waypoint waypoint1 = BPMNController.getInstance().getBpmnInstance().newInstance(Waypoint.class);
                        Waypoint waypoint2 = BPMNController.getInstance().getBpmnInstance().newInstance(Waypoint.class);

                        Collection<ModelElementInstance> boundsSource = flow.getSource().getDiagramElement().getChildElementsByType(bpmnInstance.getModel().getType(Bounds.class));
                        Collection<ModelElementInstance> boundsTarget = flow.getTarget().getDiagramElement().getChildElementsByType(bpmnInstance.getModel().getType(Bounds.class));

                        ArrayList<Bounds> bounds1Elements = new ArrayList<>();
                        boundsSource.forEach(y -> bounds1Elements.add((Bounds) y));

                        ArrayList<Bounds> bounds2Elements = new ArrayList<>();
                        boundsTarget.forEach(z -> bounds2Elements.add((Bounds) z));

                        waypoint1.setX(bounds1Elements.get(0).getX());
                        waypoint1.setY(bounds1Elements.get(0).getY());
                        waypoint2.setX(bounds2Elements.get(0).getX());
                        waypoint2.setY(bounds2Elements.get(0).getY());

                        edge.addChildElement(waypoint1);
                        edge.addChildElement(waypoint2);
                    }


                    x.getPlane().addChildElement(edge);
                    //FlowNode node = bpmnInstance.getModelElementById(x.getNode().getId());
                    //edge.setBpmnElement(node);
                }
            });

            log = log + "\nGenerate lanes";
            BPMNLane.generateLaneSet();

            log = log + "\nDone!";
            progress = 1.0;

        } catch (Exception e) {
            log = log + "\n\nFailed to add edges and shapes";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
        //---------------------------------------------------------------------------------------------
    }

    // Get all <WorkflowProcess> elements
    private Collection<WorkflowProcess> getWFPElements() {

        WorkflowProcesses wfps = xpdlInstance.getWorkflowProcesses();

        Collection<WorkflowProcess> wfpElements = new ArrayList<>();
        wfps.toElements().forEach(x -> wfpElements.add((WorkflowProcess) x));

        return wfpElements;
    }

    private void convertWFP(WorkflowProcess wfp){
        try {
            // Generate WFP
            Process process = BPMNProcess.generateProcess(bpmnInstance, wfp);
            log = log + "\nConvert process\nID: " + wfp.getId() + "\nName: " + wfp.getName();

            BpmnDiagram diagram = bpmnInstance.newInstance(BpmnDiagram.class);
            BpmnPlane plane = bpmnInstance.newInstance(BpmnPlane.class);

            // Generate Activities
            log = log + "\nGenerate activities";
            Collection<Pair<FlowNode, BpmnShape>> activities = BPMNActivity.generateActivity(wfp, process);
            activities.forEach(x -> {
                process.addChildElement(x.getKey());

                shapes.add(new Triplet(x.getKey(), x.getValue(), plane));
            });

            bpmnInstance.getDefinitions().addChildElement(process);


            // Generate Transition
            log = log + "\nGenerate transitions";
            Collection<Pair<SequenceFlow, BpmnEdge>> transitions = BPMNTransition.generateTransition(wfp, process);
            transitions.forEach(x -> {
                process.addChildElement(x.getKey());

                edges.add(new Triplet(x.getKey(), x.getValue(), plane));
            });

            diagram.addChildElement(plane);
            bpmnInstance.getDefinitions().addChildElement(diagram);
        } catch (Exception e) {
            log = log + "\n\nFailed to convert process " + wfp.getId();
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    public Package getXpdlInstance() {
        return xpdlInstance;
    }

    public BpmnModelInstance getBpmnInstance() {
        return bpmnInstance;
    }

    public String getLog() {
        return log;
    }

    public Double getProgress() {
        return progress;
    }
}