package elevator.control.system;


import org.junit.Test;
import org.junit.Assert;

public class ElevatorSystemTest {
    private ElevatorSystem elevatorSystem;
    private Elevator elevator, elevator2;


    private void setUp() {
        elevatorSystem = new ElevatorSystem();
        elevator = new Elevator();
        elevatorSystem.addNewElevator(elevator);
        elevator2 = new Elevator();
        elevatorSystem.addNewElevator(elevator2);
    }


    @Test
    public void shouldPickup() {
        setUp();
        elevatorSystem.pickup(3, 1);
        Assert.assertTrue(elevator.getFloorDestinations().contains(3));
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
    }

    @Test
    public void shouldAddNewFloorDestination() {
        setUp();
        elevatorSystem.update(0, 0, 3);
        Assert.assertEquals(Integer.valueOf(1), elevator.getDirection());
        Assert.assertTrue(elevator.getFloorDestinations().contains(3));
    }

    @Test
    public void shouldReturnSimulationStatus() {
        setUp();
        String response = elevatorSystem.status();
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
        setUp();
        elevatorSystem.update(0, 0, 8);
        elevatorSystem.update(1, 0, 3);
        for(int i=0; i < 8; i++) {
            elevatorSystem.step();
        }
        elevatorSystem.update(0, 8, 9);
        elevatorSystem.update(1, 0, 5);
        elevatorSystem.update(1, 0, 6);
        elevatorSystem.pickup(1, -1);
        Assert.assertEquals(Integer.valueOf(1), elevator.getFloorDestinations().get(1));
        elevatorSystem.step();
        elevatorSystem.pickup(9, 1);
        Assert.assertEquals(Integer.valueOf(-1), elevator.getDirection());
        Assert.assertEquals(Integer.valueOf(9), elevator2.getFloorDestinations().get(2));
        String response = elevatorSystem.status();
        Assert.assertEquals("=============\n" +
                "Elevator ID: 0\n" +
                "Elevator Floor: 9\n" +
                "Elevator Direction: -1\n" +
                "Elevator Destinations: \n" +
                "1\n" +
                "=============\n" +
                "Elevator ID: 1\n" +
                "Elevator Floor: 1\n" +
                "Elevator Direction: 1\n" +
                "Elevator Destinations: \n" +
                "5\n" +
                "6\n" +
                "9\n" +
                "=============\n", response);
    }

    @Test
    public void shouldCheckComplicatedScenario(){
        setUp();

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
        setUp();
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
        setUp();
        Elevator foundElevator = elevatorSystem.findElevatorById(1);
        Assert.assertEquals(elevator2, foundElevator);
    }

    @Test
    public void shouldAddNewElevator() {
        setUp();
        Elevator elevator3 = new Elevator();
        elevatorSystem.addNewElevator(elevator3);
        Assert.assertEquals(Integer.valueOf(2), elevator3.getId());

        Elevator elevator4 = new Elevator();
        elevator4.setId(432);
        elevatorSystem.addNewElevator(elevator4);
        Assert.assertEquals(Integer.valueOf(432), elevator4.getId());
    }
}
