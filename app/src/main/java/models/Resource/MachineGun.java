package models.Resource;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import constants.paramConst;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.R;

/**
 * пулемёт
 */
public class MachineGun extends Gun {
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
    public MachineGun(float x, float y, float spriteH, float spriteW, GameView gameView, int count, Context context) {
        super(x, y, spriteH, spriteW, gameView, 4, count, 100, 75, paramConst.damageMachineGun, paramConst.coolDownFireMachineGan, paramConst.maxAngleMachineGun);

        //Bitmap youBitmap = MainActivity.medkitImage;
        //if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
        bitMapId = R.drawable.machine_gun;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.machine_gun);
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
