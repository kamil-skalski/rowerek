package pl.rowerek.common;

public class CustomerNotFoundException extends RuntimeException {

    public static String createExceptionMessage(CustomerId customerId) {
        return String.format("Could not find customer with customerId: %s", customerId.id());
    }

    public CustomerNotFoundException(final CustomerId customerId) {
        super(createExceptionMessage(customerId));
    }
}
