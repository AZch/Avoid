package wordcreators.avoid2.AllSettingsWindow;

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

import constants.requestString;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class GameSettingWindow extends AppCompatActivity implements View.OnTouchListener {

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

    private int controlFire = 0;

    /**
     * Параметры игры
     * 0 - стрельба
     * 1 - обнавление врагов
     */
    private ArrayList<Integer> gameParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_setting_window);

        gameParams = getIntent().getIntegerArrayListExtra(requestString.GAME_PARAM);

        if (gameParams.get(0) == 0) {
            RadioButton standFire = (RadioButton) findViewById(R.id.standartFireRadioButton);
            standFire.setChecked(true);
        } else if (gameParams.get(0) == 1) {
            RadioButton touchFire = (RadioButton) findViewById(R.id.touchFireRadioButton);
            touchFire.setChecked(true);
        }

        if (gameParams.get(1) == 0) {
            RadioButton updateAll = (RadioButton) findViewById(R.id.updateAllEnemyRadioButton);
            updateAll.setChecked(true);
        } else if (gameParams.get(1) == 1) {
            RadioButton updateOnScreen = (RadioButton) findViewById(R.id.updateOnScreenEnemyRadioButton);
            updateOnScreen.setChecked(true);
        }

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.gameSettingGame);
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

    public void standartTextureRadioButton(View view) {
        gameParams.set(0, 0);
    }

    public void touchFireRadioButton(View view) {
        gameParams.set(0, 1);
    }

    public void updateAllEnemyRadioButton(View view) { gameParams.set(1, 0); }

    public void updateOnScreenEnemyRadioButton(View view) { gameParams.set(1, 1); }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putIntegerArrayListExtra(requestString.GAME_PARAM, gameParams);
        setResult(RESULT_OK, data);
        finish();
        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
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
            case R.id.gameSettingGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}