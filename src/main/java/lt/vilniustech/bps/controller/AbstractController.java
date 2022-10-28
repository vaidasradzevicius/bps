package lt.vilniustech.bps.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lt.vilniustech.bps.BpsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public abstract class AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    protected Scene getScene(ActionEvent event) {
        Node node = (Node) event.getSource();
        return node.getScene();
    }

    protected void changeScene(ActionEvent event, View view) {
        URL viewUrl = BpsApplication.class.getResource(view.getViewName());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(viewUrl);
            fxmlLoader.setControllerFactory(BpsApplication.INJECTOR::getInstance);
            Parent newRoot = fxmlLoader.load();
            getScene(event).setRoot(newRoot);
        } catch (IOException e) {
            logger.error("Could not load view: " + view.getViewName(), e);
            showDialog(Alert.AlertType.ERROR, "Įvyko nenumatyta klaida. Prašome bandyti dar kartą");
        }
    }

    /**
     * Shows a standard dialog with given message and OK button.
     *
     * @param alertType type of alert, e.g. INFORMATION, ERROR, etc.
     * @param text a text message to display in the dialog
     */
    protected void showDialog(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType, text, ButtonType.OK);
        alert.show();
    }

    /**
     * Shows a confirmation dialog with two buttons: Cancel and OK.
     *
     * @param text a text message to display in the dialog
     * @return true if OK button was clicked and false - otherwise
     */
    protected boolean showConfirmationDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> buttonClicked = alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }
}
