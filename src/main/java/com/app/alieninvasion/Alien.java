package com.app.alieninvasion;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Alien {
    private final int type;   // 0 is normal, 1 is buff, 2 is speedy, 3 is boss, 4 is miniBoss
    private int health;
    private int atkDmg;
    private int speed;  // in x per sec
    private boolean moving;
    private int range;
    private float atkRate;
    private int killCoin;
    private ImageView img;
    private int rider;
    private int row;
    private float shadowOffsetY;
    TranslateTransition m;
    private boolean immune;

    public Alien(int t, double x, int r) {
        this.type = t;
        this.moving = true;
        this.img = new ImageView();
        this.rider = -1;
        this.shadowOffsetY = 5;
        this.immune = false;
        Image aImg = null;
        if (t == 0) {
            this.health = 15;
            this.atkDmg = 5;
            this.speed = 50;
            this.range = 0;
            this.atkRate = 2;
            this.killCoin = 1;
            aImg = new Image(String.valueOf(getClass().getResource("/Images/nAlien.png")));
        }
        else if (t == 1) {
            this.health = 40;
            this.atkDmg = 7;
            this.speed = 38;
            this.range = 0;
            this.atkRate = 1;
            this.killCoin = 2;
            aImg = new Image(String.valueOf(getClass().getResource("/Images/bAlien.png")));
            rider = (int)(Math.random() * 2) - 1;
        }
        else if (t == 2) {
            this.health = 10;
            this.atkDmg = 3;
            this.speed = 125;
            this.range = 0;
            this.atkRate = 4;
            this.killCoin = 2;
            int r1 = (int)(Math.random() * 100) + 1;
            if (r1 <= 10) {
                aImg = new Image(String.valueOf(getClass().getResource("/Images/sAlienBRider.png")));
                rider = 1;
            }
            else if (r1 <= 30) {
                aImg = new Image(String.valueOf(getClass().getResource("/Images/sAlienNRider.png")));
                rider = 0;
            }
            else {
                aImg = new Image(String.valueOf(getClass().getResource("/Images/sAlien.png")));
            }
        }
        else if (t == 3) {
            this.health = 100;
            this.atkDmg = 20;
            this.speed = 25;
            this.range = 0;
            this.atkRate = 0.75f;
            this.killCoin = 5;
            aImg = new Image(String.valueOf(getClass().getResource("/Images/Boss.png")));
            this.shadowOffsetY = 10;
        }
        else if (t == 4) {
            this.health = 10;
            this.atkDmg = 100;
            this.speed = 250;
            this.range = 0;
            this.atkRate = 1;
            this.killCoin = 3;
            aImg = new Image(String.valueOf(getClass().getResource("/Images/miniBoss.png")));
        }
        else if (t == 5) {
            this.health = 10;
            this.atkDmg = 5;
            this.speed = 75;
            this.range = 0;
            this.atkRate = 2;
            this.killCoin = 1;
            this.immune = true;
            aImg = new Image(String.valueOf(getClass().getResource("/Images/flyingAlien.png")));
            this.shadowOffsetY = 30;
        }
        if (t <= 5 && aImg != null) {
            this.img.setImage(aImg);
            this.setPos(x, r);
            Color sc = new Color(0, 0, 0, 0.7);
            DropShadow ds = new DropShadow(BlurType.GAUSSIAN, sc, 10, 0, -5, this.shadowOffsetY);
            if (t == 5) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0.4);
                colorAdjust.setInput(ds);
                this.img.setEffect(colorAdjust);
            }
            else {
                this.img.setEffect(ds);
            }
            move();
        }
        img.setMouseTransparent(true);
    }
    
    public void attack(Turret tur) {
        if (!this.checkDie()) {
            boolean turDead = tur.takeDamage(this.atkDmg);
            if (this.type == 4) {
                this.killCoin = 0;
                this.takeDamage(100);
                return;
            } else if (this.type == 5) {
                if (this.immune) {
                    this.immune = false;
                    for (Turret t : GameController.gc.getTurrets()) {
                        if (t.getRow() == this.row) {
                            t.setPause(false);
                        }
                    }
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(0);
                    this.img.setEffect(colorAdjust);
                    this.speed = 50;
                    double dist = this.img.getBoundsInParent().getMaxX() - 100;
                    double spd = (double) this.speed / 1000;
                    m = new TranslateTransition(Duration.millis(dist / spd), this.img);
                    m.setByX(-dist);
                    m.setInterpolator(Interpolator.EASE_OUT);
                    m.setOnFinished(e -> {
                        collisionTimer.stop();
                        GameController.gc.gameOver();
                    });
                }
            }
            TranslateTransition w = new TranslateTransition(Duration.millis(1000 / this.atkRate), this.img);
            w.play();
            if (!turDead) {
                w.setOnFinished(e -> attack(tur));
            } else {
                moving = true;
                m.play();
            }
        }
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public ImageView getImg() {
        return img;
    }

    public void takeDamage(int dmg) {
    this.img.toFront();
        ColorAdjust colorAdjust = new ColorAdjust();
        if (this.type == 0) {
            colorAdjust.setHue(-0.5);
        } else if (this.type == 1) {
            colorAdjust.setHue(-0.25);
        } else if (this.type == 3 || this.type == 4) {
            colorAdjust.setHue(-1);
        }
        else if (this.type == 5) {
            this.shadowOffsetY = 5;
        }
        colorAdjust.setSaturation(1);
        Color sc = new Color(0, 0, 0, 0.7);
        DropShadow ds = new DropShadow(BlurType.GAUSSIAN, sc, 10, 0, -5, this.shadowOffsetY);
        colorAdjust.setInput(ds);
        this.img.setEffect(colorAdjust);
        this.health -= dmg;
        if (checkDie()) {       // Split or Rider Spawn
            collisionTimer.stop();
            m.stop();
            if (rider != -1) {
                GameController.gc.getAliens().add(new Alien(this.rider, (int) this.img.getBoundsInParent().getCenterX(), this.row));
                GameController.gc.getMainPane().getChildren().add(GameController.gc.getAliens().get(GameController.gc.getAliens().size() - 1).getImg());
            }
            if (this.type == 3) {
                int adj = -1;
                if (this.row == 1) {
                    adj = 0;
                }
                Alien mB1 = new Alien(4, (int) this.img.getBoundsInParent().getCenterX(), (this.row + adj));
                GameController.gc.getAliens().add(mB1);
                GameController.gc.getMainPane().getChildren().add(mB1.img);

                adj = 1;
                if (this.row == 6) {
                    adj = 0;
                }
                Alien mB2 = new Alien(4, (int) this.img.getBoundsInParent().getCenterX(), (this.row + adj));
                GameController.gc.getAliens().add(mB2);
                GameController.gc.getMainPane().getChildren().add(mB2.img);
            }
            remAlien();
        }
        TranslateTransition w = new TranslateTransition(Duration.millis(100), this.img);
        w.play();
        w.setOnFinished(e -> {
            colorAdjust.setHue(0);
            colorAdjust.setSaturation(0);
            if (this.img != null) {
                Color sc1 = new Color(0, 0, 0, 0.7);
                DropShadow ds1 = new DropShadow(BlurType.GAUSSIAN, sc1, 10, 0, -5, this.shadowOffsetY);
                colorAdjust.setInput(ds1);
                this.img.setEffect(colorAdjust);
            }
        });
    }

    public void setPos(double x, int r) {
        this.img.setScaleX(Main.gameScale);
        this.img.setScaleY(Main.gameScale);
        this.row = r;
        double fy = (r * 100 * Main.gameScale) + (50 * Main.gameScale);
        double dx = x - this.img.getBoundsInParent().getCenterX();   // x = 1050 for default
        double dy = fy - this.img.getBoundsInParent().getCenterY();
        this.img.setX(this.img.getX() + dx);
        this.img.setY(this.img.getY() + dy);
        for (Turret tur : GameController.gc.getTurrets()) {
            if (!this.immune && tur.getRow() == this.row) {
                tur.setPause(false);
            }
        }
    }

    public boolean checkDie(){
        return (this.health <= 0);
    }

    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            try {
                for (Turret tur : GameController.gc.getTurrets()) {
                    if (Main.collide(Alien.this.img, tur.getImg())) {
                        if (moving) {
                            moving = false;
                            m.pause();
                            Alien.this.attack(tur);
                            if (Alien.this.type != 3) {
                                break;
                            }
                        }
                    }
                }
            }
            catch (Exception ConcurrentModificationException) {
                for (Turret tur : GameController.gc.getTurrets()) {
                    if (Alien.this.img != null && Main.collide(Alien.this.img, tur.getImg())) {
                        if (moving) {
                            moving = false;
                            m.pause();
                            Alien.this.attack(tur);
                            if (Alien.this.type != 3) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    };
    
    public void move() {
        double dist = this.img.getBoundsInParent().getMaxX() - 100;
        double spd = (double) this.speed / 1000;
        m = new TranslateTransition(Duration.millis(dist / spd), this.img);
        m.setByX(-dist);
        m.setInterpolator(Interpolator.EASE_OUT);
        m.play();
        collisionTimer.start();
        m.setOnFinished(e -> {
            collisionTimer.stop();
            GameController.gc.gameOver();
        });
    }

    public void remAlien() {
        for (int i = 0; i < GameController.gc.getAliens().size(); i++) {
            if (GameController.gc.getAliens().get(i).getImg() == this.img) {
                m.stop();
                collisionTimer.stop();
                GameController.gc.getAliens().remove(i);
                GameController.gc.getMainPane().getChildren().remove(this.img);
                this.img = null;
                GameController.gc.addCoins(this.killCoin);
                boolean canPause = true;
                for (Alien al : GameController.gc.getAliens()) {
                    if (al.getRow() == this.row && !this.immune) {
                        canPause = false;
                        break;
                    }
                }
                for (Turret tur : GameController.gc.getTurrets()) {
                    if (tur.getRow() == this.row) {
                        tur.setPause(canPause);
                    }
                }
                break;
            }
        }
    }

    public boolean getImmune(){
        return this.immune;
    }
}
