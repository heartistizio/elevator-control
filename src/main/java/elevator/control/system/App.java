package elevator.control.system;

import java.util.Scanner;


public class App {
    private static void printOutMenu() {
        System.out.println("Pick your option: ");
        System.out.println("1. Add new Elevator.");
        System.out.println("2. Call a pickup");
        System.out.println("3. Pick floor to go to");
        System.out.println("4. Make a simulation step");
        System.out.println("5. Print out status");
        System.out.println("6. Quit");
        System.out.println("0. Reprint menu");
    }

    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = new ElevatorSystem();
        boolean systemOn = true;
        printOutMenu();

        while (systemOn) {
            Scanner reader = new Scanner(System.in);
            int n = reader.nextInt();
            System.out.println(n);
            switch (n) {
                case 1:
                    Elevator elevator = new Elevator();
                    elevatorSystem.addNewElevator(elevator);
                    System.out.println("New elevator added");
                    System.out.println("Awaiting next command(press 0 to print out menu again): ");
                    break;
                case 2:
                    System.out.println("Current floor number: ");
                    int currentFloor = reader.nextInt();
                    System.out.println("Direction(UP: 1, DOWN: -1) :");
                    int direction = reader.nextInt();
                    int success = elevatorSystem.pickup(currentFloor, direction);
                    if (success == 1) {
                        System.out.println("Elevator is coming to floor " + currentFloor);
                        System.out.println("Awaiting next command(press 0 to print out menu again): ");
                    } else if (success == -1) {
                        System.out.println("There's no free elevators right now.");
                    }

                    break;
                case 3:
                    System.out.println("Elevator ID:");
                    int elevatorId = reader.nextInt();
                    Elevator currentElevator = elevatorSystem.findElevatorById(elevatorId);
                    System.out.println("Floor Destination: ");
                    int destination = reader.nextInt();
                    elevatorSystem.update(elevatorId, currentElevator.getCurrentFloor(), destination);
                    System.out.println("Elevator ID: " + elevatorId + " will go to floor " + destination);
                    System.out.println("Awaiting next command(press 0 to print out menu again): ");
                    break;
                case 4:
                    elevatorSystem.step();
                    System.out.println("Elevators have moved");
                    System.out.println("Awaiting next command(press 0 to print out menu again): ");
                    break;
                case 5:
                    System.out.println(elevatorSystem.status());
                    System.out.println("Awaiting next command(press 0 to print out menu again): ");
                    break;
                case 6:
                    systemOn = false;
                    break;
                case 0:
                    printOutMenu();
                    break;
            }
        }
    }
}

