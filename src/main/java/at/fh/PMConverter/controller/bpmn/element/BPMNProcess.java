package at.fh.PMConverter.controller.bpmn.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.ModelInstance;
import org.enhydra.jxpdl.elements.WorkflowProcess;

public class BPMNProcess {

    public static Process generateProcess(ModelInstance bpmnInstance,WorkflowProcess wfp) {
        Process process = bpmnInstance.newInstance(Process.class);
        process.setId("_" + wfp.getId());
        process.setName(wfp.getName());
        process.setExecutable(false);

        return process;
    }
}
