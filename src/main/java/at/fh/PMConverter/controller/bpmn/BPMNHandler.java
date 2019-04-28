package at.fh.PMConverter.controller.bpmn;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.xml.impl.util.IoUtil;
import org.enhydra.jxpdl.elements.Package;

import java.io.File;
import java.io.FileOutputStream;

public class BPMNHandler {

    //---------------------------------------------------------------------------------------------

    private BpmnModelInstance modelInstance = null;
    private String log = "";
    private static BPMNHandler theInstance;


    public static BPMNHandler getInstance() {
        if (theInstance == null)
            theInstance = new BPMNHandler();

        return theInstance;
    }

    //---------------------------------------------------------------------------------------------

    public void convertBpmnInstance(File file) {
        try {
            modelInstance = Bpmn.readModelFromFile(file);
            log = log + "\nRead instance \nID: " + modelInstance.getDefinitions().getId() + "\nName: " + modelInstance.getDefinitions().getName();
        } catch (Exception e) {
            log = log + "\nFailed to read instance";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    //---------------------------------------------------------------------------------------------

    public void validateBpmnInstance(BpmnModelInstance bpmnInstance) {
        log = "";
        try {
            Bpmn.validateModel(bpmnInstance);
            log = log + "BPMN is valid";
        } catch (Exception e) {
            log = log + "BPMN is not valid";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    public void saveBpmnInstance(BpmnModelInstance bpmnInstance, File file){
        log = "";
        try {
            FileOutputStream os = new FileOutputStream(file);
            IoUtil.writeDocumentToOutputStream(bpmnInstance.getDocument(), os);

            log = log + "\n\nFile saved \n Path: " + file.getAbsolutePath();
        } catch (Exception e) {
            log = log + "\n\nSomething went wrong!";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    //---------------------------------------------------------------------------------------------
    
    static BpmnModelInstance newBpmnInstance(Package xpdlInstance) {

        BpmnModelInstance modelInstance = Bpmn.createEmptyModel();

        Definitions definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://camunda.org/_" + xpdlInstance.getId());
        definitions.setId("_" + xpdlInstance.getId());
        definitions.setName(xpdlInstance.getName());

        modelInstance.setDefinitions(definitions);

        return modelInstance;
    }

    //---------------------------------------------------------------------------------------------

    public BpmnModelInstance getModelInstance() {
        return modelInstance;
    }

    public String getLog() {
        return log;
    }
}
