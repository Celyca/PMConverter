package at.fh.PMConverter.view;

import at.fh.PMConverter.controller.FxController;
import at.fh.PMConverter.controller.bpmn.BPMNHandler;
import at.fh.PMConverter.controller.xpdl.XPDLHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.elements.Package;

public class SaveSceneController implements FxController {
    private BpmnModelInstance bpmnInstance;
    private Package xpdlInstance;
    private Boolean bpmn;
    private Boolean alreadyValidate = false;

    @FXML
    private Button save;

    @FXML
    private Text label;

    @FXML
    private Button validate;

    @FXML
    private TextArea log;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (bpmn) {
                label.setText("Save BPMN file");
            }else {
                label.setText("Save XPDL file");
            }
        });
    }

    public void save(ActionEvent event) {
        if (bpmn) {
            if(alreadyValidate) {

            } else {

            }
        } else {

            if(alreadyValidate) {

            } else {

            }
        }

        //appendLog("\nValidate the instance");
        //setProgress(0.9);

        //XPDLHandler.validateXpdlInstance(xpdlInstance);







        //BPMNHandler.validateBpmnInstance(bpmnInstance);
    }

    public void validate(ActionEvent event) {
        if (!bpmn) {
            BPMNHandler.getInstance().validateBpmnInstance(bpmnInstance);
            appendLog(BPMNHandler.getInstance().getLog());
        } else {
            XPDLHandler.getInstance().validateXpdlInstance(xpdlInstance);
            appendLog(XPDLHandler.getInstance().getLog());
        }
    }

    public void setBpmnInstance(BpmnModelInstance bpmnInstance) {
        this.bpmnInstance = bpmnInstance;
    }

    public void setXpdlInstance(Package xpdlInstance) {
        this.xpdlInstance = xpdlInstance;
    }

    public void setBpmn(Boolean bpmn) {
        this.bpmn = bpmn;
    }

    public void appendLog(String string) {
        log.appendText(string + "\n");
    }
}
