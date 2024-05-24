package pl.rowerek.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rowerek.common.CustomerId;

interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
}
