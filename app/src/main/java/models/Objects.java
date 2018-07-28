package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import maker.Buildings;
import maker.Geometric_Calculate;
import models.Buildings.Building;
import models.Human.Human;
import models.Human.Player;
import wordcreators.avoid2.AllGameFile.GameView;

import constants.paramConst;
import wordcreators.avoid2.AllGameFile.GameWindow;

/**
 * какой-либо объект на карте
 */
public abstract class Objects {

    // координаты объекта
    private float x, y;

    // ширина и высота квадрата
    //private float objW, objH;

    // ширина и высота спрайта
    protected float spriteW, spriteH;

    // размер объекта
    //private float size;

    // ссылка на игровое поле
    GameView gameView;

    // текстура и id объекта на карте
    protected Bitmap bitmap; // текстура
    protected int bitMapId; // id

    // кол-во строк в спрайте
    private int BMP_ROWS = 4;

    // кол-во столбцов в спрайте
    private int BMP_COLUMNS = 3;

    // время обновления поледнего кадра
    protected long frameTicker;

    // сколько миллисекунд должно пройти перед сменой кадра (1000 / fps)
    protected int framePeriod;

    // текущий кадр
    protected int currentFrame = 1;

    /**
     * Конструктор объекта на карте
     * @param x координата ч
     * @param y координата y
     * @param gameView игровое поле
     */
    public Objects(float x, float y, float spriteH, float spriteW, GameView gameView) {
        this.spriteH = spriteH;
        this.spriteW = spriteW;
        this.gameView = gameView;
        setX(x);
        setY(y);

        framePeriod = 1000 / paramConst.FPS_ANIMATION_OBJECTS;
        frameTicker = 0;
    }

    // инициализация текстуры по id (стандартная)
    protected void init(Context context) {
        Bitmap contextBitMap = BitmapFactory.decodeResource(context.getResources(), bitMapId);
        bitmap = Bitmap.createScaledBitmap(
                contextBitMap, Math.round(spriteW * gameView.getQwadWidth()), Math.round(spriteH * gameView.getQwadHeight()), false
        );
        contextBitMap.recycle();
    }

    // инициализация своей текстуры (кастомная)
    protected void init(Bitmap youBitmap) {
        bitmap = Bitmap.createScaledBitmap(
                youBitmap, Math.round(spriteW * gameView.getQwadWidth()), Math.round(spriteH * gameView.getQwadHeight()), false
        );
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getBMP_ROWS() { return BMP_ROWS; }

    public int getBMP_COLUMNS() { return BMP_COLUMNS; }

    // определить какой кадр анимации надо паказать
    protected void setCurrentFrame(long gameTime) {
        if (gameTime > frameTicker + framePeriod) {
            frameTicker = gameTime;
            currentFrame++;
            if (currentFrame >= BMP_COLUMNS)
                currentFrame = 0;
        }
    }

    // навправление объекта
    protected int getAnimationRow(double angle) {
        int[] directToAnimMap = { 1, 3, 2, 0 };
        angle = angle / (Math.PI / 2) + 2;
        return directToAnimMap[Math.abs((int)Math.round(angle)) % getBMP_ROWS()];
    }

    // получить координату х объекта
    public float getX() {
        return x;
    }

    // получить координату у объекта
    public float getY() {
        return y;
    }

    // получить центр объекта по x
    public float getCenterX() { return x + spriteW / 2; }

    // получить уентр объекта по y
    public float getCenterY() { return y + spriteH / 2; }

    public float getScreenX() {
        return getX() * getGameView().getQwadWidth() - getGameView().getVisXUp();
    }

    public float getScreenY() {
        return getY() * getGameView().getQwadHeight() - getGameView().getVisYUp();
    }

    // получить размер объекта
    //public float getSize() { return size; }

    // получить игровое поле
    public GameView getGameView() {
        return gameView;
    }

    // получить размер квадрата по высоте
    //public float getObjH() { return objH; }

    // получить размер квадрата по ширине
    //public float getObjW() { return objW; }

    // получить ширину спрайта
    public float getSpriteW() { return spriteW; }

    // получить высоту спрайта
    public float getSpriteH() { return spriteH; }

    protected void setSpriteW(float spriteW) {
        this.spriteW = spriteW;
    }

    protected void setSpriteH(float spriteH) {
        this.spriteH = spriteH;
    }

    // задание координаты х объекта
    protected boolean setX(float x) {
        if (x < 0) {
            this.x = 0;
            return false;
        }
        else if (x > gameView.getWidthMap() - spriteW) {
            this.x = gameView.getWidthMap() - spriteW;
            return false;
        }
        else {
            this.x = x;
            return true;
        }
    }

    // задание координаты у объекта
    protected boolean setY(float y) {
        if (y < 0) {
            this.y = 0;
            return false;
        }
        else if (y > gameView.getHeightMap() - spriteH) {
            this.y = gameView.getHeightMap() - spriteH;
            return false;
        }
        else {
            this.y = y;
            return true;
        }
    }

    /**
     * движение по углу
     * @param angle угол
     * @return удалось ли движение
     */
    protected boolean move(double angle, float speed) {
        float newX = getX() + speed * (float) Math.cos(angle), newY = getY() + speed * (float) Math.sin(angle); // новые координаты объекта

        // проверка на пересечение со стенами
        int countWall = 0;
        ArrayList<Objects> inOutBuild = new ArrayList<>();

        ArrayList<Integer> resultCodes = intersecWithBuild(getGameView().getVisibleObjBuilding(this, getGameView().getSizeSimpleBuilding() + getGameView().getSizeSimpleBuilding() / 2),
                newX, newY, inOutBuild);
        if (inOutBuild.size() != 0) {
            countWall++;
        }
        boolean horizonMove = true, verticalMove = true;
        for (int code : resultCodes) {
            if (code == 1)
                verticalMove = false;
            if (code == 2)
                horizonMove = false;
        }
        boolean toResult = true;
        if (horizonMove) {
            if (this instanceof Player)
                gameView.moveVisPointX(newX - getX());
            if (!setX(newX))
                toResult = false;
        }
        else
            toResult = false;
        if (verticalMove) {
            if (this instanceof Player)
                gameView.moveVisPointY(newY - getY());
            if (!setY(newY))
                toResult = false;
        }
        else
            toResult = false;

        if (this instanceof Player) {
            final GameWindow gameWindow = this.getGameView().getGameWindow();
            this.getGameView().getGameWindow().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameWindow.thingDrawInventory();
                }
            });
            for (Objects building : inOutBuild) {
                if (((Player) this).checkInBuild(getX(), getY() + spriteH - 3, building.getX(), building.getY(), building.getSpriteW(), building.getSpriteH(), building))
                    ((Building) building).changeInOutFlag(true);
                else
                    ((Building) building).changeInOutFlag(false);
                /*if (((Player) this).checkInBuild(getX() + getSpriteW(), getY() + getSpriteH(), ((Building) building).getTopBuildingPoint().get(0).get(0), ((Building) building).getTopBuildingPoint().get(0).get(1),
                        building.getSpriteW(), ((Building) building).getPointBuildings().get(0).get(3).get(1) - ((Building) building).getTopBuildingPoint().get(0).get(1), building) ||
                        ((Player) this).checkInBuild(getX(), getY() + getSpriteH(), ((Building) building).getTopBuildingPoint().get(0).get(0), ((Building) building).getTopBuildingPoint().get(0).get(1),
                                building.getSpriteW(), ((Building) building).getPointBuildings().get(0).get(3).get(1) - ((Building) building).getTopBuildingPoint().get(0).get(1), building))
                    ((Building) building).changeReduceAlpha(true);
                else
                    ((Building) building).changeReduceAlpha(false);*/
            }
        }

        return toResult;
    }

    private ArrayList<Integer> intersecWithBuild(ArrayList<Objects> objBuilding, float newX, float newY, ArrayList<Objects> inOutBuild) {
        boolean horizonMove = true, verticalMove = true;
        ArrayList<Integer> resultCodes = new ArrayList<>();
        for (Objects obj : objBuilding) {
            if (!horizonMove && !verticalMove)
                return resultCodes;
            // РАБОТАЕТ ТОЛЬКО С ЗДАНИЯМИ У КОТОРЫХ СТЕНЫ - ПРЯМЫЕ ЛИНИИ
            // так как вход всегда снизу (для удобства захода в здание), то стены идут сначала горизонтально, затем вертикально и т.д.
            ArrayList<ArrayList<ArrayList<Float>>> allLines = ((Building) obj).getPointBuildings();

            for (ArrayList<ArrayList<Float>> line : allLines) {
                for (int i = 0; i < line.size() - 1; i++) {
                    if (!horizonMove && !verticalMove)
                        return resultCodes;
                    // проверка на пересечение левой линии спрайта
                    /*if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                            line.get(i).get(0), line.get(i).get(1),
                            line.get(i + 1).get(0), line.get(i + 1).get(1),
                            newX, newY,
                            (newX), (newY + spriteH))) {
                        if (horizonMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    newX, getY(),
                                    (newX), (getY() + spriteH))) {
                                resultCodes.add(2);
                                horizonMove = false;
                            }
                        }
                        if (verticalMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    getX(), newY,
                                    (getX()), (newY + spriteH))) {
                                resultCodes.add(1);
                                verticalMove = false;
                            }
                        }
                    }
                    // проверка на пересечение правой линии спрайта

                    if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                            line.get(i).get(0), line.get(i).get(1),
                            line.get(i + 1).get(0), line.get(i + 1).get(1),
                            (newX + spriteW), newY,
                            (newX + spriteW), (newY + spriteH))) {
                        if (horizonMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (newX + spriteW), getY(),
                                    (newX + spriteW), (getY() + spriteH))) {
                                resultCodes.add(2);
                                horizonMove = false;
                            }
                        }
                        if (verticalMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (getX() + spriteW), newY,
                                    (getX() + spriteW), (newY + spriteH))) {
                                resultCodes.add(1);
                                verticalMove = false;
                            }
                        }
                    }

                    // проверка на пересечение верхней линии спрайта
                    if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                            line.get(i).get(0), line.get(i).get(1),
                            line.get(i + 1).get(0), line.get(i + 1).get(1),
                            (newX + spriteW), (newY),
                            newX, newY)) {
                        if (horizonMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (newX + spriteW), (getY()),
                                    newX, getY())) {
                                resultCodes.add(2);
                                horizonMove = false;
                            }
                        }
                        if (verticalMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (getX() + spriteW), (newY),
                                    getX(), newY)) {
                                resultCodes.add(1);
                                verticalMove = false;
                            }
                        }
                    }

                    // проверка на пересечение нижней линии спрайта
                    if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                            line.get(i).get(0), line.get(i).get(1),
                            line.get(i + 1).get(0), line.get(i + 1).get(1),
                            (newX + spriteW), (newY + spriteH),
                            newX, (newY + spriteH))) {
                        if (horizonMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (newX + spriteW), (getY() + spriteH),
                                    newX, (getY() + spriteH))) {
                                resultCodes.add(2);
                                horizonMove = false;
                            }
                        }
                        if (verticalMove) {
                            if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                    line.get(i).get(0), line.get(i).get(1),
                                    line.get(i + 1).get(0), line.get(i + 1).get(1),
                                    (getX() + spriteW), (newY + spriteH),
                                    getX(), (newY + spriteH))) {
                                resultCodes.add(1);
                                verticalMove = false;
                            }

                        }
                    }*/


                    if (horizonMove) {
                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                newX, getY(),
                                (newX), (getY() + spriteH))) {
                            resultCodes.add(2);
                            horizonMove = false;
                        }

                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (newX + spriteW), getY(),
                                (newX + spriteW), (getY() + spriteH))) {
                            resultCodes.add(2);
                            horizonMove = false;
                        }

                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (newX + spriteW), (getY()),
                                newX, getY())) {
                            resultCodes.add(2);
                            horizonMove = false;
                        }

                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (newX + spriteW), (getY() + spriteH),
                                newX, (getY() + spriteH))) {
                            resultCodes.add(2);
                            horizonMove = false;
                        }
                    }
                    if (verticalMove) {
                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                getX(), newY,
                                (getX()), (newY + spriteH))) {
                            resultCodes.add(1);
                            verticalMove = false;
                        }

                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (getX() + spriteW), newY,
                                (getX() + spriteW), (newY + spriteH))) {
                            resultCodes.add(1);
                            verticalMove = false;
                        }


                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (getX() + spriteW), (newY),
                                getX(), newY)) {
                            resultCodes.add(1);
                            verticalMove = false;
                        }

                        if (Geometric_Calculate.interesectionLinesBool(getGameView().getQwadWidth(), getGameView().getQwadHeight(),
                                line.get(i).get(0), line.get(i).get(1),
                                line.get(i + 1).get(0), line.get(i + 1).get(1),
                                (getX() + spriteW), (newY + spriteH),
                                getX(), (newY + spriteH))) {
                            resultCodes.add(1);
                            verticalMove = false;
                        }

                    }
                    if (this instanceof Player) {
                        // у входа в здание
                        inOutBuild.add(obj);

                    }
                }
            }
        }
        return resultCodes;
    }

    // перерисовка объекта
    public abstract void drow(Paint paint, Canvas canvas);

    // обновление состояния объекта
    public abstract boolean update(long gameTime);
}