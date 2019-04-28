package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;
import org.enhydra.jxpdl.elements.*;

import java.util.ArrayList;

public class Diagram {

    public static BpmnShape generateShape(Activity activity, FlowNode node){

        NodeGraphicsInfos ngis = activity.getNodeGraphicsInfos();

        ArrayList<NodeGraphicsInfo> ngiElements = new ArrayList<>();
        ngis.toElements().forEach(x -> ngiElements.add((NodeGraphicsInfo) x));

        if (!ngiElements.isEmpty() && node != null) {
            BpmnShape shape = BPMNController.getInstance().getBpmnInstance().newInstance(BpmnShape.class);
            Bounds shapeBounds = BPMNController.getInstance().getBpmnInstance().newInstance(Bounds.class);

            shapeBounds.setHeight(ngiElements.get(0).getHeight());
            shapeBounds.setWidth(ngiElements.get(0).getWidth());
            shapeBounds.setX(Double.valueOf(ngiElements.get(0).getCoordinates().getXCoordinate()));
            shapeBounds.setY(Double.valueOf(ngiElements.get(0).getCoordinates().getYCoordinate()));

            shape.addChildElement(shapeBounds);

            return shape;
        }
        return null;
    }

    public static BpmnEdge generateEdge(Transition transition, SequenceFlow flow) {

        ConnectorGraphicsInfos cgis = transition.getConnectorGraphicsInfos();

        ArrayList<ConnectorGraphicsInfo> cgiElements = new ArrayList<>();
        cgis.toElements().forEach(x -> cgiElements.add((ConnectorGraphicsInfo) x));

        if (!cgiElements.isEmpty() && flow != null) {
            BpmnEdge edge = BPMNController.getInstance().getBpmnInstance().newInstance(BpmnEdge.class);

            Coordinatess coordinates = cgiElements.get(0).getCoordinatess();

            ArrayList<Coordinates> coordinateElements = new ArrayList<>();
            coordinates.toElements().forEach(x -> coordinateElements.add((Coordinates) x));

            if (!coordinateElements.isEmpty())
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
}
