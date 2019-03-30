# Elevator Control System 

## Installation

### Prerequisites:

Java, Maven

### Install dependecies using maven

mvn clean install

### Run program

mvn exec:java

### Run tests

mvn test


## How the system picks next floor

Easiest way would be to implement First Serve First Come, but then we get a problem of inefficiency of the system. Instead I've decided to take advantage of the fact that there are multiple elevators and make sure that elevators don't change direction if it's not needed. When calling an elevator system prioritezes first close elevators that are coming in the right direction, and if there's none it picks one that's stationary. Then when we add new floor destination to the elavator it takes all current destinations and current floor of the elevator and determines the order it visit them using this algorithm:

If elevator is going up: [HigherFloors][LowerFloorsReversed]

If it is going down: [LowerFloorsReversed][HigherFloors]

Where HigherFloors are sorted floor destinations that are higher than current floor, and LowerFloors are sorted in reverse floor destinations that are lower than the current floor.

### Advantages 

* It is more efficient than First Come First Serve, while still preserving order by prioritising direction picked first
* It won't get caught in the "loop" of people traveling on top floors while a person on a bottom waits for called elevator

### Disadvantages

* Currently if there's no elevator available with correct direction or one that's stationary no elevator will be called. In future it could be fixed by implementing timeouts, but as of now the system does not support time management.
