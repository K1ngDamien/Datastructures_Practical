package model;

import java.util.Iterator;

public class Train implements Iterable<Wagon> {
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */

       if (this.firstWagon != null) {
           this.numberOfWagons = this.firstWagon.getNumberOfWagonsAttached() + 1;
       } else {
           this.numberOfWagons = 0;
       }
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    /* three helper methods that are useful in other methods */

    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1
        int position = 1;

        Wagon currentWagon = this.firstWagon;

        while (currentWagon.hasPreviousWagon()) {
            if (currentWagon.getWagonId() != wagonId) {
                position++;
                currentWagon = currentWagon.getPreviousWagon();
            } else {
                return position;
            }
        }

        if (currentWagon.getWagonId() == wagonId){
            return position;
        }

        return -1;
    }

    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */

        Wagon currentWagon = this.firstWagon;
        int comparePosition = 1;

        while (currentWagon.hasPreviousWagon()) {
            if (comparePosition == position) {
                return currentWagon;
            } else {
                comparePosition++;
                currentWagon = currentWagon.getPreviousWagon();

                if (currentWagon.getPreviousWagon() == null) {
                    throw new IndexOutOfBoundsException(String.format("Wagon with position %s does not exist. ", position));
                }
            }
        }

       return null;
    }

    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        if (this.hasNoWagons() || this.firstWagon instanceof FreightWagon) {
            return 0;
        }

        int totalNumberOfPassengerSeats = ((PassengerWagon) this.firstWagon).getNumberOfSeats();

        for (Wagon wagon : this) {
            totalNumberOfPassengerSeats += ((PassengerWagon) wagon).getNumberOfSeats();
        }

        return totalNumberOfPassengerSeats;
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        if (this.hasNoWagons() || this.firstWagon instanceof PassengerWagon) {
            return 0;
        }

        int cumulativeWagonWeight = ((FreightWagon) this.firstWagon).getMaxWeight();


        for (Wagon wagon : this) {
            cumulativeWagonWeight += ((FreightWagon) wagon).getMaxWeight();
        }

        return cumulativeWagonWeight;

    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator<Wagon> iterator() {
        return new Iterator<Wagon>() {
            Wagon currentWagon = firstWagon;

            @Override
            public boolean hasNext() {
                return currentWagon.hasPreviousWagon();
            }

            @Override
            public Wagon next() {
                return currentWagon = currentWagon.getPreviousWagon();
            }
        };
    }
}
