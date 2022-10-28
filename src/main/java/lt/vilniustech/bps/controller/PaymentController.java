package lt.vilniustech.bps.controller;

import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Ticket;
import lt.vilniustech.bps.service.OrderingFlowSingleton;
import lt.vilniustech.bps.service.ServiceOperations;
import lt.vilniustech.bps.util.TicketPriceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController extends AbstractController implements Initializable {
    private final OrderingFlowSingleton orderingFlowSingleton;
    private final ServiceOperations serviceOperations;

    @FXML
    private Label totalPriceLabel;
    @FXML
    private MFXTextField buyerNameTextField;
    @FXML
    private MFXTextField buyerSurnameTextField;
    @FXML
    private MFXTextField buyerEmailTextField;
    @FXML
    private ToggleGroup paymentMethodGroup;

    @Inject
    public PaymentController(OrderingFlowSingleton orderingFlowSingleton, ServiceOperations serviceOperations) {
        this.orderingFlowSingleton = orderingFlowSingleton;
        this.serviceOperations = serviceOperations;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTotalPriceLabel();
    }

    public void onPayClicked(ActionEvent event) {
        String buyerName = buyerNameTextField.getText();
        String buyerSurname = buyerSurnameTextField.getText();
        String buyerEmail = buyerEmailTextField.getText();
        Toggle paymentMethod = paymentMethodGroup.getSelectedToggle();

        boolean valid = isFormValid(buyerName, buyerSurname, buyerEmail, paymentMethod);
        if (valid) {
            orderingFlowSingleton.finishOrderData(buyerName, buyerSurname, buyerEmail);
            Order savedOrder = serviceOperations.saveOrder(orderingFlowSingleton.getOrder());
            List<Ticket> savedTickets = serviceOperations.saveTickets(orderingFlowSingleton.getTicketsInOrder());
            orderingFlowSingleton.setOrder(savedOrder);
            orderingFlowSingleton.setTicketsInOrder(savedTickets);

            showDialog(Alert.AlertType.INFORMATION, "Bilietai sėkmingai apmokėti");
            changeScene(event, View.PRINT_TICKETS);
        }
    }

    public void onCancelClicked(ActionEvent event) {
        if (showConfirmationDialog("Ar tikrai norite atšaukti užsakymą?")) {
            orderingFlowSingleton.clearOrderingFlow();
            changeScene(event, View.MAIN);
        }
    }

    private void setTotalPriceLabel() {
        String priceInText = TicketPriceUtil.formatPrice(orderingFlowSingleton.getOrder().getTotalPrice());
        totalPriceLabel.setText(priceInText);
    }

    private boolean isFormValid(String buyerName, String buyerSurname, String buyerEmail, Toggle paymentMethod) {
        if (StringUtils.isBlank(buyerName) || StringUtils.isBlank(buyerSurname) || StringUtils.isBlank(buyerEmail)) {
            showDialog(Alert.AlertType.WARNING, "Prašome įvesti savo duomenis");
            return false;
        }
        if (!EmailValidator.getInstance().isValid(buyerEmail)) {
            showDialog(Alert.AlertType.WARNING, "Neteisingai įvestas el. pašto adresas. Prašome patikrinti ir bandyti dar kartą");
            return false;
        }
        if (paymentMethod == null) {
            showDialog(Alert.AlertType.WARNING, "Prašome pasirinkti mokėjimo būdą");
            return false;
        }

        return true;
    }

}
