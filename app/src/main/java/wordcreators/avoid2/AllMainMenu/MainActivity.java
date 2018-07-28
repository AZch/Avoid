package wordcreators.avoid2.AllMainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import constants.requestConst;
import constants.requestString;
import constants.paramConst;
import constants.paramSaveLoad;
import maker.loadBitmap;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllGameFile.GameWindow;
import wordcreators.avoid2.AllSettingsWindow.SettingsWindow;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    // настройки
    SharedPreferences settings;

    // разбиение экрана на квадраты
    private static int maxLongQwad = 200, maxShortQwad = 112;

    /**
     * Все возможные состояния игрока
     * Первый массив - жизни
     * Второй массив - патроны
     * Третий массив - скорости
     */
    private static Bitmap[][][] playerBitmap = new Bitmap[4][4][7];

    /**
     * Все возможные состояния противника
     */
    private static Bitmap[][][] enemyBitmap = new Bitmap[2][5][5];

    /**
     * Состояния смерти противника
     */
    private static Bitmap[] enemyDeadBitmap = new Bitmap[7];

    /**
     * все текстуры игры
     * 0 - текстура игрока
     * 1 - текстура противника
     */
    private static ArrayList<Bitmap> allTextureGame = new ArrayList<>();

    public static Bitmap playerImage = null, enemyImage = null, wallImage = null, bulletImage = null,
            medkitImage = null, ammoImage = null, portalImage = null, enemyDeadImage = null, enemyTrigImage = null;

    // 0 - стандартный текстур пак и т.д.
    private static int countUseTexture = 0;

    // набор параметров для игры 0 - легко, 1 - нормально и т.д.
    private static int countParam = 0;

    // максимальное кол-во параметров игры
    private static final int maxCountParam = 8;

    // увеличение карты
    private static int zoom = 12;

    private static int playerSpriteH, playerSpriteW, qwadHeight, qwadWidth;

    /**
     * Параметры звука
     * 0 - общая громкость
     * 1 - громкость фоновой музыки
     * 2 - громкость стрельбы
     */
    private static ArrayList<Integer> soundParam = new ArrayList<>();

    public static Bitmap bitmapBack;


    /**
     * Набор параметров для игры
     * [0] - начальное кол-во здоровья игрока
     * [1] - начальное кол-во патронов игрока
     * [2] - максимальное возможное кол-во аптечек на уравне
     * [3] - максимальное возможное кол-во боеприпасов на уравне
     * [4] - начальное кол-во врагов
     * [5] - начальное кол-во жизней врагов
     * [6] - кол-во врагов, которые добавляются на следующем уровне
     * [7] - скорость игрока
     */
    private static ArrayList<Integer> param = new ArrayList<>();

    /**
     * Набор парамтеров для игры
     * 0 - способ стрельбы
     * 1 - способ обновления состояния врага
     */
    private static ArrayList<Integer> gameParam = new ArrayList<>();

    // максимальная комната и кол-во убитых врагов на сложности легко
    private int easyMaxRoom = 1;
    private int easyDeadEnemy = 0;

    // максимальная комната и кол-во убитых врагов на сложности нормально
    private int normalMaxRoom = 1;
    private int normalDeadEnemy = 0;

    // максимальная комната и кол-во убитых врагов на сложности сложно
    private int hardMaxRoom = 1;
    private int hardDeadEnemy = 0;

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

    // анимация вращения кнопок
    Animation animation;

    Animation one_rotate;

    Animation scale;

    // true когда анимация завершилась
    private boolean endAnimation = false;

    // название игры
    ArrayList<TextView> avoidText = new ArrayList<>();

    // цвета для названия игры
    ArrayList<Integer> avoidColor = new ArrayList<>();

    // счётчик цветов
    private int countColor = 0;

    // время не сменяемости цвета
    private int timeToChange = paramConst.MAX_FPS / 2;

    // счётчик текущей сменяемости
    private int countChangeColor = 0;

    // буква для смены цвета
    private int wordCount = 1;

    // для рандомизации чего либо
    Random random = new Random();

    // указатель потребности менять цвет заголовка
    private boolean stopChangeColor = false;

    // начальные координаты буквы
    private ArrayList<Float> coordWord = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        /*if (size.x > size.y) {
            maxLongQwad = size.x / 10;
            maxShortQwad = size.y / 10;
        } else {
            maxLongQwad = size.y / 10;
            maxShortQwad = size.x / 10;
        }

        zoom = size.x * size.y / 172800;*/
        zoom = 7;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        //scale.setDuration(scale.getDuration() / 2);

        avoidText.add((TextView) findViewById(R.id.avoidText1));
        avoidText.add((TextView) findViewById(R.id.avoidText2));
        avoidText.add((TextView) findViewById(R.id.avoidText3));
        avoidText.add((TextView) findViewById(R.id.avoidText4));
        avoidText.add((TextView) findViewById(R.id.avoidText5));

        wordCount = avoidText.size() - 1;



        //startAnimation();

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), maxLongQwad, maxShortQwad, true, this);

        gameLayout = (LinearLayout) findViewById(R.id.menuGame);
        gameLayout.addView(gameView);
        gameLayout.setOnTouchListener(this);

        bitmapBack = BitmapFactory.decodeResource(getResources(), R.drawable.backkkk);

        settings = getSharedPreferences(paramConst.PREFS_FILE, MODE_PRIVATE);

        setParams();

        setPlayerBitmap();
        setEnemyBitmap();
        setEnemyDeadBitmap();
        setColors();

        gameThread = new Thread(gameView);
        gameThread.start();
    }

    private void setColors() {
        int color = Color.argb(255, 192, 52, 49); // красно-оранжевый
        avoidColor.add(color);
        color = Color.argb(255, 75, 30, 121); // фиолетовый
        avoidColor.add(color);
        color = Color.argb(255, 0, 140, 141); // сине-зелёный
        avoidColor.add(color);
        color = Color.argb(255, 204, 191, 0); // жёлтый
        avoidColor.add(color);
    }

    public void changeColor() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stopChangeColor)
                    return;
                if (countChangeColor == 0) {
                    countChangeColor = timeToChange;

                    for (int i = wordCount ; i > 0; i--) {
                        if (stopChangeColor)
                            return;
                        avoidText.get(i).setTextColor(avoidText.get(i - 1).getCurrentTextColor());
                    }
                    if (countColor == avoidColor.size())
                        countColor = 0;
                    avoidText.get(0).setTextColor(avoidColor.get(countColor++));
                    /*for (int i = 0; i < wordCount + 1; i++) {
                        avoidText.get(0).setTextColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                        for (int j = i; j > 0; j--)
                            avoidText.get(j).setTextColor(avoidText.get(j - 1).getCurrentTextColor());
                    }*/

                } else {
                    countChangeColor--;
                }
            }
        });
    }

    public void startAnimation() {
        stopChangeColor = false;
        findViewById(R.id.startButton).startAnimation(animation);
        findViewById(R.id.settingsButton).startAnimation(animation);
        findViewById(R.id.recordButton).startAnimation(animation);
        findViewById(R.id.exitButton).startAnimation(animation);
    }

    public void clearAnimation() {
        stopChangeColor = true;
        findViewById(R.id.startButton).clearAnimation();
        findViewById(R.id.settingsButton).clearAnimation();
        findViewById(R.id.recordButton).clearAnimation();
        findViewById(R.id.exitButton).clearAnimation();
    }

    public void doneGame() {
        gameView.restartGame();
        try {
            //gameView.killAllEnemy();
        }
        catch (Exception e) {
            Log.d(TAG, "startButtonTouch: " + e);
        }
    }

    // задание спрайтов игрока
    private void setPlayerBitmap() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        playerBitmap = loadBitmap.loadPlayer(zoom, zoom, size.x / maxShortQwad, size.x / maxShortQwad, this.getApplicationContext());
    }

    // задание спрайтов противника
    private void setEnemyBitmap() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        enemyBitmap = loadBitmap.loadEnemy(zoom,  zoom, size.x / maxShortQwad, size.x / maxShortQwad, this.getApplicationContext());
    }

    // загрузка спрайтов противника после смерти
    private void setEnemyDeadBitmap() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        enemyDeadBitmap = loadBitmap.loadDeadEnemy(zoom, zoom, size.x / maxShortQwad, size.y / maxLongQwad, this.getApplicationContext());
    }

    // загрузить все параметры из ресурсов
    private void setParams() {
        loadParam();

        loadSoundParam();

        loadGameParam();

        countParam = settings.getInt(paramSaveLoad.COUNT_PARAM, 0);

        zoom = settings.getInt(paramSaveLoad.ZOOM, zoom);

        easyMaxRoom = settings.getInt(paramSaveLoad.EASY_MAX_ROOM, easyMaxRoom);
        easyDeadEnemy = settings.getInt(paramSaveLoad.EASY_DEAD_ENEMY, easyDeadEnemy);

        normalMaxRoom = settings.getInt(paramSaveLoad.NORMAL_MAX_ROOM, normalMaxRoom);
        normalDeadEnemy = settings.getInt(paramSaveLoad.NORMAL_DEAD_ENEMY, normalDeadEnemy);

        hardMaxRoom = settings.getInt(paramSaveLoad.HARD_MAX_ROOM, hardMaxRoom);
        hardDeadEnemy = settings.getInt(paramSaveLoad.HARD_DEAD_ENEMY, hardDeadEnemy);
    }

    // загрузить параметры игры из ресурсов
    private void loadParam() {
        param.add(settings.getInt(paramSaveLoad.LIFE_PLAYER, paramConst.easyLifePlayer));
        param.add(settings.getInt(paramSaveLoad.BULLET_PLAYER, paramConst.easyBulletCount));
        param.add(settings.getInt(paramSaveLoad.MAX_MEDKIT, paramConst.easyMaxMedkit));
        param.add(settings.getInt(paramSaveLoad.MAX_AMMO, paramConst.easyMaxAmmo));
        param.add(settings.getInt(paramSaveLoad.START_ENEMY, paramConst.easyStartEnemy));
        param.add(settings.getInt(paramSaveLoad.ENEMY_LIFE, paramConst.easyEnemyLife));
        param.add(settings.getInt(paramSaveLoad.ADD_NEXT_ENEMY, paramConst.easyAddNextEnemy));
        param.add(settings.getInt(paramSaveLoad.SPEED_PLAYER, paramConst.easySpeed));
    }

    // загрузить параметры звука
    private void loadSoundParam() {
        soundParam.add(settings.getInt(paramSaveLoad.SOUND_ALL, paramConst.soundAll));
        soundParam.add(settings.getInt(paramSaveLoad.SOUND_MUSIC, paramConst.soundMusic));
        soundParam.add(settings.getInt(paramSaveLoad.SOUND_FIRE, paramConst.soundFire));
    }

    // загрузить параметры работы игры
    private void loadGameParam() {
        gameParam.add(settings.getInt(paramSaveLoad.CONTROL_FIRE, paramConst.countrolFire));
        gameParam.add(settings.getInt(paramSaveLoad.UPDATE_ENEMY, paramConst.updateEnemy));
    }

    public static Bitmap getBitmapPlayer(int i, int j, int k) {
        return playerBitmap[i][j][k];
    }

    public static Bitmap getBitmapEnemy(int i, int j, int k) {
        //Log.d(TAG, "i: " + i + ", j: " + j + ", k: " + k);
        return enemyBitmap[i][j][k];
    }

    public static Bitmap getBitmapEnemyDead(int i) {
        return enemyDeadBitmap[i];
    }

    public static float getSoundMusicGame() { return (float) soundParam.get(1) / 100; }

    public static float getSoundFire() { return  (float) soundParam.get(2) / 100; }

    public static int getControlFire() { return gameParam.get(0); }

    public static int getUpdateAllEnemy() { return gameParam.get(1); }

    public static int getCountParam() { return countParam; }

    public static void setCountParam(int indexParam) { countParam = indexParam; }

    public static int getCountUseTexture() { return countUseTexture; }

    public static void setStandartTexturePack() { countUseTexture = 0; }

    public static void setOtherTexturePack(int indexTexturePack) { countUseTexture = indexTexturePack; }

    public static int getMaxLongQwad() { return maxLongQwad; }

    public static int getMaxShortQwad() { return maxShortQwad; }

    public static ArrayList<Integer> getParam() { return param; }

    public static int getZoom() { return zoom; }

    public static int getMaxCountParam() { return maxCountParam; }

    public static Bitmap getPlayerTexture() { return allTextureGame.size() < 1 ? null : allTextureGame.get(0); }

    public static Bitmap getEnemyTexture() { return allTextureGame.size() < 2 ? null : allTextureGame.get(1); }

    public static void clearYourTexture() { allTextureGame.clear(); }

    /**
     * Задание параметров
     * @param lifePlayer начальное кол-во здоровья игрока
     * @param bulletPlayer начальное кол-во патронов игрока
     * @param maxMedkit максимальное возможное кол-во аптечек на уравне
     * @param maxAmmo максимальное возможное кол-во боеприпасов на уравне
     * @param startEnemy начальное кол-во врагов
     * @param enemyLife начальное кол-во жизней врагов
     * @param addNextEnemy кол-во врагов, которые добавляются на следующем уровне
     * @return массив с полученными параметроми в той последовательности в которой они были отправлены сюда
     */
    private ArrayList<Integer> setParam(int lifePlayer, int bulletPlayer, int maxMedkit, int maxAmmo, int startEnemy, int enemyLife, int addNextEnemy, int speed) {
        ArrayList<Integer> param = new ArrayList<>();
        param.add(lifePlayer); // начальное кол-во здоровья игрока
        param.add(bulletPlayer); // начальное кол-во патронов игрока
        param.add(maxMedkit); // максимальное возможное кол-во аптечек на уравне
        param.add(maxAmmo); //  максимальное возможное кол-во боеприпасов на уравне
        param.add(startEnemy); // начальное кол-во врагов
        param.add(enemyLife); // начальное кол-во жизней врагов
        param.add(addNextEnemy); // кол-во врагов, которые добавляются на следующем уровне
        param.add(speed);
        return  param;
    }

    /**
     * Задание параметров звука
     * @param soundFire звук выстрела
     * @return параметры звука
     */
    private ArrayList<Integer> setSoundParam(int soundAll, int soundMusic, int soundFire) {
        ArrayList<Integer> soundParam = new ArrayList<>();
        soundParam.add(soundAll);
        soundParam.add(soundMusic);
        soundParam.add(soundFire);
        return soundParam;
    }

    /**
     * Задание параметров игры
     * @param controlFire способ стрельбы
     * @param updateAllEnemy обновление врагов на карте
     * @return парамтеры игры
     */
    private ArrayList<Integer> setGameParam(int controlFire, int updateAllEnemy) {
        ArrayList<Integer> gameParam = new ArrayList<>();
        gameParam.add(controlFire);
        gameParam.add(updateAllEnemy);
        return gameParam;
    }

    /**
     * нажатие кнопки запуска игры
     * @param view кнопка
     */
    public void startButtonTouch(View view) {
        //clearAnimation();
        view.startAnimation(scale);
        //while (!endAnimation);
        //clearAnimation();
        try {
            Intent intent = new Intent(this, GameWindow.class);

            startActivityForResult(intent, requestConst.REQUEST_MAX_ROOM_KILL);
        }
        catch (Exception e) {
            Log.d(TAG, "startButtonTouch: " + e);
        }
    }

    /**
     * нажите кнопки открытия настроек
     * @param view кнопка
     */
    public void settingsButtonTouch(View view) {
        view.startAnimation(scale);
        //while (!endAnimation);
        //clearAnimation();
        //findViewById(R.id.settingsButton).startAnimation(scaleBack);

        Intent intent = new Intent(this, SettingsWindow.class);
        intent.putIntegerArrayListExtra(requestString.SOUND, soundParam);
        intent.putIntegerArrayListExtra(requestString.GAME_PARAM, gameParam);
        startActivityForResult(intent, requestConst.REQUEST_PARAM);
    }

    /**
     * нажите кнопки помощи
     * @param view кнопка
     */
    public void recordsButtonTouch(View view) {
        view.startAnimation(scale);
        //while (!endAnimation);
        //clearAnimation();
        Intent intent = new Intent(this, RecordWindow.class);

        intent.putExtra(paramSaveLoad.EASY_MAX_ROOM, easyMaxRoom);
        intent.putExtra(paramSaveLoad.EASY_DEAD_ENEMY, easyDeadEnemy);

        intent.putExtra(paramSaveLoad.NORMAL_MAX_ROOM, normalMaxRoom);
        intent.putExtra(paramSaveLoad.NORMAL_DEAD_ENEMY, normalDeadEnemy);

        intent.putExtra(paramSaveLoad.HARD_MAX_ROOM, hardMaxRoom);
        intent.putExtra(paramSaveLoad.HARD_DEAD_ENEMY, hardDeadEnemy);

        startActivity(intent);
    }

    /**
     * нажатие кнопки закрытия игры
     * @param view кнопка
     */
    public void exitButtonTouch(View view) {
        view.startAnimation(scale);
        //while (!endAnimation);
        //clearAnimation();
        saveParams();
        this.finish();

        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    private void saveParams() {
        SharedPreferences.Editor prefEditor = settings.edit();

        prefEditor.putInt(paramSaveLoad.LIFE_PLAYER, param.get(0));
        prefEditor.putInt(paramSaveLoad.BULLET_PLAYER, param.get(1));
        prefEditor.putInt(paramSaveLoad.MAX_MEDKIT, param.get(2));
        prefEditor.putInt(paramSaveLoad.MAX_AMMO, param.get(3));
        prefEditor.putInt(paramSaveLoad.START_ENEMY, param.get(4));
        prefEditor.putInt(paramSaveLoad.ENEMY_LIFE, param.get(5));
        prefEditor.putInt(paramSaveLoad.ADD_NEXT_ENEMY, param.get(6));
        prefEditor.putInt(paramSaveLoad.SPEED_PLAYER, param.get(7));

        prefEditor.putInt(paramSaveLoad.SOUND_ALL, soundParam.get(0));
        prefEditor.putInt(paramSaveLoad.SOUND_MUSIC, soundParam.get(1));
        prefEditor.putInt(paramSaveLoad.SOUND_FIRE, soundParam.get(2));

        prefEditor.putInt(paramSaveLoad.CONTROL_FIRE, gameParam.get(0));
        prefEditor.putInt(paramSaveLoad.UPDATE_ENEMY, gameParam.get(1));

        prefEditor.putInt(paramSaveLoad.COUNT_PARAM, countParam);

        prefEditor.putInt(paramSaveLoad.ZOOM, zoom);

        prefEditor.putInt(paramSaveLoad.EASY_MAX_ROOM, easyMaxRoom);
        prefEditor.putInt(paramSaveLoad.EASY_DEAD_ENEMY, easyDeadEnemy);

        prefEditor.putInt(paramSaveLoad.NORMAL_MAX_ROOM, normalMaxRoom);
        prefEditor.putInt(paramSaveLoad.NORMAL_DEAD_ENEMY, normalDeadEnemy);

        prefEditor.putInt(paramSaveLoad.HARD_MAX_ROOM, hardMaxRoom);
        prefEditor.putInt(paramSaveLoad.HARD_DEAD_ENEMY, hardDeadEnemy);

        prefEditor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestConst.REQUEST_PARAM:
                    param = data.getIntegerArrayListExtra(requestString.PARAM);
                    int zoomThis = data.getIntExtra(requestString.ZOOM, zoom);
                    if (zoomThis != zoom) {
                        zoom = zoomThis;
                        setPlayerBitmap();
                        setEnemyBitmap();
                        setEnemyDeadBitmap();
                    }
                    soundParam = data.getIntegerArrayListExtra(requestString.SOUND);

                    gameParam = data.getIntegerArrayListExtra(requestString.GAME_PARAM);
                    allTextureGame = data.getParcelableArrayListExtra(requestString.IMAGE);

                    saveParams();
                    setParams();
                    break;
                case requestConst.REQUEST_MAX_ROOM_KILL:
                    if (countParam == 0) {
                        if (easyMaxRoom < data.getIntExtra(requestString.MAX_ROOM, 1))
                            easyMaxRoom = data.getIntExtra(requestString.MAX_ROOM, 1);
                        if (easyDeadEnemy < data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0))
                            easyDeadEnemy = data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0);
                    } else if (countParam == 1) {
                        if (normalMaxRoom < data.getIntExtra(requestString.MAX_ROOM, 1))
                            normalMaxRoom = data.getIntExtra(requestString.MAX_ROOM, 1);
                        if (normalDeadEnemy < data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0))
                            normalDeadEnemy = data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0);
                    } else if (countParam == 2) {
                        if (hardMaxRoom < data.getIntExtra(requestString.MAX_ROOM, 1))
                            hardMaxRoom = data.getIntExtra(requestString.MAX_ROOM, 1);
                        if (hardDeadEnemy < data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0))
                            hardDeadEnemy = data.getIntExtra(requestString.MAX_DEAD_ENEMY, 0);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startAnimation();
        gameView.resumeGame();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //clearAnimation();
        try {
            gameView.pauseGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /*case R.id.avoidText1:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    coordWord.add(v.getX());
                    coordWord.add(v.getY());
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.setX(event.getX());
                    v.setY(event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setX(coordWord.get(0));
                    v.setY(coordWord.get(1));
                }
                break;*/
            case R.id.menuGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }

    public void avoidText1(View view) {
        avoidText.get(0).setAnimation(one_rotate);
    }

    public void avoidText2(View view) {
        avoidText.get(1).setAnimation(one_rotate);
    }

    public void avoidText3(View view) {
        avoidText.get(2).setAnimation(one_rotate);
    }

    public void avoidText4(View view) {
        avoidText.get(3).setAnimation(one_rotate);
    }

    public void avoidText5(View view) {
        avoidText.get(4).setAnimation(one_rotate);
    }
}