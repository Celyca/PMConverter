package at.fh.PMConverter.controller.xpdl.element;

import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.Pool;

import java.util.Random;

public class XPDLPool {

    public static Pool generatePool(Package pkg) {
        Pool pool = (Pool) pkg.getPools().generateNewElement();
        pool.setBoundaryVisible(true);

        Random rand = new Random();
        int value = rand.nextInt(5000);
        pool.setId(String.valueOf(value));

        return pool;
    }
}
