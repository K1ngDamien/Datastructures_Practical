package model;

public class FreightWagon extends model.Wagon {
    private Integer maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);

        this.maxWeight = maxWeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }
}
