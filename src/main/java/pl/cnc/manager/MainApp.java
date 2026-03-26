package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.service.FileService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    static void main() {

        List<Tool> magazine = new ArrayList<>();
        Tool tool = new Drill("D", "Drill 3", 3.0);
        magazine.add(tool);
        System.out.println(tool.toString());
        magazine.add(tool);
        System.out.println(tool.getDiameter());
        System.out.println(tool.getQuantity());
        tool.setQuantity(2);
        System.out.println();
        FileService fileService = new FileService();
        fileService.saveToFile(magazine);
        for (Tool tool1 : magazine) {
            System.out.println(tool1);
        }

    }
}
