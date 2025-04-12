package com.elevator.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an elevator in the system.
 * Each elevator has a unique ID, current floor, direction, capacity, and weight limits.
 * It maintains a list of destination floors and handles passenger weight tracking.
 */
@Getter
@Setter
public class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private int maxWeight;
    private List<Integer> destinationFloors;
    private boolean isEmergency;
    private boolean isDoorOpen;
    private int currentWeight;
    private boolean isProcessingRequest;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.maxWeight = 1000;
        this.destinationFloors = new ArrayList<>();
        this.isEmergency = false;
        this.isDoorOpen = false;
        this.currentWeight = 0;
        this.isProcessingRequest = false;
    }

    /**
     * Adds a destination floor to the elevator's queue.
     * The floors are maintained in SCAN order (up then down).
     * @param floor The floor number to add
     */
    public void addDestination(int floor) {
        if (!destinationFloors.contains(floor)) {
            // Insert floor in SCAN order
            int i = 0;
            if (direction == Direction.UP) {
                while (i < destinationFloors.size() && destinationFloors.get(i) < floor) {
                    i++;
                }
            } else if (direction == Direction.DOWN) {
                while (i < destinationFloors.size() && destinationFloors.get(i) > floor) {
                    i++;
                }
            }
            destinationFloors.add(i, floor);
        }
    }

    /**
     * Removes a destination floor from the queue.
     * @param floor The floor number to remove
     */
    public void removeDestination(int floor) {
        destinationFloors.remove(Integer.valueOf(floor));
    }

    /**
     * Checks if the elevator can accommodate additional weight.
     * @param weight The weight to check
     * @return true if the elevator can accommodate the weight
     */
    public boolean canAccommodate(int weight) {
        return currentWeight + weight <= maxWeight;
    }

    /**
     * Adds weight to the elevator when a passenger enters.
     * @param weight The weight of the passenger
     */
    public void addWeight(int weight) {
        currentWeight += weight;
    }

    /**
     * Removes weight from the elevator when a passenger exits.
     * @param weight The weight of the passenger
     */
    public void removeWeight(int weight) {
        currentWeight -= weight;
    }

    /**
     * Moves the elevator according to the SCAN algorithm.
     * Automatically handles door operations when reaching a destination.
     */
    public void move() {
        if (isEmergency) {
            return;
        }

        if (destinationFloors.isEmpty()) {
            direction = Direction.IDLE;
            isDoorOpen = false;
            return;
        }

        int nextFloor = destinationFloors.get(0);
        
        // If we've reached a destination floor
        if (currentFloor == nextFloor) {
            isDoorOpen = true;
            isProcessingRequest = true;
            return;
        }

        // Move towards the next floor
        if (currentFloor < nextFloor) {
            direction = Direction.UP;
            currentFloor++;
        } else {
            direction = Direction.DOWN;
            currentFloor--;
        }
        
        isDoorOpen = false;
        isProcessingRequest = false;
    }

    /**
     * Completes the processing of the current request.
     * Called after passengers have entered/exited at a floor.
     */
    public void completeRequest() {
        if (!destinationFloors.isEmpty() && currentFloor == destinationFloors.get(0)) {
            removeDestination(destinationFloors.get(0));
            isDoorOpen = false;
            isProcessingRequest = false;
        }
    }
} 