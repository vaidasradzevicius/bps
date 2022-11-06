package lt.vilniustech.bps.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lt.vilniustech.bps.dao.DataAccessOperations;
import lt.vilniustech.bps.dto.Customer;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Route;
import lt.vilniustech.bps.dto.Station;
import lt.vilniustech.bps.dto.StopTime;
import lt.vilniustech.bps.dto.Ticket;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Singleton
public class ServiceOperations {

    private final DataAccessOperations dao;

    @Inject
    public ServiceOperations(DataAccessOperations dao) {
        this.dao = dao;
    }

    public ObservableList<Route> getAllRoutes() {
        List<Route> routes = dao.fetchAllItemsByType(Route.class);
        return FXCollections.observableList(routes);
    }

    public ObservableList<Station> getStationsForStartSelection(Route route) {
        List<Station> stations = dao.fetchStationsByRoute(route);
        if (CollectionUtils.isNotEmpty(stations)) {
            stations.remove(stations.size() - 1);
        }

        return FXCollections.observableList(stations);
    }

    public ObservableList<Station> getStationsForDestinationSelection(Route route, Station startStation) {
        List<Station> stations = dao.fetchStationsByRouteAndStartStation(route, startStation);
        return FXCollections.observableList(stations);
    }

    public ObservableList<StopTime> getStopTimesForStation(Station station, LocalDate selectedDate) {
        List<StopTime> stopTimes = dao.fetchStopTimes(station);
        eliminatePassedTimes(stopTimes, selectedDate);

        return FXCollections.observableList(stopTimes);
    }

    public List<Ticket> saveTickets(List<Ticket> tickets) {
        return dao.saveItemList(tickets);
    }

    public Order saveOrder(Order order) {
        Customer savedBuyer = dao.saveItem(order.getBuyer());
        order.setBuyer(savedBuyer);
        return dao.saveItem(order);
    }

    private void eliminatePassedTimes(List<StopTime> stopTimes, LocalDate selectedDate) {
        if (isCurrentDay(selectedDate)) {
            stopTimes.removeIf(this::isStopTimeBeforeNow);
        }
    }

    private boolean isCurrentDay(LocalDate date) {
        return date.isEqual(LocalDate.now());
    }

    private boolean isStopTimeBeforeNow(StopTime stopTime) {
        return stopTime.getTime().isBefore(LocalTime.now());
    }

}
