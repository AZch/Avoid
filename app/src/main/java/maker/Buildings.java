package maker;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import models.Objects;
import models.Wall;

import wordcreators.avoid2.AllGameFile.GameView;

import static android.content.ContentValues.TAG;

/**
 * Набор строений для карты
 */
public class Buildings {
    // XY[0], XY[1], qwadHeight, qwadWidth, wallSize, this, getContext()
    public static void boxBuild(GameView gameView, Context context, ArrayList<Objects> objWall, float sizeOneWall, float countWallWidth, float countWallHeight, int sideDoor) {
        float[] XY;
        if (countWallWidth > countWallHeight)
            XY = Geometric_Calculate.findXY(gameView.getAllObj(), gameView.getObjWall(), gameView.getWidthMap(), gameView.getHeightMap(), gameView.getQwadWidth(), gameView.getQwadHeight(), sizeOneWall * countWallWidth);
        else
            XY = Geometric_Calculate.findXY(gameView.getAllObj(), gameView.getObjWall(), gameView.getWidthMap(), gameView.getHeightMap(), gameView.getQwadWidth(), gameView.getQwadHeight(), sizeOneWall * countWallHeight);
        if (XY[0] == -1 && XY[1] == -1) {
            Log.d(TAG, "boxBuild: miss");
            return;
        }

        Objects wallCorner;

        // верхняя грань
        if (sideDoor != 0) {
            wallCorner = new Wall(XY[0], XY[1], sizeOneWall, sizeOneWall * countWallWidth, gameView, context);
            objWall.add(wallCorner);
        } else {
            try {
                int deviation = 2;
                if (countWallWidth % 2 == 0)
                    deviation = 1;
                wallCorner = new Wall(XY[0], XY[1], sizeOneWall, sizeOneWall * (countWallWidth - (float) (Math.round(countWallWidth / 2)) - 1), gameView, context);
                objWall.add(wallCorner);
                wallCorner = new Wall(XY[0] + sizeOneWall * (countWallWidth - (float) (Math.round(countWallWidth / 2)) + deviation), XY[1], sizeOneWall, sizeOneWall * (countWallWidth - (float) Math.round(countWallWidth / 2) - 1), gameView, context);
                objWall.add(wallCorner);
            } catch (Exception e) {
                Log.d(TAG, "boxBuild: " + e);
            }
        }

        // левая грань
        if (sideDoor != 1) {
            wallCorner = new Wall(XY[0], XY[1], sizeOneWall * countWallHeight, sizeOneWall, gameView, context);
            objWall.add(wallCorner);
        } else {
            try {
                int deviation = 2;
                if (countWallHeight % 2 == 0)
                    deviation = 1;
                wallCorner = new Wall(XY[0], XY[1], sizeOneWall * (countWallHeight - (float) (Math.round(countWallHeight / 2)) - 1), sizeOneWall, gameView, context);
                objWall.add(wallCorner);
                wallCorner = new Wall(XY[0], XY[1] + sizeOneWall * (countWallHeight - (float) (Math.round(countWallHeight / 2)) + deviation), sizeOneWall * (countWallHeight - (float) (Math.round(countWallWidth / 2)) - 1), sizeOneWall, gameView, context);
                objWall.add(wallCorner);
            } catch (Exception e) {
                Log.d(TAG, "boxBuild: " + e);
            }
        }

        // правая грань
        if (sideDoor != 2) {
            wallCorner = new Wall(XY[0] + sizeOneWall * (countWallWidth - 1), XY[1], sizeOneWall * countWallHeight, sizeOneWall, gameView, context);
            objWall.add(wallCorner);
        } else {
            try {
                int deviation = 2;
                if (countWallHeight % 2 == 0)
                    deviation = 1;
                wallCorner = new Wall(XY[0] + sizeOneWall * (countWallWidth - 1), XY[1], sizeOneWall * (countWallHeight - (float) (Math.round(countWallHeight / 2)) - 1), sizeOneWall, gameView, context);
                objWall.add(wallCorner);
                wallCorner = new Wall(XY[0] + sizeOneWall * (countWallWidth - 1), XY[1] + sizeOneWall * (countWallHeight - (float) (Math.round(countWallHeight / 2)) + deviation), sizeOneWall * (countWallHeight - (float) (Math.round(countWallWidth / 2)) - 1), sizeOneWall, gameView, context);
                objWall.add(wallCorner);
            } catch (Exception e) {
                Log.d(TAG, "boxBuild: " + e);
            }
        }

        // нижняя грань
        if (sideDoor != 3) {
            wallCorner = new Wall(XY[0], XY[1] + sizeOneWall * (countWallHeight - 1), sizeOneWall, sizeOneWall * countWallWidth, gameView, context);
            objWall.add(wallCorner);
        } else {
            try {
                int deviation = 2;
                if (countWallWidth % 2 == 0)
                    deviation = 1;
                wallCorner = new Wall(XY[0], XY[1] + sizeOneWall * (countWallHeight - 1), sizeOneWall, sizeOneWall * (countWallWidth - (float) (Math.round(countWallWidth / 2)) - 1), gameView, context);
                objWall.add(wallCorner);
                wallCorner = new Wall(XY[0] + sizeOneWall * (countWallWidth - (float) (Math.round(countWallWidth / 2)) + deviation), XY[1] + sizeOneWall * (countWallHeight - 1), sizeOneWall, sizeOneWall * (countWallWidth - (float) Math.round(countWallWidth / 2) - 1), gameView, context);
                objWall.add(wallCorner);
            } catch (Exception e) {
                Log.d(TAG, "boxBuild: " + e);
            }
        }
    }
}