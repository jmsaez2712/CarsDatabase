package com.saezgarcia.carsdatabase.model.entity;

import androidx.room.Embedded;

public class CarType {

    @Embedded
    public Car car;

    @Embedded(prefix = "type_")
    public Type type;
}
