package model;

public class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    private int numberOfWagons;

    public Wagon (int wagonId) {
        this.wagonId = wagonId;
    }

    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon <!-- Check this.
        Wagon currentWagon = this;

        while (true) {
            if (currentWagon.previousWagon == null) {
                return currentWagon;
            } else {
                currentWagon = currentWagon.previousWagon;
            }
        }
    }

    public void setNextWagon(Wagon nextWagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        this.nextWagon = nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }


    /**
     * Implemented by Students, using previous wagon as current wagon, then moving along in the list. Starts counting at 0.
     * @return integer amountOfWagonsAttached
     */
    public int getNumberOfWagonsAttached() {
        int amountOfWagonsAttached = 0;
        Wagon currentWagon = this;

        while (currentWagon.hasPreviousWagon()) {
            amountOfWagonsAttached++;
            currentWagon = currentWagon.previousWagon;
        }

        return amountOfWagonsAttached;


    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
