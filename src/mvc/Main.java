package mvc;

import mvc.controller.Controller;
import mvc.view.GraphicUI;

public class Main {
    public static void main(String[] args) {
        Controller c = new Controller();
        GraphicUI gui = new GraphicUI(c);
        c.setView(gui);
    }
}


