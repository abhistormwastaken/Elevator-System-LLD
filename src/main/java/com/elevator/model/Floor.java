package com.elevator.model;

import lombok.Getter;

@Getter
public class Floor {
    private final int floorNumber;
    private final Button upButton;
    private final Button downButton;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upButton = new Button();
        this.downButton = new Button();
    }

    public void pressUpButton() {
        upButton.press();
    }

    public void pressDownButton() {
        downButton.press();
    }
}