package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.service.FileService;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    static void main() {

        List<Tool> magazine = new ArrayList<>();
        Tool tool = new Drill("D", "Drill 3", 3);
        magazine.add(tool);
        System.out.println(tool.toString());

    }
}
