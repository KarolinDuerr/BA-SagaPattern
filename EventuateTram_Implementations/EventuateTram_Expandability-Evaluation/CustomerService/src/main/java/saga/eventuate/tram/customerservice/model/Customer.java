package saga.eventuate.tram.customerservice.model;

import javax.persistence.*;

@Entity
@Table(name = "customers")
@Access(AccessType.FIELD)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private String eMail;

    public Customer() {
    }

    public Customer(final String name, final Address address, final String eMail) {
        this.name = name;
        this.address = address;
        this.eMail = eMail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", eMail='" + eMail + '\'' +
                '}';
    }
}
