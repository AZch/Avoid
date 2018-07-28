package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import maker.Geometric_Calculate;
import models.Human.Player;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

/**
 * Портал на следующий уровень
 */
public class Portal extends Objects {

    public static int TIME_TO_CHANGE = 8000;
    public static int TIME_TO_VIS = 50;

    // количество времени перед показом портала
    private int timeToVis;

    // указатель телепортации в новую комнату
    private boolean isTeleport;

    // определяет видно ли телепорт
    public boolean visible;

    // количество времени перед сменой местоположения портала
    private int timeToChange;

    // угол вращения
    int angle = 0;

    // матрица для задания вращения
    Matrix matrix = new Matrix();

    /**
     * Конструктор портала
     * @param x абсцисса
     * @param y ордината
     * @param objH высота квадрата
     * @param objW ширина квадрата
     * @param size размер
     * @param gameView игровое поле
     * @param context контекст
     */
    public Portal(float x, float y, float spriteH, float spriteW, GameView gameView, Context context) {
        super(x, y, spriteH, spriteW, gameView);

        Bitmap youBitmap = MainActivity.portalImage;
        if (youBitmap == null || MainActivity.getCountUseTexture() == 0) {
            bitMapId = R.drawable.portal;
            init(context);
        }
        else {
            init(youBitmap);
        }

        timeToVis = 0;
        timeToChange = TIME_TO_CHANGE;
        isTeleport = false;
        visible = false;
    }

    /**
     * Получить указатель телепортации в новую комнату
     * @return указатель телепортации в новую комнату
     */
    public boolean getIsTeleport() { return isTeleport; }

    /**
     * Один тик карты (если игрок в поратле, то перемещяем в новую комнату, а также необходимость моргнуть, либо все время показываться в области видимости игрока)
     * @return
     */
    public boolean update(long gameTime) {
        Objects player = getGameView().getPlayer();

        float sizeToCheck = 1;
        if (getSpriteW() > getSpriteH())
            sizeToCheck = getSpriteW();
        else
            sizeToCheck = getSpriteH();
        if (Geometric_Calculate.destination(player, this) < sizeToCheck) {
            isTeleport = true;
            return true;
        }

        // нужно ли сменить координаты портала
        if (timeToChange <= 0) {
            float[] newPoint;
            //newPoint = Geometric_Calculate.findXY(gameView.getAllObj(), gameView.getObjWall(), gameView.getWidthMap(), gameView.getHeightMap(), gameView.getQwadWidth(), gameView.getQwadHeight(), getSize());
            //setX(newPoint[0]);
            //setY(newPoint[1]);
            timeToChange = TIME_TO_CHANGE;
        } else {
            timeToChange--;
        }

        // нужно ли показать портал
        if (!((Player) getGameView().getPlayer()).getVisPortal()) {
            if (timeToVis <= 0) {
                visible = true;
                timeToVis = TIME_TO_VIS;
            } else {
                timeToVis--;
                visible = false;
            }
        }
        return false;
    }


    // перерисовка портала
    public void drow(Paint paint, Canvas canvas) {
        angle += 50;
        matrix.setTranslate(getX() * gameView.getQwadWidth() - gameView.getVisXUp(), getY() * gameView.getQwadHeight() - gameView.getVisYUp());
        matrix.preRotate( (float) (angle * Math.PI / 180) + 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        //matrix.setTranslate(gameView.getPlayer().getCenterX() * gameView.getQwadWidth() - gameView.getVisXUp(), gameView.getPlayer().getCenterY() * gameView.getQwadHeight() - gameView.getVisYUp());
        canvas.drawBitmap(bitmap, matrix, paint);
        //canvas.drawBitmap(bitmap, getX() * getObjW() - gameView.getVisXUp(), getY() * getObjH() - gameView.getVisYUp(), paint);
    }
}