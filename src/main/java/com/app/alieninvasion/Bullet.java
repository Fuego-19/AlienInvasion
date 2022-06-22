package com.app.alieninvasion;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Bullet extends Projectile {

    public Bullet(int d, int r, ImageView src,int level) {
        super.setDmg(d);
        super.setRange(r);
        if (level == 1) {
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/bullet.png")))));
        }
        else if (level == 2) {
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/bulletL2.png")))));
        }
        else if (level == 3) {
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/bulletL3.png")))));
        }
        GameController.gc.getMainPane().getChildren().add(super.getImg());
        if (super.setPos(src)) {
            if (super.getImg().getBoundsInParent().getMinY() < 100) {
                super.remProj();
                return;
            }
            move();
        }
        if (super.getImg() != null) {
            super.getImg().setMouseTransparent(true);
        }
    }


    @Override
    public void onHit(Alien a) {
        a.getImg().toFront();
        if(!a.getImmune()) {
            a.takeDamage(super.getDmg());
            collisionTimer.stop();
            super.remProj();
        }
    }

    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            for (Alien al : GameController.gc.getAliens()) {
                if (Main.collide(Bullet.super.getImg(), al.getImg())) {
                    Bullet.this.onHit(al);
                    break;
                }
            }
        }
    };

    @Override
    public void move() {
        TranslateTransition m = new TranslateTransition(Duration.millis(super.getRange()/0.5), this.getImg());
        m.setByX(super.getRange());
        m.setInterpolator(Interpolator.LINEAR);
        m.play();
        collisionTimer.start();
        m.setOnFinished(e -> {
            collisionTimer.stop();
            super.remProj();
        });
    }
}
