package at.fh.PMConverter.controller;

import javafx.stage.FileChooser;

import java.io.File;

public class FileLoader {

    private static FileLoader theInstance;

    public static FileLoader getInstance() {
        if (theInstance == null)
            theInstance = new FileLoader();

        return theInstance;
    }

    //---------------------------------------------------------------------------------------------

    public File loadFile() throws Exception {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .bpmn or .xpdl file");
        fileChooser.setInitialFileName("PMConverter.bpmn");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BPMN 2.0, XPDL", "*.bpmn", "*.xpdl"),
                new FileChooser.ExtensionFilter("BPMN 2.0", "*.bpmn"),
                new FileChooser.ExtensionFilter("XPDL", "*.xpdl")
        );
        File file = fileChooser.showOpenDialog(null);
        return file;
    }

    public File saveFile(Boolean bpmn) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save instance");
        if (bpmn) {
            fileChooser.setInitialFileName("PMConverter.bpmn");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("BPMN 2.0", "*.bpmn")
            );
        } else {
            fileChooser.setInitialFileName("PMConverter.xpdl");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XPDL", "*.xpdl")
            );
        }
        File saveFile = fileChooser.showSaveDialog(null);
        return saveFile;
    }
}