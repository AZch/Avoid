package models.Human;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.util.Random;

import maker.Geometric_Calculate;
import models.Box;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

import constants.paramConst;

/**
 * Враг
 */
public class Enemy extends Human {

    // воспроизведение звука стрельбы
    private MediaPlayer mediaPlayer;

    // угол зрения
    private double angleSight;

    // время на возможность игроку убежать
    private int coolDown = getVision() * (int) ((float)(paramConst.randomDiapSpeed + paramConst.randomMinSpeed) / 10);

    // начальное значения на время позволяющее игроку убежать
    private int thisCoolDown = 0;

    // сколько времени будут не менять направления просмотра
    private int stopChgVis;

    // true если игрок замечен
    private boolean triggired;

    // количество, на которое будет поворочиваться угол зрения
    private int chg;

    // скорость поворота зрения
    private int speedChg;

    // сигнал поворота на одном месте
    private boolean rotatOnPlace;

    // измение стандартного состояния
    private Bitmap bitmapDead = null; //  враг умер

    private int damage = 1;

    // флаг сигнализирующий о готовности удалить объект
    private boolean delete = false;

    // флаг сигнализирующий о том что данный враг уже засчитан в убийства
    private boolean counting = false;

    /**
     * Счётчик битмап для вывода после убийства
     * Счётчик для переодичности вывода
     * Время между выводами
     */
    private int countEnemyDeadBitmap = 0;
    private int timeCountEnemyDeadBitmap = paramConst.MAX_FPS / 15;
    private int maxTimeCountEnemyDeadBitmap = paramConst.MAX_FPS / 15;

    /**
     * конструктор противника на карте
     * @param x абсцисса
     * @param y ордината
     * @param gameView игровое поле
     * @param context контекст
     * @param live жизни
     * @param speed скорость
     */
    public Enemy(float x, float y, float spriteH, float spriteW, GameView gameView, Context context, int live, float speed, Bitmap bitmapHalth) {
        super(x, y, spriteH, spriteW, gameView, context, speed, live, bitmapHalth);

        damage = paramConst.damageEnemy;

        Bitmap youBitmap = MainActivity.enemyImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            //bitMapId = R.drawable.enemy;
            bitmap = BitmapFactory.decodeResource(context.getResources(), bitMapId);
            //init(context);
        }
        else {
            init(youBitmap);
        }
        /*Bitmap youBitmapTrig = MainActivity.enemyTrigImage;
        if (youBitmapTrig == null || MainActivity.getCountUseTexture() == 0) {
            Bitmap contextBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemytriggired);
            /*bitmapTriggered = Bitmap.createScaledBitmap(
                    contextBitMap, (int) (getSize() * getObjW()), (int) (getSize() * getObjH()), false
            );
            contextBitMap.recycle();
        }
        else {
            /*bitmapTriggered = Bitmap.createScaledBitmap(
                    MainActivity.enemyTrigImage, (int) (getSize() * getObjW()), (int) (getSize() * getObjH()), false
            );
        }

        Bitmap youBitmapDead = MainActivity.enemyDeadImage;
        if (youBitmapDead == null || MainActivity.getCountUseTexture() == 0) {
            Bitmap contextBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemydead);
            bitmapDead = Bitmap.createScaledBitmap(
                    contextBitMap, (int)(getSpriteW() * gameView.getQwadWidth()), (int)(getSpriteH() * gameView.getQwadHeight()), false
            );
            contextBitMap.recycle();
        }
        else {
            bitmapDead = Bitmap.createScaledBitmap(
                    MainActivity.enemyDeadImage, (int)(getSpriteW() * gameView.getQwadWidth()), (int)(getSpriteH() * gameView.getQwadHeight()), false
            );
        }*/


        Random random = new Random();
        angleSight = random.nextInt(360);
        stopChgVis = 1;
        triggired = false;
        findPlayer(0);
        rotatOnPlace = false;
    }

    // необходимо ли засчитать данного противника в убийства
    public boolean isCountingKill() {
        return counting;
    }

    // нужно ли удалить объект
    public boolean isDelete() {
        return delete;
    }

    public double getAngleSight() {
        return angleSight;
    }

    /**
     * Перемещение к точке
     */
    public void goToPoint(long gameTime) {
        //rotatOnPlace = false;

        setCurrentFrame(gameTime);

        if (!move(angleSight, getSpeed()) && !triggired) {
            rotatOnPlace = true;
            chg = 0;
            stopChgVis = 0;
        }
    }

    /**
     * Поиск игрока
     */
    public void findPlayer(long gameTime) {
        if (coolDown > 0 || triggired)
            return;

        Random random = new Random();

        if (!rotatOnPlace) { // если враг не врезался то идём к указанной точке
            goToPoint(gameTime);
        }

        // если необходимо изменить направление видимости
        if (chg == 0 && stopChgVis <= 0) {
            if (rotatOnPlace) { // если враг врезался
                chg = random.nextInt(36) - 18;
                speedChg = 5;
            }
            else {
                chg = random.nextInt(80) - 40;
                speedChg = random.nextInt(1) + 1;
            }
        }

        // изменение направления видимости
        if (chg != 0 && !triggired && stopChgVis <= 0) {
            if (chg > 0) {
                angleSight += speedChg * Math.PI / 180;
                chg--;
            }
            else {
                angleSight -= speedChg * Math.PI / 180;
                chg++;
            }
        }

        // если изменение угла видимости закончено, то некоторое время оставляем его без изменения
        if (chg == 0 && stopChgVis <= 0 && !triggired) {
            stopChgVis = random.nextInt(50);
            rotatOnPlace = false;
        }
        else
            stopChgVis--;
    }

    /**
     * Один тик карты (ударить игрока, идти за игроком, либо искать игрока)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        // если враг умер только что
        if (getLiveCount() == 0) {
            contact(damage);
            return true;
        }
        // если враг мёртв более одного тика карты
        if (getLiveCount() < 0)
            return true;

        //
        //
        //
        // // SIZE
        //
        //
        //
        if (coolDown <= 0) {
            if (!getGameView().getfMenu() && Geometric_Calculate.destination(this, getGameView().getPlayer()) < getSpriteH() * getSpriteW()) { // проверка на необходимости ударить игрока
                if (((Player) getGameView().getPlayer()).getInBox()) {
                    ((Player) getGameView().getPlayer()).goOutBox();
                    ((Box) getGameView().getBox()).breakeBox();
                }
                ((Player) getGameView().getPlayer()).contact(damage);
                getGameView().makeSound.Hit();
                coolDown = getVision() * (int) ((float)(paramConst.randomDiapSpeed + paramConst.randomMinSpeed) / 10);
                thisCoolDown = coolDown;
                return false;
            }
            if (getGameView().visPlayer(this)) { // проверка на следование за игроком
                    triggired = true;
                    float pointPlayer = (float) (Math.atan2(getCenterY() - getGameView().getPlayer().getCenterY(), getGameView().getPlayer().getCenterX() - getCenterX()));
                    angleSight = pointPlayer * (-1);
                    setCurrentFrame(gameTime);
                    move(angleSight, getSpeed());
                    return false;
            }
        }
        else {
            coolDown--;
        }

        // поиск игрока
        triggired = false;
        findPlayer(gameTime);
        return false;
    }

    Matrix matrix = new Matrix();

    // перерисовка противника
    public void drow(Paint paint, Canvas canvas) {

        if (getLiveCount() <= 0) { // отрисовка при смерти противника
            if (countEnemyDeadBitmap >= 7) {
                delete = true;
                return;
            }

            if (!counting && timeCountEnemyDeadBitmap != maxTimeCountEnemyDeadBitmap)
                counting = true;

            bitmap = MainActivity.getBitmapEnemyDead(countEnemyDeadBitmap);
            matrix.setTranslate(getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp());
            matrix.preRotate( (float) (angleSight * 180 / Math.PI) + 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            if (timeCountEnemyDeadBitmap == 0) {
                countEnemyDeadBitmap++;
                timeCountEnemyDeadBitmap = maxTimeCountEnemyDeadBitmap;
            } else
                timeCountEnemyDeadBitmap--;

            if (countEnemyDeadBitmap >= 7) {
                delete = true;
                return;
            }

            canvas.drawBitmap(bitmap, matrix, paint);
            return;
        }

        if (getLiveCount() > 0) { // отрисовка при жизни противника
            /*Rect srcRect = new Rect(currentFrame * bitmap.getWidth() / getBMP_COLUMNS(), getAnimationRow(angleSight) * bitmap.getHeight() / getBMP_ROWS(),
                    currentFrame * bitmap.getWidth() / getBMP_COLUMNS() + bitmap.getWidth() / getBMP_COLUMNS(), getAnimationRow(angleSight) * bitmap.getHeight() / getBMP_ROWS() + bitmap.getHeight() / getBMP_ROWS());
            //canvas.drawBitmap(bitmap, getX() * getObjW() - gameView.getVisXUp(), getY() * getObjH() - gameView.getVisYUp(), paint);
            Rect destRect = new Rect((int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    (int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp() + getSpriteW() * getGameView().getQwadWidth()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp() + getSpriteH() * getGameView().getQwadHeight()));
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);*/

            int i = 0, j = 0, k = 0;

            if (triggired)
                i = 1;

            if (coolDown == thisCoolDown)
                k = 0;
            if (coolDown < 4 * thisCoolDown / 5)
                k = 1;
            if (coolDown < 3 * thisCoolDown / 5)
                k = 2;
            if (coolDown < 2 * thisCoolDown / 5)
                k = 3;
            if (coolDown <= 0)
                k = 4;

            j = Math.round(liveCount / 20);

            if (j - 1 < 0)
                j = 1;

            if (j == 1 && liveCount > 0)
                j = 2;

            bitmap = MainActivity.getBitmapEnemy(i, j - 1, k);

            matrix.setTranslate(getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp());
            matrix.preRotate( (float) (angleSight * 180 / Math.PI) + 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

            canvas.drawBitmap(bitmap, matrix, paint);

            /*Rect srcRect = new Rect(0, 0, getHalthLive().getWidth(), getHalthLive().getHeight());
            Rect destRect = new Rect((int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), (int) ((getY() - 4) * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    (int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp() + getSpriteW() * (((float) getLiveCount()) / 100) * getGameView().getQwadWidth()), (int) ((getY() - 2) * getGameView().getQwadHeight() - getGameView().getVisYUp()));

            canvas.drawBitmap(getHalthLive(), srcRect, destRect, paint);*/
        }
    }
}