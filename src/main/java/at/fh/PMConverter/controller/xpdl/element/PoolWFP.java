package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.Pool;

public class PoolWFP {

    public static Pool generatePool(Package pkg, BpmnModelInstance bpmnInstance) {
        Pool p = (Pool) pkg.getPools().generateNewElement();
        p.setId(id);
        p.setName(name);
        p.setProcess(id);
    }

    public static Pool generateWFP(Package pkg, BpmnModelInstance bpmnInstance) {
        Pool p = (Pool) pkg.getPools().generateNewElement();
        p.setId(id);
        p.setName(name);
        p.setProcess(id);
    }
}
