package at.fh.PMConverter.model;

import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;

public class Triplet {
    private FlowNode node;
    private SequenceFlow flow;
    private BpmnShape shape;
    private BpmnEdge edge;
    private BpmnPlane plane;

    public Triplet(FlowNode node, BpmnShape shape, BpmnPlane plane) {
        this.node = node;
        this.shape = shape;
        this.plane = plane;
    }

    public Triplet(SequenceFlow flow, BpmnEdge edge, BpmnPlane plane) {
        this.flow = flow;
        this.edge = edge;
        this.plane = plane;
    }

    public FlowNode getNode() {
        return node;
    }

    public void setNode(FlowNode node) {
        this.node = node;
    }

    public SequenceFlow getFlow() {
        return flow;
    }

    public void setFlow(SequenceFlow flow) {
        this.flow = flow;
    }

    public BpmnShape getShape() {
        return shape;
    }

    public void setShape(BpmnShape shape) {
        this.shape = shape;
    }

    public BpmnEdge getEdge() {
        return edge;
    }

    public void setEdge(BpmnEdge edge) {
        this.edge = edge;
    }

    public BpmnPlane getPlane() {
        return plane;
    }

    public void setPlane(BpmnPlane plane) {
        this.plane = plane;
    }
}
