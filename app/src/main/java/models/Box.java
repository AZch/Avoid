package models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import maker.Geometric_Calculate;
import models.Human.Player;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.R;

/**
 * коробка для того чтобы спрятаться
 */
public class Box extends Objects {

    // указатель цела ли коробка
    private boolean isWhole = true;

    /**
     * Конструктор коробки на карте
     * @param x абсцисса
     * @param y ордината
     * @param objH высота квадрата
     * @param objW ширина квадрата
     * @param size размер
     * @param gameView игровое поле
     * @param context контекст
     */
    public Box(float x, float y, float spriteH, float spriteW, GameView gameView, Context context) {
        super(x, y, spriteH, spriteW, gameView);

        //bitMapId = R.drawable.box;

        init(context);
    }

    /**
     * Сломать коробку
     */
    public void breakeBox() { isWhole = false; }

    /**
     * Получить состояние коробки
     * @return цела или нет
     */
    public boolean getIsWhole() { return isWhole; }

    /**
     * Один тик таймера (игрок либо выходит из коробки, либо заходит в неё)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        Objects player = getGameView().getPlayer();

        if (!isWhole && ((Player) player).getInBox()) {
            ((Player) player).goOutBox();
        }

        /*if (player.getCenterX() > getCenterX() - player.getSize() && player.getCenterX() < getCenterX() + player.getSize())
            if (player.getCenterY() > getCenterY() - player.getSize() && player.getCenterY() < getCenterY() + player.getSize()) {
                if (isWhole && !((Player) player).getInBox())
                    ((Player) player).goToBox();
            }
        */
        if (Geometric_Calculate.destination(this, player) < getSpriteW() * getSpriteH())
            if (isWhole && !((Player) player).getInBox())
                ((Player) player).goToBox();
        return false;
    }

    public void drow(Paint paint, Canvas canvas) {
        //if (!((Player) getGameView().getPlayer()).getInBox() && isWhole)
            //canvas.drawBitmap(bitmap, getX() * getObjW() - gameView.getVisXUp(), getY() * getObjH() - gameView.getVisYUp(), paint);
    }
}