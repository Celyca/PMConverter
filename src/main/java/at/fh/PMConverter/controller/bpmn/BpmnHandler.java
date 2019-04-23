package at.fh.PMConverter.controller.bpmn;

import at.fh.PMConverter.controller.xpdl.XpdlHandler;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;

public class BpmnHandler {

    private BpmnModelInstance modelInstance = null;
    private static BpmnHandler theInstance;


    public static BpmnHandler getInstance() {
        if (theInstance == null)
            theInstance = new BpmnHandler();

        return theInstance;
    }

    public void loadBpmnInstance(File file) {
        try {
            modelInstance = Bpmn.readModelFromFile(file);
            XpdlHandler.getInstance().newXpdlInstance(modelInstance);
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }

        UserTask asd = new UserTask();

    }
}
