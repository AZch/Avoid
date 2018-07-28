package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

import maker.Buildings;
import maker.Geometric_Calculate;
import models.Buildings.Building;
import models.Human.Enemy;
import models.Human.Human;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;

import constants.paramConst;

/**
 * Один патрон программы
 *
 *
 *
 *
 * у пили добавлен небольшой рендж 1
 *
 *
 *
 *
 */
public class Bullet extends Objects {

    // указатель остановки пули
    private boolean stop = false;

    // указатель что пуля ещё рисуется
    private boolean drawing = true;

    // максимальное время жизни пули
    private int maxTimeDrawing = paramConst.MAX_FPS;

    // время жизни пули
    private int timeDrawing = maxTimeDrawing;

    // область удара пули
    private int range = 0;

    // скорость полёта пули
    private float speed = (float) 1.5;

    private float angle = 0;

    // матрица для задания поворота
    Matrix matrix = new Matrix();

    private int damage = 1;

    private float toX = 0, toY = 0;

    // ближайший к игроку противник находящийся под выстрелом
    private Objects closeEnemy = null;

    /**
     * Конструктор объекта на карте
     *
     * @param x   абсцисса объекта
     * @param y   ордината объекта
     */
    public Bullet(float x, float y, float spriteH, float spriteW, Bitmap bitmapBullet, GameView gameView, Context context, float fireAngle, int damage, int numberShotOneTime, int maxAngle) {
        super(x, y, spriteH, spriteW, gameView);

        stop = false;

        this.damage = damage;

        Bitmap youBitmap = MainActivity.bulletImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitmap = bitmapBullet;
        } else {
            init(youBitmap);
        }

        if (numberShotOneTime > 2) {

            Random random = new Random();
            if (numberShotOneTime > maxAngle) {
                if ((float) numberShotOneTime % 2 == 0)
                    angle = fireAngle + random.nextInt(maxAngle) * (float)Math.PI / 180;
                else
                    angle = fireAngle - random.nextInt(maxAngle) * (float)Math.PI / 180;
            } else {
                if ((float) numberShotOneTime % 2 == 0)
                    angle = fireAngle + random.nextInt(numberShotOneTime) * (float)Math.PI / 180;
                else
                    angle = fireAngle - random.nextInt(numberShotOneTime) * (float)Math.PI / 180;
            }
        } else {
            angle = fireAngle;
        }
    }

    /**
     * Получить статус пули
     * @return статус пули
     */
    public boolean getStop() { return stop; }

    /**
     * движение пули
     * @return удалось ли движение
     */
    public boolean goToPoint() {
        if (!move(angle, speed))
            return true;
        return false;
    }

    /**
     * Один тик карты (либо ударить врага, либо идти к точке)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        if (!drawing)
            return true;
        if (!stop) {
            stop = true;
            ArrayList<Objects> objLet = getGameView().getObjBuilding(); // все объекты припятствий
            ArrayList<Objects> objEnemy = getGameView().getObjEnemy();

            double angleGrad = angle * 180 / Math.PI;
            //float[] XY;
            toX = getX() + 100000 * (float) Math.cos(angle);
            toY = getY() + 100000 * (float) Math.sin(angle);

            /*if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, 0, 0, 0, getGameView().getHeightMap())) {
                float[] XY1 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, 0, 0, 0, getGameView().getHeightMap());
                toX = XY1[0];
                toY = XY1[1];

            } else if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, 0, 0, getGameView().getWidthMap(), 0)) {
                float[] XY2 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, 0, 0, getGameView().getWidthMap(), 0);
                toX = XY2[0];
                toY = XY2[1];
            } else if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), getGameView().getWidthMap(), 0)) {
                float[] XY3 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), getGameView().getWidthMap(), 0);
                toX = XY3[0];
                toY = XY3[1];
            } else if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), 0, getGameView().getHeightMap())) {
                float[] XY4 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), 0, getGameView().getHeightMap());
                toX = XY4[0];
                toY = XY4[1];
            }*/


            /*float[] XY1 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, 0, 0, 0, getGameView().getHeightMap());
            float[] XY2 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, 0, 0, getGameView().getWidthMap(), 0);
            float[] XY3 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), getGameView().getWidthMap(), 0);
            float[] XY4 = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, getGameView().getWidthMap(), getGameView().getHeightMap(), 0, getGameView().getHeightMap());

            if (XY1[0] != -1 && XY1[1] != -1 && XY1[1] >= 0 && XY1[1] <= getGameView().getHeightMap()) {
                toX = XY1[0];
                toY = XY1[1];
            } else if (XY2[0] != -1 && XY2[1] != -1 && XY2[0] >= 0 && XY2[0] <= getGameView().getWidthMap()) {
                toX = XY2[0];
                toY = XY2[1];
            } else if (XY3[0] != -1 && XY3[1] != -1 && XY3[1] >= 0 && XY3[1] <= getGameView().getHeightMap()) {
                toX = XY3[0];
                toY = XY3[1];
            } else if (XY4[0] != -1 && XY4[1] != -1 && XY4[1] >= 0 && XY4[0] <= getGameView().getHeightMap()) {
                toX = XY4[0];
                toY = XY4[1];
            }*/

           ArrayList<Objects> nearLet = new ArrayList<>();

            for (Objects obj : objLet) {
                double size = Math.sqrt((obj.getSpriteH() / 2) * (obj.getSpriteH() / 2) + (obj.getSpriteW() / 2) * (obj.getSpriteW() / 2));
                if (Geometric_Calculate.destLinePoint(getCenterX(), getCenterY(), toX, toY, obj.getCenterX(), obj.getCenterY()) < size) {
                    for (ArrayList<ArrayList<Float>> line : ((Building) obj).getPointBuildings()) {
                        for (int i = 0; i < line.size() - 1; i++) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, line.get(i).get(0), line.get(i).get(1), line.get(i + 1).get(0), line.get(i + 1).get(1))) {
                                float[] XY = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, line.get(i).get(0), line.get(i).get(1), line.get(i + 1).get(0), line.get(i + 1).get(1));
                                if (XY[0] != -1 && XY[1] != -1 &&
                                        ((XY[0] >= line.get(i).get(0) && XY[0] <= line.get(i + 1).get(0)) || (XY[0] <= line.get(i).get(0) && XY[0] >= line.get(i + 1).get(0))) &&
                                        ((XY[1] >= line.get(i).get(1) && XY[1] <= line.get(i + 1).get(1)) || (XY[1] <= line.get(i).get(1) && XY[1] >= line.get(i + 1).get(1))) &&
                                        Geometric_Calculate.destination(getCenterX(), getCenterY(), toX, toY) > Geometric_Calculate.destination(getCenterX(), getCenterY(), XY[0], XY[1])) {
                                    toX = XY[0];
                                    toY = XY[1];
                                }
                            }
                        }
                    }
                    //nearLet.add(obj);
                }
            }
/*
            for (Objects obj : nearLet) {
                for (ArrayList<ArrayList<Float>> line : ((Building) obj).getPointBuildings()) {
                    for (int i = 0; i < line.size() - 1; i++) {
                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(), getCenterX(), getCenterY(), toX, toY, line.get(i).get(0), line.get(i).get(1), line.get(i + 1).get(0), line.get(i + 1).get(1))) {
                            float[] XY = Geometric_Calculate.intersectionLines(getCenterX(), getCenterY(), toX, toY, line.get(i).get(0), line.get(i).get(1), line.get(i + 1).get(0), line.get(i + 1).get(1));
                            if (XY[0] != -1 && XY[1] != -1 &&
                                    ((XY[0] >= line.get(i).get(0) && XY[0] <= line.get(i + 1).get(0)) || (XY[0] <= line.get(i).get(0) && XY[0] >= line.get(i + 1).get(0))) &&
                                    ((XY[1] >= line.get(i).get(1) && XY[1] <= line.get(i + 1).get(1)) || (XY[1] <= line.get(i).get(1) && XY[1] >= line.get(i + 1).get(1))) &&
                                    Geometric_Calculate.destination(getCenterX(), getCenterY(), toX, toY) > Geometric_Calculate.destination(getCenterX(), getCenterY(), XY[0], XY[1])) {
                                toX = XY[0];
                                toY = XY[1];
                            }
                        }
                    }
                }
            }*/

            for (Objects obj : objEnemy) {
                double size = Math.sqrt((obj.getSpriteH() / 2) * (obj.getSpriteH() / 2) + (obj.getSpriteW() / 2) * (obj.getSpriteW() / 2));
                if (Geometric_Calculate.destLinePoint(getCenterX(), getCenterY(), toX, toY, obj.getCenterX(), obj.getCenterY()) < size) {
                    if ((Math.abs(getCenterX() - toX) > size && ((obj.getCenterX() >= getCenterX() && obj.getCenterX() <= toX) || (obj.getCenterX() >= toX && obj.getCenterX() <= getCenterX()))) ||
                            ((obj.getCenterY() >= getCenterY() && obj.getCenterY() <= toY) || (obj.getCenterY() >= toY && obj.getCenterY() <= getCenterY()))) {
                        if (closeEnemy == null || Geometric_Calculate.destination(closeEnemy, this) > Geometric_Calculate.destination(obj, this)) {
                            closeEnemy = obj;
                        }
                        //((Human) obj).contact(damage);

                    }
                }
            }
            if (closeEnemy != null) {
                /*float toXY[] = findToXY();
                if (toXY != null && toXY[0] != -1 && toXY[1] != -1) {
                    toX = toXY[0];
                    toY = toXY[1];
                }*/
                ((Human) closeEnemy).contact(damage);
            }

            getGameView().makeSound.shot();
            return false;
        } else {
            stop = true;
            //return true;
        }

        return false;

        /*boolean returnState = false;
        for (int i = 0; i < 1; i++) {
            if (goToPoint())
                returnState = true;

            if (!stop) {
                float sizeToCheck = 1;
                if (getSpriteW() < getSpriteH())
                    sizeToCheck = getSpriteW();
                else
                    sizeToCheck = getSpriteH();
                // нужно ли стукнуть противника
                ArrayList<Objects> allObjEnemy = getGameView().getVisibleObjEnemy(this, sizeToCheck + range); // size
                for (Objects obj : allObjEnemy) {
                    if (((Enemy) obj).getLiveCount() > 0) {
                        ((Enemy) obj).contact(damage);
                        returnState = true;
                    }
                }

                // нужно ли стукнуться о стенку
                ArrayList<Objects> allObjWall = getGameView().getVisibleObjWall(this, sizeToCheck + range); // size
                if (allObjWall.size() > 0) {
                    returnState = true;
                    break;
                }
            }
        }

        return returnState;*/
    }

    private float[] findToXY() {
        float XY[] = new float[2];
        XY[0] = -1;
        XY[1] = -1;
        float r = MainActivity.getBitmapEnemy(0, 0, 0).getWidth() / 2;
        //XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY(), getX(), getY(), toX, toY);
        /*if (Geometric_Calculate.interesectionLinesBool(1, 1,closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY(), getX(), getY(), toX, toY)) {
            XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY(), getX(), getY(), toX, toY);
            return XY;
        }

       // XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY(), closeEnemy.getX() + r, closeEnemy.getY() + r / 2, getX(), getY(), toX, toY);
        if (Geometric_Calculate.interesectionLinesBool(1, 1,closeEnemy.getX() + r / 2, closeEnemy.getY(), closeEnemy.getX() + r, closeEnemy.getY() + r / 2, getX(), getY(), toX, toY)) {
            XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY(), closeEnemy.getX() + r, closeEnemy.getY() + r / 2, getX(), getY(), toX, toY);
            return XY;
        }

       // XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY(), getX(), getY(), toX, toY);
        if (Geometric_Calculate.interesectionLinesBool(1, 1,closeEnemy.getX() + r, closeEnemy.getY() + r / 2, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY)) {
            XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r, closeEnemy.getY() + r / 2, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY);
            return XY;
        }

       // XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY(), getX(), getY(), toX, toY);
        if (Geometric_Calculate.interesectionLinesBool(1, 1,closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY)) {
            XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY);
            return XY;
        }*/
        /*XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY(), closeEnemy.getX() + r, closeEnemy.getY() + r / 2, getX(), getY(), toX, toY);
        if (XY[0] != -1 && XY[1] != -1)
            return XY;
        XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r, closeEnemy.getY() + r / 2, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY);
        if (XY[0] != -1 && XY[1] != -1)
            return XY;
        XY = Geometric_Calculate.intersectionLines(closeEnemy.getX() + r / 2, closeEnemy.getY() + r, closeEnemy.getX() + r / 2, closeEnemy.getY() + r, getX(), getY(), toX, toY);
        if (XY[0] != -1 && XY[1] != -1)
            return XY;*/


        float a = getY() - toY, b = toX - getX(), c = getX() * toY - toX * getY();
        float x0 = -a * c / (a * a + b * b), y0 = -b * c / (a * a + b * b);
        //float r = MainActivity.getBitmapEnemy(0, 0, 0).getWidth() / 2;
        if (c * c > r * r * (a * a + b * b) + 0.001) {
            XY[0] = -1;
            XY[1] = -1;
        } else if (Math.abs(c * c - r * r * (a * a + b * b)) < 0.001) {
            XY[0] = x0;
            XY[1] = y0;
        } else {
            float d = r * r - c * c / (a * a + b * b);
            float mult = (float)Math.sqrt(d / (a * a + b * b));
            float ax,ay,bx,by;
            ax = x0 + b * mult;
            bx = x0 - b * mult;
            ay = y0 - a * mult;
            by = y0 + a * mult;
            if (Geometric_Calculate.destination(ax, ay, getX(), getY()) < Geometric_Calculate.destination(bx, by, getX(), getY())) {
                XY[0] = ax;
                XY[1] = ay;
            } else {
                XY[0] = bx;
                XY[1] = by;
            }
        }

        return XY;
    }

    public void drow(Paint paint, Canvas canvas) {
        //stop = true;
        //if (getStop()) {
        if (timeDrawing == 0)
            drawing = false;
        if (drawing) {
            paint.setColor(Color.RED);
            paint.setStrokeWidth(MainActivity.getBitmapPlayer(0, 0, 0).getWidth() / 10);
            paint.setAlpha((timeDrawing * 255) / maxTimeDrawing);
            canvas.drawLine(getX() * gameView.getQwadWidth() - gameView.getVisXUp(), getY() * gameView.getQwadHeight() - gameView.getVisYUp(), toX * gameView.getQwadWidth() - gameView.getVisXUp(), toY * gameView.getQwadHeight() - gameView.getVisYUp(), paint);
            paint.setAlpha(255);
            timeDrawing--;
        }
            //matrix.setTranslate(getX() * gameView.getQwadWidth() - gameView.getVisXUp(), getY() * gameView.getQwadHeight() - gameView.getVisYUp());
            //matrix.preRotate(angle * 180 / (float)Math.PI, getSpriteW() / 2, getSpriteH() / 2);
            //canvas.drawBitmap(bitmap, matrix, paint);
        //}
    }
}