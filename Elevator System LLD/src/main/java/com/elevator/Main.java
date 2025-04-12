package com.elevator;

import com.elevator.service.ElevatorController;
import com.elevator.display.Display;
import java.util.Scanner;

/**
 * Main class for the Elevator System.
 * Provides a command-line interface for interacting with the elevator system.
 */
public class Main {
    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController();
        Display display = new Display(controller.getElevators());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nElevator System Menu:");
            System.out.println("1. Request Elevator");
            System.out.println("2. Show Status");
            System.out.println("3. Request Floor (Inside Elevator)");
            System.out.println("4. Emergency Stop");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter floor (0-19): ");
                    int requestFloor = scanner.nextInt();
                    System.out.print("Enter direction (1 for UP, 2 for DOWN): ");
                    int requestDirection = scanner.nextInt();
                    if (requestDirection == 1) {
                        controller.pressUpButton(requestFloor);
                    } else if (requestDirection == 2) {
                        controller.pressDownButton(requestFloor);
                    } else {
                        System.out.println("Invalid direction!");
                    }
                    break;
                case 2:
                    display.showAllDisplays();
                    break;
                case 3:
                    System.out.print("Enter elevator ID (0-5): ");
                    int elevatorId = scanner.nextInt();
                    System.out.print("Enter target floor (0-19): ");
                    int targetFloor = scanner.nextInt();
                    controller.requestFloor(elevatorId, targetFloor);
                    break;
                case 4:
                    controller.emergencyStop();
                    System.out.println("Emergency stop activated!");
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            // Move elevators and update their state
            controller.moveElevators();
        }
    }
}