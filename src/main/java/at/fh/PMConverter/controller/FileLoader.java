package at.fh.PMConverter.controller;

import at.fh.PMConverter.controller.bpmn.BpmnHandler;
import javafx.stage.FileChooser;

import java.io.File;

public class FileLoader {

    private static FileLoader theInstance;

    public static FileLoader getInstance() {
        if (theInstance == null)
            theInstance = new FileLoader();

        return theInstance;
    }

    public void loadFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .bpmn or .xpdl file");
        fileChooser.setInitialFileName("PMConverter.bpmn");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BPMN 2.0, XPDL", "*.bpmn", "*.xpdl"),
                new FileChooser.ExtensionFilter("BPMN 2.0", "*.bpmn"),
                new FileChooser.ExtensionFilter("XPDL", "*.xpdl")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String filePath = file.getAbsolutePath();

            String ext =  filePath.substring(filePath.length() - 4);
            if (ext.toLowerCase().equals("bpmn")) {
                BpmnHandler.getInstance().loadBpmnInstance(file);
            } else {
                if (ext.toLowerCase().equals("xpdl"))
                System.out.println("XPDL");
            }
            System.out.println(filePath);
        }
    }
}