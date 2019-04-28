package at.fh.PMConverter.controller.xpdl;

import at.fh.PMConverter.controller.bpmn.BPMNController;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.StandardPackageValidator;
import org.enhydra.jxpdl.XMLInterface;
import org.enhydra.jxpdl.XMLInterfaceImpl;
import org.enhydra.jxpdl.XMLUtil;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.PackageHeader;
import org.enhydra.jxpdl.elements.RedefinableHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XPDLHandler {

    //---------------------------------------------------------------------------------------------

    private Package xpdlFileInstance = null;
    private String log = "";
    private static XPDLHandler theInstance;


    public static XPDLHandler getInstance() {
        if (theInstance == null)
            theInstance = new XPDLHandler();

        return theInstance;
    }

    //---------------------------------------------------------------------------------------------

    public void convertXpdlInstance(File file) {
        try {
            XMLInterfaceImpl xmli = new XMLInterfaceImpl();
            String inputFile = file.getCanonicalPath();
            xpdlFileInstance = xmli.openPackage(inputFile, true);
            xpdlFileInstance.setReadOnly(true);
            log = log + "\nRead instance \nID: " + xpdlFileInstance.getId() + "\nName: " + xpdlFileInstance.getName();
        } catch (Exception e) {
            log = log + "\nFailed to read instance";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    //---------------------------------------------------------------------------------------------

    public void validateXpdlInstance(Package xpdlInstance) {
        log = "";
        try {
            XMLInterface xmli = new XMLInterfaceImpl();
            StandardPackageValidator pv = new StandardPackageValidator();
            pv.init(new Properties(), xmli);
            List verrs = new ArrayList();
            pv.validateElement(xpdlInstance, verrs, true);
            if (verrs.size() > 0) {
                if (pv.hasErrors(verrs)) {
                    log = log + ("XPDL is NOT valid");
                    log = log + ("Errors=" + verrs);
                } else {
                    log = log + ("...XPDL is valid ");
                    log = log + ("...There are following warnings:" + verrs);
                }
            } else {
                log = log + ("...XPDL is valid");
            }
        } catch (Exception e) {
            log = log + "\nSomething went wrong";
            log = log + "\nError: " + e.toString() + "\nMessage: " + e.getMessage();
        }
    }

    static void saveXpdlInstance(Package xpdlInstance) throws Exception {
        File file = File.createTempFile("TestFile", ".xpdl");
        XMLUtil.writeToFile(file.getAbsolutePath(), xpdlInstance);


        System.out.println(file.getAbsolutePath());
    }

    static Package newXpdlInstance(BpmnModelInstance bpmnInstance) {
        Package xpdlInstance = new Package();
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

        return xpdlInstance;
    }

    public Package getXpdlFileInstance() {
        return xpdlFileInstance;
    }

    public String getLog() {
        return log;
    }
}
