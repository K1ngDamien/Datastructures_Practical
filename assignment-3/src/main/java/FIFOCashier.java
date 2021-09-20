import java.time.LocalTime;
import java.util.Map;

public class FIFOCashier extends Cashier {

    public FIFOCashier(String cashierName) {
        super(cashierName);
    }

    @Override
    public int expectedCheckOutTime(int amountOfItems) {
        int timeToCheckout = 0;

        if(amountOfItems == 0) {
            return timeToCheckout;
        }

        timeToCheckout = checkOutTimePerCustomer + (checkOutTimePerItem * amountOfItems);

        return timeToCheckout;
    }

    @Override
    public int expectedWaitingTime(Customer customer) {
        int timeToWait = 0;

        for(int i=0; i < this.waitingQueue.size(); i++) {
            Customer currCustomer = (Customer) this.waitingQueue.get(i);
            timeToWait += checkOutTimePerCustomer + (checkOutTimePerItem * currCustomer.getNumberOfItems());
        }

        if(this.customersBeingHelped != null) {
            timeToWait += checkOutTimePerCustomer + (checkOutTimePerItem * this.customersBeingHelped.getNumberOfItems());
        }

        return timeToWait - this.workedAmount;
    }

}
