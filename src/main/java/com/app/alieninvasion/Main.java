package com.app.alieninvasion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.Serializable;


public class Main extends Application implements Serializable {

    public static Stage s;
    @Override
    public void start(Stage stage) throws IOException {
        s = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
        Sound music = new Sound();
        music.setFile(6);
        music.loop();
    }

    public static void main(String[] args) {
        launch();
    }
}