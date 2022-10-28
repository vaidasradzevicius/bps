package lt.vilniustech.bps.service;

import com.google.inject.Singleton;
import lt.vilniustech.bps.dto.Customer;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Ticket;
import lt.vilniustech.bps.util.TicketPriceUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OrderingFlowSingleton {
    private Order order;
    private Ticket fullPriceTicketTemplate;
    private List<Ticket> ticketsInOrder;

    public OrderingFlowSingleton() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Ticket getFullPriceTicketTemplate() {
        return fullPriceTicketTemplate;
    }

    public void setFullPriceTicketTemplate(Ticket fullPriceTicketTemplate) {
        this.fullPriceTicketTemplate = fullPriceTicketTemplate;
    }

    public List<Ticket> getTicketsInOrder() {
        return ticketsInOrder;
    }

    public void setTicketsInOrder(List<Ticket> ticketsInOrder) {
        this.ticketsInOrder = ticketsInOrder;
    }

    public void clearOrderingFlow() {
        order = null;
        fullPriceTicketTemplate = null;
        ticketsInOrder = new ArrayList<>();
    }

    public void generateTickets(int fullPriceTicketCount, int lowerPriceTicketCount) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        ticketsInOrder = new ArrayList<>();
        for (int i = 0; i < fullPriceTicketCount; i++) {
            Ticket fullPriceTicket = fullPriceTicketTemplate.clone();
            ticketsInOrder.add(fullPriceTicket);
            totalPrice = totalPrice.add(fullPriceTicket.getPrice());
        }
        for (int i = 0; i < lowerPriceTicketCount; i++) {
            Ticket lowerPriceTicket = fullPriceTicketTemplate.clone();
            lowerPriceTicket.setPrice(TicketPriceUtil.calculateLowerTicketPrice(fullPriceTicketTemplate.getPrice()));
            lowerPriceTicket.setLoweredPrice(true);
            ticketsInOrder.add(lowerPriceTicket);
            totalPrice = totalPrice.add(lowerPriceTicket.getPrice());
        }

        order.setTotalPrice(totalPrice);
    }

    public void finishOrderData(String buyerName, String buyerSurname, String buyerEmail) {
        order.setDateTimeOrdered(LocalDateTime.now());
        order.setPaid(true);
        order.setBuyer(new Customer(buyerName, buyerSurname, buyerEmail));
    }
}
