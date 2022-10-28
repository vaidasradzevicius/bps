package lt.vilniustech.bps.config;

import com.google.inject.AbstractModule;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lt.vilniustech.bps.controller.MainWindowController;
import lt.vilniustech.bps.controller.OrderTicketsController;
import lt.vilniustech.bps.controller.PaymentController;
import lt.vilniustech.bps.controller.PrintTicketsController;
import lt.vilniustech.bps.controller.TicketCartController;
import lt.vilniustech.bps.dao.DataAccessOperations;
import lt.vilniustech.bps.service.ServiceOperations;

public class GuiceBindingConfiguration extends AbstractModule {
    private static final String DATABASE_NAME = "bps";

    @Override
    protected void configure() {
        bind(DataAccessOperations.class);
        bind(ServiceOperations.class);
        bind(MainWindowController.class);
        bind(OrderTicketsController.class);
        bind(TicketCartController.class);
        bind(PaymentController.class);
        bind(PrintTicketsController.class);
        bind(Datastore.class).toInstance(Morphia.createDatastore(DATABASE_NAME));
    }

}
