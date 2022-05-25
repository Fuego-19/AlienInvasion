package com.app.alieninvasion;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Projectile {
    private int dmg;
    private int range;
    private ImageView img;

    public ImageView getImg() {
        return img;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void changeImg(String s) {
        this.img.setImage(new Image(s));
    }

    public void remProj() {
        GameController.gc.getMainPane().getChildren().remove(this.img);
        this.img = null;
    }

    public boolean setPos(ImageView source) {
        if (source == null) {
            this.remProj();
            return false;
        }
        int fx = ((int) (source.getBoundsInParent().getCenterX()/100) * 100) + 50;
        int fy = ((int) (source.getBoundsInParent().getCenterY()/100) * 100) + 50;
        int dx = fx - (int) this.img.getBoundsInParent().getCenterX();
        int dy = fy - (int) this.img.getBoundsInParent().getCenterY();
        this.img.setX(this.img.getX() + dx);
        this.img.setY(this.img.getY() + dy);
        return true;
    }

    public abstract void onHit(Alien a);

    public abstract void move();
}
