package lt.vilniustech.bps.controller;

import javafx.event.ActionEvent;

public class MainWindowController extends AbstractController {

    public void onBuyTicketClick(ActionEvent event) {
        changeScene(event, View.ORDER_TICKETS);
    }

    public void onExitClick(ActionEvent event) {
        getScene(event).getWindow().hide();
    }
}
