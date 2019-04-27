package at.fh.PMConverter.controller.bpmn;

import at.fh.PMConverter.controller.xpdl.XPDLController;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.impl.util.IoUtil;
import org.enhydra.jxpdl.XMLUtil;
import org.enhydra.jxpdl.elements.Package;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

public class BPMNHandler {

    //---------------------------------------------------------------------------------------------

    private BpmnModelInstance modelInstance = null;
    private static BPMNHandler theInstance;


    public static BPMNHandler getInstance() {
        if (theInstance == null)
            theInstance = new BPMNHandler();

        return theInstance;
    }

    //---------------------------------------------------------------------------------------------

    public void convertBpmnInstance(File file) throws Exception {
        //try {
            modelInstance = Bpmn.readModelFromFile(file);
            XPDLController.getInstance().convertToXpdl(modelInstance);

        //} catch (Exception e) {
            //System.out.println("Something went wrong.");
            //System.out.println(e.toString());
        //}
    }

    //---------------------------------------------------------------------------------------------

    static void validateBpmnInstance(BpmnModelInstance bpmnInstance) throws Exception {
        Bpmn.validateModel(bpmnInstance);
        saveBpmnInstance(bpmnInstance);
    }

    static void saveBpmnInstance(BpmnModelInstance bpmnInstance) throws Exception {
        File file = File.createTempFile("bpmn-model-api-", ".bpmn");

        //FileOutputStream os = new FileOutputStream(file);
        //IoUtil.writeDocumentToOutputStream(bpmnInstance.getDocument(), os);

        Bpmn.writeModelToFile(file, bpmnInstance);
        System.out.println(file.getAbsolutePath());
    }
    
    static BpmnModelInstance newBpmnInstance(Package xpdlInstance) {

        BpmnModelInstance modelInstance = Bpmn.createEmptyModel();

        Definitions definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://camunda.org/_" + xpdlInstance.getId());
        definitions.setId("_" + xpdlInstance.getId());
        definitions.setName(xpdlInstance.getName());

        modelInstance.setDefinitions(definitions);

        return modelInstance;
    }
}
