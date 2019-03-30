package elevator.control.system;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

class ElevatorSystem {
    private ArrayList<Elevator> elevatorList = new ArrayList<>();


    // Calls elevator with matching direction to the provided floor
    Integer pickup(Integer callingFloor, Integer direction) {
        Integer floorOfClosestElevator = 0;
        Integer tempFloorDifference;
        Integer idOfClosestElevator = Integer.MIN_VALUE;
        Integer floorDifference = Integer.MAX_VALUE;
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getDirection().equals(direction)) {
                switch (direction) {
                    case 1: {
                        tempFloorDifference = callingFloor - elevator.getCurrentFloor();
                        if ((abs(tempFloorDifference) < floorDifference) && tempFloorDifference > 0) {
                            floorDifference = abs(floorDifference);
                            floorOfClosestElevator = elevator.getCurrentFloor();
                            idOfClosestElevator = elevator.getId();
                        }
                    }
                    case -1: {
                        tempFloorDifference = elevator.getCurrentFloor() - callingFloor;
                        if ((abs(tempFloorDifference) < floorDifference) && tempFloorDifference > 0) {
                            floorDifference = abs(floorDifference);
                            floorOfClosestElevator = elevator.getCurrentFloor();
                            idOfClosestElevator = elevator.getId();
                        }
                    }
                }
            } else if (elevator.getDirection().equals(0)) {
                tempFloorDifference = abs(elevator.getCurrentFloor() - callingFloor);
                if ((tempFloorDifference < floorDifference)) {
                    floorDifference = abs(tempFloorDifference);
                    floorOfClosestElevator = elevator.getCurrentFloor();
                    idOfClosestElevator = elevator.getId();
                }
            }
        }

        // if matching elevator was found return, if implemented with time solution could use a timeout to repeat pickup request
        if (idOfClosestElevator != Integer.MIN_VALUE) {
            update(idOfClosestElevator, floorOfClosestElevator, callingFloor);
            return 1;
        }
        return -1;

    }

    // Adds new floor destination to provided elevator
    void update(Integer idOfElevator, Integer currentFloor, Integer newFloorDestination) {
        ArrayList<Integer> elevatorFloorDestinations;
        Elevator elevator = findElevatorById(idOfElevator);
        if(elevator != null) {
            elevator.setCurrentFloor(currentFloor);
            elevatorFloorDestinations = elevator.getFloorDestinations();
            elevatorFloorDestinations.add(newFloorDestination);
            elevator.setFloorDestinations(determineFinalFloorDestinations(elevator, currentFloor, elevatorFloorDestinations));

            if (elevator.getCurrentFloor() < elevator.getFloorDestinations().get(0)) {
                elevator.setDirection(1);
            } else if (elevator.getCurrentFloor() > elevator.getFloorDestinations().get(0)) {
                elevator.setDirection(-1);
            } else if (elevator.getFloorDestinations().isEmpty()) {
                elevator.setDirection(0);
            }

            this.elevatorList.get(findIndexOfElevator(elevator)).set(elevator);
        }
        else {
            System.out.println("There's no Elevator of that ID");
        }
    }


    // Moves simulation one step forward
    void step() {
        ArrayList<Integer> elevatorFloorDestinations;
        for (Elevator elevator : this.elevatorList) {
            elevatorFloorDestinations = elevator.getFloorDestinations();
            if (elevator.getDirection() != 0) {
                elevator.setCurrentFloor(elevatorFloorDestinations.remove(0));
                elevator.setFloorDestinations(elevatorFloorDestinations);
                if (elevator.getFloorDestinations().isEmpty()) {
                    elevator.setDirection(0);
                }
                this.elevatorList.get(findIndexOfElevator(elevator)).set(elevator);
            }

        }
    }


    // Could use optimization, return information on all current elevators
    String status() {
        String newline = "\n";
        String response = "=============" + newline;
        for (Elevator elevator : this.elevatorList) {
            response = response +
                    "Elevator ID: " +
                    elevator.getId().toString() +
                    newline +
                    "Elevator Floor: " +
                    elevator.getCurrentFloor().toString() +
                    newline +
                    "Elevator Direction: " +
                    elevator.getDirection().toString() +
                    newline +
                    "Elevator Destinations: " +
                    newline;

            for (Integer destination : elevator.getFloorDestinations()) {
                response = response + destination.toString() + newline;
            }
            response = response + "=============" + newline;
        }
        return response;
    }

/*
    ArrayList<Elevator> status() {
        return this.elevatorList;
    }
*/

    // Adds new Elevator to elevator list, if ID was already used returns first id that's not used
    void addNewElevator(Elevator newElevator) {
        Integer id;
        if (!this.elevatorList.isEmpty()) {
            for (Elevator elevator : this.elevatorList) {
                if (elevator.getId().equals(newElevator.getId())) {
                    id = newElevator.getId();
                    id++;
                    newElevator.setId(id);
                }
            }
        }
        this.elevatorList.add(newElevator);
    }

    // Finds and returns elevator by provided id
    Elevator findElevatorById(Integer idOfElevator) {
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getId().equals(idOfElevator)) {
                return elevator;
            }
        }
        return null; //should throw error instead
    }

    // Determines what order should elevator visit floors
    private ArrayList<Integer> determineFinalFloorDestinations(Elevator elevator, Integer currentFloor, ArrayList<Integer> elevatorFloorDestinations) {
        ArrayList<Integer> higherFloors = new ArrayList<>();
        ArrayList<Integer> lowerFloors = new ArrayList<>();
        ArrayList<Integer> finalFloorDestinations = new ArrayList<>();
        if (elevator.getDirection() == 1 || elevator.getDirection() == -1) {
            Collections.sort(elevatorFloorDestinations);
            for (Integer destination : elevatorFloorDestinations) {
                if (destination > currentFloor) {
                    higherFloors.add(destination);
                }
                if (destination < currentFloor) {
                    lowerFloors.add(destination);
                }
            }
        }
        switch (elevator.getDirection()) {
            case 1: {
                finalFloorDestinations.addAll(higherFloors);
                Collections.reverse(lowerFloors);
                finalFloorDestinations.addAll(lowerFloors);
                return finalFloorDestinations;
            }

            case -1: {
                Collections.reverse(lowerFloors);
                finalFloorDestinations.addAll(lowerFloors);
                finalFloorDestinations.addAll(higherFloors);
                return finalFloorDestinations;
            }

            case 0: {
                finalFloorDestinations = elevatorFloorDestinations;
                return finalFloorDestinations;
            }
        }
        return finalFloorDestinations;
    }

    // Find index of elevator
    private Integer findIndexOfElevator(Elevator queriedElevator) {
        Integer index = -1;
        for (Elevator elevator : this.elevatorList) {
            index++;
            if (elevator.equals(queriedElevator)) {
                return index;
            }
        }
        return -1;
    }

}
