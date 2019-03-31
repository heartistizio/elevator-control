package elevator.control.system;

import java.util.Optional;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        final ElevatorSystem elevatorSystem = new ElevatorSystem();
        boolean systemOn = true;
        printOutMenu();

        while (systemOn) {
            Scanner reader = new Scanner(System.in);
            int n = reader.nextInt();
            System.out.println(n);
            switch (n) {
                case 1:
                    addNewElevator(elevatorSystem);
                    break;
                case 2:
                    callPickup(elevatorSystem, reader);
                    break;
                case 3:
                    pickFloorToGo(elevatorSystem, reader);
                    break;
                case 4:
                    makeStep(elevatorSystem);
                    break;
                case 5:
                    printStatus(elevatorSystem);
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

    private static void printOutMenu() {
        System.out.println("Pick your option: ");
        System.out.println("1. Add a new Elevator");
        System.out.println("2. Call a pickup");
        System.out.println("3. Pick floor to go to");
        System.out.println("4. Make a simulation step");
        System.out.println("5. Print out status");
        System.out.println("6. Quit");
        System.out.println("0. Reprint menu");
    }

    private static void addNewElevator(ElevatorSystem elevatorSystem){
        Elevator elevator = new Elevator();
        elevatorSystem.addNewElevator(elevator);
        System.out.println("New elevator added");
        System.out.println("Awaiting next command(press 0 to print out menu again): ");
    }

    private static void callPickup(ElevatorSystem elevatorSystem, Scanner reader){
        System.out.println("Current floor number: ");
        int currentFloor = reader.nextInt();
        System.out.println("Direction(UP: 1, DOWN: -1) :");
        int direction = reader.nextInt();
        elevatorSystem.pickup(currentFloor, direction);
        System.out.println("Elevator is coming to floor " + currentFloor);
        System.out.println("Awaiting next command(press 0 to print out menu again): ");
    }

    private static void pickFloorToGo(ElevatorSystem elevatorSystem, Scanner reader){
        System.out.println("Elevator ID:");
        int elevatorId = reader.nextInt();
        Optional<Elevator> currentElevatorOptional = elevatorSystem.findElevatorById(elevatorId);
        Elevator currentElevator = currentElevatorOptional.get();
        System.out.println("Floor Destination: ");
        int destination = reader.nextInt();
        elevatorSystem.update(elevatorId, currentElevator.getCurrentFloor(), destination);
        System.out.println("Elevator ID: " + elevatorId + " will go to floor " + destination);
        System.out.println("Awaiting next command(press 0 to print out menu again): ");
    }

    private static void makeStep(ElevatorSystem elevatorSystem){
        elevatorSystem.step();
        System.out.println("Elevators have moved");
        System.out.println("Awaiting next command(press 0 to print out menu again): ");
    }

    private static void printStatus(ElevatorSystem elevatorSystem){
        elevatorSystem.status();
        System.out.println(elevatorSystem.status());
        System.out.println("Awaiting next command(press 0 to print out menu again): ");
    }
}

