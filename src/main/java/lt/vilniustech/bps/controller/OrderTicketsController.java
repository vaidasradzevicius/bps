package lt.vilniustech.bps.controller;

import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXDateCell;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Route;
import lt.vilniustech.bps.dto.Station;
import lt.vilniustech.bps.dto.StopTime;
import lt.vilniustech.bps.dto.Ticket;
import lt.vilniustech.bps.service.OrderingFlowSingleton;
import lt.vilniustech.bps.service.ServiceOperations;
import lt.vilniustech.bps.util.TicketPriceUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OrderTicketsController extends AbstractController implements Initializable  {
    private static final BigDecimal PRICE_FOR_ONE_STATION_TRIP = new BigDecimal("0.55");

    private final ServiceOperations serviceOps;
    private final OrderingFlowSingleton orderingFlowSingleton;

    @FXML
    private MFXComboBox<Route> routeDropdown;
    @FXML
    private MFXComboBox<Station> startStationDropdown;
    @FXML
    private MFXComboBox<Station> destinationStationDropdown;
    @FXML
    private MFXDatePicker tripDatePicker;
    @FXML
    private MFXListView<StopTime> stopTimesListView;

    @Inject
    public OrderTicketsController(ServiceOperations serviceOps, OrderingFlowSingleton orderingFlowSingleton) {
        this.serviceOps = serviceOps;
        this.orderingFlowSingleton = orderingFlowSingleton;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderingFlowSingleton.clearOrderingFlow();
        routeDropdown.setItems(serviceOps.getAllRoutes());
        disablePastDatesForDatepicker(tripDatePicker);
        stopTimesListView.getSelectionModel().setAllowsMultipleSelection(false);
    }

    public void onRouteChanged() {
        Route selectedRoute = routeDropdown.valueProperty().get();

        startStationDropdown.clearSelection();
        destinationStationDropdown.clearSelection();
        destinationStationDropdown.setItems(FXCollections.emptyObservableList());
        if (selectedRoute == null) {
            startStationDropdown.setItems(FXCollections.emptyObservableList());
        } else {
            startStationDropdown.setItems(serviceOps.getStationsForStartSelection(selectedRoute));
        }
    }

    public void onStartStationChanged() {
        Station station = startStationDropdown.valueProperty().get();
        destinationStationDropdown.clearSelection();
        if (station == null) {
            destinationStationDropdown.setItems(FXCollections.emptyObservableList());
        } else {
            Route selectedRoute = routeDropdown.valueProperty().get();
            destinationStationDropdown.setItems(serviceOps.getStationsForDestinationSelection(selectedRoute, station));
        }
    }

    public void onSearchClicked() {
        Station startStation = startStationDropdown.valueProperty().get();
        Station destinationStation = destinationStationDropdown.valueProperty().get();
        LocalDate selectedDate = tripDatePicker.getValue();
        if (startStation == null || destinationStation == null || selectedDate == null) {
            showDialog(Alert.AlertType.WARNING, "Prašome pasirinkti kelionės pradžios, pabaigos stoteles ir datą");
        } else {
            stopTimesListView.setItems(serviceOps.getStopTimesForStation(startStation, selectedDate));
        }
    }

    public void onClearClicked() {
        routeDropdown.clearSelection();
        startStationDropdown.clearSelection();
        destinationStationDropdown.clearSelection();
        tripDatePicker.clear();
        stopTimesListView.getSelectionModel().clearSelection();
        stopTimesListView.getItems().clear();
    }

    public void onBackClicked(ActionEvent actionEvent) {
        changeScene(actionEvent, View.MAIN);
    }

    public void onBuyClicked(ActionEvent event) {
        Route selectedRoute = routeDropdown.getSelectedItem();
        Station startStation = startStationDropdown.getSelectedItem();
        Station destinationStation = destinationStationDropdown.getSelectedItem();
        LocalDate selectedDate = tripDatePicker.getValue();
        List<StopTime> selectedStopTimes = stopTimesListView.getSelectionModel().getSelectedValues();
        if (selectedRoute == null || startStation == null || destinationStation == null || selectedDate == null
                || CollectionUtils.isEmpty(selectedStopTimes)) {
            // Not all required fields are selected
            showDialog(Alert.AlertType.WARNING, "Prašome išsirinkti kelionės maršrutą ir laiką");
            return;
        }

        BigDecimal tripPrice = calculatePriceForTrip(startStation, destinationStation);
        // TODO: Remove unused code
        /*if (orderingFlowSingleton.isOrderInProgress()) {
            // Another ticket is being added to an existing order
            Order order = orderingFlowSingleton.getOrder();
            Ticket ticket = new Ticket(selectedRoute, startStation, destinationStation, order, tripPrice, false);

            orderingFlowSingleton.getTicketsInOrder().add(ticket);
            order.setTotalPrice(order.getTotalPrice().add(tripPrice));
        } else {
            // The first ticket is being added to a new order
            Order order = new Order(false, LocalDateTime.now(), tripPrice);
            Ticket ticket = new Ticket(selectedRoute, startStation, destinationStation, order, tripPrice, false);

            orderingFlowSingleton.setOrder(order);
            orderingFlowSingleton.addTicketToOrder(ticket);
        }*/

        Order order = new Order(false, tripPrice);
        Ticket ticket = new Ticket(selectedRoute, startStation, destinationStation, order, tripPrice, false, false);

        orderingFlowSingleton.setOrder(order);
        orderingFlowSingleton.setFullPriceTicketTemplate(ticket);

        changeScene(event, View.TICKET_CART);
    }

    private void disablePastDatesForDatepicker(MFXDatePicker datePicker) {
        datePicker.setCellFactory(param -> new MFXDateCell(datePicker, param) {
            @Override
            public void updateItem(LocalDate date) {
                super.updateItem(date);
                setDisable(date.compareTo(LocalDate.now()) < 0 );
            }
        });
    }

    private BigDecimal calculatePriceForTrip(Station startStation, Station destinationStation) {
        int stationCount = destinationStation.getStopOrderNo() - startStation.getStopOrderNo();
        return TicketPriceUtil.roundPrice(PRICE_FOR_ONE_STATION_TRIP.multiply(BigDecimal.valueOf(stationCount)));
    }

}
