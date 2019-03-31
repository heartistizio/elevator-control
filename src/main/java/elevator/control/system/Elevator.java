package elevator.control.system;

import java.util.ArrayList;

class Elevator {
    private Integer id;
    private Integer currentFloor;
    private ArrayList<Integer> floorDestinations;
    private Integer direction; // -1 down, 0 not moving, 1 going up

    Elevator() {
        this.id = 0;
        this.currentFloor = 0;
        this.floorDestinations = new ArrayList<>();
        this.direction = 0;
    }

    Elevator(Integer id, Integer currentFloor, ArrayList<Integer> floorDestinations, Integer direction) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.floorDestinations = floorDestinations;
        this.direction = direction;
    }

    void set(Elevator elevator) {
        this.currentFloor = elevator.currentFloor;
        this.floorDestinations = elevator.floorDestinations;
        this.direction = elevator.direction;
    }

    Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    Integer getCurrentFloor() {
        return currentFloor;
    }

    void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

    Integer getDirection() {
        return direction;
    }

    void setDirection(Integer direction) {
        this.direction = direction;
    }

    ArrayList<Integer> getFloorDestinations() {
        return floorDestinations;
    }

    void setFloorDestinations(ArrayList<Integer> floorDestinations) {
        this.floorDestinations = floorDestinations;
    }

}
