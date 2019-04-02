package elevator.control.system;

import elevator.control.system.model.Elevator;
import elevator.control.system.model.ElevatorSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    public final static ElevatorSystem elevatorSystem = new ElevatorSystem();

    public static void main(String[] args) {
        for(int i = 0; i < 4; i++){
            elevatorSystem.addNewElevator(new Elevator());
        }
        SpringApplication.run(App.class, args);
    }
}

