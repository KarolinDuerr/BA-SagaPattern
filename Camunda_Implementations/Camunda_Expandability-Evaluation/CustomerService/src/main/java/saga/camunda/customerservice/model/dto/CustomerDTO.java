package saga.camunda.customerservice.model.dto;

import saga.camunda.customerservice.model.Address;

public class CustomerDTO {

    private Long id;

    private String name;

    private Address address;

    private String eMail;

    public CustomerDTO() {

    }

    public CustomerDTO(final Long id, final String name, final Address address, final String eMail) {
        this.id = id;
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
        return "CustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", eMail='" + eMail + '\'' +
                '}';
    }
}
