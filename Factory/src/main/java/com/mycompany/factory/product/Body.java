package com.mycompany.factory.product;

import lombok.Getter;

@Getter
public class Body implements Product {

    private final int id;

    public Body(int id) {
        this.id = id;
    }
}
