package com.app.alieninvasion;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    private ImageView tile00, tile10, tile20, tile30, tile40, tile50, tile60, tile70, tile80, tile90, tile100, tile110;
    @FXML
    private ImageView tile01, tile11, tile21, tile31, tile41, tile51, tile61, tile71, tile81, tile91, tile101, tile111;
    @FXML
    private ImageView tile02, tile12, tile22, tile32, tile42, tile52, tile62, tile72, tile82, tile92, tile102, tile112;
    @FXML
    private ImageView tile03, tile13, tile23, tile33, tile43, tile53, tile63, tile73, tile83, tile93, tile103, tile113;
    @FXML
    private ImageView tile04, tile14, tile24, tile34, tile44, tile54, tile64, tile74, tile84, tile94, tile104, tile114;
    @FXML
    private ImageView tile05, tile15, tile25, tile35, tile45, tile55, tile65, tile75, tile85, tile95, tile105, tile115;
    @FXML
    private ImageView tile06, tile16, tile26, tile36, tile46, tile56, tile66, tile76, tile86, tile96, tile106, tile116;
    @FXML
    private ImageView tile07, tile17, tile27, tile37, tile47, tile57, tile67, tile77, tile87, tile97, tile107, tile117;
    @FXML
    private ImageView NTBtn, BTBtn, LTBtn, destroyImg, coinImg;
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
        setTiles();
        NTBtn.setImage(new Image(String.valueOf(getClass().getResource("/Images/nT1.png"))));
        BTBtn.setImage(new Image(String.valueOf(getClass().getResource("/Images/bT1.png"))));
        LTBtn.setImage(new Image(String.valueOf(getClass().getResource("/Images/lT1.png"))));
        coinImg.setImage(new Image(String.valueOf(getClass().getResource("/Images/gold.png"))));
        destroyImg.setImage(new Image(String.valueOf(getClass().getResource("/Images/destroy.png"))));
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
        this.startGame();
    }

    public GameController() {
        wave = 0;
        highWave = 0;
        chanceN = 100;
        chanceB = 0;
        chanceS = 0;
        chanceF= 0;
        aliensInWave = 3;
        bossInWave = 1;
        selectedTurret = -1;
        gc = this;
    }

    private void setTiles() {
        tile00.setImage(new Image(String.valueOf(getClass().getResource("/Images/tile_0037.png"))));

        Image top = new Image(String.valueOf(getClass().getResource("/Images/tile_0038.png")));
        tile10.setImage(top);
        tile20.setImage(top);
        tile30.setImage(top);
        tile40.setImage(top);
        tile50.setImage(top);
        tile60.setImage(top);
        tile70.setImage(top);
        tile80.setImage(top);
        tile90.setImage(top);
        tile100.setImage(top);

        tile110.setImage(new Image(String.valueOf(getClass().getResource("/Images/tile_0039.png"))));

        Image right = new Image(String.valueOf(getClass().getResource("/Images/tile_0051.png")));
        tile111.setImage(right);
        tile112.setImage(right);
        tile113.setImage(right);
        tile114.setImage(right);
        tile115.setImage(right);
        tile116.setImage(right);

        tile117.setImage(new Image(String.valueOf(getClass().getResource("/Images/tile_0063.png"))));

        Image left = new Image(String.valueOf(getClass().getResource("/Images/tile_0049.png")));
        tile01.setImage(left);
        tile02.setImage(left);
        tile03.setImage(left);
        tile04.setImage(left);
        tile05.setImage(left);
        tile06.setImage(left);

        tile07.setImage(new Image(String.valueOf(getClass().getResource("/Images/tile_0061.png"))));

        Image bottom = new Image(String.valueOf(getClass().getResource("/Images/tile_0062.png")));
        tile17.setImage(bottom);
        tile27.setImage(bottom);
        tile37.setImage(bottom);
        tile47.setImage(bottom);
        tile57.setImage(bottom);
        tile67.setImage(bottom);
        tile77.setImage(bottom);
        tile87.setImage(bottom);
        tile97.setImage(bottom);
        tile107.setImage(bottom);

        Image leftPath = new Image(String.valueOf(getClass().getResource("/Images/tile_0101.png")));
        tile11.setImage(leftPath);
        tile12.setImage(leftPath);
        tile13.setImage(leftPath);
        tile14.setImage(leftPath);
        tile15.setImage(leftPath);
        tile16.setImage(leftPath);

        Image path = new Image(String.valueOf(getClass().getResource("/Images/tile_0098.png")));
        tile21.setImage(path);
        tile31.setImage(path);
        tile41.setImage(path);
        tile51.setImage(path);
        tile61.setImage(path);
        tile71.setImage(path);
        tile81.setImage(path);
        tile91.setImage(path);
        tile22.setImage(path);
        tile32.setImage(path);
        tile42.setImage(path);
        tile52.setImage(path);
        tile62.setImage(path);
        tile72.setImage(path);
        tile82.setImage(path);
        tile92.setImage(path);
        tile23.setImage(path);
        tile33.setImage(path);
        tile43.setImage(path);
        tile53.setImage(path);
        tile63.setImage(path);
        tile73.setImage(path);
        tile83.setImage(path);
        tile93.setImage(path);
        tile24.setImage(path);
        tile34.setImage(path);
        tile44.setImage(path);
        tile54.setImage(path);
        tile64.setImage(path);
        tile74.setImage(path);
        tile84.setImage(path);
        tile94.setImage(path);
        tile25.setImage(path);
        tile35.setImage(path);
        tile45.setImage(path);
        tile55.setImage(path);
        tile65.setImage(path);
        tile75.setImage(path);
        tile85.setImage(path);
        tile95.setImage(path);
        tile26.setImage(path);
        tile36.setImage(path);
        tile46.setImage(path);
        tile56.setImage(path);
        tile66.setImage(path);
        tile76.setImage(path);
        tile86.setImage(path);
        tile96.setImage(path);

        Image rightPath = new Image(String.valueOf(getClass().getResource("/Images/tile_0113.png")));
        tile101.setImage(rightPath);
        tile102.setImage(rightPath);
        tile103.setImage(rightPath);
        tile104.setImage(rightPath);
        tile105.setImage(rightPath);
        tile106.setImage(rightPath);
    }

    private void startGame() {
        stop = false;
        moveSelectedTurret();
        addCoins(40);
        TranslateTransition w = new TranslateTransition(Duration.millis(2000), MainPane);
        w.play();
        w.setOnFinished(e -> {
            this.createWave();
            giveCoins();
        });
    }

    private void giveCoins() {
        if(stop) {
            this.coins = 0;
            return;
        }
        this.addCoins(10);
        TranslateTransition w = new TranslateTransition(Duration.seconds(20), MainPane);
        w.play();
        w.setOnFinished(e -> giveCoins());
    }
    
    private void createWave() {
        if(stop) {
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
            if (wave % 3 == 0) {
                aliensInWave++;
                if (chanceN >= 20) {
                    chanceN -= 3;
                    chanceB += 2;
                    chanceS += 1;
                    if(chanceN <= 50){
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
            else if (chanceN <= 30) {
                aliensInWave++;
            }
            generateAlien(0, aliensInWave);
        }
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
        Alien al = new Alien(type, 1050, (int)(Math.random()*6 + 1));
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
        }
    }

    @FXML
    private void selectBombTurret(ActionEvent event) {
        if (selectedTurret == 1) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 1;
        }
    }

    @FXML
    private void selectLaserTurret(ActionEvent event) {
        if (selectedTurret == 2) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 2;
        }
    }

    @FXML
    private void removeTurret(ActionEvent event) {
        if (selectedTurret == 9) {
            selectedTurret = -1;
        }
        else {
            selectedTurret = 9;
        }
    }

    @FXML
    public void placeTurret(MouseEvent event) {
        if (selectedTurret == -1) {
            return;
        }
        int cursorX = (int) (event.getSceneX()/100) * 100;
        int cursorY = (int) (event.getSceneY()/100) * 100;
        ImageView checker = new ImageView();
        checker.setFitWidth(100);
        checker.setFitHeight(100);
        checker.setX(cursorX);
        checker.setY(cursorY);
        for (Turret tur : turrets) {
            if (checker.getBoundsInParent().intersects(tur.getImg().getBoundsInParent())) {
                return;
            }
        }
        if (selectedTurret == 0) {
            if (this.coins >= 10) {
                Turret nT = new NormalT();
                turrets.add(nT);
                int dx = (int) ((cursorX + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterX());
                turrets.get(turrets.size()-1).getImg().setX(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinX() + dx);
                int dy = (int) ((cursorY + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterY());
                turrets.get(turrets.size()-1).getImg().setY(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinY() + dy);
                turrets.get(turrets.size()-1).setCostTextPos(turrets.get(turrets.size()-1).getImg().getX(), turrets.get(turrets.size()-1).getImg().getY());
                turrets.get(turrets.size()-1).setRow(cursorY/100);
                for (Alien al : aliens) {
                    if (al.getRow() == turrets.get(turrets.size()-1).getRow()) {
                        turrets.get(turrets.size()-1).setPause(false);
                        break;
                    }
                }
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.addCoins(-10);
                this.playSound(0);

            }
            else {
                this.playSound(5);
            }
        }
        else if (selectedTurret == 1) {
            if(this.coins >= 30){
                Turret bT = new BombT();
                turrets.add(bT);
                int dx = (int) ((cursorX + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterX());
                turrets.get(turrets.size()-1).getImg().setX(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinX() + dx);
                int dy = (int) ((cursorY + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterY());
                turrets.get(turrets.size()-1).getImg().setY(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinY() + dy);
                turrets.get(turrets.size()-1).setCostTextPos(turrets.get(turrets.size()-1).getImg().getX(), turrets.get(turrets.size()-1).getImg().getY());
                turrets.get(turrets.size()-1).setRow(cursorY/100);
                for (Alien al : aliens) {
                    if (al.getRow() == turrets.get(turrets.size()-1).getRow()) {
                        turrets.get(turrets.size()-1).setPause(false);
                        break;
                    }
                }
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.playSound(0);
                this.addCoins(-30);
            }
            else {
                this.playSound(5);
            }
        }
        else if (selectedTurret == 2) {
            if(this.coins >= 50){
                Turret lT = new LaserT();
                turrets.add(lT);
                int dx = (int) ((cursorX + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterX());
                turrets.get(turrets.size()-1).getImg().setX(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinX() + dx);
                int dy = (int) ((cursorY + 50) - turrets.get(turrets.size()-1).getImg().getBoundsInParent().getCenterY());
                turrets.get(turrets.size()-1).getImg().setY(turrets.get(turrets.size()-1).getImg().getBoundsInParent().getMinY() + dy);
                turrets.get(turrets.size()-1).setCostTextPos(turrets.get(turrets.size()-1).getImg().getX(), turrets.get(turrets.size()-1).getImg().getY());
                turrets.get(turrets.size()-1).setRow(cursorY/100);
                for (Alien al : aliens) {
                    if (al.getRow() == turrets.get(turrets.size()-1).getRow()) {
                        turrets.get(turrets.size()-1).setPause(false);
                        break;
                    }
                }
                MainPane.getChildren().add(turrets.get(turrets.size()-1).getImg());
                this.playSound(0);
                this.addCoins(-50);
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

    public void gameOver() {
        if (stop) {
            return;
        }
        this.stop = true;
        WaveText.setFont(new Font("Britannic Bold", 80));
        WaveText.toFront();
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