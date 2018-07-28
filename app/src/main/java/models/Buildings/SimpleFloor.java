package models.Buildings;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import models.Human.Player;
import models.Objects;
import wordcreators.avoid2.AllGameFile.GameView;



public class SimpleFloor extends Objects {
    /**
     * Конструктор объекта на карте
     *
     * @param x        координата ч
     * @param y        координата y
     * @param spriteH
     * @param spriteW
     * @param gameView игровое поле
     */
    public SimpleFloor(float x, float y, float spriteH, float spriteW, GameView gameView, Bitmap bitmap) {
        super(x, y, spriteH, spriteW, gameView);
        this.bitmap = bitmap;
    }

    @Override
    public void drow(Paint paint, Canvas canvas) {
        paint.setAlpha(255);
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect(Math.round(getX() * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round(getY() * getGameView().getQwadHeight() - getGameView().getVisYUp()),
                Math.round((getX() + getSpriteW()) * getGameView().getQwadWidth() - getGameView().getVisXUp()), Math.round((getY() + getSpriteH()) * getGameView().getQwadHeight() - getGameView().getVisYUp()));
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);
    }

    @Override
    public boolean update(long gameTime) {
        return false;
    }
}
