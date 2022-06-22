package com.app.alieninvasion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class MainMenuController implements Serializable {

    @FXML
    private AnchorPane MainMenu;
    @FXML
    private ImageView BGImg;
    @FXML
    private Text HighScoreText, VersionText;

    private static Integer highScore = 0;
    public static MainMenuController mmc;

    @FXML
    public void initialize() throws IOException {
        BGImg.setImage(new Image(String.valueOf(getClass().getResource("/Images/BG.jpeg"))));
        getHighScore();
        VersionText.setText("beta 4.0");
        mmc = this;
    }

    @FXML
    public void onQuit(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void startGame(ActionEvent event) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Main.s.setScene(scene);
    }

    public static void closeGame() throws IOException {
        File myObj = new File("sd.txt");
        if (highScore < GameController.gc.getHighWave()) {
            highScore = GameController.gc.getHighWave();
            FileWriter myFile = new FileWriter(myObj);
            myFile.write(String.valueOf(highScore));
            myFile.close();
        }
    }

    public void getHighScore() throws IOException {
        File myObj = new File("sd.txt");
        if (myObj.createNewFile()) {
            FileWriter myFile = new FileWriter(myObj);
            myFile.write("0");
            myFile.close();
        }
        Scanner myReader = new Scanner(myObj);
        String data = myReader.nextLine();
        highScore = Integer.parseInt(data);
        myReader.close();
        HighScoreText.setText("High Score: " + highScore);
    }

    public static AnchorPane getAp() {
        return mmc.MainMenu;
    }
}
