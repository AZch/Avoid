package wordcreators.avoid2.AllGameFile;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import models.Buildings.Building;
import models.Buildings.SimpleFloor;
import models.Buildings.SimpleHouse;
import models.Bullet;
import models.Human.Enemy;
import models.Human.Human;
import models.Objects;
import models.Human.Player;
import models.Portal;
import models.Box;
import models.Resource.AutoGun;
import models.Resource.Gun;
import models.Resource.MachineGun;
import models.Resource.Medkit;
import models.Resource.Ammo;

import maker.Buildings;
import maker.Sounds;
import maker.Geometric_Calculate;

import constants.paramConst;
import models.Resource.PistolGun;
import models.Resource.Resource;
import wordcreators.avoid2.AllMainMenu.DifficultyWindow;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.AllMainMenu.RecordWindow;
import wordcreators.avoid2.AllSettingsWindow.GameSettingWindow;
import wordcreators.avoid2.AllSettingsWindow.ImageWindow;
import wordcreators.avoid2.AllSettingsWindow.SettingsWindow;
import wordcreators.avoid2.AllSettingsWindow.SoundSetting;
import wordcreators.avoid2.AllSettingsWindow.yourSettingWindow;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

/**
 * Игровое поле
 */

public class GameView extends SurfaceView implements Runnable {

    // флаг игры, указывает на возможность играть
    boolean dontPlaying = false;

    // указатель того что на джостик нажали и нужно двигаться
    public boolean go = false;
    // угол для движения
    public double goAngle = 0;

    // угол для стрельбы
    public float fireAngle = 0;

    // максимальное кол-во квадратов по длине и ширине устройства
    private static int maxLongQwad = 0, maxShortQwad = 0;
    // максимальное кол-во квадратов по горизонтали и вертикали
    private int maxX = 0, maxY = 0;
    // размеры квадратов по высоте и длине
    private float qwadHeight, qwadWidth;

    // размер карты
    private int heightMap = 0, widthMap = 0;

    // точки видимости карты экрана (левая верхняя точка экрана)
    private float visXUp = 0, visYUp = 0;

    // ссылка на игрока
    private Objects player;

    // ссылка на портал
    private Objects portal;

    // ссылка на коробку
    private Objects box;

    // указатель стрельбы стрельбы
    private boolean fireBulletTouch = false;
    private boolean fireBulletJoysitck = false;
    // точка куда необходимо вести стрельбу
    private float xFireBullet = 0, yFireBullet = 0;

    // указатель размера объектов
    private float zoom = 3;

    /**
     * Набор параметров для игры
     * [0] - начальное кол-во здоровья игрока
     * [1] - начальное кол-во патронов игрока
     * [2] - максимальное возможное кол-во аптечек на уравне
     * [3] - максимальное возможное кол-во боеприпасов на уравне
     * [4] - начальное кол-во врагов
     * [5] - начальное кол-во жизней врагов
     * [6] - кол-во врагов, которые добавляются на следующем уровне
     */
    private ArrayList<Integer> param;
    // жизни игрока
    private int lifePlayer;
    // патроны игрока
    private int bulletPlayer;
    // макс возм. кол-во аптечек на уровне
    private int numberOfPossibleMedkit;
    // макс возм. кол-во боеприпасов на уровне
    private int numberOFPossibleAmmo;
    // начальное кол-во врагов
    private int startEnemy;
    // кол-во жизней врагов
    private int lifeEnemy;
    // кол-во врагов, которые добавятся на след. уровне
    private int addEnemyOnNextLvl;
    // скорость игрока
    private int speedPlayer;

    // все объекты на карте
    private final ArrayList<ArrayList<models.Objects>> allObj = new ArrayList<>();
    private ArrayList<Objects> objPlayer = new ArrayList<>(); // все игроки
    private ArrayList<Objects> objWall = new ArrayList<>(); // все стены
    private ArrayList<Objects> objResource = new ArrayList<>(); // все ресурсы
    private ArrayList<Objects> objBullet = new ArrayList<>(); // все патроны
    private ArrayList<Objects> objPortal = new ArrayList<>(); // все парталы
    private ArrayList<Objects> objEnemy = new ArrayList<>(); // все враги
    private ArrayList<Objects> objBuilding = new ArrayList<>(); // трава
    private ArrayList<Objects> objFloor = new ArrayList<>();

    // рисование карты
    private SurfaceHolder surfaceHolder; // объект, который поддерживает рисование (подходит для рисования при частых обновлениях)
    private Paint paint; // то чем рисуем
    private Canvas canvas; // то на чём рисуем

    // игровой поток
    private Thread gameThread = null;

    // указатель остановки игры
    private boolean stopGame = false;

    // размеры
    private static float wallSize = 0; // стена
    private static float playerSize = 0; // игрок
    private static float ammoSize = 0; // боеприпасы
    private static float medkitSize = 0; // аптечка
    private static float boxSize = 0; // коробка
    private static float bulletSize = 0; // пуля
    private static float enemySize = 0; // враг
    private static float portalSize = 0; // портал
    private static float grassSize = 0; // трава
    private static float simpleBuildingSize = 0; // размер просто квадратного здания

    ImageView lifeIcon = null; // иконка жизней игрока
    ImageView bulletIcon = null; // иконка патрон игрока
    ImageView roomsIcon = null; // иконка комнат игрока
    ImageView deadEnemyIcon = null; // иконка кол-ва убийств игрока

    // кол-во пройденных комнат игрока
    private int roomCount = 1;

    // оружие из которого ведётся огонь
    public int fireGun = 1;

    // максимальная комната в забеге
    private int maxRoom = 1;

    // максимальное кол-во убитых врагов в забеге
    private int maxDeadEnemy = 0;

    // указатель паузы
    private boolean pause = false;

    // указатель перехода на новый уровень
    private boolean nextLvl = false;

    // фон пули
    private Bitmap bitmapBullet;

    // звуки в игре для воспроизведения
    public Sounds makeSound = new Sounds(getContext());

    // фон игры
    Bitmap bitmapMap;

    // ссылка на окно с игрой
    Activity parentActivity;

    // номер пули выпущенной с одного зажима для разброса
    private int numberBulletOneShot = 0;

    // флаг запуска игры в меню
    private boolean fMenu = false;

    // обработка прикосновений к экарну в меню
    private ArrayList<ArrayList<Float>> checkTouch = new ArrayList<>();

    // время для исчезания добавленной в инвентарь вещи
    private final int timeAddInInventory = 255; // 255 // но можно и 3 секунды

    // счётчик времени для добавления вещи в инвентарь
    private float timeCountAddInInventory = 0;

    // шаг времени
    private final int stepTimeCountAddInInventory = timeAddInInventory / (paramConst.MAX_FPS * 4);

    /**
     * Конструктор класса GameView
     * @param context контекст
     * @param maxLongQwad максимальное кол-во квдратов по длинной строне ус-ва
     * @param maxShortQwad максимальное кол-во квадратов по короткой стороне ус-ва
     * @param heightMap длина карты
     * @param widthMap ширина карты
     */
    public GameView(Context context, int maxLongQwad, int maxShortQwad, int heightMap, int widthMap, boolean fMenu, Activity parent) {
        super(context);

        this.fMenu = fMenu;

        // ссылка на окно с игрой
        parentActivity = parent;

        // задание размера карты
        this.heightMap = heightMap;
        this.widthMap = widthMap;

        // задание максимального и минимального кол-ва квадратов по сторонам экрана
        this.maxLongQwad = maxLongQwad;
        this.maxShortQwad = maxShortQwad;

        // задание приближения игры
        this.zoom = MainActivity.getZoom();

        // задание рисовки карты в зависимости от ориентации экрана
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setPortrait();
        else
            setLandscape();

        // задание всех объектов
        setObjs();

        if (!fMenu) {
            // задание всех размеров
            setSize();

            // задание всех параметров игры
            setParams();
        }


        // задание ссылок на объект который поддерживает рисование и то чем рисуем
        surfaceHolder = getHolder(); // объект, который поддерживает рисование
        paint = new Paint(); // то чем рисуем
    }

    /**
     * генерация объектов на карте
     * @param enemyCount количество врагов
     * @param wallCount кол-во стен
     * @param lifePlayer кол-во жизней игрока
     * @param medkitCount максимально возможное кол-во аптечек
     * @param ammoCount максимально возможное кол-во боеприпасов
     * @param countRoom номер уровня
     * @param bulletCount кол-во пуль
     * @param startLvlBox уровень с которого спавнится коробка
     * @param enemyLife кол-во жизней врага
     * @param deadEnemy кол-во убитых врагов
     */
    private void generate(int enemyCount, int wallCount, int grassCount, int lifePlayer, int medkitCount, int ammoCount, int countRoom, int bulletCount, int startLvlBox, int enemyLife, int deadEnemy, int speedPlayer, Inventory inventory) {
        // задание карты

        //bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backk), (int) (widthMap * qwadWidth), (int) (heightMap * qwadHeight), false);
        loadGame(0, "Генерирование зданий");
        bitmapMap = MainActivity.bitmapBack;
        //Bitmap bitmapHelt = BitmapFactory.decodeResource(getResources(), R.drawable.halth_live);

        //Bitmap bitmapBulletRes = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        //bitmapBullet = Bitmap.createScaledBitmap(bitmapBulletRes, bitmapBulletRes.getWidth() / (int) ((bulletSize) * qwadWidth), bitmapBulletRes.getHeight() / (int) ((bulletSize) * qwadHeight), false);
        //bitmapBulletRes.recycle();

        Random random = new Random();
        float XY[];

        Bitmap bitmapFloor = BitmapFactory.decodeResource(getResources(), R.drawable.floor);
        // генерирование стен
        for (int i = 0; i < 30; i++) {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, simpleBuildingSize);
            if (XY[0] != -1 && XY[1] != -1) {
                Objects simpleHouse = new SimpleHouse(XY[0], XY[1], simpleBuildingSize, simpleBuildingSize, this, getContext(), MainActivity.getBitmapPlayer(0, 0, 0).getWidth());
                objBuilding.add(simpleHouse);
                Objects simpleFloor = new SimpleFloor(((Building) simpleHouse).getPointBuildings().get(0).get(2).get(0), ((Building) simpleHouse).getPointBuildings().get(0).get(2).get(1),
                        ((Building) simpleHouse).getPointBuildings().get(0).get(3).get(0) - ((Building) simpleHouse).getPointBuildings().get(0).get(2).get(0), ((Building) simpleHouse).getPointBuildings().get(0).get(1).get(1) - ((Building) simpleHouse).getPointBuildings().get(0).get(2).get(1),
                        this, bitmapFloor);
                objFloor.add(simpleFloor);
            } else {
                break;
            }
            //Buildings.boxBuild(this, getContext(), objWall, wallSize / (random.nextInt(2) + 2), random.nextInt(6) + 5, random.nextInt(6) + 5, random.nextInt(4));
        }

        /*for (int i = 0; i < wallCount / 2; i++) {
            XY = Geometric_Calculate.findXY(allObj, objWall, widthMap, heightMap, qwadWidth, qwadHeight, wallSize);
            Objects wall = new Wall(XY[0], XY[1], wallSize, wallSize, this, getContext());
            objWall.add(wall);
        }*/

        loadGame(40, "Генерирование игрока");
        //loadGame(40, "Генерирование игрока");
        // генерирование игрока
        do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, playerSize);
        } while (XY[0] == -1 && XY[1] == -1);
        Objects player = new Player(XY[0], XY[1], playerSize, playerSize, this, getContext(), lifePlayer, ((float) speedPlayer) / 15, bulletCount, deadEnemy, null, inventory);
        this.player = player;
        objPlayer.add(player);
        setStartVisPoint();

        loadGame(45, "Генерирование аптечек");
        // генерирование аптечек
        while (medkitCount > 0) {
            do {
                XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, medkitSize);
            } while (XY[0] == -1 && XY[1] == -1);
            Objects medkit = new Medkit(XY[0], XY[1], medkitSize, medkitSize, this, getContext());
            objResource.add(medkit);
            medkitCount--;
        }

        loadGame(55, "Генерирование коробок с боеприпасами");
        // генерирование коробок с боеприпасами
        while (ammoCount > 0) {
            do {
                XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, ammoSize);
            } while (XY[0] == -1 && XY[1] == -1);
            Objects ammo = new Ammo(XY[0], XY[1], ammoSize, ammoSize, this, getContext());
            objResource.add(ammo);
            ammoCount--;
        }

        do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, ammoSize);
        } while (XY[0] == -1 && XY[1] == -1);
        Objects autoGun = new AutoGun(XY[0], XY[1], ammoSize, ammoSize, this, 1, getContext());
        objResource.add(autoGun);

        do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, ammoSize);
        } while (XY[0] == -1 && XY[1] == -1);
        autoGun = new AutoGun(XY[0], XY[1], ammoSize, ammoSize, this, 1, getContext());
        objResource.add(autoGun);

        /*do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, ammoSize);
        } while (XY[0] == -1 && XY[1] == -1);*/
        Objects pistolGun = new PistolGun(player.getX() + 5, player.getY() + 5, ammoSize, ammoSize, this, 1, getContext());
        objResource.add(pistolGun);

        do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, ammoSize);
        } while (XY[0] == -1 && XY[1] == -1);
        Objects machineGun = new MachineGun(XY[0], XY[1], ammoSize, ammoSize, this, 1, getContext());
        objResource.add(machineGun);

        // генерирование коробки
        /*XY = Geometric_Calculate.findXY(allObj, objWall, widthMap, heightMap, qwadWidth, qwadHeight, boxSize);
        Objects box = new Box(XY[0], XY[1], boxSize, boxSize, this,  getContext());
        this.box = box;
        objResource.add(box);
        if (true)
            ((Box) box).breakeBox();*/

        loadGame(60, "Генерирование портала");
        // генерирование портала
        do {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, portalSize);
        } while (XY[0] == -1 && XY[1] == -1);
        Objects portal = new Portal(XY[0], XY[1], portalSize, portalSize, this, getContext());
        this.portal = portal;
        objPortal.add(portal);

        loadGame(80, "Генерирование противников");
        for (int i = 0; i < enemyCount; i++) {
            XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, enemySize);
            if (XY[0] != -1 && XY[1] != -1) {
                Objects enemy = new Enemy(XY[0], XY[1], enemySize, enemySize, this, getContext(), enemyLife, (float) (random.nextInt(paramConst.randomDiapSpeed) + paramConst.randomMinSpeed) / 15, null);
                objEnemy.add(enemy);
            }
        }


        loadGame(100, "Генерирование закончено");
        //allObj.add(0, objGrass);
    }

    /**
     * Необходимость следования за игроком
     * @param enemy враг
     * @return необходимо-ли следовать за игроком
     */
    public boolean visPlayer(Objects enemy) {
        // если игрок не в области видимости, либо находится в коробке
        if (fMenu || Geometric_Calculate.destination(enemy, player) > ((Enemy) enemy).getVision() * ((Enemy) enemy).getVision() || ((Player) player).getInBox()) {
            return false;
        }

        if (!Geometric_Calculate.visPlayer(player, enemy)) {
            return false;
        }

        // получение стен в области видимости
        ArrayList<Objects> wallAndBuild /*= getVisibleObjWall(enemy, ((Enemy) enemy).getVision() + (int) (wallSize * 1.5))*/;

        // если игрока видно, а стен не видно
        /*if (wallAndBuild.size() == 0) {
            return true;
        }*/

        // определение: находится ли игрок за стеной
        //for (Objects oneWall : wallAndBuild) {
        //    if (Geometric_Calculate.intersectLineWall(qwadWidth, qwadHeight, oneWall, enemy.getCenterX() * qwadWidth, enemy.getCenterY() * qwadHeight, player.getCenterX() * qwadWidth, player.getCenterY() * qwadHeight))
        //        return false;
        //}

        // получение зданий в области видимости
        wallAndBuild = getVisibleObjBuilding(enemy, ((Enemy) enemy).getVision() + (int)(simpleBuildingSize +  simpleBuildingSize / 2));

        if (wallAndBuild.size() == 0) {
            return true;
        }

        for (Objects oneBuild : wallAndBuild) {
            //if (Math.sqrt(Geometric_Calculate.destination(enemy, oneBuild)) + oneBuild.getSpriteW() / 2 <= Math.sqrt(Geometric_Calculate.destination(enemy, player)))
                if (Geometric_Calculate.intersectionLinesBuilding(qwadWidth, qwadHeight, oneBuild, enemy.getCenterX() * qwadWidth, enemy.getCenterY() * qwadHeight, player.getCenterX() * qwadWidth, player.getCenterY() * qwadHeight))
                    return false;
        }

        return true;
    }

    /**
     * Удаление всех объектов с карты
     */
    public void clearObjects() {
        for (ArrayList<Objects> objs : allObj)
            objs.clear();
        //allObj.remove(0);
    }

    //
    // методы обработки игры
    //

    // игровой цикл
    @Override
    public void run() {
        Random random = new Random();
        float[] XY = new float[2];
        // задание начальных параметров и сохранение кол-во жизней и пуль
        int enemyCount = startEnemy;
        nextLvl = false;
        int lifePlayerSave = lifePlayer, bulletPlayerSave = bulletPlayer;
        while(true) {
            if (dontPlaying)
                continue;
            if (stopGame)
                return;
            if (surfaceHolder.getSurface().isValid()) {
                qwadHeight = surfaceHolder.getSurfaceFrame().height() / maxY;
                qwadWidth = surfaceHolder.getSurfaceFrame().width() / maxX;

                // генерациия нового уровня
                if (!fMenu)
                    enemyCount = generateLvl(enemyCount, lifePlayerSave, bulletPlayerSave);
                else
                    generateMenu(10);

                // один уровень
                dontPlaying = false;

                long startTime; // время начала цикла
                long stepTime; // время выполнения шага цикла
                int sleepTime = 0; // сколько можно спать в мс (если < 0, то выполнение опаздывает)
                int framesSkipped = 0; // кол-во кадров у которых не выводилась графика на экран

                while (!dontPlaying) {

                    if (stopGame) // если игра закончена
                        return;
                    if (pause) // игру поставили на паузу
                        continue;

                    if (fMenu) {
                        if (parentActivity instanceof MainActivity)
                            ((MainActivity) parentActivity).changeColor();
                    }

                    if (!fMenu) {
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (parentActivity instanceof GameWindow)
                                    ((GameWindow)parentActivity).updateActiveGun();
                            }
                        });
                    } else {
                        for (int i = 0; i < checkTouch.size(); i++) {
                            for (int j = 0; j < objEnemy.size(); j++) {
                                if (checkTouch.get(i).get(0) / getQwadWidth()  >= objEnemy.get(j).getX() &&
                                        checkTouch.get(i).get(0) / getQwadWidth() <= objEnemy.get(j).getX() + MainActivity.getBitmapEnemy(0,0,0).getWidth() / getQwadWidth() &&
                                        checkTouch.get(i).get(1) / getQwadHeight() >= objEnemy.get(j).getY() &&
                                        checkTouch.get(i).get(1) / getQwadHeight() <= objEnemy.get(j).getY() + MainActivity.getBitmapEnemy(0,0,0).getHeight() / getQwadHeight()) {

                                    XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, enemySize);
                                    objEnemy.add(new Enemy(XY[0], XY[1], 0, 0, this, getContext(), 100,
                                            (float)(random.nextInt(paramConst.randomDiapSpeed) + paramConst.randomMinSpeed) / 15, null));

                                    XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, enemySize);
                                    objEnemy.add(new Enemy(XY[0], XY[1], 0, 0, this, getContext(), 100,
                                            (float)(random.nextInt(paramConst.randomDiapSpeed) + paramConst.randomMinSpeed) / 15, null));
                                    try {
                                        ((Enemy) objEnemy.get(j)).contact(110);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        checkTouch.clear();
                    }

                    try {
                        startTime = System.currentTimeMillis();
                        framesSkipped = 0; // обнулим счётчик пропущенных кадров

                        // проверка на необходимости выстрела и воспроизведение выстрела
                        shotPlayer();

                        // обновление игрового состояния
                        dontPlaying = update();

                        draw(); // перерисовка

                        if (fMenu && objEnemy.size() == 0)
                            dontPlaying = true;

                        if(fMenu && objEnemy.size() > random.nextInt(200) + 100)
                            killAllEnemy();

                        stepTime = System.currentTimeMillis() - startTime; // время работы цикла в мс
                        sleepTime = (int) (paramConst.FRAME_PERIOD - stepTime);

                        if (dontPlaying)
                            break;

                        if (sleepTime > 0)  // пауза, если таковая необходима
                            pause(sleepTime);

                        while (sleepTime < 0 && framesSkipped < paramConst.MAX_FRAME_SKIPS) {
                            // обновление игрового состояния
                            dontPlaying = update();
                            if (dontPlaying) {
                                draw(); // перерисовка
                                break;
                            }
                            sleepTime += paramConst.FRAME_PERIOD;
                            framesSkipped++;
                            Log.d(TAG, "RUN, frame skipped: " + framesSkipped);
                        }

                    } finally {
                        //Log.d(TAG, "run: finally");
                        //if (framesSkipped > 0)
                           // draw();
                    }
                }

                if (parentActivity instanceof GameWindow) {
                    dontPlaying = false;
                    clearObjects(); // очистка всех объектов
                } else {
                    /*if (parentActivity instanceof MainActivity)
                        ((MainActivity) parentActivity).doneGame();
                    else if (parentActivity instanceof DifficultyWindow)
                        ((DifficultyWindow) parentActivity).doneGame();
                    else if (parentActivity instanceof RecordWindow)
                        ((RecordWindow) parentActivity).doneGame();
                    else if (parentActivity instanceof GameSettingWindow)
                        ((GameSettingWindow) parentActivity).doneGame();
                    else if (parentActivity instanceof ImageWindow)
                        ((ImageWindow) parentActivity).doneGame();
                    else if (parentActivity instanceof SettingsWindow)
                        ((SettingsWindow) parentActivity).doneGame();
                    else if (parentActivity instanceof SoundSetting)
                        ((SoundSetting) parentActivity).doneGame();
                    else if (parentActivity instanceof yourSettingWindow)
                        ((DifficultyWindow) parentActivity).doneGame();*/

                }
            }
        }
    }

    /**
     * генерация нового уровня
     * @param enemyCount кол-во врагов до генерации
     * @param lifePlayerSave изначальное кол-во жизней игрока
     * @param bulletPlayerSave изначальное кол-во патрон игрока
     * @return кол-во врагов после генерации
     */
    private int generateLvl(int enemyCount, int lifePlayerSave, int bulletPlayerSave) {
        java.util.Random random = new java.util.Random();
        // генерация если игрок зашёл в новую комнату, погиб или первый раз запустил игру
        if (nextLvl) { // если игрок перешёл на новый уровень
            generate(enemyCount, (int) (1 / zoom * 1000), (0), lifePlayer, random.nextInt(numberOfPossibleMedkit), random.nextInt(numberOFPossibleAmmo), roomCount, bulletPlayer, 10, /*lifeEnemy*/100, ((Player) player).getKillCount(), speedPlayer, ((Player) player).getInventory());
            enemyCount += addEnemyOnNextLvl;
            roomCount++;
        } else {
            if (roomCount > maxRoom)
                maxRoom = roomCount;
            if (player != null && ((Player) player).getKillCount() > maxDeadEnemy)
                maxDeadEnemy = ((Player) player).getKillCount();
            enemyCount = startEnemy;
            roomCount = 1;
            lifePlayer = lifePlayerSave;
            bulletPlayer = bulletPlayerSave;
            generate(enemyCount, (int) (1 / zoom * 1000), (0), /*lifePlayer*/ 100, random.nextInt(numberOfPossibleMedkit), random.nextInt(numberOFPossibleAmmo), roomCount, bulletPlayer, 10, /*lifeEnemy*/ 100, 0, speedPlayer, null);
        }
        return enemyCount;
    }

    private void generateMenu(int enemyCount) {
        bitmapMap = MainActivity.bitmapBack;
        float[] XY;
        Random random = new Random();
        for (int i = 0; i < enemyCount; i++) {
            do {
                XY = Geometric_Calculate.findXY(allObj, objBuilding, widthMap, heightMap, qwadWidth, qwadHeight, enemySize);
            } while (XY[0] == -1 && XY[1] == -1);
            Objects enemy = new Enemy(XY[0], XY[1], 0, 0, this, getContext(), 100, (float)(random.nextInt(paramConst.randomDiapSpeed) + paramConst.randomMinSpeed) / 15, null);
            objEnemy.add(enemy);
        }
    }

    public void restartGame() {
        clearObjects();
        dontPlaying = false;
    }

    // проверка на необходимости выстрела и воспроизведение выстрела
    private void shotPlayer() {
        if (fireBulletJoysitck || fireBulletTouch) {
            ((Player) player).shot(fireGun);
            numberBulletOneShot++;
        }
        /*if (((Player) player).getBulletCount() > 0 && ((Player) player).getCoolDownFire() == 0) { // если нужно произвести выстрел
            if (fireBulletJoysitck) {
                ((Player) player).shot(fireGun);
                Bullet newBullet = new Bullet(player.getCenterX(), player.getCenterY(), bulletSize, bulletSize, bitmapBullet, this, getContext(), fireAngle);
                objBullet.add(newBullet);
            } else if (fireBulletTouch) {
                ((Player) player).shot(fireGun);
                fireAngle = (float) (Math.atan2(player.getCenterY() * qwadHeight - yFireBullet + visYUp, xFireBullet + visXUp - player.getCenterX() * qwadWidth)) * (-1);
                Bullet newBullet = new Bullet(player.getCenterX(), player.getCenterY(), bulletSize, bulletSize, bitmapBullet, this, getContext(), fireAngle);
                objBullet.add(newBullet);
            }
        }*/
    }

    // обновление игрового состояния
    private boolean update() {
        boolean dontPlaying = false;
        // обновление состояния всех объектов, для которых это нужно
        for (int i = 0; i < allObj.size(); i++) {
            //if (stopGame)
                //return;
            if (allObj.get(i) != objWall && allObj.get(i) != objBuilding && allObj.get(i) != objFloor) {
                for (int j = 0; j < allObj.get(i).size(); j++) {
                    if (MainActivity.getUpdateAllEnemy() == 0) // если необходимо обновлять состояние всех динамических объектов на карте
                        dontPlaying = allObj.get(i).get(j).update(System.currentTimeMillis()); // обновление игрового состояния объекта на карте
                    else { // если необходимо обновлять врагов только на экране
                            if (checkObjVis(allObj.get(i).get(j)))
                                dontPlaying = allObj.get(i).get(j).update(System.currentTimeMillis());

                    }
                    // обновление данных о состоянии игры
                    if (allObj.get(i).get(j) instanceof Portal) { // если игрок зашёл в портал
                        if (dontPlaying) {
                            nextLvl = true;
                            break;
                        }
                    } else if (allObj.get(i).get(j) instanceof Player) { // у игрока кончились жизни
                        lifePlayer = ((Player) player).getLiveCount();
                        bulletPlayer = ((Player) player).getBulletCount();
                        if (((Player) allObj.get(i).get(j)).getLiveCount() <= 0) {
                            if (dontPlaying) {
                                nextLvl = false;
                                break;
                            }
                        } else {
                            if (fireBulletJoysitck && dontPlaying) {
                                ((Player) player).shot(fireGun);
                                Bullet newBullet = new Bullet(player.getCenterX(), player.getCenterY(), bulletSize, bulletSize, bitmapBullet, this, getContext(), fireAngle, ((Player) player).getDamage(fireGun), numberBulletOneShot, ((Player) player).getMaxAngle(fireGun));
                                objBullet.add(newBullet);
                            }
                            dontPlaying = false;
                        }
                    } else if ((allObj.get(i).get(j) instanceof Bullet || allObj.get(i).get(j) instanceof Resource) && dontPlaying) { // если пуля встретилась с препятствием
                        allObj.get(i).remove(allObj.get(i).get(j));
                        dontPlaying = false;
                        j--;
                    } else if (allObj.get(i).get(j) instanceof Enemy && dontPlaying && !fMenu) {
                        if (!((Enemy) allObj.get(i).get(j)).isCountingKill())
                            ((Player) player).addKill();
                        else if (((Enemy) allObj.get(i).get(j)).isDelete()) {
                            allObj.get(i).remove(allObj.get(i).get(j));
                            j--;
                        }
                        dontPlaying = false;
                    } else if (allObj.get(i).get(j) instanceof Enemy && dontPlaying && fMenu) {
                        dontPlaying = false;
                        if (((Enemy) allObj.get(i).get(j)).isDelete()) {
                            allObj.get(i).remove(allObj.get(i).get(j));
                            j--;
                        }
                    }
                }
            }

            if (dontPlaying)
                break;
        }

        return dontPlaying;
    }

    // убийство всех противников
    public void killAllEnemy() {
        for (int i = 0; i < objEnemy.size(); i++) {
            ((Enemy) objEnemy.get(i)).contact(100);
        }
    }

    // пауза
    private void pause(int sleepTime) {
        try {
            gameThread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startTimeAddInventory() {
        timeCountAddInInventory = timeAddInInventory;
    }

    // перерисока
    private void draw() {
        try {
            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.LTGRAY);
                Rect srcRect = new Rect(Math.round(visXUp), Math.round(visYUp), Math.round(visXUp + surfaceHolder.getSurfaceFrame().width()), Math.round(visYUp + surfaceHolder.getSurfaceFrame().height()));
                Rect destRect = new Rect(0, 0, surfaceHolder.getSurfaceFrame().width(), surfaceHolder.getSurfaceFrame().height());
                canvas.drawBitmap(bitmapMap, srcRect, destRect, paint);

                // canvas.drawBitmap(bitmap, 0, 0, paint);
                try {
                    // рисование только тех объектов котрые видны на экране
                    for (ArrayList<Objects> objs : allObj)
                        for (Objects obj : objs)
                            //if (obj.getX() * obj.getObjW() > visXUp - wallSize * 1.5 * qwadWidth && obj.getY() * obj.getObjH() > visYUp - wallSize * 1.5 * qwadHeight && (obj.getX() + obj.getSpriteW()) * obj.getObjW()  < visXUp + surfaceHolder.getSurfaceFrame().width() + wallSize * 1.5 * qwadWidth && (obj.getY() + obj.getSize()) * obj.getObjH() < visYUp + surfaceHolder.getSurfaceFrame().height() + wallSize * 1.5 * qwadHeight)
                            if (checkObjVis(obj))
                                //if (obj instanceof SimpleFloor && )
                                obj.drow(paint, canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // вывод кол-ва патрон и жизний игрок ана экран
                if (!fMenu) {
                    if (parentActivity instanceof GameWindow)
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (timeCountAddInInventory >= 0) {
                                    ((GameWindow) parentActivity).addInInventory.setAlpha(timeCountAddInInventory / 255);
                                    timeCountAddInInventory -= stepTimeCountAddInInventory;
                                    if (timeCountAddInInventory < 0)
                                        timeCountAddInInventory = 0;
                                }

                                ((GameWindow) parentActivity).lifeText.setText(String.valueOf(((Player) player).getLiveCount()));
                                ((GameWindow) parentActivity).bulletCountText.setText(String.valueOf(((Player) player).getBulletCount()));
                                ((GameWindow) parentActivity).roomsCountText.setText(String.valueOf(roomCount));
                                ((GameWindow) parentActivity).enemyDeadCountText.setText(String.valueOf(((Player) player).getKillCount()));
                                if (((Player) player).getInventory().getGun(fireGun) != null)
                                    ((GameWindow) parentActivity).thisBulletCountText.setText(String.valueOf(((Gun) ((Player) player).getInventory().getGun(fireGun)).getCountBulletInGun()));
                                else
                                    ((GameWindow) parentActivity).thisBulletCountText.setText("");
                            }
                        });

                    /*paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(25);
                    // вывод кол-ва жизней
                    paint.setColor(Color.RED);
                    canvas.drawText(String.valueOf(lifePlayer), lifeIcon.getX() + lifeIcon.getWidth() + 5, lifeIcon.getY() + lifeIcon.getHeight() / 2 + 5, paint);
                    // вывод кол-ва патрон
                    paint.setColor(Color.rgb(120, 120, 120));
                    canvas.drawText(String.valueOf(((Player) player).getBulletCount()), bulletIcon.getX() + bulletIcon.getWidth() + 5, bulletIcon.getY() + bulletIcon.getHeight() / 2 + 5, paint);
                    // вывод кол-ва пройденных комнат
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.roomsCountIconText));
                    canvas.drawText(String.valueOf(roomCount), roomsIcon.getX() + roomsIcon.getWidth() + 5, roomsIcon.getY() + roomsIcon.getHeight() / 2 + 5, paint);
                    // вывод кол-ва убитых врагов
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.deadEnemyIconText));
                    canvas.drawText(String.valueOf(((Player) player).getKillCount()), deadEnemyIcon.getX() + deadEnemyIcon.getWidth() + 5, deadEnemyIcon.getY() + deadEnemyIcon.getHeight() / 2 + 5, paint);*/

                }
                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // проверяет объект на нахождение его на экране
    private boolean checkObjVis(Objects obj) {
        int screenW = surfaceHolder.getSurfaceFrame().width();
        int screenH = surfaceHolder.getSurfaceFrame().height();
        float deviation = (float) 1.5 * obj.getSpriteH() / 2 ; // 1.5 - sqrt(2), тк все предметы здесь - квадраты
        // левая верхняя точка
        if (obj.getX() * qwadWidth < visXUp + screenW + deviation && obj.getY() * qwadHeight < visYUp + screenH + deviation)
            return true;
        // правая верхняя точка
        if ((obj.getX() + obj.getSpriteW()) * qwadWidth > visXUp - deviation && obj.getY() * qwadHeight < visYUp + screenH + deviation)
            return true;
        // левая нижняя точка
        if (obj.getX() * qwadWidth < visXUp + screenW + deviation && (obj.getY() + obj.getSpriteH()) * qwadHeight > visYUp - deviation)
            return true;
        // правая нижняя точка
        if ((obj.getX() + obj.getSpriteW()) * qwadWidth > visXUp - deviation && (obj.getY() + obj.getSpriteH()) * qwadHeight > visYUp - deviation)
            return true;
        return false;
    }

    //
    // методы обработки состояния игры
    //

    // остановить поток игры
    public void stopThread() {
        if (roomCount > maxRoom)
            maxRoom = roomCount;
        if (player != null && ((Player) player).getKillCount() > maxDeadEnemy)
            maxDeadEnemy = ((Player) player).getKillCount();
        makeSound.killSounds();
        stopGame = true;
    }

    // поставить поток игры на паузу
    public void pauseGame() {
        pause = true;
    }

    // возобновить поток игры
    public void resumeGame() {
        pause = false;
    }

    // вывод состояния загрузки
    private void loadGame(final int progressLoad, final String whatLoad) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (parentActivity instanceof GameWindow)
                    ((GameWindow) parentActivity).loadGame(progressLoad, whatLoad);
            }
        });
    }

    // выбросить вещь
    public void dropThing(Objects thing) {
        ((Resource) thing).drop(player.getX(), player.getY());
        objResource.add(thing);
    }

    // подобрать вещь через инвентарь
    public void riseThing(Objects thing) {
        ((Resource) thing).raise();
    }

    //
    // методы движения точки экрана
    //

    /**
     * движение верхней левой точки по карте
     * @param angle угол движения
     * @param speed скорость движения
     */
    public void moveVisPoint(double angle, float speed) {
        float newVisXUp = visXUp + speed * qwadWidth * (float) Math.cos(angle),
                newVisYUp = visYUp + speed * qwadHeight * (float) Math.sin(angle);

        if ((player.getCenterX() * qwadWidth >= surfaceHolder.getSurfaceFrame().width() / 2) && player.getCenterX() * qwadWidth <= widthMap * qwadWidth - surfaceHolder.getSurfaceFrame().width() / 2) {
            visXUp = newVisXUp;
        }
        else if (player.getCenterX() * qwadHeight < surfaceHolder.getSurfaceFrame().width() / 2)
            visXUp = 0;
        else
            visXUp = widthMap * qwadWidth - surfaceHolder.getSurfaceFrame().width();


        if ((player.getCenterY() * qwadHeight > surfaceHolder.getSurfaceFrame().height() / 2) && player.getCenterY() * qwadHeight < heightMap * qwadHeight - surfaceHolder.getSurfaceFrame().height() / 2) {
            visYUp = newVisYUp;
        }
        else if (player.getCenterY() * qwadHeight < surfaceHolder.getSurfaceFrame().height() / 2)
            visYUp = 0;
        else
            visYUp = heightMap * qwadHeight - surfaceHolder.getSurfaceFrame().height();

    }

    // движение точки экрана по OX
    public void moveVisPointX(float addX) {
        if ((player.getCenterX() * qwadWidth >= surfaceHolder.getSurfaceFrame().width() / 2) && player.getCenterX() * qwadWidth <= widthMap * qwadWidth - surfaceHolder.getSurfaceFrame().width() / 2) {
            visXUp += addX * qwadWidth;
        }
        else if (player.getCenterX() * qwadWidth < surfaceHolder.getSurfaceFrame().width() / 2)
            visXUp = 0;
        else
            visXUp = widthMap * qwadWidth - surfaceHolder.getSurfaceFrame().width();
    }

    // движение точки экрана по OY
    public void moveVisPointY(float addY) {
        if ((player.getCenterY() * qwadHeight > surfaceHolder.getSurfaceFrame().height() / 2) && player.getCenterY() * qwadHeight < heightMap * qwadHeight - surfaceHolder.getSurfaceFrame().height() / 2) {
            visYUp += addY * qwadHeight;
        }
        else if (player.getCenterY() * qwadHeight < surfaceHolder.getSurfaceFrame().height() / 2)
            visYUp = 0;
        else
            visYUp = heightMap * qwadHeight - surfaceHolder.getSurfaceFrame().height();
    }

    //
    // методы связанные со стрельбой
    //

    /**
     * Начало стрельбы
     * @param xFireBullet куда стрелять по х
     * @param yFireBullet куда стрелять по y
     */
    public void startFire(float xFireBullet, float yFireBullet) {
        this.xFireBullet = xFireBullet;
        this.yFireBullet = yFireBullet;
        fireBulletTouch = true;
    }

    // начало стрельбы отностительно угла
    public void startFire(float fireAngle) {
        this.fireAngle = fireAngle;
        fireBulletJoysitck = true;
    }

    //Остановка стрельбы
    public void stopFire() {
        numberBulletOneShot = 0;
        fireBulletTouch = false;
        fireBulletJoysitck = false;
        ((Player) getPlayer()).stopFire();
    }

    public void addTouch(float x, float y) {
        /*float[] XY = new float[2];
        XY[0] = x;
        XY[1] = y;*/
        ArrayList<Float> point = new ArrayList<>();
        point.add(x);
        point.add(y);
        checkTouch.add(point);
    }

    //
    // все set методы
    //

    public void setGameWindow(GameWindow gameWindow) {
        this.parentActivity = gameWindow;
    }

    /**
     * задание размера объектам на карте
     */
    private void setSize() {
        wallSize = zoom + zoom / 2; // стена
        playerSize = zoom; // игрок
        ammoSize = (zoom - zoom / 2); // боеприпасы
        medkitSize = (zoom - zoom / 2); // аптечка
        boxSize = (zoom - zoom / 3); // коробка
        bulletSize = zoom / 3; // пуля
        enemySize = zoom; // враг
        portalSize = zoom + zoom / 3;
        grassSize = zoom + zoom / 2; // портал
        simpleBuildingSize = zoom * 4;
    }

    //Задание параметров на карте
    private void setParams() {
        param = MainActivity.getParam();
        lifePlayer = param.get(0);
        bulletPlayer = param.get(1);
        numberOfPossibleMedkit = param.get(2);
        numberOFPossibleAmmo = param.get(3);
        startEnemy = param.get(4);
        lifeEnemy = param.get(5);
        addEnemyOnNextLvl = param.get(6);
        speedPlayer = param.get(7);
    }

    //Задание всех массивов объектов на карте
    private void setObjs() {
        allObj.add(objFloor);
        allObj.add(objResource);
        allObj.add(objPortal);
        allObj.add(objEnemy);
        allObj.add(objPlayer);
        allObj.add(objBullet);
        allObj.add(objBuilding);
        allObj.add(objWall);
    }

    // при вертикальной ориентации устр-ва
    public void setPortrait() {
        if (maxX == 0) {
            maxX = maxShortQwad;
            maxY = maxLongQwad;
        } else {
            maxX = maxShortQwad;
            maxY = maxLongQwad;
            qwadHeight = surfaceHolder.getSurfaceFrame().height() / maxY;
            qwadWidth = surfaceHolder.getSurfaceFrame().width() / maxX;
        }
    }

    // при горизонтальной ориентации ус-ва
    public void setLandscape() {
        if (maxX == 0) {
            maxX = maxLongQwad;
            maxY = maxShortQwad;
        } else {
            maxX = maxLongQwad;
            maxY = maxShortQwad;
            qwadHeight = surfaceHolder.getSurfaceFrame().height() / maxY;
            qwadWidth = surfaceHolder.getSurfaceFrame().width() / maxX;
        }
    }

    // задание начального положения точки экрана
    private void setStartVisPoint() {
        // задание левой врехней точки экрана на карте
        if (player.getCenterX() * qwadWidth - surfaceHolder.getSurfaceFrame().width() / 2 <= 0)
            visXUp = 0;
        else if (player.getCenterX() * qwadWidth + surfaceHolder.getSurfaceFrame().width() / 2 > widthMap * qwadWidth)
            visXUp = widthMap * qwadWidth - surfaceHolder.getSurfaceFrame().width();
        else
            visXUp = player.getCenterX() * qwadWidth - surfaceHolder.getSurfaceFrame().width() / 2;

        if (player.getCenterY() * qwadHeight - surfaceHolder.getSurfaceFrame().height() / 2 <= 0)
            visYUp = 0;
        else if (player.getCenterY() * qwadHeight + surfaceHolder.getSurfaceFrame().height() / 2 > heightMap * qwadHeight)
            visYUp = heightMap * qwadHeight - surfaceHolder.getSurfaceFrame().height();
        else
            visYUp = player.getCenterY() * qwadHeight - surfaceHolder.getSurfaceFrame().height() / 2;
    }

    //
    // все get методы
    //

    // получить размер здания
    public float getSizeSimpleBuilding() {
        return simpleBuildingSize;
    }

    // получить игровое окно
    public GameWindow getGameWindow() {
        if (parentActivity instanceof GameWindow)
            return (GameWindow) parentActivity;
        else
            return null;
    }

    // получить размер инвенторя
    public int getHeightInventory() {
        if (parentActivity instanceof GameWindow)
            return ((GameWindow) parentActivity).getHeightScrollInventory();
        else
            return 0;
    }

    /**
     * Получить все стены которые находятся от переданного в переданной области
     * @param objCheck объект для проверки
     * @param radius область для проверки
     * @return объекты из области
     */
    public ArrayList<Objects> getVisibleObjWall (Objects objCheck, float radius) {
        ArrayList<Objects> visibleObjectsWall = new ArrayList<Objects>();
        for (Objects obj : objWall) {
            if (Geometric_Calculate.destination(objCheck, obj) < radius * radius)
                visibleObjectsWall.add(obj);
        }
        return visibleObjectsWall;
    }

    /**
     * Получить всех врагов которые находятся от переданного в переданной области
     * @param objCheck объект для проверки
     * @param radius область для проверки
     * @return объекты из области
     */
    public ArrayList<Objects> getVisibleObjEnemy (Objects objCheck, float radius) {
        ArrayList<Objects> visibleObjectsEnemy = new ArrayList<Objects>();
        for (Objects obj : objEnemy) {
            if (Geometric_Calculate.destination(objCheck, obj) < radius * radius)
                visibleObjectsEnemy.add(obj);
        }
        return visibleObjectsEnemy;
    }

    // получение видимых зданий
    public ArrayList<Objects> getVisibleObjBuilding (Objects objCheck, float radius) {
        ArrayList<Objects> visibleObjectsBuilding = new ArrayList<Objects>();
        for (Objects obj : objBuilding) {
            if (Geometric_Calculate.destination(objCheck, obj) < radius * radius)
                //if (objCheck instanceof Enemy && Geometric_Calculate)
                visibleObjectsBuilding.add(obj);
        }
        return visibleObjectsBuilding;
    }

    // получить признак начала стрельбы
    public boolean isFire() {
        if (fireBulletTouch || fireBulletJoysitck)
            return true;
        else
            return false;
    }

    public boolean getfMenu() {
        return fMenu;
    }

    // получить указатель паузы игры
    public boolean getPause() { return pause; }

    // получение максимальной комнаты
    public int getMaxRoom() { return maxRoom; }

    // получение максимального кол-ва убитых врагов
    public int getMaxDeadEnemy() { return maxDeadEnemy; }

    // получить высоту квадрата
    public float getQwadHeight() { return  qwadHeight; }

    // получиьт ширину квадрата
    public float getQwadWidth() { return qwadWidth; }

    // получить всех игроков
    public ArrayList<Objects> getObjPlayer() { return objPlayer; }

    // получение всех стен
    public ArrayList<Objects> getObjWall() { return objWall; }

    // получить все здания
    public ArrayList<Objects> getObjBuilding() { return objBuilding; }

    // полученипе всех боперипасов
    public ArrayList<Objects> getObjResource() { return objResource; }

    // получение всех пуль
    public ArrayList<Objects> getObjBullet() {return objBullet; }

    // получение всех порталов
    public ArrayList<Objects> getObjPortal() { return objPortal; }

    // получение всех противников
    public ArrayList<Objects> getObjEnemy() { return objEnemy; }

    // получение всех объектов
    public ArrayList<ArrayList<Objects>> getAllObj() { return allObj; }

    // получить объект для рисования
    public SurfaceHolder getSurfaceHolder() { return surfaceHolder; }

    // получить х левой верхней точки экрана на карте
    public float getVisXUp() { return visXUp; }

    // получить у левой верхней точки экрана на карте
    public float getVisYUp() { return visYUp;}

    // максимальное кол-во квадратов по горизонтали экрана
    public int getMaxX() { return maxX; }

    // получить максимальное кол-во квадратов по вертикали экрана
    public int getMaxY() { return maxY; }

    // получить максимальное кол-во квадратов по вертикали карты
    public int getHeightMap() { return heightMap; }

    // получить максимальное кол-во квадратов по горизонтали карты
    public int getWidthMap() { return widthMap; }

    /**
     * Получить коробку
     * @return коробка
     */
    public Objects getBox() { return box; }

    /**
     * Получить игрока
     * @return игрок
     */
    public Objects getPlayer() { return player; }

    /**
     * Получить портал
     * @return портал
     */
    public Objects getPortal() { return portal; }
}