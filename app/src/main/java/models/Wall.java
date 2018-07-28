package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

/**
 * Стена
 */
public class Wall extends Objects {

    /**
     * Ориентации стены у здания
     * -1 стена не нуждается в определении ориентации
     * 0 верхняя стена здания
     * 1 нижняя стена здания
     */
    private int orientationWall = -1;

    /**
     * Конструктор стены
     * @param x абсцисса
     * @param y ордината
     * @param objH высота объекта
     * @param objW ширина объекта
     * @param size размер
     * @param gameView игровое поле
     * @param context контекст
     */
    public Wall(float x, float y, float spriteH, float spriteW, GameView gameView, Context context) {
        super(x, y, spriteH, spriteW, gameView);

        this.orientationWall = orientationWall;

        Bitmap youBitmap = MainActivity.wallImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            Random random = new Random();
            switch (random.nextInt(2)) {
                case 0:
                    bitMapId = R.drawable.wall;
                    break;
                case 1:
                    bitMapId = R.drawable.wall;
                    break;
            }
            init(context);
        }
        else {
            init(youBitmap);
        }
    }

    /**
     * Один тик карты
     * @return необходимо-ли измененять карту
     */
    public boolean update(long gameTime) {
        return false;
    }

    public void drow(Paint paint, Canvas canvas) {
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect(Math.round(getX() * gameView.getQwadWidth() - gameView.getVisXUp()), Math.round(getY() * gameView.getQwadHeight() - gameView.getVisYUp()),
                Math.round(getX() * gameView.getQwadWidth() - gameView.getVisXUp()) + bitmap.getWidth(), Math.round(getY() * gameView.getQwadHeight() - gameView.getVisYUp()) + bitmap.getHeight());
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);
    }
}