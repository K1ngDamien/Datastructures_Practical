import java.util.Comparator;

public class SortCustomersByQueuingTime implements Comparator<Customer> {
    @Override
    public int compare(Customer customer, Customer nextCustomer) {
        return customer.getQueuedAt().compareTo(nextCustomer.getQueuedAt());
    }
}
