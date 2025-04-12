package com.elevator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {
    private int floor;
    private Direction direction;
    private long timestamp;
} 