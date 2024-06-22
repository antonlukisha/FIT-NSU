package com.mycompany.factory.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Auto implements Product {
    private final int bodyId;
    private final int motorId;
    private final int accessoryId;
    private final int id;
}
