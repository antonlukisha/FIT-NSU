package com.mycompany.factory.supplier;

public abstract class BasicSupplier implements Supplier {

    protected int speed;

    @Override
    public void changeSpeed(int newSpeed) {
        this.speed = newSpeed;
    }
}
