package models.Resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import wordcreators.avoid2.AllGameFile.GameView;

import constants.paramConst;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

/**
 * Автоматическая винтовка
 */
public class AutoGun extends Gun {
    /**
     * конструктор ресурсов
     *
     * @param x              абсцисса
     * @param y              ордината
     * @param spriteH
     * @param spriteW
     * @param gameView       игровое поле
     * @param count
     */
    public AutoGun(float x, float y, float spriteH, float spriteW, GameView gameView, int count, Context context) {
        super(x, y, spriteH, spriteW, gameView, 3, count, 30, 50, paramConst.damageAK, paramConst.coolDownFireAK, paramConst.maxAngleAK);

        //Bitmap youBitmap = MainActivity.medkitImage;
        //if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitMapId = R.drawable.automatic_gun;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.automatic_gun);
            setSpriteH(spriteW * (bitmap.getHeight() / bitmap.getWidth()));
            bitmap.recycle();
            init(context);
        //}
        //else {
         //   init(youBitmap);
        //}
    }

    @Override
    public void use() {
        //useTrue();
    }

    @Override
    public void drow(Paint paint, Canvas canvas) {
        if (!getUsed())
            canvas.drawBitmap(bitmap, getX() * getGameView().getQwadWidth() - getGameView().getVisXUp(), getY() * getGameView().getQwadHeight() - getGameView().getVisYUp(), paint);
    }

    @Override
    public boolean update(long gameTime) {
        if (getUsed()) {
            useFalse();
            return true;
        }

        if (getShot() && !getReload())
            if (getCoolDownFire() == 0) {
                decrBulletInGun();
                setCoolDownFire();
                return true;
            }

        if (getCoolDownFire() > 0)
            decrCoolDownFire();

        if (getReload() && getTimeReload() == 0) {
            reloaded();
        }

        if (getReload() && getTimeReload() > 0)
            decrTimeReload();

        return false;
    }
}
