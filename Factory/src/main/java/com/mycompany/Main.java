package com.mycompany;

import com.mycompany.factory.FactoryController;
import com.mycompany.view.Controller;
import com.mycompany.view.FactoryFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FactoryController factoryController = new FactoryController();
        Controller controller = new Controller();
        SwingUtilities.invokeLater(() -> new FactoryFrame(controller, factoryController));
    }
}

