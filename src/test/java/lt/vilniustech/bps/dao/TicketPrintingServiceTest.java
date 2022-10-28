package lt.vilniustech.bps.dao;

import com.itextpdf.text.DocumentException;
import lt.vilniustech.bps.dto.Customer;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Route;
import lt.vilniustech.bps.dto.Station;
import lt.vilniustech.bps.dto.Ticket;
import lt.vilniustech.bps.service.TicketPrintingService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TicketPrintingServiceTest {
    private final TicketPrintingService ticketPrintingService = new TicketPrintingService();

    @Test
    public void generateTicketsPdf_shouldGeneratePdf() throws DocumentException, IOException {
        Order order = new Order();
        order.setBuyer(new Customer("Jonas", "Petraitis", "jonas@petraitis.lt"));
        order.setPaid(false);
        order.setDateTimeOrdered(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.valueOf(11));

        Route route = new Route("Vilnius - Kaunas");
        Station startStation = new Station(route, "Vilnius", 1);
        Station destinationStation = new Station(route, "Kaunas", 10);

        Ticket firstTicket = new Ticket(route, startStation, destinationStation, order, BigDecimal.valueOf(7.5), false, false);
        Ticket secondTicket = new Ticket(route, startStation, destinationStation, order, BigDecimal.valueOf(3.5), true, false);

        List<Ticket> tickets = List.of(firstTicket, secondTicket);

        ticketPrintingService.generateTicketsPdf(order, tickets, "C:/bps docs");
    }

}
