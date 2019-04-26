package at.fh.PMConverter.view;

import at.fh.PMConverter.controller.FileLoader;
import at.fh.PMConverter.controller.FxController;

import java.io.File;

public class MainSceneFxController implements FxController {

    public void loadFile() throws Exception {
        FileLoader.getInstance().loadFile();
    }
}