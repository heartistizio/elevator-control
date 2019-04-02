package elevator.control.system;

import elevator.control.system.model.Elevator;
import elevator.control.system.model.ElevatorSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    public static ElevatorSystem elevatorSystem;

    public static void startUp(){
        elevatorSystem = new ElevatorSystem();
        for(int i = 0; i < 4; i++){
            elevatorSystem.addNewElevator(new Elevator());
        }
    }


    public static void main(String[] args) {
        startUp();
        SpringApplication.run(App.class, args);
    }
}

