package com.elevator.service;

import com.elevator.model.Direction;
import com.elevator.model.Elevator;
import com.elevator.model.Request;
import com.elevator.model.Floor;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Controls the operation of multiple elevators in the system.
 * Handles external requests, internal requests, and coordinates elevator movements.
 */
public class ElevatorController {
    private List<Elevator> elevators;
    private PriorityQueue<Request> requestQueue;
    private List<Floor> floors;
    private static final int TOTAL_FLOORS = 20;
    private static final int TOTAL_ELEVATORS = 6;
    private static final int PASSENGER_WEIGHT = 65; // Average passenger weight

    public ElevatorController() {
        elevators = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEVATORS; i++) {
            elevators.add(new Elevator(i));
        }

        floors = new ArrayList<>();
        for (int i = 0; i < TOTAL_FLOORS; i++) {
            floors.add(new Floor(i));
        }

        requestQueue = new PriorityQueue<>(Comparator.comparingLong(Request::getTimestamp));
    }

    /**
     * Handles an external request for an elevator at a specific floor.
     * @param floor The floor where the request is made
     * @param direction The direction the passenger wants to go
     */
    public void requestElevator(int floor, Direction direction) {
        Request request = new Request(floor, direction, System.currentTimeMillis());
        requestQueue.add(request);
        assignRequestToElevator(request);
    }

    /**
     * Handles an internal request from a passenger inside an elevator.
     * @param elevatorId The ID of the elevator
     * @param targetFloor The floor the passenger wants to go to
     */
    public void requestFloor(int elevatorId, int targetFloor) {
        if (elevatorId >= 0 && elevatorId < TOTAL_ELEVATORS) {
            Elevator elevator = elevators.get(elevatorId);
            if (elevator.isDoorOpen() && elevator.canAccommodate(PASSENGER_WEIGHT)) {
                elevator.addWeight(PASSENGER_WEIGHT);
                elevator.addDestination(targetFloor);
            }
        }
    }

    /**
     * Assigns a request to the most suitable elevator using the SCAN algorithm.
     * @param request The request to assign
     */
    private void assignRequestToElevator(Request request) {
        Elevator bestElevator = findBestElevator(request);
        if (bestElevator != null) {
            bestElevator.addDestination(request.getFloor());
        }
    }

    /**
     * Finds the best elevator to handle a request based on current positions and directions.
     * @param request The request to handle
     * @return The most suitable elevator
     */
    private Elevator findBestElevator(Request request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = calculateDistance(elevator, request);
            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }

    /**
     * Calculates the distance between an elevator and a request, considering direction.
     * @param elevator The elevator to check
     * @param request The request to check
     * @return The calculated distance
     */
    private int calculateDistance(Elevator elevator, Request request) {
        int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
        
        if (elevator.getDirection() == Direction.IDLE) {
            return distance;
        }

        if ((elevator.getDirection() == Direction.UP && request.getFloor() >= elevator.getCurrentFloor()) ||
            (elevator.getDirection() == Direction.DOWN && request.getFloor() <= elevator.getCurrentFloor())) {
            return distance;
        }

        // If elevator is moving in opposite direction, calculate the distance to the farthest destination
        List<Integer> destinations = elevator.getDestinationFloors();
        int farthestDestination = destinations.get(destinations.size() - 1);
        return distance + 2 * Math.abs(farthestDestination - request.getFloor());
    }

    /**
     * Moves all elevators and handles door operations.
     * Called periodically to update the system state.
     */
    public void moveElevators() {
        for (Elevator elevator : elevators) {
            elevator.move();
            
            // Handle passenger exit at destination
            if (elevator.isProcessingRequest() && !elevator.getDestinationFloors().isEmpty() &&
                elevator.getCurrentFloor() == elevator.getDestinationFloors().get(0)) {
                elevator.removeWeight(PASSENGER_WEIGHT);
                elevator.completeRequest();
            }
        }
    }

    /**
     * Activates emergency stop for all elevators.
     */
    public void emergencyStop() {
        for (Elevator elevator : elevators) {
            elevator.setEmergency(true);
            elevator.setDirection(Direction.IDLE);
            elevator.getDestinationFloors().clear();
            elevator.setDoorOpen(false);
        }
    }

    /**
     * Handles the press of the up button on a floor.
     * @param floorNumber The floor number where the button is pressed
     */
    public void pressUpButton(int floorNumber) {
        if (floorNumber >= 0 && floorNumber < TOTAL_FLOORS) {
            floors.get(floorNumber).pressUpButton();
            requestElevator(floorNumber, Direction.UP);
        }
    }

    /**
     * Handles the press of the down button on a floor.
     * @param floorNumber The floor number where the button is pressed
     */
    public void pressDownButton(int floorNumber) {
        if (floorNumber >= 0 && floorNumber < TOTAL_FLOORS) {
            floors.get(floorNumber).pressDownButton();
            requestElevator(floorNumber, Direction.DOWN);
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}