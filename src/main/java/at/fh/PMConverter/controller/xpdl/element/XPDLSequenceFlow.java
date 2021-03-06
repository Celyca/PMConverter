package at.fh.PMConverter.controller.xpdl.element;

import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.enhydra.jxpdl.elements.ConnectorGraphicsInfo;
import org.enhydra.jxpdl.elements.Transition;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPDLSequenceFlow {

    public static List<Transition> generateSf(WorkflowProcess wfp, Process bpmnProcess, ModelElementType elementType) {
        Collection<SequenceFlow> sfElements = new ArrayList<>();

        Collection<ModelElementInstance> elements = bpmnProcess.getChildElementsByType(elementType);

        elements.forEach(x -> sfElements.add((SequenceFlow)x));

        List<Transition> xpdlSF = new ArrayList<>();
        sfElements.forEach(x -> {
            Transition sf = (Transition) wfp.getTransitions().generateNewElement();

            sf.setId(x.getId());
            sf.setFrom(x.getSource().getId());
            sf.setTo(x.getTarget().getId());
            sf.setName(x.getName());
            sf.getCondition().setTypeNONE();

            //---------------------------------------------------------------------------------------------

            ConnectorGraphicsInfo cgi = GraphicsInfo.generateSequenceFlowGraphics(x, sf);
            sf.getConnectorGraphicsInfos().add(cgi);

            xpdlSF.add(sf);
        });

        return xpdlSF;
    }
}
