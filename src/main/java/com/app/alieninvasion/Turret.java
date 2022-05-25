package com.app.alieninvasion;

import javafx.animation.TranslateTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public abstract class Turret {
//    private int type;   // 0 is normal, 1 is bomb, 2 is laser
    private int health;
    private int atkDmg;
    private int range;
    private int fireRate;
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

    public void setFireRate(int fireRate) {
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

    public int getFireRate() {
        return fireRate;
    }

    public int getLevel() {
        return level;
    }

    public ImageView getImg() {
        return img;
    }

    public boolean takeDamage(int dmg) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.6);
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
        int fx = ((int) (this.img.getBoundsInParent().getCenterX()/100) * 100) + 50;
        int fy = ((int) (this.img.getBoundsInParent().getCenterY()/100) * 100) + 50;
        int dx = fx - (int) this.img.getBoundsInParent().getCenterX();
        int dy = fy - (int) this.img.getBoundsInParent().getCenterY();
        this.img.setX(this.img.getX() + dx);
        this.img.setY(this.img.getY() + dy);
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
    }

    public void setCostTextPos(double x, double y) {
        this.costText.setX(x);
        this.costText.setY(y);
    }
}
