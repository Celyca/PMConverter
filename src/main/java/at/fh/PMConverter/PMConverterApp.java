package at.fh.PMConverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PMConverterApp extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/at/fh/PMConverter/fxml/MainScene.fxml"));
        stage.setTitle("PMConverter");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 686, 502));
        stage.show();

    }
}