package elevator.control.system.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter public class Elevator {
    private Integer id;
    private Integer currentFloor;
    private ArrayList<Integer> floorDestinations;
    private Integer direction; // -1 down, 0 not moving, 1 going up

    public Elevator() {
        this.id = 0;
        this.currentFloor = 0;
        this.floorDestinations = new ArrayList<>();
        this.direction = 0;
    }

    public Elevator(Integer id, Integer currentFloor, ArrayList<Integer> floorDestinations, Integer direction) {
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

}

