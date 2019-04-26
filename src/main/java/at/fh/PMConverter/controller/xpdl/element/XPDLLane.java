package at.fh.PMConverter.controller.xpdl.element;

import at.fh.PMConverter.controller.xpdl.XPDLController;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.enhydra.jxpdl.elements.Lane;
import org.enhydra.jxpdl.elements.NodeGraphicsInfo;
import org.enhydra.jxpdl.elements.Pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLLane {

    public static List<Lane> generateLanes(Pool pool, Process bpmnProcess) {
        Collection<org.camunda.bpm.model.bpmn.instance.Lane> laneElements = new ArrayList<>();
        bpmnProcess.getLaneSets().forEach(x -> laneElements.addAll(x.getLanes()));

        List<Lane> xpdlLanes = new ArrayList<>();
        laneElements.forEach(x -> {
            Lane lane = (Lane) pool.getLanes().generateNewElement();
            lane.setId(x.getId());
            lane.setName(x.getName());

            //---------------------------------------------------------------------------------------------

            Collection<ModelElementInstance> boundsElements = x.getDiagramElement().getChildElementsByType(XPDLController.getInstance().boundsType);

            NodeGraphicsInfo ngi = (NodeGraphicsInfo) lane.getNodeGraphicsInfos().generateNewElement();

            if (boundsElements.iterator().hasNext()) {
                Bounds bounds = (Bounds) boundsElements.iterator().next();

                ngi.setWidth(bounds.getWidth().intValue());
                ngi.setHeight(bounds.getHeight().intValue());
                ngi.getCoordinates().setXCoordinate(String.valueOf(bounds.getWidth().intValue()));
                ngi.getCoordinates().setYCoordinate(String.valueOf(bounds.getWidth().intValue()));

                lane.getNodeGraphicsInfos().add(ngi);
            }

            xpdlLanes.add(lane);
        });

        return xpdlLanes;
    }
}
