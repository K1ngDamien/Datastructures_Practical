/**
 * Supermarket Customer check-out and Cashier simulation
 *
 * @author hbo-ict@hva.nl
 */

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Cashier {

    protected String name;
    protected LinkedList waitingQueue;
    protected LocalTime currentTime;
    protected int totalIdleTime;
    final public int checkOutTimePerCustomer = 20;
    final public int checkOutTimePerItem = 2;
    protected int workedAmount;
    protected int totalWorkedAmount;
    protected int maximumQueueLength;
    protected int totalTimeWorkedAmount;
    protected Customer customersBeingHelped;
    protected int totalCustomers;
    protected List<Integer> waitingTimesList;

    protected Cashier(String name) {
        this.name = name;
        this.waitingQueue = new LinkedList<>();
    }

    /**
     * restart the state if simulation of the cashier to initial time
     * with empty queues
     *
     * @param currentTime
     */
    public void reStart(LocalTime currentTime) {
        this.waitingQueue.clear();
        this.currentTime = currentTime;
        this.totalIdleTime = 0;
        this.maximumQueueLength = 0;
        this.totalWorkedAmount = 0;
        this.totalTimeWorkedAmount = 0;
        this.totalCustomers = 0;
        this.customersBeingHelped = null;
        this.waitingTimesList = new ArrayList<>();
    }

    /**
     * calculate the expected nett checkout time of a customer with a given number of items
     * this may be different for different types of Cashiers
     *
     * @param numberOfItems
     * @return
     */
    public abstract int expectedCheckOutTime(int numberOfItems);

    /**
     * calculate the currently expected waiting time of a given customer for this cashier.
     * this may depend on:
     * a) the type of cashier,
     * b) the remaining work of the cashier's current customer(s) being served
     * c) the position that the given customer may obtain in the queue
     * d) and the workload of the customers in the waiting queue in front of the given customer
     *
     * @param customer
     * @return
     */
    public abstract int expectedWaitingTime(Customer customer);

    public int getMaxWaitingTime() {
        int maximumTimeWaiting= 0;
        for (int timeWaiting : this.waitingTimesList)
        {
            if(timeWaiting > maximumTimeWaiting)
            {
                maximumTimeWaiting = timeWaiting;
            }
        }

        return maximumTimeWaiting;
    }

    public double getAverageWaitingTime() {
        int totalTimeWaiting = 0;
        for (int timeWaiting : this.waitingTimesList)
        {
            totalTimeWaiting += timeWaiting;
        }

        if(totalTimeWaiting == 0) {
            return 0.0;
        }
        return totalTimeWaiting / this.totalCustomers;
    }

    /**
     * proceed the cashier's work until the given targetTime has been reached
     * this work may involve:
     * a) continuing or finishing the current customer(s) begin served
     * b) serving new customers that are waiting on the queue
     * c) sitting idle, taking a break until time has reached targetTime,
     * after which new customers may arrive.
     *
     * @param targetTime
     */

    public void doTheWorkUntil(LocalTime targetTime)
    {
        int timePassed = (int) ChronoUnit.SECONDS.between(this.getCurrentTime(), targetTime);
        this.totalWorkedAmount += timePassed;

        if(this.customersBeingHelped != null) {
            int totalTimeCustomers = checkOutTimePerCustomer + (checkOutTimePerItem * this.customersBeingHelped.getNumberOfItems());
            if(totalTimeCustomers < timePassed) {
                timePassed = timePassed - totalTimeCustomers;
                this.totalTimeWorkedAmount += totalTimeCustomers;
                this.customersBeingHelped = null;
                this.workedAmount = 0;
            }else {
                this.workedAmount += timePassed;
                this.setCurrentTime(targetTime);
                return;
            }
        }

        while(this.waitingQueue.size() > 0) {
            Customer currCustomer = (Customer) this.waitingQueue.peek();
            if(currCustomer.getNumberOfItems() == 0) {
                this.waitingQueue.remove();
                continue;
            }

            int totalTimeCustomers = checkOutTimePerCustomer + (checkOutTimePerItem * currCustomer.getNumberOfItems());
            if(totalTimeCustomers < timePassed ) {
                timePassed = timePassed - totalTimeCustomers;
                this.totalTimeWorkedAmount += totalTimeCustomers;
                waitingTimesList.add((int) ChronoUnit.SECONDS.between( currCustomer.getQueuedAt(), this.getCurrentTime()));
                this.workedAmount = 0;
                this.waitingQueue.remove();


            }else {
                this.workedAmount = timePassed;
                this.customersBeingHelped = currCustomer;
                waitingTimesList.add((int) ChronoUnit.SECONDS.between( currCustomer.getQueuedAt(), this.getCurrentTime()));
                this.waitingQueue.remove();
                break;
            }
        }

        this.setTotalIdleTime(this.totalWorkedAmount - this.totalTimeWorkedAmount);
        this.setCurrentTime(targetTime);
    }

    /**
     * add a new customer to the queue of the cashier
     * the position of the new customer in the queue will depend on the priority configuration of the queue
     *
     * @param customer
     */
    public void add(Customer customer) {
        // TODO add the customer to the queue of the cashier (if check-out is required)
        this.totalCustomers++;
        this.waitingQueue.add(customer);
        if(this.waitingQueue.size() >= this.maximumQueueLength) {
            if(this.customersBeingHelped != null) {
                this.maximumQueueLength = this.waitingQueue.size() + 1;
            }else {
                this.maximumQueueLength = this.waitingQueue.size();
            }
        }
    }


    // TODO implement relevant overrides and/or local classes to be able to
    //  print Cashiers and/or use them in sets, maps and/or priority queues.


    public int getTotalIdleTime() {
        return totalIdleTime;
    }

    public int getTotalCustomers() {
        return this.totalCustomers;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public String getName() {
        return name;
    }

    public int getMaxQueueLength() {
        return maximumQueueLength;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    public void setTotalIdleTime(int totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }

    public LinkedList<Customer> getWaitingQueue() {
        return waitingQueue;
    }

}
