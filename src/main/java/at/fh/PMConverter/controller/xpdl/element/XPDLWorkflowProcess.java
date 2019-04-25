package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.enhydra.jxpdl.XMLUtil;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.WorkflowProcess;

public class XPDLWFP {

    public static WorkflowProcess generateWFP(Package pkg, Process bpmnProcess) {
        WorkflowProcess wfp = (WorkflowProcess) pkg.getWorkflowProcesses().generateNewElement();
        wfp.setId(bpmnProcess.getId());
        wfp.setName(bpmnProcess.getName());
        wfp.getProcessHeader().setCreated(XMLUtil.getCurrentDateAndTime());
        return wfp;
    }
}
