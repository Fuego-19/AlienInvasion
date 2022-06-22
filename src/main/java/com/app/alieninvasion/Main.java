package com.app.alieninvasion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;


public class Main extends Application implements Serializable {

    public static Stage s;
    public static double gameScale = 1;

    @Override
    public void start(Stage stage) throws IOException {
//        if (Screen.getPrimary().getBounds().getHeight() < 800) {
            gameScale = (Screen.getPrimary().getBounds().getHeight() - 50) / 800;
            if ((gameScale * 1200) > (Screen.getPrimary().getBounds().getWidth() - 50)) {
                gameScale = (Screen.getPrimary().getBounds().getWidth() - 50) / 1200;
            }
//        }
        s = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setFullScreen(true);
        System.out.println("scale = " + gameScale);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/Images/sAlienBRider.png"))));
//        stage.setResizable(false);
        stage.setTitle("Alien Invasion beta 4.0");
        stage.setScene(scene);
//        stage.setHeight(800 * Main.gameScale);
//        stage.setWidth(1200 * Main.gameScale);
        stage.show();
        Sound music = new Sound();
        music.setFile(6);
        music.loop();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            gameScale = stage.getWidth() / 1200;
//            stage.setHeight(800 * gameScale);
            resize();
            System.out.println("scale = " + gameScale);
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            gameScale = stage.getHeight() / 800;
//            stage.setWidth(1200 * gameScale);
            resize();
            System.out.println("scale = " + gameScale);
        });
    }

    private void resize() {
        for (Node n : MainMenuController.getAp().getChildren()) {
            Main.setToScale(n);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static boolean collide(ImageView i1, ImageView i2) {
        double minX1 = i1.getBoundsInParent().getMinX();
        double minY1 = i1.getBoundsInParent().getMinY();
        double maxX1 = minX1 + i1.getImage().getWidth();
        double maxY1 = minY1 + i1.getImage().getHeight();

        double minX2 = i2.getBoundsInParent().getMinX();
        double minY2 = i2.getBoundsInParent().getMinY();
        double maxX2 = minX2 + i2.getImage().getWidth();
        double maxY2 = minY2 + i2.getImage().getHeight();
//
//        System.out.println();
//        System.out.println(i1.getImage().getUrl() + " " + i2.getImage().getUrl());
//        System.out.println("i1: " + minX1 + ", " + minY1 + "; " + maxX1 + ", " + maxY1);
//        System.out.println("i2: " + minX2 + ", " + minY2 + "; " + maxX2 + ", " + maxY2);

        return (minX1 <= maxX2 && maxX1 >= minX2 && minY1 <= maxY2 && maxY1 >= minY2);
    }

    public static void setToScale(@NotNull Node n) {
//        n.setScaleX(gameScale);
//        n.setScaleY(gameScale);
        if (n.getLayoutX() < 0) {
            n.setTranslateX(n.getTranslateX() + ((n.getLayoutX() * gameScale) - (n.getLayoutX() + n.getTranslateX())));
            System.out.println(n.getLayoutX() + ", " + n.getTranslateX());
        }

//        n.setTranslateX(n.getTranslateX() + ((n.getLayoutX() * gameScale) - (n.getLayoutX() + n.getTranslateX())));
        n.setTranslateY(n.getTranslateY() + (n.getLayoutY() * gameScale) - (n.getLayoutY() + n.getTranslateY()));
//        n.setLayoutX(n.getLayoutX() + (n.getLayoutX() * gameScale) - n.getLayoutX());
//        n.setLayoutY(n.getLayoutY() + (n.getLayoutY() * gameScale) - n.getLayoutY());
//        n.setLayoutX(n.getLayoutX() + ((n.getLayoutX() * gameScale) - n.getLayoutX()));
//        n.setLayoutY(n.getLayoutY() + ((n.getLayoutY() * gameScale) - n.getLayoutY()));
    }
}