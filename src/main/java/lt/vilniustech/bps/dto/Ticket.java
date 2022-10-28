package lt.vilniustech.bps.dto;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Entity("tickets")
public class Ticket implements Cloneable {
    @Id
    private ObjectId id;
    @Reference
    private Route route;
    @Reference
    private Station startStation;
    @Reference
    private Station destinationStation;
    @Reference
    private Order order;
    private BigDecimal price;
    private boolean loweredPrice;
    private boolean used;

    public Ticket(Route route, Station startStation, Station destinationStation, Order order, BigDecimal price,
                  boolean loweredPrice, boolean used) {
        this.route = route;
        this.startStation = startStation;
        this.destinationStation = destinationStation;
        this.order = order;
        this.price = price;
        this.loweredPrice = loweredPrice;
        this.used = used;
    }

    public Ticket() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(Station destinationStation) {
        this.destinationStation = destinationStation;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isLoweredPrice() {
        return loweredPrice;
    }

    public void setLoweredPrice(boolean loweredPrice) {
        this.loweredPrice = loweredPrice;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public Ticket clone() {
        try {
            return (Ticket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
