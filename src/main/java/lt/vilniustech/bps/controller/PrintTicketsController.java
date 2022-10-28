package lt.vilniustech.bps.controller;

import com.google.inject.Inject;
import com.itextpdf.text.DocumentException;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import lt.vilniustech.bps.service.OrderingFlowSingleton;
import lt.vilniustech.bps.service.TicketPrintingService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrintTicketsController extends AbstractController implements Initializable {

    private final OrderingFlowSingleton orderingFlowSingleton;
    private final TicketPrintingService printingService = new TicketPrintingService();

    @FXML
    private MFXProgressSpinner loadingIndicator;

    @Inject
    public PrintTicketsController(OrderingFlowSingleton orderingFlowSingleton) {
        this.orderingFlowSingleton = orderingFlowSingleton;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onDownloadTicketsClicked(ActionEvent event) {
        File directoryToSaveFileIn = getDirectoryForDocumentSave(event);
        if (directoryToSaveFileIn == null) {
            return;
        }

        setLoading(true);
        try {
            printingService.generateTicketsPdf(orderingFlowSingleton.getOrder(), orderingFlowSingleton.getTicketsInOrder(),
                    directoryToSaveFileIn.getAbsolutePath());
        } catch (DocumentException | IOException e) {
            setLoading(false);
            showDialog(Alert.AlertType.ERROR, "Įvyko klaida bandant sugeneruoti PDF dokumentą");
        }

        setLoading(false);
        showDialog(Alert.AlertType.INFORMATION, "PDF dokumentas sėkmingai išsaugotas");
    }

    public void onSendTicketsViaEmailClicked() {
    }

    public void onBackToMainWindowClicked(ActionEvent event) {
        changeScene(event, View.MAIN);
    }

    private File getDirectoryForDocumentSave(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        return dirChooser.showDialog(getScene(event).getWindow());
    }

    private void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
    }

}
