package lt.vilniustech.bps.controller;

import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.FXCollectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lt.vilniustech.bps.service.OrderingFlowSingleton;
import lt.vilniustech.bps.util.TicketPriceUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class TicketCartController extends AbstractController implements Initializable {
    private final OrderingFlowSingleton orderingFlowSingleton;

    @FXML
    private Label totalPriceLabel;
    @FXML
    private MFXComboBox<Integer> fullPriceTicketCountDropdown;
    @FXML
    private MFXComboBox<Integer> lowerPriceTicketCountDropdown;

    @Inject
    public TicketCartController(OrderingFlowSingleton orderingFlowSingleton) {
        this.orderingFlowSingleton = orderingFlowSingleton;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTicketCountDropdownValues(fullPriceTicketCountDropdown);
        initTicketCountDropdownValues(lowerPriceTicketCountDropdown);

        fullPriceTicketCountDropdown.selectItem(1);
        lowerPriceTicketCountDropdown.selectItem(0);
    }

    public void onFullPriceTicketCountDropdownChanged() {
        recalculateTotalPrice();
    }

    public void onLowerPriceTicketCountDropdownChanged() {
        recalculateTotalPrice();
    }

    public void onPayClicked(ActionEvent event) {
        int fullPriceTicketCount = fullPriceTicketCountDropdown.getValue();
        int lowerPriceTicketCount = lowerPriceTicketCountDropdown.getValue();

        if (fullPriceTicketCount > 0 || lowerPriceTicketCount > 0) {
            orderingFlowSingleton.generateTickets(fullPriceTicketCount, lowerPriceTicketCount);
            changeScene(event, View.PAYMENT);
        } else {
            // No tickets selected
            showDialog(Alert.AlertType.WARNING, "Prašome pasirinkti bent vieną bilietą");
        }
    }

    public void onCancelClicked(ActionEvent event) {
        if (showConfirmationDialog("Ar tikrai norite atšaukti užsakymą?")) {
            orderingFlowSingleton.clearOrderingFlow();
            changeScene(event, View.MAIN);
        }
    }

    private void initTicketCountDropdownValues(MFXComboBox<Integer> dropdown) {
        ObservableList<Integer> list = IntStream.rangeClosed(0, 10)
                .boxed()
                .collect(FXCollectors.toList());
        dropdown.setItems(list);
    }

    private void updateTotalPriceLabel() {
        String priceInText = TicketPriceUtil.formatPrice(orderingFlowSingleton.getOrder().getTotalPrice());
        totalPriceLabel.setText(priceInText);
    }

    private void recalculateTotalPrice() {
        if (fullPriceTicketCountDropdown.getValue() == null || lowerPriceTicketCountDropdown.getValue() == null) {
            // Dropdowns are not initialized yet
            return;
        }

        BigDecimal oneFullTicketPrice = orderingFlowSingleton.getFullPriceTicketTemplate().getPrice();
        BigDecimal oneLowerTicketPrice = TicketPriceUtil.calculateLowerTicketPrice(oneFullTicketPrice);
        BigDecimal fullPriceTicketCount = BigDecimal.valueOf(fullPriceTicketCountDropdown.getValue());
        BigDecimal lowerPriceTicketCount = BigDecimal.valueOf(lowerPriceTicketCountDropdown.getValue());

        BigDecimal totalPrice = BigDecimal.ZERO;
        if (fullPriceTicketCount.signum() > 0) {
            // If user selected more than one full price ticket
            totalPrice = oneFullTicketPrice.multiply(fullPriceTicketCount);
        }
        if (lowerPriceTicketCount.signum() > 0) {
            // If user selected more than one lower price ticket
            totalPrice = totalPrice.add(oneLowerTicketPrice.multiply(lowerPriceTicketCount));
        }

        orderingFlowSingleton.getOrder().setTotalPrice(totalPrice);

        updateTotalPriceLabel();
    }

}
