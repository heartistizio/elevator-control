package elevator.control.system.controllers;

import elevator.control.system.App;
import elevator.control.system.services.Pickup;
import elevator.control.system.services.Update;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ElevatorSystemController {

    @GetMapping(value = "/elevators")
    public String getElevators() {
        return App.elevatorSystem.status();
    }

    @GetMapping(value = "/step")
    public String getStep() {
        App.elevatorSystem.step();
        return App.elevatorSystem.status();
    }

    @PostMapping(value = "/pickup")
    public String postPickup(@RequestBody Pickup pickupBody) {
        App.elevatorSystem.pickup(pickupBody.getCallingFloor(), pickupBody.getDirection());
        return App.elevatorSystem.status();
    }

    @PostMapping(value = "/update")
    public String postUpdate(@RequestBody Update updateBody) {
        App.elevatorSystem.update(updateBody.getIdOfElevator(), updateBody.getCurrentFloor(), updateBody.getNewFloorDestination());
        return App.elevatorSystem.status();
    }
}
