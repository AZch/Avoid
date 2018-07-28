package wordcreators.avoid2.AllSettingsWindow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import constants.requestConst;
import constants.requestString;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.DifficultyWindow;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SettingsWindow extends AppCompatActivity implements View.OnTouchListener {

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

    // анимация нажатия кнопок
    private Animation scaleAnimation;

    private ArrayList<Bitmap> allTextureGame = new ArrayList<>();

    /**
     * Параметры звука
     * 0 - общий звук
     * 1 - звук фоновой музыки
     * 2 - звук стрельбы
     */
    private ArrayList<Integer> soundParam;

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
    private static ArrayList<Integer> param = new ArrayList<>();

    /**
     * Набор параметров для свойств игры
     * 0 - способ стрельбы
     * 1 - способ обнавления состояния врага
     */
    public ArrayList<Integer> gameParam = new ArrayList<>();

    private static int zoom = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings_window);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        soundParam = getIntent().getIntegerArrayListExtra(requestString.SOUND);

        gameParam = getIntent().getIntegerArrayListExtra(requestString.GAME_PARAM);

        zoom = MainActivity.getZoom();

        // заданиме начальных параметров для сложности легко
        param = MainActivity.getParam();

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.settingGame);
        gameLayout.addView(gameView);
        gameLayout.setOnTouchListener(this);

        gameThread = new Thread(gameView);
        gameThread.start();
    }

    public void doneGame() {
        gameView.restartGame();
        try {
            Intent intent = new Intent(this, ErrorScreen.class);
            startActivity(intent);
        }
        catch (Exception e) {
            Log.d(TAG, "startButtonTouch: " + e);
        }
    }

    public static int getZoom() { return zoom; }

    public static ArrayList<Integer> getParam() { return  param; }

    /**
     * нажатие кнопки перехода к сложности игры
     * @param view кнопка
     */
    public void difficultyButton(View view) {
        view.startAnimation(scaleAnimation);
        Intent intent = new Intent(this, DifficultyWindow.class);

        startActivityForResult(intent, requestConst.REQUEST_PARAM);
    }

    /**
     * нажатие кнопки перехода к настройкам звука
     * @param view кнопка
     */
    public void soundButton(View view) {
        view.startAnimation(scaleAnimation);
        Intent intent = new Intent(this, SoundSetting.class);
        intent.putIntegerArrayListExtra(requestString.SOUND, soundParam);
        startActivityForResult(intent, requestConst.REQUEST_PARAM_SOUND);
    }

    /**
     * нажатие кнопки перехода к настройкам изображения
     * @param view кнопка
     */
    public void imageButton(View view) {
        view.startAnimation(scaleAnimation);
        Intent intent = new Intent(this, ImageWindow.class);

        startActivityForResult(intent, requestConst.REQUEST_ZOOM);
    }

    public void controlButton(View view) {
        view.startAnimation(scaleAnimation);
        Intent intent = new Intent(this, GameSettingWindow.class);
        intent.putIntegerArrayListExtra(requestString.GAME_PARAM, gameParam);
        startActivityForResult(intent, requestConst.REQUEST_PARAM_GAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestConst.REQUEST_PARAM:
                    param = data.getIntegerArrayListExtra(requestString.PARAM);
                    break;
                case requestConst.REQUEST_ZOOM:
                    int zoomNew = data.getIntExtra(requestString.ZOOM, zoom);
                    if (zoomNew != zoom) {
                        zoom = zoomNew;
                        //cont
                    }
                    allTextureGame = data.getParcelableArrayListExtra(requestString.IMAGE);
                    break;
                case requestConst.REQUEST_PARAM_SOUND:
                    soundParam = data.getIntegerArrayListExtra(requestString.SOUND);
                    break;
                case requestConst.REQUEST_PARAM_GAME:
                    gameParam = data.getIntegerArrayListExtra(requestString.GAME_PARAM);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putIntegerArrayListExtra(requestString.PARAM, param);
        data.putExtra(requestString.ZOOM, zoom);
        data.putExtra(requestString.IMAGE, allTextureGame);
        data.putIntegerArrayListExtra(requestString.SOUND, soundParam);
        data.putIntegerArrayListExtra(requestString.GAME_PARAM, gameParam);
        setResult(RESULT_OK, data);
        finish();

        gameView.stopThread();
  //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        gameView.resumeGame();
        /*gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);
        gameLayout.addView(gameView);

        gameThread = new Thread(gameView);
        gameThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
//      gameView.pauseGame();
        /*gameView.stopThread();
        gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.settingGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}