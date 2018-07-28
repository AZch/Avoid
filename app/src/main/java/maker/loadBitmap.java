package maker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import wordcreators.avoid2.R;

/**
 * Created by anton on 12.01.2018.
 */

public class loadBitmap {
    public static Bitmap[][][] loadPlayer(float spriteH, float spriteW, float qwadHeight, float qwadWidth, Context context) {
        Bitmap bitmapRes;
        Bitmap[][][] player = new Bitmap[4][4][7];
        int oneW = 49, oneH = 49, countBitmap = 0;

        bitmapRes = BitmapFactory.decodeResource(context.getResources(), R.drawable.playerframe);
        oneW = bitmapRes.getWidth() / 7;
        oneH = (bitmapRes.getHeight()) / 16;
        //player[0][0][0] = Bitmap.createScaledBitmap(bitmapRes, Math.round(spriteW * qwadWidth), Math.round(spriteH * qwadHeight), false);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 7; k++) {
                    player[i][j][k] = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmapRes, k * bitmapRes.getWidth() / 7, countBitmap * bitmapRes.getHeight() / 16,
                            bitmapRes.getWidth() / 7, bitmapRes.getHeight() / 16),
                            Math.round(spriteW * qwadWidth), Math.round(spriteH * qwadHeight), false);
                }
                countBitmap++;
            }
        }
        bitmapRes = null;
        return player;
    }

    public static Bitmap[][][] loadEnemy(float spriteH, float spriteW, float qwadHeight, float qwadWidth, Context context) {
        Bitmap bitmapRes;
        Bitmap[][][] enemy = new Bitmap[2][5][5];
        int countBitmap = 0;

        bitmapRes = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyframe);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    enemy[i][j][k] = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmapRes, k * bitmapRes.getWidth() / 5, countBitmap * bitmapRes.getHeight() / 10,
                            bitmapRes.getWidth() / 5, bitmapRes.getHeight() / 10),
                            Math.round(spriteW * qwadWidth), Math.round(spriteH * qwadHeight), false);
                }
                countBitmap++;
            }
        }

        return enemy;
    }

    public static Bitmap[] loadDeadEnemy(float spriteH, float spriteW, float qwadHeight, float qwadWidth, Context context) {
        Bitmap bitmapRes;
        Bitmap[] deadEnemy = new Bitmap[7];
        int countBitmap = 0;

        bitmapRes = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemydeadframe);

        for (int i = 0; i < 7; i++) {
            deadEnemy[i] = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmapRes, 0, i * bitmapRes.getHeight() / 7,
                    bitmapRes.getWidth(), bitmapRes.getHeight() / 7),
                    Math.round(spriteW * qwadWidth), Math.round(spriteH * qwadHeight), false);
        }

        return deadEnemy;
    }
}
