package lt.vilniustech.bps.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.filters.Filters;
import lt.vilniustech.bps.dto.Route;
import lt.vilniustech.bps.dto.Station;
import lt.vilniustech.bps.dto.StopTime;

import java.util.List;

@Singleton
public class DataAccessOperations {

    private final Datastore datastore;

    @Inject
    public DataAccessOperations(Datastore datastore) {
        this.datastore = datastore;
    }

    public <T> List<T> fetchAllItemsByType(Class<T> clazz) {
        return datastore.find(clazz).stream().toList();
    }

    public List<Station> fetchStationsByRoute(Route route) {
        return datastore.find(Station.class)
                .filter(Filters.eq("route", route))
                .iterator(new FindOptions()
                        .sort(Sort.ascending("stopOrderNo")))
                .toList();
    }

    public List<Station> fetchStationsByRouteAndStartStation(Route route, Station startStation) {
        Filter[] filters = {
                Filters.eq("route", route),
                Filters.gt("stopOrderNo", startStation.getStopOrderNo())
        };
        return datastore.find(Station.class)
                .filter(filters)
                .iterator(new FindOptions()
                        .sort(Sort.ascending("stopOrderNo")))
                .toList();
    }

    public List<StopTime> fetchStopTimes(Station station) {
        return datastore.find(StopTime.class)
                .filter(Filters.eq("station", station))
                .iterator(new FindOptions()
                        .sort(Sort.ascending("time")))
                .toList();
    }

    public <T> List<T> saveItemList(List<T> itemsToSave) {
        return datastore.save(itemsToSave);
    }

    public <T> T saveItem(T itemToSave) {
        return datastore.save(itemToSave);
    }

}
