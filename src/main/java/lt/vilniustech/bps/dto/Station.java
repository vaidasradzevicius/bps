package lt.vilniustech.bps.dto;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("stations")
public class Station {
    @Id
    private ObjectId id;
    @Reference
    private Route route;
    private String stopName;
    private Integer stopOrderNo;

    public Station() {
    }

    public Station(Route route, String stopName, Integer stopOrderNo) {
        this.route = route;
        this.stopName = stopName;
        this.stopOrderNo = stopOrderNo;
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

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Integer getStopOrderNo() {
        return stopOrderNo;
    }

    public void setStopOrderNo(Integer stopOrderNo) {
        this.stopOrderNo = stopOrderNo;
    }

    public String toString() {
        return stopName;
    }
}
