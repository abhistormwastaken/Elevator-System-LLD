package com.elevator.model;

import lombok.Getter;

@Getter
public class Button {
    private boolean isPressed;

    public Button() {
        this.isPressed = false;
    }

    public void press() {
        this.isPressed = true;
    }

    public void reset() {
        this.isPressed = false;
    }
} 