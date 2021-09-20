import model.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class TrainsTest {

    private ArrayList<PassengerWagon> pwList;
    private Train firstPassengerTrain;
    private Train secondPassengerTrain;
    private Train firstFreightTrain;
    private Train secondFreightTrain;

   @BeforeEach
    private void makeListOfPassengerWagons() {
        pwList = new ArrayList<>();
        pwList.add(new PassengerWagon(3, 100));
        pwList.add(new PassengerWagon(24, 100));
        pwList.add(new PassengerWagon(17, 140));
        pwList.add(new PassengerWagon(32, 150));
        pwList.add(new PassengerWagon(38, 140));
        pwList.add(new PassengerWagon(11, 100));
    }

    private void makeTrains() {
        Locomotive thomas = new Locomotive(2453, 7);
        Locomotive gordon = new Locomotive(5277, 8);
        Locomotive emily = new Locomotive(4383, 11);
        Locomotive rebecca = new Locomotive(2275, 4);

        firstPassengerTrain = new Train(thomas, "Amsterdam", "Haarlem");
        for (Wagon w : pwList) {
            Shunter.hookWagonOnTrainRear(firstPassengerTrain, w);
        }
        secondPassengerTrain = new Train(gordon, "Joburg", "Cape Town");
        firstFreightTrain = new Train(emily, "Amsterdam", "Rotterdam");
        secondFreightTrain = new Train(rebecca, "Frankfurt", "Amsterdam");
    }

    @Test
    public void checkNumberOfWagonsOnTrain() {
        makeTrains();
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        System.out.println(firstPassengerTrain);
   }

    @Test
    public void checkNumberOfSeatsOnTrain() {
        makeTrains();
        assertEquals( 730, firstPassengerTrain.getNumberOfSeats());
        System.out.println(firstPassengerTrain);
    }

    @Test
    public void checkPositionOfWagons() {
        makeTrains();
        int position = 1;
        for (PassengerWagon pw : pwList) {
            assertEquals( position, firstPassengerTrain.getPositionOfWagon(pw.getWagonId()));
            position++;
        }
    }

    @Test
    public void checkHookOneWagonOnTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 140));
        assertEquals( 7, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals( 1, firstPassengerTrain.getPositionOfWagon(21));

    }

    @Test
    public void checkHookRowWagonsOnTrainRearFalse() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have still have 6 wagons, capacity reached");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(43));
    }

    @Test
    public void checkMoveOneWagon() {
        makeTrains();
        Shunter.moveOneWagon(firstPassengerTrain, secondPassengerTrain, pwList.get(3));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons(), "Train should have 5 wagons");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(32));
        assertEquals(1, secondPassengerTrain.getNumberOfWagons(), "Train should have 1 wagon");
        assertEquals( 1, secondPassengerTrain.getPositionOfWagon(32));

    }

    @Test
    public void checkMoveRowOfWagons() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(secondPassengerTrain, w1);
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(2));
        assertEquals(2, firstPassengerTrain.getNumberOfWagons(), "Train should have 2 wagons");
        assertEquals( 2, firstPassengerTrain.getPositionOfWagon(24));
        assertEquals(6, secondPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals( 4, secondPassengerTrain.getPositionOfWagon(32));
    }

    @Test
    public void checkWagonIsCorrectType() {
       makeTrains();

       PassengerWagon firstPassengerWagon = new PassengerWagon(1, 100);
       FreightWagon firstFreightWagon = new FreightWagon(1, 100);

       Shunter.hookWagonOnTrainFront(this.secondPassengerTrain, firstPassengerWagon);
       Shunter.hookWagonOnTrainFront(this.firstFreightTrain, firstFreightWagon);

       assertTrue(this.secondPassengerTrain.isPassengerTrain());
       assertTrue(this.firstFreightTrain.isFreightTrain());
    }

    @Test
    public void checkCorrectFreightTrainWeight() {
       makeTrains();

       FreightWagon freightWagon1 = new FreightWagon(1, 10);
       FreightWagon freightWagon2 = new FreightWagon(2, 10);

       Shunter.hookWagonOnTrainFront(this.secondFreightTrain, freightWagon1);
       Shunter.hookWagonOnTrainRear(this.secondFreightTrain, freightWagon2);

       // Test if there is place for one wagon (there is only 1 spot left)
        assertEquals(20, this.secondFreightTrain.getTotalMaxWeight());

       // Test if passengerTrain weight shall be 0
        PassengerWagon passengerWagon1 = new PassengerWagon(1, 100);
        Shunter.hookWagonOnTrainFront(this.firstPassengerTrain, passengerWagon1);

        assertEquals(0, this.firstPassengerTrain.getTotalMaxWeight());

    }

}
