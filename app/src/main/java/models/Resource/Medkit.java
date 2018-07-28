package models.Resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import maker.Geometric_Calculate;
import models.Objects;
import models.Human.Player;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

/**
 * Аптечка
 */
public class Medkit extends Resource {

    /**
     * Конструктор аптечки
     * @param x абсцисса
     * @param y ордината
     * @param gameView игровое поле
     * @param context контекст
     */
    public Medkit(float x, float y, float spriteH, float spriteW, GameView gameView, Context context) {
        super(x, y, spriteH, spriteW, gameView, 0, 1);

        Bitmap youBitmap = MainActivity.medkitImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitMapId = R.drawable.medkit;
            init(context);
        }
        else {
            init(youBitmap);
        }
    }

    public void use() {
        ((Player) getGameView().getPlayer()).addLive();
        setUsed();
    }

    /**
     * одно тик аптечки (либо дать персонажу дополнительную жизнь, либо ничего не делать)
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        if (getUsed())
            return true;
        return false;
    }

    public void drow(Paint paint, Canvas canvas) {
        if (!getUsed())
            canvas.drawBitmap(bitmap, getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp(), paint);
    }
}