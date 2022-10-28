package lt.vilniustech.bps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lt.vilniustech.bps.config.GuiceBindingConfiguration;
import lt.vilniustech.bps.controller.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


public class BpsApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(BpsApplication.class);

    public static final String APP_TITLE = "Bilietų pardavimo sistema";
    public static final Injector INJECTOR = Guice.createInjector(new GuiceBindingConfiguration());

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BpsApplication.class.getResource(View.MAIN.getViewName()));
        fxmlLoader.setControllerFactory(INJECTOR::getInstance);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_TITLE);
        Image logoImage = getApplicationLogoImage();
        if (logoImage != null) {
            stage.getIcons().add(logoImage);
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Image getApplicationLogoImage() {
        try (InputStream imageStream = BpsApplication.class.getResourceAsStream("/img/BPS_logo.png")) {
            if (imageStream == null) {
                throw new IOException("Image not found.");
            }
            return new Image(imageStream);
        } catch (IOException e) {
            logger.error("Could not load application logo image.", e);
            return null;
        }
    }
}