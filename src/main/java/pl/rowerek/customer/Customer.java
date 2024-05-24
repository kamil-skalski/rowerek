package pl.rowerek.customer;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import pl.rowerek.common.CustomerId;

@Entity
@Getter
class Customer {

    @EmbeddedId
    private CustomerId id;

    private String firstName;

    private String lastName;

    @Version
    private int version;

    public Customer(String firstName, String lastName) {
        this.id = CustomerId.newOne();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //For JPA
    protected Customer() {
    }
}
