package at.fh.PMConverter.view;

import at.fh.PMConverter.controller.FileLoader;
import at.fh.PMConverter.controller.FxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainSceneFxController implements FxController {

    private File file;

    @FXML
    private TextField path;

    @FXML
    private Button choose;

    @FXML
    private Button next;

    public void loadFile(ActionEvent event) throws Exception {
        file = FileLoader.getInstance().loadFile();
        if (file != null) {
            String filePath = file.getAbsolutePath();
            path.clear();
            path.setText(filePath);
            next.setDisable(false);
            next.setVisible(true);
        }
    }

    public void next(ActionEvent event) throws Exception{
        changeScreen(event);
    }

    public void changeScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fh/PMConverter/fxml/ConvertScene.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        ConvertSceneController controller = fxmlLoader.<ConvertSceneController>getController();
        controller.setFile(file);

        Scene viewScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }
}