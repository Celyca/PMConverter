package at.fh.PMConverter.view;

import at.fh.PMConverter.controller.FileLoader;
import at.fh.PMConverter.controller.FxController;
import at.fh.PMConverter.controller.bpmn.BPMNHandler;
import at.fh.PMConverter.controller.xpdl.XPDLHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.elements.Package;

import java.io.File;
import java.io.IOException;

public class SaveSceneFxController implements FxController {

    private BpmnModelInstance bpmnInstance;
    private Package xpdlInstance;
    private Boolean bpmn;

    //---------------------------------------------------------------------------------------------

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

    //---------------------------------------------------------------------------------------------

    public void save(ActionEvent event) {
        File file = FileLoader.getInstance().saveFile(bpmn);
        if (bpmn) {
            BPMNHandler.getInstance().saveBpmnInstance(bpmnInstance, file);
            appendLog(BPMNHandler.getInstance().getLog());
        } else {
            XPDLHandler.getInstance().saveXpdlInstance(xpdlInstance, file);
            appendLog(XPDLHandler.getInstance().getLog());
        }
    }

    public void validate(ActionEvent event) {
        if (bpmn) {
            BPMNHandler.getInstance().validateBpmnInstance(bpmnInstance);
            appendLog(BPMNHandler.getInstance().getLog());
            validate.setDisable(true);
        } else {
            XPDLHandler.getInstance().validateXpdlInstance(xpdlInstance);
            appendLog(XPDLHandler.getInstance().getLog());
            validate.setDisable(true);
        }
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fh/PMConverter/fxml/MainScene.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        MainSceneFxController controller = fxmlLoader.<MainSceneFxController>getController();

        Scene viewScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }

    //---------------------------------------------------------------------------------------------

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
