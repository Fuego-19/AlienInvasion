package com.app.alieninvasion;

import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

public abstract class Turret {
//    private int type;   // 0 is normal, 1 is bomb, 2 is laser
    private int health;
    private int atkDmg;
    private int range;
    private float fireRate;
    private int cost;
    private int level;
    private ImageView img;
    private Text costText;
    public static Turret tur;
    private int row;
    private boolean pause;

    public boolean getPause() {
        return pause;
    }

    public void setPause(boolean alienThere) {
        this.pause = alienThere;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAtkDmg(int atkDmg) {
        this.atkDmg = atkDmg;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public int getAtkDmg() {
        return atkDmg;
    }

    public int getRange() {
        return range;
    }

    public float getFireRate() {
        return fireRate;
    }

    public int getLevel() {
        return level;
    }

    public ImageView getImg() {
        return img;
    }

    public void setPos(double x, int r) {
        this.img.setScaleX(Main.gameScale);
        this.img.setScaleY(Main.gameScale);
        double dx = ((x + (50 * Main.gameScale)) - this.img.getBoundsInParent().getCenterX());
        this.img.setX(this.img.getBoundsInParent().getMinX() + dx);
        double dy = (int) (((r * 100 * Main.gameScale) + (50 * Main.gameScale)) - this.img.getBoundsInParent().getCenterY());
        this.img.setY(this.img.getBoundsInParent().getMinY() + dy);
        this.setCostTextPos(this.img.getX(), this.img.getY());
        this.row = r;
        for (Alien al : GameController.gc.getAliens()) {
            if (al.getRow() == this.row) {
                if (!al.getImmune()) {
                    this.setPause(false);
                    break;
                }
            }
        }
    }

    public boolean takeDamage(int dmg) {
        if (this.img == null) {
            return true;
        }
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.6);
        Color sc = new Color(0, 0, 0, 0.7);
        DropShadow ds = new DropShadow(BlurType.GAUSSIAN, sc, 10, 0, -5, 5);
        colorAdjust.setInput(ds);
        this.img.setEffect(colorAdjust);
        this.health -= dmg;
        if (checkDie()) {
            remTurret();
            removeImg();
        }
        TranslateTransition w = new TranslateTransition(Duration.millis(100), this.img);
        w.play();
        w.setOnFinished(e -> {
            colorAdjust.setBrightness(0);
            if (this.img != null) {
                Color sc1 = new Color(0, 0, 0, 0.7);
                DropShadow ds1 = new DropShadow(BlurType.GAUSSIAN, sc1, 10, 0, -5, 5);
                colorAdjust.setInput(ds1);
                this.img.setEffect(colorAdjust);
            }
        });
        return checkDie();
    }

    public void upHealth() {
        this.health += 10;
    }

    public boolean checkDie(){
        return (this.health <= 0);
    }

    public void changePos() {
        Effect curEffect = this.img.getEffect();
        this.img.setEffect(null);
        int fx = ((int) (this.img.getBoundsInParent().getCenterX()/100) * 100) + 50;
        int fy = ((int) (this.img.getBoundsInParent().getCenterY()/100) * 100) + 50;
        int dx = fx - (int) this.img.getBoundsInParent().getCenterX();
        int dy = fy - (int) this.img.getBoundsInParent().getCenterY();
        this.img.setX(this.img.getX() + dx);
        this.img.setY(this.img.getY() + dy);
        this.img.setEffect(curEffect);
    }

    public void removeImg() {
        this.img = null;
    }

    public void remTurret() {
        for (int i = 0; i < GameController.gc.getTurrets().size(); i++) {
            if (GameController.gc.getTurrets().get(i).getImg() == this.img) {
                GameController.gc.getTurrets().remove(i);
                GameController.gc.getMainPane().getChildren().remove(this.img);
                GameController.gc.getMainPane().getChildren().remove(this.costText);
                if(!GameController.gc.isOver()){
                    GameController.gc.playSound(1);
                }
                break;
            }
        }
    }

    public abstract void shoot();

    public abstract void attack();

    public abstract void upgrade();

    public abstract void incCost();

    public abstract String getUpCost();

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public void setCostText(String s) {
        if (costText == null) {
            this.costText = new Text(s);
            GameController.gc.getMainPane().getChildren().add(costText);
        }
        else {
            this.costText.setText(s);
        }
        this.costText.setFill(Color.WHITE);
    }

    public void setCostTextPos(double x, double y) {
        this.costText.setX(x);
        this.costText.setY(y);
    }
}
