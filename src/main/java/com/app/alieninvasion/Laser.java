package com.app.alieninvasion;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class Laser extends Projectile {
    private int hitTimes;
    private final int penetration;
    private final ArrayList<ImageView> damagedAliens = new ArrayList<>();
    private boolean canDamage;

    public Laser(int d, int r, int p, ImageView src,int level ) {
        this.penetration = p;
        this.hitTimes = 0;
        super.setDmg(d);
        super.setRange(r);
        this.canDamage = true;
        super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/laser.png")))));
        if (level == 2) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setHue(-0.7);
            super.getImg().setEffect(adjust);
        }
        else if (level == 3) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setHue(-0.45);
            adjust.setContrast(0.3);
            super.getImg().setEffect(adjust);
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
        if(!a.getImmune()) {
            this.hitTimes++;
            if (this.hitTimes <= this.penetration) {
                if (a.getImg() != null) {
                    if (damagedAliens.contains(a.getImg())) {
                        canDamage = false;
                        this.hitTimes--;
                    } else {
                        canDamage = true;
                    }
                }
                if (canDamage) {
                    this.damagedAliens.add(a.getImg());
                    a.takeDamage(super.getDmg());
                }
            }
            if (this.hitTimes == this.penetration) {
                collisionTimer.stop();
                super.remProj();
            }
        }
    }

    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            for (Alien al : GameController.gc.getAliens()) {
                if (Main.collide(Laser.super.getImg(), al.getImg())) {
                    Laser.this.onHit(al);
                    break;
                }
            }
        }
    };

    @Override
    public void move() {
        TranslateTransition m = new TranslateTransition(Duration.millis(super.getRange()/0.8), this.getImg());
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
