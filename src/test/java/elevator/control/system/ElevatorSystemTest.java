package elevator.control.system;


import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class ElevatorSystemTest {
    private ElevatorSystem elevatorSystem;
    private Elevator elevator, elevator2;

    @Before
    public void setUp() {
        elevatorSystem = new ElevatorSystem();
        elevator = new Elevator();
        elevatorSystem.addNewElevator(elevator);
        elevator2 = new Elevator();
        elevatorSystem.addNewElevator(elevator2);
    }


    @Test
    public void shouldPickup() {
        elevatorSystem.pickup(3, 1);
        Assert.assertTrue(elevator.getFloorDestinations().contains(3));
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
    }

    @Test
    public void shouldAddNewFloorDestination() {
        elevatorSystem.update(0, 0, 3);
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
        Assert.assertTrue(elevator.getFloorDestinations().contains(3));
    }

    @Test
    public void shouldReturnSimulationStatus() {
        String response = elevatorSystem.printStatus();
        Assert.assertEquals(
                "=============\n" +
                "Elevator ID: 0\n" +
                "Elevator Floor: 0\n" +
                "Elevator Direction: 0\n" +
                "Elevator Destinations: \n" +
                "=============\n" +
                "Elevator ID: 1\n" +
                "Elevator Floor: 0\n" +
                "Elevator Direction: 0\n" +
                "Elevator Destinations: \n" +
                "=============\n", response);
    }

    @Test
    public void shouldCallElevatorThatsCloseToFinish(){
        elevatorSystem.update(0, 0, 8); // Should add new destination to elevator of ID 0
        elevatorSystem.update(1, 0, 3); // Should add new destination to elevator of ID 1
        for(int i=0; i < 8; i++) {
            elevatorSystem.step();
        }
        elevatorSystem.update(0, 8, 9); // Should add new destination to elevator of ID 0 on floor 8
        elevatorSystem.update(1, 3, 5); // Should add new destination to elevator of ID 1 on floor 3
        elevatorSystem.pickup(1, -1);   // Should call an elevator to floor 1, and pick elevator 1
        Assert.assertEquals(Integer.valueOf(1), elevator.getFloorDestinations().get(1)); // Second destination of elevator should be 1
        elevatorSystem.step();
        elevatorSystem.pickup(9, 1); // Should call elevator to floor 9 and pick elevator 2 with direction floor 1
        Assert.assertEquals(Integer.valueOf(-1), elevator.getDirection()); // Direction of elevator should be -1
        Assert.assertEquals(Integer.valueOf(9), elevator2.getFloorDestinations().get(1)); //  Second destination of elevator2 should be floor 9
        String response = elevatorSystem.printStatus();
        Assert.assertEquals("=============\n" +
                "Elevator ID: 0\n" +
                "Elevator Floor: 9\n" +
                "Elevator Direction: -1\n" +
                "Elevator Destinations: \n" +
                "1\n" +
                "=============\n" +
                "Elevator ID: 1\n" +
                "Elevator Floor: 4\n" +
                "Elevator Direction: 1\n" +
                "Elevator Destinations: \n" +
                "5\n" +
                "9\n" +
                "=============\n", response);
    }

    @Test
    public void shouldCheckComplicatedScenario(){

        // Person on 8th floor calls elevator intending to go down
        elevatorSystem.pickup(8, -1);
        for(int i=0; i < 8; i++) {
            elevatorSystem.step();
        }
        Assert.assertEquals(Integer.valueOf(8), elevator.getCurrentFloor()); // Elevator 1 should be on 8th floor

        // Person in elevator 1 presses button to go down to floor 0
        elevatorSystem.update(0, 8, 0);
        Assert.assertEquals(Integer.valueOf(-1), elevator.getDirection()); // Elevator 1 should be going down

        // Person on 2th floor calls elevator intending to go up
        elevatorSystem.pickup(2, 1);
        Assert.assertEquals(Integer.valueOf(2), elevator2.getFloorDestinations().get(0)); // Elevator 2 should be going to 2th floor
        Assert.assertEquals(Integer.valueOf(1), elevator2.getDirection()); // Elevator 2 should be going up

        // Person on 7th floor calls elevator intending to go down
        elevatorSystem.pickup(7, -1);
        Assert.assertEquals(Integer.valueOf(7), elevator.getFloorDestinations().get(0)); // Elevator 1 should be going first to 7th floor
        Assert.assertEquals(Integer.valueOf(0), elevator.getFloorDestinations().get(1)); // Elevator 1 should be then going to floor 0

        elevatorSystem.step();
        Assert.assertEquals(Integer.valueOf(7), elevator.getCurrentFloor()); // Elevator 1 should be on 7th floor
        Assert.assertEquals(Integer.valueOf(-1), elevator.getDirection()); // Elevator 1 should be going down
        Assert.assertEquals(Integer.valueOf(0), elevator.getFloorDestinations().get(0)); // Elevator 1 should be going to floor 0
        Assert.assertEquals(Integer.valueOf(1), elevator2.getCurrentFloor()); // Elevator 2 should be on 1st floor
        Assert.assertEquals(Integer.valueOf(1), elevator2.getDirection()); // Elevator 2 should be going up


        elevatorSystem.step();
        // Person in elevator 2 presses button to go down to floor 0
        elevatorSystem.update(1, 2, 0);
        Assert.assertEquals(Integer.valueOf(0), elevator2.getFloorDestinations().get(0)); // Elevator 2 should be going to floor 0
        Assert.assertEquals(Integer.valueOf(-1), elevator2.getDirection()); // Elevator 2 should be going down

        elevatorSystem.step();
        Assert.assertEquals(Integer.valueOf(5), elevator.getCurrentFloor()); // Elevator 1 should be on 5th floor
        Assert.assertEquals(Integer.valueOf(-1), elevator.getDirection()); // Elevator 1 should be going down
        Assert.assertEquals(Integer.valueOf(1), elevator2.getCurrentFloor()); // Elevator 2 should be on 1st floor
        Assert.assertEquals(Integer.valueOf(-1), elevator2.getDirection()); // Elevator 2 should be going down
    }

    @Test
    public void shouldMoveSimulationByOneStepForward() {
        elevatorSystem.pickup(3, 1);
        elevatorSystem.step();
        Assert.assertEquals(Integer.valueOf(1), elevator.getCurrentFloor());
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
        elevatorSystem.step();
        Assert.assertEquals(Integer.valueOf(2), elevator.getCurrentFloor());
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
        elevatorSystem.step();
        Assert.assertEquals(Integer.valueOf(3), elevator.getCurrentFloor());
        Assert.assertEquals(Integer.valueOf(0), elevator.getDirection());
    }


    @Test
    public void shouldFindElevatorById() {
        Elevator foundElevator = elevatorSystem.findElevatorById(1).get();
        Assert.assertEquals(elevator2, foundElevator);
    }

    @Test
    public void shouldAddNewElevator() {
        Elevator elevator3 = new Elevator();
        elevatorSystem.addNewElevator(elevator3);
        Assert.assertEquals(Integer.valueOf(2), elevator3.getId());

        Elevator elevator4 = new Elevator();
        elevator4.setId(432);
        elevatorSystem.addNewElevator(elevator4);
        Assert.assertEquals(Integer.valueOf(432), elevator4.getId());
    }

    @Test
    public void shouldAddOnlyOneDestination(){
        elevatorSystem.update(0, 0, 1);
        elevatorSystem.pickup(1, 1);
        Assert.assertEquals(1, elevator.getFloorDestinations().size());
    }

    @Test
    public void shouldUpdateNotExistentElevator(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        elevatorSystem.update(2, 0, 0);
        Assert.assertEquals("There's no Elevator of that ID\n", outContent.toString());
        System.setOut(originalOut);
    }

}
