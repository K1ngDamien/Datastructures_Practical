import java.util.LinkedList;

public class PriorityCashier extends FIFOCashier {

    private final int maxNumPriorityItems;

    public PriorityCashier(String name, int maxNumPriorityItems) {
        super(name);
        this.maxNumPriorityItems = maxNumPriorityItems;
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

        LinkedList temporaryList = new LinkedList();

        for(int i=0; i < this.waitingQueue.size(); i++) {
            Customer currCustomer = (Customer) this.waitingQueue.get(i);

            if(customer.getNumberOfItems() < currCustomer.getNumberOfItems() && customer.getNumberOfItems() <= 5 && currCustomer.getNumberOfItems() >= 6)
            {
            }else {
                temporaryList.add(currCustomer);
            }
        }

        for(int i=0; i < temporaryList.size(); i++) {
            Customer currCustomer = (Customer) temporaryList.get(i);

            timeToWait += checkOutTimePerCustomer + (checkOutTimePerItem * currCustomer.getNumberOfItems());
        }

        if(this.customersBeingHelped != null) {
            timeToWait += checkOutTimePerCustomer + (checkOutTimePerItem * this.customersBeingHelped.getNumberOfItems());
        }

        return timeToWait - this.workedAmount;
    }

    @Override
    public void add(Customer customer)
    {
        LinkedList temporaryList = new LinkedList();
        this.totalCustomers++;

        while(this.waitingQueue.size() > 0) {
            Customer nextInLine = (Customer) this.waitingQueue.peek();
            if(nextInLine.getNumberOfItems() > customer.getNumberOfItems() && customer.getNumberOfItems() > 5) {
                temporaryList.add(nextInLine);
                this.waitingQueue.remove();
            } else {
                this.waitingQueue.add(customer);
                while(temporaryList.size() > 0) {
                    Customer temporaryCustomer = (Customer) temporaryList.peek();
                    this.waitingQueue.add(temporaryCustomer);
                    temporaryList.remove();
                }
                break;
            }
        }
        if(this.waitingQueue.isEmpty())
        {
            this.waitingQueue.add(customer);
        }
        if(this.waitingQueue.size() >= this.maximumQueueLength) {
            if(this.customersBeingHelped != null) {
                this.maximumQueueLength = this.waitingQueue.size() + 1;
            }else {
                this.maximumQueueLength = this.waitingQueue.size();
            }
        }
    }
}
