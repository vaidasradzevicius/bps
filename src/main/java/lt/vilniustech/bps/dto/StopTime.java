package lt.vilniustech.bps.dto;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity("stopTimes")
public class StopTime {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Id
    private ObjectId id;
    @Reference
    private Station station;
    private LocalTime time;

    public StopTime() {
    }

    public StopTime(Station station, LocalTime time) {
        this.station = station;
        this.time = time;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String toString() {
        return TIME_FORMAT.format(time);
    }
}
