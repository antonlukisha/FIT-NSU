package com.mycompany.view;

import lombok.Setter;
import com.mycompany.factory.FactoryController;
import com.mycompany.factory.pattern.observer.Changes;
import com.mycompany.factory.pattern.observer.Observable;
import com.mycompany.factory.pattern.observer.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ChangeListener, Observable, ActionListener {

    private JSlider accessoriesSlider;
    private JSlider motorsSlider;
    private JSlider bodySlider;
    private JSlider requestSlider;
    private JButton factoryControlButton;
    private int buttonState = 0;

    private Observer observer;

    @Setter
    private FactoryController factoryController;

    public void setSliders(JSlider accessoriesSlider, JSlider motorsSlider, JSlider bodySlider, JSlider requestSlider) {
        this.accessoriesSlider = accessoriesSlider;
        this.motorsSlider = motorsSlider;
        this.bodySlider = bodySlider;
        this.requestSlider = requestSlider;
    }

    public void setButton(JButton factoryControlButton) {
        this.factoryControlButton = factoryControlButton;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == accessoriesSlider) {
            notifyObservers(Changes.UPDATE_ACCESSORIES_SPEED);
            factoryController.setAccessorySpeed(accessoriesSlider.getValue());
        }
        else if (e.getSource() == motorsSlider) {
            notifyObservers(Changes.UPDATE_MOTORS_SPEED);
            factoryController.setMotorSpeed(motorsSlider.getValue());
        }
        else if (e.getSource() == bodySlider) {
            notifyObservers(Changes.UPDATE_BODY_SPEED);
            factoryController.setBodySpeed(bodySlider.getValue());
        }
        else if (e.getSource() == requestSlider) {
            notifyObservers(Changes.UPDATE_REQUEST_SPEED);
            factoryController.setDealerSpeed(requestSlider.getValue());
        }
    }

    @Override
    public void notifyObservers(Changes change) {
        observer.update(change);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == factoryControlButton) {
            switch (buttonState) {
                case 0 -> {
                    new Thread(() -> factoryController.startFactory()).start();
                    factoryControlButton.setText("Stop Factory");
                    buttonState = 1;
                }
                case 1 -> {
                    factoryController.shutdownFactory();
                    factoryControlButton.setText("Exit");
                    buttonState = 2;
                }
                case 2 -> System.exit(1);
            }
        }
    }
}
