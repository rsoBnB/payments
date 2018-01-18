package si.fri.rso.rsobnb.payments;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;

@Entity(name = "payment")
@NamedQueries(value =
        {
                @NamedQuery(name = "Payment.getAll", query = "SELECT r FROM payment r"),
                @NamedQuery(name = "Payment.findByUser", query = "SELECT r FROM payment r WHERE r.realEstateId = " + ":realEstateId")
        })
@UuidGenerator(name = "idGenerator")
public class Payment {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "realestate_id")
    private String realEstateId;

    @Column(name = "lease_id")
    private String leaseId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "date")
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(String realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}