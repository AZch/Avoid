package models.Buildings;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import maker.Buildings;
import models.Objects;
import wordcreators.avoid2.AllGameFile.GameView;

/**
 * Здание на карте
 */
public abstract class Building extends Objects {

    protected ArrayList<ArrayList<ArrayList<Float>>> pointBuildings = new ArrayList<>();

    protected ArrayList<ArrayList<Float>> topBuildingPoint = new ArrayList<>();

    // флаг сигнализирующий о входе в здание
    private boolean inOutFlag = false;

    // флаг сигнализирующий о необходимости сделать его полупрозрачным
    private boolean reduceAlpha = false;

    /**
     * Конструктор объекта на карте
     *
     * @param x        координата ч
     * @param y        координата y
     * @param spriteH
     * @param spriteW
     * @param gameView игровое поле
     */
    public Building(float x, float y, float spriteH, float spriteW, GameView gameView) {
        super(x, y, spriteH, spriteW, gameView);
    }

    public boolean getInOutFlag() { return inOutFlag; }

    public boolean getReduceAlpha() { return reduceAlpha; }

    public void changeInOutFlag(boolean newFlagValue) {
        if ((!inOutFlag && newFlagValue) || (inOutFlag && !newFlagValue))
        inOutFlag = newFlagValue;
    }

    public void changeReduceAlpha(boolean newReduceAlpha) {
        if ((!reduceAlpha && newReduceAlpha) || (reduceAlpha && !newReduceAlpha))
        reduceAlpha = newReduceAlpha;
    }

    public ArrayList<ArrayList<Float>> getTopBuildingPoint() {
        return topBuildingPoint;
    }

    public ArrayList<ArrayList<ArrayList<Float>>> getPointBuildings() { return pointBuildings; }

    public abstract void drawInsideHouse(Paint paint, Canvas canvas);
}