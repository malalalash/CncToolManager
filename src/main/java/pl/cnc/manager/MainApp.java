package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.service.FileService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    static void main() {

        List<Tool> magazine = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.next();

            switch (input) {
                case "1":
                    System.out.println("1");
                    break;
                case "2":
                    System.out.println("2");
                    break;
                case "3":
                    System.out.println("3");
                    break;
                case "0": {
                    running = false;
                    break;
                }
                default:
                    System.out.println("Brak");
            }
        } scanner.close();
    }
}
