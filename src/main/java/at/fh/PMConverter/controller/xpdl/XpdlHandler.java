package at.fh.PMConverter.controller.xpdl;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.XMLUtil;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.PackageHeader;
import org.enhydra.jxpdl.elements.RedefinableHeader;

import java.io.File;

public class XpdlHandler {

    private Package xpdlInstance;
    private static XpdlHandler theInstance;

    public static XpdlHandler getInstance() {
        if (theInstance == null)
            theInstance = new XpdlHandler();

        return theInstance;
    }

    public void saveXpdlInstance() throws Exception {
        File file = File.createTempFile("TestFile", ".xpdl");
        XMLUtil.writeToFile(file.getAbsolutePath(), xpdlInstance);

        System.out.println(file.getAbsolutePath());
    }

    public void newXpdlInstance(BpmnModelInstance bpmnInstance) throws Exception {
        xpdlInstance = new Package();
        xpdlInstance.setId(bpmnInstance.getDefinitions().getId());
        xpdlInstance.setName(bpmnInstance.getDefinitions().getName());

        PackageHeader ph = xpdlInstance.getPackageHeader();
        ph.setXPDLVersion("2.1");
        ph.setVendor("(c) Together Teamsolutions Co., Ltd.");
        ph.setCreated(XMLUtil.getCurrentDateAndTime());
        ph.setDescription("Created with PMConverter");

        RedefinableHeader refHead = xpdlInstance.getRedefinableHeader();
        refHead.setAuthor("PMConverter");
        refHead.setVersion("1.0");
        refHead.setCountrykey("CO");

        saveXpdlInstance();
    }

    public Package getXpdlInstance() {
        return xpdlInstance;
    }
}
