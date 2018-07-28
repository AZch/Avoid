package models.Human;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import models.Objects;
import models.Wall;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.R;

import constants.paramConst;

/**
 * Класс человека на карте
 */
public abstract class Human extends Objects {

    // жизни
    protected int liveCount = 100;

    // скорость передвижения
    protected float speed;

    // дальность зрения
    private final int vision;

    // кол-во здоровья
    private ProgressBar progressBar;

    private Bitmap halthLive;


    /**
     * конструктор человека
     * @param x координата х человека
     * @param y координата у человека
     * @param gameView игровое поле
     * @param speed скорость
     * @param live жизни
     */
    public Human(float x, float y, float spriteH, float spriteW, GameView gameView, Context context, float speed, int live, Bitmap bitmapHalth) {
        super(x, y, spriteH, spriteW, gameView);
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        halthLive = bitmapHalth;
        this.speed = speed;
        this.liveCount = live;
        java.util.Random random = new java.util.Random();
        vision = random.nextInt(20) + 30;
    }

    public ProgressBar getProgressbar() {
        return progressBar;
    }

    public Bitmap getHalthLive() { return halthLive; }

    /**
     * Контакт человека с объектом, при котором человек теряет одну единицу здоровья
     */
    public void contact(int damage) {
        if (damage > liveCount && this instanceof Player)
            liveCount = 0;
        else
            liveCount -= damage;
    }

    /**
     * Добавить одну жизнь
     */
    public void addLive() {
        if (liveCount + paramConst.addLive > 100)
            liveCount = 100;
        else
            liveCount += paramConst.addLive;
    }

    /**
     * Получить кол-во жизней
     * @return кол-во жизней
     */
    public int getLiveCount() { return liveCount; }

    /**
     * Задать скрость
     * @param speed скорость
     */
    public void setSpeed(float speed) { this.speed = speed; }

    /**
     * Получить скорость передвижения
     * @return скорость передвижения
     */
    public float getSpeed() { return speed; }

    /**
     * Получить поле зрения
     * @return поле зрения
     */
    public int getVision() { return vision; }

    /**
     * движение по кнопкам
     * @param key кнопка
     * @param speed скорость
     * @return удалось ли движение
     */
    protected boolean move(Character key, float  speed) {
        ArrayList<Objects> allObj = getGameView().getObjWall();
        if (key == 'W' || key == 'w') {
            for (Objects obj : allObj) {
                if (obj instanceof Wall)
                    if (getY() - speed < obj.getY() + obj.getSpriteH() && getY() - speed > obj.getY() && getX() + getSpriteW() > obj.getX() && getX() < obj.getX() + obj.getSpriteW())
                        return false;
            }
            return this.setY(this.getY() - speed);
        }
        if (key == 'A' || key == 'a') {
            for (Objects obj : allObj) {
                if (obj instanceof Wall)
                    if (getX() - speed < obj.getX() + obj.getSpriteW() && getX() - speed > obj.getX() && getY() + getSpriteH() > obj.getY() && getY() < obj.getY() + obj.getSpriteH())
                        return false;
            }
            return this.setX(this.getX() - speed);
        }
        if (key == 'S' || key == 's') {
            for (Objects obj : allObj) {
                if (obj instanceof Wall)
                    if (getY() + speed + getSpriteH() > obj.getY() && getY() + speed + getSpriteH() < obj.getY() + obj.getSpriteH() && getX() + getSpriteW() > obj.getX() && getX() < obj.getX() + obj.getSpriteW())
                        return false;
            }
            return this.setY(this.getY() + speed);
        }
        if (key == 'D' || key == 'd') {
            for (Objects obj : allObj) {
                if (obj instanceof Wall)
                    if (getX() + speed + getSpriteW() > obj.getX() && getX() + speed + getSpriteW() < obj.getX() + obj.getSpriteW() && getY() + getSpriteH() > obj.getY() && getY() < obj.getY() + obj.getSpriteH())
                        return false;
            }
            return this.setX(this.getX() + speed);
        }

        return true;
    }
}