package org.datanglagi.controller;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.util.Duration;
import org.datanglagi.App;

public class LoadingController {

    @FXML
    public void initialize() {

        PauseTransition delay =
                new PauseTransition(Duration.seconds(3));

        delay.setOnFinished(event -> {
            try {
                App.setRoot("login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delay.play();
    }
}