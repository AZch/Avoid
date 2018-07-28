package models.Buildings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import maker.Geometric_Calculate;
import models.Objects;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

/**
 * Простое жилое здание
 */
public class SimpleHouse extends Building {
    // текстура пола
    private Bitmap bitmapFloor;

    // текстура задней стены
    private Bitmap bitmapBackWall;

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
    public SimpleHouse(float x, float y, float spriteH, float spriteW, GameView gameView, Context context, float sizePlayer) {
        super(x, y, spriteH, spriteW, gameView);
        //bitmapBackWall = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_wall);

        bitMapId = R.drawable.build;
        //init(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), bitMapId);

        ArrayList<ArrayList<Float>> oneLineWalls = new ArrayList<>();
        ArrayList<Float> onePoint = new ArrayList<>();
        onePoint.add(x + spriteW / 2 - 10);
        onePoint.add(y + spriteH);
        oneLineWalls.add(onePoint);

        onePoint = new ArrayList<>();

        onePoint.add(x);
        onePoint.add(y + spriteH);
        oneLineWalls.add(onePoint);
        onePoint = new ArrayList<>();

        onePoint.add(x);
        onePoint.add(y);
        oneLineWalls.add(onePoint);
        onePoint = new ArrayList<>();

        onePoint.add(x + spriteW);
        onePoint.add(y);
        oneLineWalls.add(onePoint);
        onePoint = new ArrayList<>();

        onePoint.add(x + spriteW);
        onePoint.add(y + spriteH);
        oneLineWalls.add(onePoint);
        onePoint = new ArrayList<>();

        onePoint.add(x + spriteW / 2 + 10);
        onePoint.add(y + spriteH);
        oneLineWalls.add(onePoint);
        pointBuildings.add(oneLineWalls);

        onePoint = new ArrayList<>();

        onePoint.add(x);
        onePoint.add(y - spriteH / 2 - 2);
        topBuildingPoint.add(onePoint);
    }



    // ссылка на текстуру пола
    @Override
    public void drawInsideHouse(Paint paint, Canvas canvas) {



    }

    @Override
    public void drow(Paint paint, Canvas canvas) {
        Rect srcRect;
        Rect destRect;
        /*if (getReduceAlpha())
            paint.setAlpha(100);
        srcRect = new Rect(0, 0, bitmapBackWall.getWidth(), bitmapBackWall.getHeight());
        destRect = new Rect(Math.round(topBuildingPoint.get(0).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round(topBuildingPoint.get(0).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp() + 45),
                Math.round(pointBuildings.get(0).get(3).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round(pointBuildings.get(0).get(3).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp()));
        canvas.drawBitmap(bitmapBackWall, srcRect, destRect, paint);*/

        srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAlpha(255);
        if (getInOutFlag())
            paint.setAlpha(0);

        if (getReduceAlpha())
            paint.setAlpha(100);
        destRect = new Rect(Math.round(pointBuildings.get(0).get(2).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round(pointBuildings.get(0).get(2).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                Math.round(pointBuildings.get(0).get(4).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round(pointBuildings.get(0).get(4).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp()));
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);

        /*for (int i = 0; i < pointBuildings.get(0).size() - 1; i++) {
            canvas.drawLine(Math.round(pointBuildings.get(0).get(i).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()),
                    Math.round(pointBuildings.get(0).get(i).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                    Math.round(pointBuildings.get(0).get(i + 1).get(0) * getGameView().getQwadWidth() - getGameView().getVisXUp()),
                    Math.round(pointBuildings.get(0).get(i + 1).get(1) * getGameView().getQwadHeight() - getGameView().getVisYUp()), paint);
        }*/

    }

    @Override
    public boolean update(long gameTime) {
        return false;
    }
}