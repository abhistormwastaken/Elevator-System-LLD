package com.elevator.display;

import com.elevator.model.Elevator;
import java.util.List;

public class Display {
    private List<Elevator> elevators;
    private static final int TOTAL_FLOORS = 20;

    public Display(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void showFloorDisplay(int floor) {
        System.out.println("\nFloor " + floor + " Display:");
        System.out.println("------------------------");
        for (Elevator elevator : elevators) {
            if (elevator.getCurrentFloor() == floor) {
                System.out.println("Elevator " + elevator.getId() + 
                                 " - Status: " + elevator.getDirection() +
                                 " - Door: " + (elevator.isDoorOpen() ? "Open" : "Closed"));
            }
        }
    }

    public void showElevatorDisplay(int elevatorId) {
        Elevator elevator = elevators.get(elevatorId);
        System.out.println("\nElevator " + elevatorId + " Display:");
        System.out.println("------------------------");
        System.out.println("Current Floor: " + elevator.getCurrentFloor());
        System.out.println("Direction: " + elevator.getDirection());
        System.out.println("Door Status: " + (elevator.isDoorOpen() ? "Open" : "Closed"));
        System.out.println("Emergency Status: " + (elevator.isEmergency() ? "Active" : "Inactive"));
        System.out.println("Current Weight: " + elevator.getCurrentWeight() + " kg");
    }

    public void showAllDisplays() {
        for (int floor = 0; floor < TOTAL_FLOORS; floor++) {
            showFloorDisplay(floor);
        }
        
        for (int i = 0; i < elevators.size(); i++) {
            showElevatorDisplay(i);
        }
    }
} 