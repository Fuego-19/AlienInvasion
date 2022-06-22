package com.app.alieninvasion;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class GameController implements Serializable {
    @FXML
    private AnchorPane MainPane;
    @FXML
    private GridPane GPane;
    @FXML
    private VBox AlienShips;
    @FXML
    private ImageView destroyImg, HouseImg;
    @FXML
    private Text coinText, WaveText;

    private boolean stop = false;
    public static GameController gc;
    private final Sound music = new Sound();
    private int coins = 0;
    public static int selectedTurret = -1;     // -1 is null, 0 is normal, 1 is bomb, 2 is laser, 9 is destroy
    private ArrayList<Turret> turrets = new ArrayList<>();
    private ArrayList<Alien> aliens = new ArrayList<>();
    private Integer wave, highWave;
    private int chanceN, chanceB, chanceS, chanceF;
    private int aliensInWave, bossInWave;
    private final int waveLength = 20000;
    private final ImageView tempTur = new ImageView();
    private ImageView finalTempTur = tempTur;
    private boolean move = true;

    @FXML
    public void initialize() {
        tempTur.setMouseTransparent(true);
        MainPane.getChildren().add(tempTur);
        MainPane.setOnMouseMoved(me -> {
            if (move) {
                finalTempTur.toFront();
                double dx = me.getSceneX() - finalTempTur.getBoundsInParent().getCenterX();
                double dy = me.getSceneY() - finalTempTur.getBoundsInParent().getCenterY();
                finalTempTur.setX(finalTempTur.getX() + dx);
                finalTempTur.setY(finalTempTur.getY() + dy);
            }
        });
        for (int i = 0; i < GPane.getChildren().size(); i++) {
            GPane.getChildren().get(i).setOnMouseMoved(me -> {
                if (move) {
                    finalTempTur.toFront();
                    double dx = me.getSceneX() - finalTempTur.getBoundsInParent().getCenterX();
                    double dy = me.getSceneY() - finalTempTur.getBoundsInParent().getCenterY();
                    finalTempTur.setX(finalTempTur.getX() + dx);
                    finalTempTur.setY(finalTempTur.getY() + dy);
                }
            });
        }
//        for (Node n : MainPane.getChildren()) {
//            n.setScaleX(Main.gameScale);
//            n.setScaleY(Main.gameScale);
//        }
//        for (Node n : GPane.getChildren()) {
//            n.setScaleX(Main.gameScale);
//            n.setScaleY(Main.gameScale);
//        }
//        for (Node n : AlienShips.getChildren()) {
//            n.setScaleX(Main.gameScale);
//            n.setScaleY(Main.gameScale);
//        }
//        for (int i = 0; i < 60; i++) {
//            GPane.getChildren().get(i).setScaleX(Main.gameScale);
//            GPane.getChildren().get(i).setScaleY(Main.gameScale);
//        }

        for (Node n : MainPane.getChildren()) {
            Main.setToScale(n);
        }
        for (Node n : GPane.getChildren()) {
            Main.setToScale(n);
        }
        for (Node n : AlienShips.getChildren()) {
            Main.setToScale(n);
        }
        for (int i = 0; i < 60; i++) {
            Main.setToScale(GPane.getChildren().get(i));
        }
        this.intro();
    }

    public GameController() {
        wave = 0;
        highWave = 0;
        chanceN = 100;
        chanceB = 0;
        chanceS = 0;
        chanceF = 0;
        aliensInWave = 3;
        bossInWave = 1;
        selectedTurret = -1;
        gc = this;
    }

    private void intro() {
        MainPane.setTranslateX(315 * Main.gameScale);
        Main.s.setWidth(1200 * Main.gameScale);
        Main.s.setHeight(800 * Main.gameScale);
        TranslateTransition as = new TranslateTransition(Duration.seconds(3), AlienShips);
        as.setByX(2100 * Main.gameScale);
        as.play();
        as.setOnFinished(e -> {
            Color sc = new Color(0, 0, 0, 0.7);
            DropShadow ds = new DropShadow(BlurType.GAUSSIAN, sc, 10, 0, -5, 5);
            AlienShips.setEffect(ds);
            TranslateTransition mp = new TranslateTransition(Duration.seconds(2), MainPane);
            mp.setByX(-315 * Main.gameScale);
            mp.play();
            mp.setOnFinished(e1 -> {
                HouseImg.setImage(new Image(String.valueOf(getClass().getResource("/Images/building_close.jpg"))));
                this.startGame();
            });
        });
    }

    private void startGame() {
        stop = false;
        moveSelectedTurret();
        addCoins(40);
        TranslateTransition w = new TranslateTransition(Duration.seconds(1), MainPane);
        w.play();
        w.setOnFinished(e -> {
            this.createWave();
            giveCoins();
        });
    }

    private void giveCoins() {
        if (stop) {
            this.coins = 0;
            return;
        }
        this.addCoins((int) (Math.random() * 11) + 10);                              // 10 to 20
        TranslateTransition w = new TranslateTransition(Duration.seconds(20), MainPane);
        w.play();
        w.setOnFinished(e -> giveCoins());
    }
    
    private void createWave() {
        if (stop) {
            wave = 0;
            chanceN = 0;
            chanceB = 0;
            chanceS = 0;
            chanceF = 0;
            return;
        }
        this.playSound(3);
        wave++;
        highWave = wave;
        WaveText.setText("WAVE " + wave);
        ScaleTransition st = new ScaleTransition(Duration.millis(250), WaveText);
        st.setToX(1.3);
        st.setToY(1.3);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
        if (wave % 5 == 0) {
            generateBoss(0, bossInWave);
            bossInWave++;
        }
        else {
            if (wave >= 67) {
                chanceN = chanceB = chanceS = chanceF = 25;
                aliensInWave += 3;
            }
            else if (wave % 2 == 0) {
                aliensInWave++;
                if (chanceN >= 30) {
                    chanceN -= 3;
                    chanceB += 2;
                    chanceS += 1;
                    if (chanceN <= 75) {
                        chanceN -= 1;
                        chanceF += 1;
                    }
                }
                else if (chanceB >= 30) {
                    chanceB -= 2;
                    chanceS += 1;
                    chanceF += 1;
                }
            }
            else if (chanceB <= 30 && chanceN <= 30) {
                aliensInWave++;
            }
            generateAlien(0, aliensInWave);
        }
//        System.out.println("\n----------------------");
//        System.out.println("WAVE " + wave);
//        System.out.println("aliensInWave " + aliensInWave);
//        System.out.println("chanceN " + chanceN);
//        System.out.println("chanceB " + chanceB);
//        System.out.println("chanceS " + chanceS);
//        System.out.println("chanceF " + chanceF);
    }

    private void generateAlien(int i, int f) {
        if(stop) {
            return ;
        }
        int chance = (int)(Math.random() * 100) + 1;
        if (chance <= chanceN) {
            createAlien(0);
        }
        else if (chance <= (chanceN+chanceB)) {
            createAlien(1);
        }
        else if(chance <= (chanceN + chanceB + chanceS)) {
            createAlien(2);
        }
        else{
            createAlien(5);
        }
        i++;
        TranslateTransition w = new TranslateTransition(Duration.millis(waveLength/f), MainPane);
        w.play();
        int finalI = i;
        w.setOnFinished(event -> {
            if (finalI == f) {
                createWave();
            }
            else {
                generateAlien(finalI, f);
            }
        });
    }

    private void generateBoss(int i, int f) {
        if(stop) {
            return;
        }
        createAlien(3);
        i++;
        TranslateTransition w = new TranslateTransition(Duration.millis(waveLength/f), MainPane);
        w.play();
        int finalI = i;
        w.setOnFinished(event -> {
            if (finalI == f) {
                createWave();
            }
            else {
                generateBoss(finalI, f);
            }
        });
    }

    private void createAlien(int type) {
        if (stop) {
            return;
        }
        Alien al = new Alien(type, (1050 * Main.gameScale), (int)(Math.random()*6 + 1));
        aliens.add(al);
        MainPane.getChildren().add(al.getImg());
    }

    public void moveSelectedTurret() {
        if (stop) {
            return;
        }
        Image turImg;
        move = true;
        if (selectedTurret == 0) {
            turImg = new Image(String.valueOf(getClass().getResource("/Images/nT1.png")));
        }
        else if (selectedTurret == 1) {
            turImg = new Image(String.valueOf(getClass().getResource("/Images/bT1.png")));
        }
        else if (selectedTurret == 2) {
            turImg = new Image(String.valueOf(getClass().getResource("/Images/lT1.png")));
        }
        else if (selectedTurret == 9) {
            turImg = destroyImg.getImage();
        }
        else {
            move = false;
            turImg = null;
            tempTur.setOpacity(0);
        }
        if (move) {
            tempTur.setImage(turImg);
            tempTur.setOpacity(0.5);
            if (selectedTurret == 9) {
                tempTur.setScaleX(0.1);
                tempTur.setScaleY(0.1);
            }
            else {
                tempTur.setScaleX(1);
                tempTur.setScaleY(1);
            }
            finalTempTur = tempTur;
        }
        TranslateTransition w = new TranslateTransition(Duration.millis(100), MainPane);
        w.play();
        w.setOnFinished(e -> moveSelectedTurret());
    }

    public AnchorPane getMainPane() {
        return MainPane;
    }

    public ArrayList<Turret> getTurrets() {
        return turrets;
    }

    public ArrayList<Alien> getAliens() {
        return aliens;
    }

    //Turrets selection eventHandling (select turret to add it to platform)
    @FXML
    private void selectNormalTurret(ActionEvent event) {
        if (selectedTurret == 0) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 0;
            tempTur.setX(125);
            tempTur.setY(725);
        }
    }

    @FXML
    private void selectBombTurret(ActionEvent event) {
        if (selectedTurret == 1) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 1;
            tempTur.setX(225);
            tempTur.setY(725);
        }
    }

    @FXML
    private void selectLaserTurret(ActionEvent event) {
        if (selectedTurret == 2) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 2;
            tempTur.setX(325);
            tempTur.setY(725);
        }
    }

    @FXML
    private void removeTurret(ActionEvent event) {
        if (selectedTurret == 9) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 9;
            tempTur.setX(425 - 225);
            tempTur.setY(725 - 225);
        }
    }

    @FXML
    public void placeTurret(MouseEvent event) {
        if (selectedTurret == -1) {
            return;
        }
        double cursorX = (event.getSceneX()/(100 * Main.gameScale)) * (100 * Main.gameScale);
        double cursorY = (event.getSceneY()/(100 * Main.gameScale)) * (100 * Main.gameScale);
        ImageView checker = new ImageView();
        checker.setFitWidth(20 * Main.gameScale);
        checker.setFitHeight(20 * Main.gameScale);
        checker.setX(cursorX + (40 * Main.gameScale));
        checker.setY(cursorY + (40 * Main.gameScale));
        for (Turret tur : turrets) {
            if (checker.getBoundsInParent().intersects(tur.getImg().getBoundsInParent())) {
                return;
            }
        }
        int r = (int)(event.getSceneY()/(100 * Main.gameScale));
        if (selectedTurret == 0) {
            if (this.coins >= 10) {
                Turret nT = new NormalT(cursorX, r);
                turrets.add(nT);
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.playSound(0);
                this.addCoins(-10);
            }
            else {
                this.playSound(5);
            }
        }
        else if (selectedTurret == 1) {
            if (this.coins >= 20) {
                Turret bT = new BombT(cursorX, r);
                turrets.add(bT);
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.playSound(0);
                this.addCoins(-20);
            }
            else {
                this.playSound(5);
            }
        }
        else if (selectedTurret == 2) {
            if (this.coins >= 30) {
                Turret lT = new LaserT(cursorX, r);
                turrets.add(lT);
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.playSound(0);
                this.addCoins(-30);
            }
            else {
                this.playSound(5);
            }
        }
        selectedTurret = -1;
    }

    public void addCoins(int c) {
        this.coins += c;
        this.coinText.setText("Coins: ".concat(String.valueOf(coins)));
    }

    public int getCoins() {
        return this.coins;
    }

    @FXML
    public void onBackPress(ActionEvent event) {
        gameOver();
    }

    public void gameOver() {
        if (stop) {
            return;
        }
        this.stop = true;
        selectedTurret = -1;
        WaveText.setFont(new Font("Britannic Bold", 80));
        WaveText.toFront();
        WaveText.setStrokeWidth(3);
        WaveText.setY(WaveText.getY() + (400 - WaveText.getBoundsInParent().getCenterY()));
        WaveText.setText("GAME OVER AT WAVE " + wave);
        this.playSound(2);
        try {
            MainMenuController.mmc.getHighScore();
        }
        catch (IOException ignored) {}
        TranslateTransition w = new TranslateTransition(Duration.millis(3000), MainPane);
        w.play();
        w.setOnFinished(e -> {
            while(turrets.size() > 0){
                turrets.get(0).remTurret();
            }
            while (aliens.size() > 0){
                aliens.get(0).remAlien();
            }
            turrets = new ArrayList<>();
            aliens = new ArrayList<>();
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainMenu.fxml"));
            Scene scene = null;
            try {
                MainMenuController.closeGame();
                scene = new Scene(fxmlLoader.load());
                Main.s.setScene(scene);
            } catch (IOException ignored) {}
        });
    }

    public void playSound(int i){
        music.setFile(i);
        music.play();
    }

    public Integer getHighWave(){
        return this.highWave;
    }

    public boolean isOver(){
        return this.stop;
    }
}