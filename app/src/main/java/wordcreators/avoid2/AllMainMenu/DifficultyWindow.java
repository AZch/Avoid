package wordcreators.avoid2.AllMainMenu;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

import constants.paramConst;
import constants.requestConst;
import constants.requestString;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllSettingsWindow.SettingsWindow;
import wordcreators.avoid2.AllSettingsWindow.yourSettingWindow;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class DifficultyWindow extends AppCompatActivity implements View.OnTouchListener {

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

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

    // указатель набора параметров (0 - легко, 1 - нормально и т.д)
    private int countParam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_difficulty_window);

        // заданиме начальных параметров для сложности легко
        param = SettingsWindow.getParam();

        countParam = MainActivity.getCountParam();

        if (countParam == 0) {
            RadioButton easy = (RadioButton) findViewById(R.id.easyRadioButton);
            easy.setChecked(true);
        } else if (countParam == 1) {
            RadioButton normal = (RadioButton) findViewById(R.id.normalRadioButton);
            normal.setChecked(true);
        } else if (countParam == 2) {
            RadioButton hard = (RadioButton) findViewById(R.id.hardRadioButton);
            hard.setChecked(true);
        } else if (countParam == 3) {
            RadioButton random = (RadioButton) findViewById(R.id.randomRadioButton);
            random.setChecked(true);
        } else if (countParam == 4) {
            RadioButton yourSetting = (RadioButton) findViewById(R.id.yourSettingRadioButton);
            yourSetting.setChecked(true);
        }

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.difficultyGame);
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

    // получение параметров игры
    public static ArrayList<Integer> getParam() { return param; }

    /**
     * Задание параметров
     * @param lifePlayer начальное кол-во здоровья игрока
     * @param bulletPlayer начальное кол-во патронов игрока
     * @param maxMedkit максимальное возможное кол-во аптечек на уравне
     * @param maxAmmo максимальное возможное кол-во боеприпасов на уравне
     * @param startEnemy начальное кол-во врагов
     * @param enemyLife начальное кол-во жизней врагов
     * @param addNextEnemy кол-во врагов, которые добавляются на следующем уровне
     * @param speed скорость игрока
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

    // заданиме начальных параметров для сложности легко
    public void easyButton(View view) {
        param = setParam(paramConst.easyLifePlayer, paramConst.easyBulletCount, paramConst.easyMaxMedkit,
                paramConst.easyMaxAmmo, paramConst.easyStartEnemy, paramConst.easyEnemyLife, paramConst.easyAddNextEnemy, paramConst.easySpeed);
        countParam = 0;
    }

    // задание параметров для сложности нормально
    public void normalButton(View view) {
        param = setParam(paramConst.normalLifePlayer, paramConst.normalBulletCount, paramConst.normalMaxMedkit,
                paramConst.normalMaxAmmo, paramConst.normalStartEnemy, paramConst.normalEnemyLife, paramConst.normalAddNextEnemy, paramConst.normalSpeed);
        countParam = 1;
    }

    // задание параметров для сложности тяжело
    public void hardButton(View view) {
        param = setParam(paramConst.hardLifePlayer, paramConst.hardBulletCount, paramConst.hardMaxMedkit,
                paramConst.hardMaxAmmo, paramConst.hardStartEnemy, paramConst.hardEnemyLife, paramConst.hardAddNextEnemy, paramConst.hardSpeed);
        countParam = 2;
    }

    // задание параметров для сложности произвольно
    public void randomButton(View view) {
        java.util.Random random = new java.util.Random();
        param = setParam(random.nextInt(paramConst.randomDiapLifePlayer) + paramConst.randomMinLifePlayer,
                random.nextInt(paramConst.randomDiapBulletCount) + paramConst.randomMinBulletCount,
                random.nextInt(paramConst.randomDiapMaxMedkit) + paramConst.randomMinMaxMedkit,
                random.nextInt(paramConst.randomDiapMaxAmmo) + paramConst.randomMinMaxAmmo,
                random.nextInt(paramConst.randomDiapStartEnemy) + paramConst.randomMinStartEnemy,
                random.nextInt(paramConst.randomDiapEnemyLife) + paramConst.randomMinEnemyLife,
                random.nextInt(paramConst.randomDiapAddNextEnemy) + paramConst.randomMinAddNextEnemy,
                random.nextInt(paramConst.randomDiapSpeed) + paramConst.randomMinSpeed);
        countParam = 3;
    }

    /**
     * нажатие кнопки перехода к своим настройкам одиночной игры
     * @param view кнопка
     */
    public void yourSettingButton(View view) {
        Intent intent = new Intent(this, yourSettingWindow.class);

        startActivityForResult(intent, requestConst.REQUEST_PARAM);

        countParam = 4;
    }

    public void okeyClick(View view) {
        Intent data = new Intent();
        data.putIntegerArrayListExtra(requestString.PARAM, param);
        setResult(RESULT_OK, data);
        MainActivity.setCountParam(countParam);
        finish();
        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    public void closeClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestConst.REQUEST_PARAM:
                    param = data.getIntegerArrayListExtra(requestString.PARAM);
                    break;
            }
        }
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);
        gameLayout.addView(gameView);

        gameThread = new Thread(gameView);
        gameThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopThread();
        gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.difficultyGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}