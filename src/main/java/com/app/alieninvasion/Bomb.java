package com.app.alieninvasion;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Bomb extends Projectile {
    private boolean split;

    public Bomb(int d, int r, ImageView src, int level) {
        this.split = false;
        super.setDmg(d);
        super.setRange(r);
        if(level ==1){
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/bomb.png")))));
        }
        else if(level == 2){
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/missileL2.png")))));
        }
        else if(level == 3){
            super.setImg(new ImageView(new Image(String.valueOf(getClass().getResource("/Images/nukeL3.png")))));
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
            a.takeDamage(super.getDmg());
            if (!split) {
                split = true;
                Projectile b1 = new Bullet(super.getDmg() / 2, 10, super.getImg(), 1);
                Projectile b2 = new Bullet(super.getDmg() / 2, 10, super.getImg(), 1);
                b1.changeImg(String.valueOf(getClass().getResource("/Images/explosion.png")));
                b2.changeImg(String.valueOf(getClass().getResource("/Images/explosion.png")));
                b1.getImg().setScaleX(2);
                b1.getImg().setScaleY(2);
                b2.getImg().setScaleX(2);
                b2.getImg().setScaleY(2);
                b1.getImg().setY(super.getImg().getBoundsInParent().getCenterY() - 100);
                b2.getImg().setY(super.getImg().getBoundsInParent().getCenterY() + 100);
                collisionTimer.stop();
                for (Alien al : GameController.gc.getAliens()) {
                    if (al != a && !al.getImmune() && (int)(Bomb.super.getImg().getBoundsInParent().getCenterY() / 100) == al.getRow() && (Bomb.super.getImg().getBoundsInParent().getMaxX() + 50) >= al.getImg().getBoundsInParent().getMinX()) {
                        al.takeDamage(super.getDmg());
                    }
                }
                super.remProj();
            }
        }
    }

    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            for (Alien al : GameController.gc.getAliens()) {
                if (Main.collide(Bomb.super.getImg(), al.getImg())) {
                    Bomb.this.onHit(al);
                    break;
                }
            }
        }
    };

    @Override
    public void move() {
        TranslateTransition m = new TranslateTransition(Duration.millis(super.getRange()/0.4), this.getImg());
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
