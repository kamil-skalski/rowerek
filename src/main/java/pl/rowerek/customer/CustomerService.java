package pl.rowerek.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.common.CustomerId;
import pl.rowerek.common.CustomerNotFoundException;
import pl.rowerek.customer.dto.CreateCustomerDto;
import pl.rowerek.customer.dto.CustomerDto;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto getCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> new CustomerDto(customer.getId().id(), customer.getFirstName(), customer.getLastName()))
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public CustomerId addCustomer(CreateCustomerDto customerDto) {
        var customer = new Customer(customerDto.firstName(), customerDto.lastName());
        return customerRepository.save(customer).getId();
    }

    public void checkCustomerExists(CustomerId id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
    }
}