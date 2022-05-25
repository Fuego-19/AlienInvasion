package com.app.alieninvasion;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LaserT extends Turret {
    private int penetration;

    public LaserT() {
        super.setAtkDmg(1);
        super.setHealth(15);
        super.setFireRate(3);
        super.setRange(900);
        super.setLevel(1);
        super.setCost(50);
        super.setPause(true);
        this.penetration = 2;
        tur = this;
        super.setImg(new ImageView());
        super.getImg().setImage(new Image(String.valueOf(getClass().getResource("/Images/lT1.png"))));
        super.getImg().setOnMouseClicked(e -> upgrade());
        super.setCostText(this.getUpCost() + "⬆");
        this.attack();
    }

    @Override
    public void shoot() {
        new Laser(super.getAtkDmg(), super.getRange(), this.penetration, super.getImg(), super.getLevel());
        if (super.getImg() != null) {
            super.getImg().toFront();
        }
    }

    @Override
    public void attack() {
        if (checkDie() || GameController.gc.isOver()) {
            return;
        }
        if (!super.getPause()) {
            shoot();
        }
        TranslateTransition s = new TranslateTransition(Duration.millis(1000/super.getFireRate()), super.getImg());
        s.play();
        s.setOnFinished(e -> attack());
    }

    @Override
    public void upgrade() {
        if (GameController.selectedTurret == 2) {
            this.incCost();
            if (super.getLevel() == 1 && GameController.gc.getCoins() >= this.getCost()){
                super.setLevel(2);
                super.upHealth();
                GameController.gc.addCoins(-this.getCost());
                super.getImg().setImage(new Image(String.valueOf(getClass().getResource("/Images/lT2.png"))));
                super.changePos();
                this.penetration++;
                GameController.gc.playSound(4);
            }
            else if (super.getLevel() == 2 && GameController.gc.getCoins() >= this.getCost()) {
                super.setLevel(3);
                super.upHealth();
                GameController.gc.addCoins(-this.getCost());
                super.getImg().setImage(new Image(String.valueOf(getClass().getResource("/Images/lT3.png"))));
                this.penetration++;
                GameController.gc.playSound(4);
            }
            else {
                GameController.gc.playSound(5);
            }
            if (super.getLevel() == 3) {
                super.setCostText(this.getUpCost() );
            }
            else {
                super.setCostText(this.getUpCost() + "⬆");

            }
            super.setCostTextPos(super.getImg().getX(), super.getImg().getY());
        }
        else if (GameController.selectedTurret == 9) {
            GameController.gc.addCoins(this.getCost() / 2);
            super.remTurret();
            super.removeImg();
        }
        GameController.selectedTurret = -1;
    }

    @Override
    public void incCost() {
        if (super.getLevel() == 1) {
            super.setCost(120);
        }
        else if (super.getLevel() == 2) {
            super.setCost(200);
        }
    }

    @Override
    public String getUpCost() {
        if (super.getLevel() == 1) {
            return "120";
        }
        else if (super.getLevel() == 2) {
            return "200";
        }
        else {
            return "";
        }
    }
}
