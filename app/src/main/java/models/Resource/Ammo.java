package models.Resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import models.Objects;
import models.Human.Player;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

import maker.Geometric_Calculate;

/**
 * Коробка с патронами на карте
 */
public class Ammo extends Resource {

    /**
     * Конструктор боеприпасов
     * @param x абсцисса
     * @param y ордината
     * @param gameView игровое полу
     * @param context контекст
     */
    public Ammo(float x, float y, float spriteH, float spriteW, GameView gameView, Context context) {
        super(x, y, spriteH, spriteW, gameView, 1, 1);

        Bitmap youBitmap = MainActivity.ammoImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitMapId = R.drawable.ammo;
            init(context);
        }
        else {
            init(youBitmap);
        }
    }

    public void use() {
        ((Player) getGameView().getPlayer()).addAmmo();
        setUsed();
    }

    /**
     * одно тик коробки c боеприпасами (либо дать персонажу патроны, либо ничего не делать)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        if (getUsed())
            return true;
        return false;
    }

    // перерисовка боеприпасов
    public void drow(Paint paint, Canvas canvas) {
        if (!getUsed())
            canvas.drawBitmap(bitmap, getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp(), paint);
    }
}