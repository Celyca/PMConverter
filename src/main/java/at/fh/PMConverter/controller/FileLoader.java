package main.java.at.fh.PMConverter.controller;

import javafx.stage.FileChooser;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.ModelInstance;

import java.io.File;

public class FileLoader {

    public void readFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load sync file");
        fileChooser.setInitialFileName("BattleShip.ktv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BPMN 2.0", "*.bpmn"),
                new FileChooser.ExtensionFilter("XPDL", "*.xpdl")
        );
        File file = fileChooser.showOpenDialog(null);
        String filePath = file.getAbsolutePath();

    }
}
