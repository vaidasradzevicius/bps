package lt.vilniustech.bps.controller;

public enum View {
    MAIN("main-view.fxml"),
    ORDER_TICKETS("order-tickets-view.fxml"),
    TICKET_CART("ticket-cart-view.fxml"),
    PAYMENT("payment-view.fxml"),
    PRINT_TICKETS("print-tickets-view.fxml");

    private final String viewName;

    View(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
