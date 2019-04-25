package at.fh.PMConverter.controller.xpdl;

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

    static void validateXpdlInstance(Package xpdlInstance) throws Exception {

        XMLInterface xmli = new XMLInterfaceImpl();
        StandardPackageValidator pv = new StandardPackageValidator();
        pv.init(new Properties(), xmli);
        List verrs = new ArrayList();
        pv.validateElement(xpdlInstance, verrs, true);
        if (verrs.size() > 0) {
            if (pv.hasErrors(verrs)) {
                System.out.println("...XPDL is NOT valid");
                System.out.println("...errors=" + verrs);
                saveXpdlInstance(xpdlInstance);
            } else {
                System.out.println("...XPDL is valid ");
                System.out.println("...There are following warnings:" + verrs);
                saveXpdlInstance(xpdlInstance);
            }
        } else {
            System.out.println("...XPDL is valid");
            saveXpdlInstance(xpdlInstance);
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
}
