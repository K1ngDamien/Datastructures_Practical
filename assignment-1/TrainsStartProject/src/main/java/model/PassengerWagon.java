package model;

public class PassengerWagon extends model.Wagon  {
    private Integer numberOfSeats;

    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);

        this.numberOfSeats = numberOfSeats;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }
}
