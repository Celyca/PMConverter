package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.Lane;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.Pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Pools {

    public static Pool generatePool(Package pkg) {
        Pool pool = (Pool) pkg.getPools().generateNewElement();
        pool.setBoundaryVisible(true);

        Random rand = new Random();
        int value = rand.nextInt(5000);
        pool.setId(String.valueOf(value));

        return pool;
    }

    public static List<Lane> generateLanes(Pool pool, Process bpmnProcess) {
        Collection<org.camunda.bpm.model.bpmn.instance.Lane> laneElements = new ArrayList<>();
        bpmnProcess.getLaneSets().forEach(x -> laneElements.addAll(x.getLanes()));

        List<Lane> xpdlLanes = new ArrayList<>();
        laneElements.forEach(x -> {
            Lane lane = (Lane) pool.getLanes().generateNewElement();
            lane.setId(x.getId());
            lane.setName(x.getName());
            xpdlLanes.add(lane);
        });

        return xpdlLanes;
    }
}
