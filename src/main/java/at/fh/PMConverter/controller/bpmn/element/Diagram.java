package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnLabelStyle;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;
import org.enhydra.jxpdl.elements.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Diagram {

    private Collection<BpmnShape> shapes = new ArrayList<>();;
    private Collection<BpmnEdge> edges = new ArrayList<>();;

    public static BpmnShape generateShape(Activity activity, FlowNode node){

        NodeGraphicsInfos ngis = activity.getNodeGraphicsInfos();

        ArrayList<NodeGraphicsInfo> ngiElements = new ArrayList<>();
        ngis.toElements().forEach(x -> ngiElements.add((NodeGraphicsInfo) x));

        if (!ngiElements.isEmpty() && node == null) {
            BpmnShape shape = BPMNController.getInstance().getBpmnInstance().newInstance(BpmnShape.class);
            Bounds shapeBounds = BPMNController.getInstance().getBpmnInstance().newInstance(Bounds.class);

            shape.setBpmnElement(node);

            shapeBounds.setHeight(ngiElements.get(0).getHeight());
            shapeBounds.setWidth(ngiElements.get(0).getWidth());
            shapeBounds.setX(Double.valueOf(ngiElements.get(0).getCoordinates().getXCoordinate()));
            shapeBounds.setX(Double.valueOf(ngiElements.get(0).getCoordinates().getYCoordinate()));

            shape.addChildElement(shapeBounds);

            Random rand = new Random();
            int value = rand.nextInt(1000000000);

            return shape;
        }
        return null;
    }

    public static BpmnEdge generateEdge(Transition transition, SequenceFlow flow) {

        ConnectorGraphicsInfos cgis = transition.getConnectorGraphicsInfos();

        ArrayList<ConnectorGraphicsInfo> cgiElements = new ArrayList<>();
        cgis.toElements().forEach(x -> cgiElements.add((ConnectorGraphicsInfo) x));

        if (!cgiElements.isEmpty() && flow == null) {
            BpmnEdge edge = BPMNController.getInstance().getBpmnInstance().newInstance(BpmnEdge.class);

            edge.setBpmnElement(flow);

            Coordinatess coordinates = cgiElements.get(0).getCoordinatess();

            ArrayList<Coordinates> coordinateElements = new ArrayList<>();
            cgis.toElements().forEach(x -> coordinateElements.add((Coordinates) x));

            coordinateElements.forEach(y -> {
                Waypoint waypoint = BPMNController.getInstance().getBpmnInstance().newInstance(Waypoint.class);
                waypoint.setX(Double.valueOf(y.getXCoordinate()));
                waypoint.setY(Double.valueOf(y.getYCoordinate()));
                edge.addChildElement(waypoint);
            });

            return edge;
        }
        return null;
    }

    public Collection<BpmnShape> getShapes() {
        return shapes;
    }

    public Collection<BpmnEdge> getEdges() {
        return edges;
    }
}
