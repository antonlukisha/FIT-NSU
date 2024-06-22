package com.mycompany.factory.product;

import lombok.Getter;

@Getter
public class Accessory implements Product {

    private final int id;

    public Accessory(int id) {
        this.id = id;
    }
}
