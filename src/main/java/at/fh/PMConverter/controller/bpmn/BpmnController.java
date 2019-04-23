/*
package at.fh.PMConverter.controller.bpmn;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.ModelInstance;

public class BpmnController {

    private BpmnModelInstance modelInstance = null;
    private Definitions definitions = null;

    public ModelInstance buildModel() {

        modelInstance = Bpmn.createEmptyModel();
        definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://camunda.org/examples");
        modelInstance.setDefinitions(definitions);

        Process process = modelInstance.newInstance(Process.class);
        process.setId("process");
        definitions.addChildElement(process);
    }
}
*/