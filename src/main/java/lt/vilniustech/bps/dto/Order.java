package lt.vilniustech.bps.dto;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity("orders")
public class Order {
    @Id
    private ObjectId id;
    private boolean paid;
    private LocalDateTime dateTimeOrdered;
    private BigDecimal totalPrice;
    @Reference
    private Customer buyer;

    public Order(boolean paid, BigDecimal totalPrice) {
        this.paid = paid;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getDateTimeOrdered() {
        return dateTimeOrdered;
    }

    public void setDateTimeOrdered(LocalDateTime dateTimeOrdered) {
        this.dateTimeOrdered = dateTimeOrdered;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }
}
