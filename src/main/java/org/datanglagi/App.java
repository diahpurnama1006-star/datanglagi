package org.datanglagi;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/loading.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("datanglagi");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}