package elevator.control.system.model;

import com.google.gson.Gson;
import elevator.control.system.services.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static java.lang.Math.abs;

public class ElevatorSystem {
    private ArrayList<Elevator> elevatorList = new ArrayList<>();


    // Calls elevator with matching direction to the provided floor
    public void pickup(Integer callingFloor, Integer direction) {
        Integer[] response;
        response = findClosestElevatorWithMatchingDirection(callingFloor, direction);

        // if matching elevator was found return, if implemented with time solution could use a timeout to repeat pickup request
        if (response[0] != null) {
            update(response[0], response[1], callingFloor);
        }
        // else find an elevator that's about to finish it's trip
        else {
            response = findElevatorCloseToFinalDestination();
            update(response[0], response[1], callingFloor);
        }

    }

    // Adds new floor destination to provided elevator
    public void update(Integer idOfElevator, Integer currentFloor, Integer newFloorDestination) {
        ArrayList<Integer> elevatorFloorDestinations;
        Optional<Elevator> elevatorOptional = findElevatorById(idOfElevator);
        Integer newDirection;
        Elevator elevator;
        if (elevatorOptional.isPresent()) {
            elevator = elevatorOptional.get();
            elevator.setCurrentFloor(currentFloor);
            if (!elevator.getFloorDestinations().contains(newFloorDestination)) {
                elevatorFloorDestinations = elevator.getFloorDestinations();
                elevatorFloorDestinations.add(newFloorDestination);
                elevator.setFloorDestinations(determineFinalFloorDestinations(elevator, currentFloor, elevatorFloorDestinations));
            }
            newDirection = determineDirection(elevator);
            elevator.setDirection(newDirection);

            this.elevatorList.get(findIndexOfElevator(elevator)).set(elevator);
        } else {
            System.out.println(Strings.NO_ELEVATOR_FOUND);
        }
    }


    // Moves simulation one step forward
    public void step() {
        ArrayList<Integer> elevatorFloorDestinations;
        Integer newDirection;
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getDirection() != 0) {
                elevator.setCurrentFloor(elevator.getCurrentFloor() + elevator.getDirection());
                if (elevator.getCurrentFloor().equals(elevator.getFloorDestinations().get(0))) {
                    elevatorFloorDestinations = elevator.getFloorDestinations();
                    elevator.setCurrentFloor(elevatorFloorDestinations.remove(0));
                    elevator.setFloorDestinations(elevatorFloorDestinations);
                }
                newDirection = determineDirection(elevator);
                elevator.setDirection(newDirection);
                this.elevatorList.get(findIndexOfElevator(elevator)).set(elevator);
            }

        }
    }


    // Could use optimization, return information on all current elevators
    public String printStatus() {
        StringBuilder response = new StringBuilder(Strings.HORIZONTAL_DELIMETER).append(Strings.NEW_LINE);
        for (Elevator elevator : this.elevatorList) {
            response = new StringBuilder(response)
                    .append(Strings.ELEVATOR_ID)
                    .append(elevator.getId().toString())
                    .append(Strings.NEW_LINE)
                    .append(Strings.ELEVATOR_FLOOR)
                    .append(elevator.getCurrentFloor().toString())
                    .append(Strings.NEW_LINE)
                    .append(Strings.ELEVATOR_DIRECTION)
                    .append(elevator.getDirection().toString())
                    .append(Strings.NEW_LINE)
                    .append(Strings.ELEVATOR_DESTINATIONS)
                    .append(Strings.NEW_LINE);


            for (Integer destination : elevator.getFloorDestinations()) {
                response = new StringBuilder(response)
                        .append(destination.toString())
                        .append(Strings.NEW_LINE);
            }
            response = new StringBuilder(response)
                    .append(Strings.HORIZONTAL_DELIMETER)
                    .append(Strings.NEW_LINE);
        }
        return response.toString();
    }

    public String status(){
        Gson gson = new Gson();
        return gson.toJson(this.elevatorList);
    }


    // Adds new Elevator to elevator list, if ID was already used returns first id that's not used
    public void addNewElevator(Elevator newElevator) {
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
    public Optional<Elevator> findElevatorById(Integer idOfElevator) {
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getId().equals(idOfElevator)) {
                return Optional.of(elevator);
            }
        }
        return Optional.empty();
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
//TO DO- FIGHT with NullPointerException
    private Integer[] findClosestElevatorWithMatchingDirection(Integer callingFloor, Integer direction) {
        Integer tempFloorDifference;
        Integer floorDifference = Integer.MAX_VALUE;
        Integer[] response = new Integer[2];
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getDirection().equals(direction)) {
                switch (direction) {
                    case 1: {
                        tempFloorDifference = callingFloor - elevator.getCurrentFloor();
                        if ((abs(tempFloorDifference) < floorDifference) && tempFloorDifference > 0) {
                            floorDifference = abs(floorDifference);
                            response[0] = elevator.getId();
                            response[1] = elevator.getCurrentFloor();
                        }
                    }
                    case -1: {
                        tempFloorDifference = elevator.getCurrentFloor() - callingFloor;
                        if ((abs(tempFloorDifference) < floorDifference) && tempFloorDifference > 0) {
                            floorDifference = abs(floorDifference);
                            response[0] = elevator.getId();
                            response[1] = elevator.getCurrentFloor();
                        }
                    }
                }
            } else if (elevator.getDirection().equals(0)) {
                tempFloorDifference = abs(elevator.getCurrentFloor() - callingFloor);
                if ((tempFloorDifference < floorDifference)) {
                    floorDifference = abs(tempFloorDifference);
                    response[0] = elevator.getId();
                    response[1] = elevator.getCurrentFloor();
                }
            }
        }

        return response;
    }

    private Integer[] findElevatorCloseToFinalDestination() {
        Integer[] response = new Integer[2];
        int tripLength = Integer.MAX_VALUE;
        int floorDifference = Integer.MAX_VALUE;
        int tempFloorDifference;
        for (Elevator elevator : this.elevatorList) {
            if (elevator.getFloorDestinations().size() < tripLength) {
                tripLength = elevator.getFloorDestinations().size();
                tempFloorDifference = abs(elevator.getCurrentFloor() - elevator.getFloorDestinations().get(tripLength - 1));
                if (tempFloorDifference < floorDifference) {
                    floorDifference = tempFloorDifference;
                    response[0] = elevator.getId();
                    response[1] = elevator.getCurrentFloor();
                }
            }
        }
        return response;
    }


    private Integer determineDirection(Elevator elevator) {
        if (elevator.getFloorDestinations().isEmpty()) {
            return 0;
        } else if (elevator.getCurrentFloor() < elevator.getFloorDestinations().get(0)) {
            return 1;
        } else if (elevator.getCurrentFloor() > elevator.getFloorDestinations().get(0)) {
            return -1;
        } else
            return null;
    }
}

