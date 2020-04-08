package at.dalex.addressbook;

import at.dalex.addressbook.repository.ContactCSVRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("mainWindow.fxml").openStream());
        ViewController controller = fxmlLoader.getController();

        primaryStage.setTitle("Address Book");
        primaryStage.setScene(new Scene(root, 650, 250));
        primaryStage.setOnShowing(controller);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
