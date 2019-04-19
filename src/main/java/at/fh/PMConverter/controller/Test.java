package at.fh.PMConverter.controller;

import javafx.event.ActionEvent;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.io.File;

public class Test {
    private BpmnModelInstance modelInstance = null;
    private Definitions definitions = null;

    public void ModelBuilder() {
        modelInstance = Bpmn.createEmptyModel();
        definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://camunda.org/examples");
        modelInstance.setDefinitions(definitions);

        Process process = modelInstance.newInstance(Process.class);
        process.setId("process");
        definitions.addChildElement(process);
    }

    public void create() throws Exception {

        ModelBuilder();

        Process process = createElement(definitions, "process-with-one-task", Process.class);

        // create start event, user task and end event
        StartEvent startEvent = createElement(process, "start", StartEvent.class);
        UserTask task1 = createElement(process, "task1", UserTask.class);
        task1.setName("User Task");
        EndEvent endEvent = createElement(process, "end", EndEvent.class);

        // create the connections between the elements
        createSequenceFlow(process, startEvent, task1);
        createSequenceFlow(process, task1, endEvent);

        // validate and write model to file
        Bpmn.validateModel(modelInstance);
        File file = File.createTempFile("bpmn-model-api-", ".bpmn");
        Bpmn.writeModelToFile(file, modelInstance);

        System.out.println(file.getAbsolutePath());
    }

    protected <T extends BpmnModelElementInstance> T createElement(BpmnModelElementInstance parentElement, String id,
                                                                   Class<T> elementClass) {
        T element = modelInstance.newInstance(elementClass);
        element.setAttributeValue("id", id, true);
        parentElement.addChildElement(element);
        return element;
    }

    public SequenceFlow createSequenceFlow(Process process, FlowNode from, FlowNode to) {
        String identifier = from.getId() + "-" + to.getId();
        SequenceFlow sequenceFlow = createElement(process, identifier, SequenceFlow.class);
        process.addChildElement(sequenceFlow);
        sequenceFlow.setSource(from);
        from.getOutgoing().add(sequenceFlow);
        sequenceFlow.setTarget(to);
        to.getIncoming().add(sequenceFlow);
        return sequenceFlow;
    }

}
