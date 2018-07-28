package models.Human;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import maker.Geometric_Calculate;
import models.Buildings.Building;
import models.Buildings.SimpleHouse;
import models.Objects;
import models.Portal;
import models.Resource.Gun;
import models.Resource.Resource;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllGameFile.Inventory;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

import constants.paramConst;

/**
 * Игрок
 */
public class Player extends Human {

    public int countMP = 1;

    // ссылка на здание в котором находитсся игрок
    private Objects building;

    // указатель нахождения игрока в здании
    private boolean inBuilding = false;

    // указатель видимости портала
    private boolean visPortal = false;

    // указатель нахождения в коробке
    private boolean inBox = false;

    // количество патрон игрока
    private int bulletCount;

    // сохранение скорости игрока
    private float saveSpeed = (float) 0.3;

    // максимальная скорость движения
    private float maxSpeed;

    // указатель что происходит движение
    private boolean move;

    // начало движения
    private long startMoveTime;

    // скорость максимального разгона
    private long timeToMaxSpeed = 1000;

    // кол-во убитых противников
    private int countKills = 0;

    // контекст
    private Context context;

    // задержка между выстрелами
    private int coolDownFire = 0;

    // инвентарь
    private Inventory inventory;

    private int pause = 7;

    /**
     * Конструктор игрока
     * @param x абсцисса
     * @param y ордината
     * @param gameView игровое поле
     * @param context контекст
     * @param live жизни
     * @param speed скорость
     * @param bulletCount кол-во пуль
     * @param countKills кол-во убитых врагов
     */
    public Player(float x, float y, float spriteH, float spriteW, GameView gameView, Context context, int live, float speed, int bulletCount, int countKills, Bitmap bitmapHalth, Inventory inventory) {
        super(x, y, spriteH, spriteW, gameView, context, speed, live, bitmapHalth);
        if (inventory == null) {
            this.inventory = new Inventory(context, getGameView().getHeightInventory());
        } else {
            this.inventory = inventory;
        }

        /*Bitmap youBitmap = MainActivity.playerImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitMapId = R.drawable.player;
            bitmap = BitmapFactory.decodeResource(context.getResources(), bitMapId);
            //init(context);
        }
        else {
            init(youBitmap);
        }*/
        spriteW = MainActivity.getBitmapPlayer(0,0,0).getWidth();
        spriteH = MainActivity.getBitmapPlayer(0,0,0).getHeight();

        this.countKills = countKills;

        this.bulletCount = bulletCount;
        saveSpeed = speed;
        maxSpeed = speed + speed / 2;
    }

    public void startMove(long timeStart) {
        startMoveTime = timeStart;
        move = true;
    }

    public void stopMove() {
        startMoveTime = 0;
        move = false;
    }

    public Inventory getInventory() { return inventory; }

    // получить кол-во убитых врагов
    public int getKillCount() { return countKills; }

    // получиьт задержку между выстрелами
    public int getCoolDownFire() { return coolDownFire; }

    // враг убит
    public void addKill() { countKills++; }

    /**
     * Произвести выстрел
     */
    public void shot(int fireGun) {
        switch (fireGun) {
            case 0:
                if (inventory.getFirstGun() != null)
                    bulletCount = ((Gun) inventory.getFirstGun()).shot(getBulletCount());
                break;
            case 1:
                if (inventory.getSecondGun() != null)
                    bulletCount = ((Gun) inventory.getSecondGun()).shot(getBulletCount());
                break;
        }
    }

    public int getMaxAngle(int fireGun) {
        switch (fireGun) {
            case 0:
                if (inventory.getFirstGun() != null)
                    return ((Gun) inventory.getFirstGun()).getMaxAngle();
                break;
            case 1:
                if (inventory.getSecondGun() != null)
                    return ((Gun) inventory.getSecondGun()).getMaxAngle();
                break;
        }
        return paramConst.maxAngleFireGun;
    }

    public int getDamage(int fireGun) {
        switch (fireGun) {
            case 0:
                if (inventory.getFirstGun() != null)
                    return  ((Gun) inventory.getFirstGun()).getDamage();
                break;
            case 1:
                if (inventory.getSecondGun() != null)
                    return  ((Gun) inventory.getSecondGun()).getDamage();
                break;
        }
        return 1;
    }

    public void stopFire() {
        if (inventory.getFirstGun() != null)
            ((Gun) inventory.getFirstGun()).stopFire();
        if (inventory.getSecondGun() != null)
            ((Gun) inventory.getSecondGun()).stopFire();
    }

    /**
     * Подобрать патроны
     */
    public void addAmmo() { bulletCount += 30; }

    /**
     * Получить кол-во имеющихся патрон
     * @return кол-во имеющихся патрон
     */
    public int getBulletCount() { return bulletCount; }

    /**
     * Получиьт признак нахождения в коробке
     * @return true, если игрок в коробке, иначе false
     */
    public boolean getInBox() {
        return inBox;
    }

    /**
     * поместить игрока в коробку
     */
    public void goToBox() {
        inBox = true;
        setSpeed(2);
    }

    /**
     * Убрать игрока из коробки
     */
    public void goOutBox() {
        inBox = false;
        setSpeed(saveSpeed);
    }

    /**
     * Получить признак видимости портала
     * @return true, если игрок видит портал, иначе false
     */
    public boolean getVisPortal() { return visPortal; }

    public void reload(boolean firstGun) {
        if (firstGun && inventory.getFirstGun() != null)
            bulletCount = ((Gun) inventory.getFirstGun()).reload(bulletCount);
        else if (!firstGun && inventory.getSecondGun() != null)
            bulletCount = ((Gun) inventory.getSecondGun()).reload(bulletCount);
    }

    public boolean checkInBuild(float playerX, float playerY, float buildX, float buildY, float buildSpriteW, float buildSpriteH, Objects building) {
        if (playerX >= buildX && playerX <= buildX + buildSpriteW)
            if (playerY >= buildY && playerY <= buildY + buildSpriteH) {
                if (this.building == null || Geometric_Calculate.destination(this.building, this) > Geometric_Calculate.destination(building, this))
                    this.building = building;
                return true;
            }
        return false;
    }

    /**
     * Один тик карты (проверка игрока на жизнь)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {

        if (getGameView().go) { // движение по джостику
            move(getGameView().goAngle, getSpeed());

            setCurrentFrame(gameTime);
        }
        else {
            currentFrame = 1;
        }

        if (getLiveCount() <= 0)
            return true;

        /*if (move) {
            if (gameTime - startMoveTime < timeToMaxSpeed) {
                float percentSpeed = (gameTime - startMoveTime) * 100 / timeToMaxSpeed;
                speed += (maxSpeed - speed) * percentSpeed / 100;
            } else
                speed = maxSpeed;
        } else {
            speed = saveSpeed;
        }*/

        if (inventory.getFirstGun() != null) {
            ((Resource) inventory.getFirstGun()).useFalse();
            if (inventory.getFirstGun().update(gameTime))
                return true;
        }

        if (inventory.getSecondGun() != null) {
            ((Resource) inventory.getSecondGun()).useFalse();
            if (inventory.getSecondGun().update(gameTime))
                return true;
        }

        return false;
    }

    Matrix matrix = new Matrix();
    Objects gun;
    // перерисовка игрока
    public void drow(Paint paint, Canvas canvas) {
        if (getLiveCount() > 0) {

            // задание направления анимации игрока
            double waySight;
            //if (getGameView().isFire())
                //waySight = getGameView().fireAngle;
            //else
                waySight = getGameView().goAngle;

            /*Rect srcRect = new Rect(currentFrame * bitmap.getWidth() / getBMP_COLUMNS(), waySight * bitmap.getHeight() / getBMP_ROWS(),
                    currentFrame * bitmap.getWidth() / getBMP_COLUMNS() + bitmap.getWidth() / getBMP_COLUMNS(), waySight * bitmap.getHeight() / getBMP_ROWS() + bitmap.getHeight() / getBMP_ROWS());*/

            /*Rect destRect = new Rect((int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    (int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp() + getSpriteW() * getGameView().getQwadWidth()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp() + getSpriteH() * getGameView().getQwadHeight()));
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            RectF srcRectF = new RectF(0, 0, 5, 5
            );
            RectF destRectF = new RectF((int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    (int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp() + getSpriteW() * getGameView().getQwadWidth()), (int) (getY() * getGameView().getQwadHeight() - getGameView().getVisYUp() + getSpriteH() * getGameView().getQwadHeight()));
                    */

            int i = 0, j = 0, k = 0;

            i = (int) Math.floor(liveCount / 25);
            if (i - 1 < 0)
                i = 1;
            if (i == 1 && liveCount > 0)
                i = 2;

            gun = inventory.getGun(getGameView().fireGun);
            if (gun != null && gun instanceof Gun) {
                j = (int) Math.floor((((Gun) gun).getCountBulletInGun() * 4) / ((Gun) gun).getMaxBulletInGun());


            }
            if (j - 1 < 0)
                j = 1;
            if (j == 1 && gun != null && gun instanceof Gun && ((Gun) gun).getCountBulletInGun() > 0)
                j = 2;

            if (inventory.getWeight() * 100 / inventory.getMaxWeight() > 33) // заполненность больше 33%
                k = 1;
            if (inventory.getWeight() * 100 / inventory.getMaxWeight() > 66)
                k = 2;
            if (inventory.getWeight() * 100 / inventory.getMaxWeight() > 90)
                k = 3;


            /*if (move) {
                k = 1;
                if (speed > saveSpeed + (maxSpeed - saveSpeed) / 2)
                    k = 2;
                if (speed == maxSpeed)
                    k = 3;
            }*/


            bitmap = MainActivity.getBitmapPlayer(i - 1, j - 1, k);

            matrix.setTranslate(getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp());
            matrix.preRotate( (float) (waySight * 180 / Math.PI) + 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

            canvas.drawBitmap(bitmap, matrix, paint);

            /*Rect srcRect = new Rect(0, 0, getHalthLive().getWidth(), getHalthLive().getHeight());
            Rect destRect = new Rect((int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), (int) ((getY() - 4) * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    (int) (getX() * getGameView().getQwadWidth() - getGameView().getVisXUp() + getSpriteW() * (((float) getLiveCount()) / 100) * getGameView().getQwadWidth()), (int) ((getY() - 2) * getGameView().getQwadHeight() - getGameView().getVisYUp()));

            canvas.drawBitmap(getHalthLive(), srcRect, destRect, paint);*/
        }
    }
}