package at.fh.PMConverter.controller.bpmn.element;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import at.fh.PMConverter.controller.xpdl.XPDLController;
import org.camunda.bpm.model.bpmn.instance.LaneSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.enhydra.jxpdl.elements.Lane;
import org.enhydra.jxpdl.elements.Pool;
import org.enhydra.jxpdl.elements.Pools;

import java.util.ArrayList;
import java.util.Collection;

public class BPMNLane {

    public static void generateLaneSet() {

        Pools pools = BPMNController.getInstance().getXpdlInstance().getPools();

        Collection<Pool> poolElements = new ArrayList<>();
        pools.toElements().forEach(x -> poolElements.add((Pool) x));

        poolElements.forEach(x -> {
            Process process = (Process) BPMNController.getInstance().getBpmnInstance().getModelElementById("_" + x.getProcess());
            if (process != null) {
                Collection<Lane> laneElements = new ArrayList<>();
                x.getLanes().toElements().forEach(y -> laneElements.add((Lane) y));

                LaneSet laneSet = BPMNController.getInstance().getBpmnInstance().newInstance(LaneSet.class);
                laneElements.forEach(z -> {
                    org.camunda.bpm.model.bpmn.instance.Lane lane = BPMNController.getInstance().getBpmnInstance().newInstance(org.camunda.bpm.model.bpmn.instance.Lane.class);
                    lane.setName(z.getName());
                    lane.setId("_" + z.getId());
                    laneSet.addChildElement(lane);
                });

                process.addChildElement(laneSet);
            }});
    }
}
