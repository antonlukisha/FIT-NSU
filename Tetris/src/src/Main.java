package src;

import src.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        // Создание контроллера игрового меню и передача ему главного окна
        MenuController menuController = new MenuController();
        
        // Запуск главного окна и инициализация контроллеров
        menuController.startTetris();
    }
}
