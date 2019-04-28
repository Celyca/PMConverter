package at.fh.PMConverter.view;

import at.fh.PMConverter.controller.FxController;
import at.fh.PMConverter.controller.bpmn.BPMNController;
import at.fh.PMConverter.controller.bpmn.BPMNHandler;
import at.fh.PMConverter.controller.xpdl.XPDLController;
import at.fh.PMConverter.controller.xpdl.XPDLHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.enhydra.jxpdl.elements.Package;

import java.io.File;


public class ConvertSceneController implements FxController {

    private File file;
    private Boolean bpmn;
    private BpmnModelInstance bpmnInstance;
    private Package xpdlInstance;
    private static ConvertSceneController theInstance;

    @FXML
    private Button start;

    @FXML
    private Button next;

    @FXML
    private Text convertLabel;

    @FXML
    private TextArea log;

    @FXML
    private ProgressBar progress;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            String filePath = file.getAbsolutePath();

            String ext =  file.getAbsolutePath().substring(filePath.length() - 4);
                if (ext.toLowerCase().equals("bpmn")) {
                    bpmn = true;
                    convertLabel.setText("Convert BPMN → XPDL");
                    appendLog("BPMN file has been loaded");
                    start.setDisable(false);
            } else {
                if (ext.toLowerCase().equals("xpdl")) {
                    bpmn = false;
                    convertLabel.setText("Convert XPDL → BPMN");
                    appendLog("XPDL file has been loaded");
                    start.setDisable(false);
                }
            }
        });
    }

    public static ConvertSceneController getInstance() {
        if (theInstance == null)
            theInstance = new ConvertSceneController();

        return theInstance;
    }

    public void start(ActionEvent event) throws Exception{
        start.setDisable(true);
        if (bpmn) {
            BPMNHandler.getInstance().convertBpmnInstance(file);
            appendLog(BPMNHandler.getInstance().getLog());
            setProgress(0.1);

            XPDLController.getInstance().convertToXpdl(BPMNHandler.getInstance().getModelInstance());

            setProgress(XPDLController.getInstance().getProgress());
            appendLog(XPDLController.getInstance().getLog());

            xpdlInstance = XPDLController.getInstance().getXpdlInstance();

            next.setDisable(false);
        } else {
            XPDLHandler.getInstance().convertXpdlInstance(file);
            appendLog(XPDLHandler.getInstance().getLog());
            setProgress(0.1);

            BPMNController.getInstance().convertToBpmn(XPDLHandler.getInstance().getXpdlFileInstance());

            setProgress(BPMNController.getInstance().getProgress());
            appendLog(BPMNController.getInstance().getLog());

            bpmnInstance = BPMNController.getInstance().getBpmnInstance();

            next.setDisable(false);
        }
    }

    public void next(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fh/PMConverter/fxml/SaveScene.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        SaveSceneController controller = fxmlLoader.<SaveSceneController>getController();
        controller.setBpmnInstance(bpmnInstance);
        controller.setXpdlInstance(xpdlInstance);
        controller.setBpmn(bpmn);

        Scene viewScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void appendLog(String string) {
        log.appendText(string + "\n");
    }

    public void setProgress(Double value) {
        progress.setProgress(value);
    }

}
