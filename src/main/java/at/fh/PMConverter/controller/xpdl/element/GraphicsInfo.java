package at.fh.PMConverter.controller.xpdl.element;

import at.fh.PMConverter.controller.xpdl.XPDLController;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.enhydra.jxpdl.elements.*;

import java.util.Collection;

public class GraphicsInfo {

    public static NodeGraphicsInfo generateGraphics(FlowNode flowNode, Activity activity) {

        Collection<ModelElementInstance> boundsElements = flowNode.getDiagramElement().getChildElementsByType(XPDLController.getInstance().boundsType);

        NodeGraphicsInfo ngi = (NodeGraphicsInfo) activity.getNodeGraphicsInfos().generateNewElement();

        if (boundsElements.iterator().hasNext()) {
            Bounds bounds = (Bounds) boundsElements.iterator().next();

            ngi.setWidth(bounds.getWidth().intValue());
            ngi.setHeight(bounds.getHeight().intValue());
            ngi.getCoordinates().setXCoordinate(String.valueOf(bounds.getX().intValue()));
            ngi.getCoordinates().setYCoordinate(String.valueOf(bounds.getY().intValue()));
        }

        return ngi;
    }

    //---------------------------------------------------------------------------------------------

    public static ConnectorGraphicsInfo generateSequenceFlowGraphics(SequenceFlow sequenceFlow, Transition transition) {

        BpmnEdge edge = sequenceFlow.getDiagramElement();

        ConnectorGraphicsInfo cgi = (ConnectorGraphicsInfo) transition.getConnectorGraphicsInfos().generateNewElement();

        if (edge.getWaypoints().iterator().hasNext()) {

            edge.getWaypoints().forEach(x -> {
                Coordinates coordinates = (Coordinates) cgi.getCoordinatess().generateNewElement();
                coordinates.setXCoordinate(String.valueOf(x.getX().intValue()));
                coordinates.setYCoordinate(String.valueOf(x.getY().intValue()));
                cgi.getCoordinatess().add(coordinates);
            });
        }

        return cgi;
    }
}
