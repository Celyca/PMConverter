package at.fh.PMConverter.controller.bpmn;

import at.fh.PMConverter.controller.xpdl.XPDLController;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.File;

public class BpmnHandler {

    private BpmnModelInstance modelInstance = null;
    private static BpmnHandler theInstance;


    public static BpmnHandler getInstance() {
        if (theInstance == null)
            theInstance = new BpmnHandler();

        return theInstance;
    }

    public void convertBpmnInstance(File file) {
        try {
            modelInstance = Bpmn.readModelFromFile(file);
            XPDLController.getInstance().convertToXpdl(modelInstance);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e.toString());
        }
    }
}
