package maker;

import android.provider.Settings;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import models.Buildings.Building;
import models.Human.Enemy;
import models.Human.Player;
import models.Objects;
import wordcreators.avoid2.AllGameFile.GameView;

/**
 * геометрическия вычисления
 */
public class Geometric_Calculate {
    // получить рандомные координаты для генерации
    public static float[] findXY(ArrayList<ArrayList<Objects>> allObj, ArrayList<Objects> objWall, int widthMap, int heightMap, float qwadWidth, float qwadHeight, float objSize) {
        Random random = new Random();
        boolean add = false;
        float X = -1, Y = -1;
        float firstX = random.nextInt(widthMap), firstY = random.nextInt(heightMap);
        int allObjs = objWall.size();
        long beginTime = System.currentTimeMillis();
        while (!add) {
            if (System.currentTimeMillis() - beginTime == 500) { // если ожидание более 1 / 2-ой секунды
                float[] XY = new float[2];
                XY[0] = -1;
                XY[1] = -1;
                return XY;
            }

            add = true;
            X = random.nextInt(widthMap -  (int) objSize * 2) + (int) objSize;
            Y = random.nextInt(widthMap -  (int) objSize * 2) + (int) objSize;
            if (allObjs == 0)
                break;
            if (X < 0 || Y < 0 || X > widthMap || Y > heightMap) {
                continue;
            }

            for (ArrayList<Objects> objs : allObj) {
                for (Objects obj : objs) {
                    float sizeToCheck;
                    if (obj.getSpriteW() > obj.getSpriteH())
                        sizeToCheck = obj.getSpriteW();
                    else
                        sizeToCheck = obj.getSpriteH();
                    if (destination(X, Y, obj.getCenterX(), obj.getCenterY()) <= destination(obj.getX(), obj.getY(), obj.getX() + obj.getSpriteW(), obj.getY() + obj.getSpriteH()) +
                            destination(X, Y, X + objSize, Y + objSize)) {
                        add = false;
                        break;
                    }
                    if (X > obj.getX() && X < obj.getX() + obj.getSpriteW() && Y > obj.getY() && Y < obj.getY() + obj.getSpriteH()) {
                        add = false;
                        break;
                    }
                    add = true;
                }
                if (!add)
                    break;
            }
        }

        float XY[] = new float[2];
        if (add) {
            XY[0] = X;
            XY[1] = Y;
        }
        else {
            XY[0] = firstX;
            XY[1] = firstY;
        }
        return XY;
    }

    // проверка на видимость игрока
    public static boolean visPlayer(Objects player, Objects enemy) {
        float VectorEnemyPlayer[] = new float[2];
        float VectorEnemySight[] = new float[2];

        VectorEnemyPlayer[0] = player.getCenterX() - enemy.getCenterX();
        VectorEnemyPlayer[1] = player.getCenterY() - enemy.getCenterY();

        VectorEnemySight[0] = (enemy.getCenterX()) * (float) Math.cos(((Enemy) enemy).getAngleSight()) * 2 - enemy.getCenterX();
        VectorEnemySight[1] = (enemy.getCenterY()) * (float) Math.sin(((Enemy) enemy).getAngleSight()) * 2 - enemy.getCenterY();

        VectorEnemyPlayer = normalize(VectorEnemyPlayer);
        VectorEnemySight = normalize(VectorEnemySight);

        double angle = Math.acos(VectorEnemyPlayer[0] * VectorEnemySight[0] + VectorEnemyPlayer[1] * VectorEnemySight[1]) * 180 / Math.PI;
        if (Math.abs(angle) > 90)
            return false;
        else
            return true;





    }

    // нормализация вектора
    public static float[] normalize(float vector[]) {
        float denom = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
        vector[0] = vector[0] / denom;
        vector[1] = vector[1] / denom;
        return vector;
    }

    public static boolean intersectionLinesBuilding(float qwadWidth, float qwadHeight, Objects oneBuilding, float x1, float y1, float x2, float y2) {

        ArrayList<ArrayList<ArrayList<Float>>> allLines = ((Building) oneBuilding).getPointBuildings();

        for (ArrayList<ArrayList<Float>> line : allLines)
            for (int i = 0; i < line.size() - 1; i++)
                if (interesectionLinesBool(qwadWidth, qwadHeight, x1, y1, x2, y2, line.get(i).get(0) * qwadWidth, line.get(i).get(1) * qwadHeight, line.get(i + 1).get(0) * qwadWidth, line.get(i + 1).get(1) * qwadHeight))
                    return true;

        return false;
    }

    /**
     * Определяет пересекается линия со стеной
     *
     * @param oneWall
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean intersectLineWall(float qwadWidth, float qwadHeight, Objects oneWall, float x1, float y1, float x2, float y2) {
        float leftX = oneWall.getX() * qwadWidth,
                topY = oneWall.getY() * qwadHeight,
                bottomY = (oneWall.getY() + oneWall.getSpriteH()) * qwadHeight,
                rightX = (oneWall.getX() + oneWall.getSpriteW()) * qwadWidth;

        if (interesectionLinesBool(qwadWidth, qwadHeight, x1, y1, x2, y2, leftX, topY, leftX, bottomY))
            return true;
        if (interesectionLinesBool(qwadWidth, qwadHeight, x1, y1, x2, y2, leftX, topY, rightX, topY))
            return true;
        if (interesectionLinesBool(qwadWidth, qwadHeight, x1, y1, x2, y2, rightX, bottomY, rightX, topY))
            return true;
        if (interesectionLinesBool(qwadWidth, qwadHeight, x1, y1, x2, y2, rightX, bottomY, leftX, bottomY))
            return true;
        return false;
    }

    public static boolean interesectionLinesBool(float qwadWidth, float qwadHeight, float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2) {
        float v1 = (bx2 - bx1) * (ay1 - by1) - (by2 - by1) * (ax1 - bx1),
              v2 = (bx2 - bx1) * (ay2 - by1) - (by2 - by1) * (ax2 - bx1),
              v3 = (ax2 - ax1) * (by1 - ay1) - (ay2 - ay1) * (bx1 - ax1),
              v4 = (ax2 - ax1) * (by2 - ay1) - (ay2 - ay1) * (bx2 - ax1);
        return (v1 * v2 < 0) && (v3 * v4 < 0);
    }

    public static boolean checkVisObj(Objects thisObj, Objects objVis) {

        float sightXY[] = new float[2];
        if (thisObj instanceof Enemy) {
            sightXY[0] = 1 * (float) Math.cos(((Enemy) thisObj).getAngleSight());
            sightXY[1] = 1 * (float) Math.sin(((Enemy) thisObj).getAngleSight());

            //float
        }

        return false;
    }

    /**
     * Расстояние между объектами в квдарате
     * @param objFirst первый объект
     * @param objSecond второй объект
     * @return расстояние между объектами
     */
    public static float destination(Objects objFirst, Objects objSecond) {
        return (objFirst.getCenterX() - objSecond.getCenterX())*(objFirst.getCenterX() - objSecond.getCenterX()) + (objFirst.getCenterY() - objSecond.getCenterY())*(objFirst.getCenterY() - objSecond.getCenterY());
    }

    /**
     * расстояние между точками
     * @param x1 координата X первой точки
     * @param y1 координата Y первой точки
     * @param x2 координата X второй точки
     * @param y2 координата Y второй точки
     * @return расстояние между точками
     */
    public static float destination(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt( (double) ((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2)));
    }

    /**
     * Определение пересечения линий и получение точки пересечения
     * @param x11 Координата X начала первой линии
     * @param y11 Координата Y начала первой линии
     * @param x12 Координата X конца перовой линии
     * @param y12 Координата Y конца перовй линии
     * @param x21 Координата X начала второй линии
     * @param y21 Координата Y начала второй линии
     * @param x22 Координата X конца второй линии
     * @param y22 Координата Y конца второй линии
     * @return точка пересечения линий, возврщяет -1 -1, если линии не пересекаются (так как карта (по ширине и высоте) не содержит отрицательных координат)
     */
    public static float[] intersectionLines(float x11, float y11, float x12, float y12, float x21, float y21, float x22, float y22) {
        float[] xy = new float[2];
        xy[0] = -1;
        xy[1] = -1;

        float a1 = y11 - y12, a2 = y21 - y22;
        float b1 = x12 - x11, b2 = x22 - x21;

        float denomin = a1 * b2 - a2 * b1;

        if (denomin == 0)
            return xy;

        float c1 = x11 * y12 - x12 * y11, c2 = x21 * y22 - x22 * y21;
        xy[0] = (-1) * ((c1 * b2 - c2 * b1) / (a1 * b2 - a2 * b1));
        xy[1] = (-1) * ((a1 * c2 - a2 * c1) / (a1 * b2 - a2 * b1));

        return xy;
    }

    /**
     * Определение принадлежности точки линии
     * @param x1 Координата X начала линии
     * @param y1 Координата Y начала линии
     * @param x2 Координата X конца линии
     * @param y2 Координата Y конца линии
     * @param x Координата X точки
     * @param y Координата Y точки
     * @return true если точка принадлежит линии, иначе false
     */
    public static boolean pointOnLine(float x1, float y1, float x2, float y2, float x, float y) {
        if ((y - y1) * (x2 - x1) - (x - x1) * (y2 - y1) == 0)
            return true;
        return false;
    }

    /**
     * Расстояние от точки до прямой
     * @param x1 координата X начала прямой
     * @param y1 координата Y начала прямой
     * @param x2 координата X конца прямой
     * @param y2 координата Y конца прямой
     * @param x координата X точки
     * @param y координата Y точки
     * @return расстояние от точки до прямой
     */
    public static double destLinePoint(float x1, float y1, float x2, float y2, float x, float y) {

        float a = y1 - y2, b = x2 - x1, c = x1 * y2 - x2 * y1; // коэфициенты прямой

        return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
    }

    public static ArrayList<Objects> getOtherThings(GameView gameView) {
        ArrayList<Objects> otherThings = new ArrayList<>();
        for (int i = 0; i < gameView.getObjResource().size(); i++) {
            if (Geometric_Calculate.destination(gameView.getPlayer(), gameView.getObjResource().get(i)) <= 10 * 10) {
                otherThings.add(gameView.getObjResource().get(i));
            }
        }
        return otherThings;
    }

    // получить четверть по углу
    public static int getQuarterSight(double angle) {
        while (angle < 0)
            angle += Math.PI * 2;
        if (angle >= 0 && angle < Math.PI / 2)
            return 1;
        if (angle >= Math.PI / 2 && angle < Math.PI)
            return 2;
        if (angle >= Math.PI && angle < 3 * Math.PI / 2)
            return 3;
        if (angle >= 3 * Math.PI / 2 && angle < Math.PI * 2)
            return 4;
        return 0;
    }
}