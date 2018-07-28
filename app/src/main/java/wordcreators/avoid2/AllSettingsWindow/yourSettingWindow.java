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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import constants.requestString;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.DifficultyWindow;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class yourSettingWindow extends AppCompatActivity implements View.OnTouchListener {

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_your_setting_window);

        setListnersSB((SeekBar) findViewById(R.id.lifePlayerSeekBar), (TextView) findViewById(R.id.lifePlayerText), getString(R.string.lifePlayerCountText), 0); // жизни игрока
        setListnersSB((SeekBar) findViewById(R.id.bulletPlayerSeekBar), (TextView) findViewById(R.id.bulletPlayerText), getString(R.string.bulletPlayerCountText), 1); // кол-во патрон игрока
        setListnersSB((SeekBar) findViewById(R.id.numberOfPossibleMedkitrSeekBar), (TextView) findViewById(R.id.numberOfPossibleMedkitText), getString(R.string.numberOfPossibleMedkitText), 2); // макс. возм. кол-во аптечек
        setListnersSB((SeekBar) findViewById(R.id.numberOfPossibleAmmoSeekBar), (TextView) findViewById(R.id.numberOfPossibleAmmoText), getString(R.string.numberOfPossibleAmmoText), 3); // макс. возм. кол-во патрон
        setListnersSB((SeekBar) findViewById(R.id.startEnemyCountSeekBar), (TextView) findViewById(R.id.startEnemyCountText), getString(R.string.startEnemyCount), 4); // кол-во начальных врагов
        setListnersSB((SeekBar) findViewById(R.id.startEnemyLifeCountSeekBar), (TextView) findViewById(R.id.startEnemyLifeCountText), getString(R.string.startEnemyLifeCount), 5); // кол-во жизней врагов
        setListnersSB((SeekBar) findViewById(R.id.AddEnemyOnNextLvlSeekBar), (TextView) findViewById(R.id.AddEnemyOnNextLvlText), getString(R.string.AddEnemyOnNextLvl), 6); // кол-во врогов на след. уровне
        setListnersSB((SeekBar) findViewById(R.id.speedPlayerSeekBar), (TextView) findViewById(R.id.speedPlayerText), getString(R.string.speedPlayer), 7);

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.yourSettingGame);
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

    public void setListnersSB(SeekBar sb, final TextView tv, final String textRes, int index) {
        if (DifficultyWindow.getParam().get(index) == 1) {
            sb.setProgress(0);
            tv.setText(textRes + " " + 1);
        }
        else {
            sb.setProgress(DifficultyWindow.getParam().get(index));
            tv.setText(textRes + " " + sb.getProgress());
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    tv.setText(textRes + " " + 1);
                else
                    tv.setText(textRes + " " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private ArrayList<Integer> setParam(SeekBar sbLifePlayer, SeekBar sbBulletPlayer, SeekBar sbNumberOfPossibleMedkitr,
                                        SeekBar sbNumberOfPossibleAmmo, SeekBar sbStartEnemyCount, SeekBar sbStartEnemyLifeCount,
                                        SeekBar sbAddEnemyOnNextLvl, SeekBar sbSpeedPlayer) {
        ArrayList<Integer> param = new ArrayList<>();
        if (sbLifePlayer.getProgress() == 0)
            param.add(1);
        else
            param.add(sbLifePlayer.getProgress());

        if (sbBulletPlayer.getProgress() == 0)
            param.add(1);
        else
            param.add(sbBulletPlayer.getProgress());

        if (sbNumberOfPossibleMedkitr.getProgress() == 0)
            param.add(1);
        else
            param.add(sbNumberOfPossibleMedkitr.getProgress());

        if (sbNumberOfPossibleAmmo.getProgress() == 0)
            param.add(1);
        else
            param.add(sbNumberOfPossibleAmmo.getProgress());

        if (sbStartEnemyCount.getProgress() == 0)
            param.add(1);
        else
            param.add(sbStartEnemyCount.getProgress());

        if (sbStartEnemyLifeCount.getProgress() == 0)
            param.add(1);
        else
            param.add(sbStartEnemyLifeCount.getProgress());

        if (sbAddEnemyOnNextLvl.getProgress() == 0)
            param.add(1);
        else
            param.add(sbAddEnemyOnNextLvl.getProgress());

        if (sbSpeedPlayer.getProgress() == 0)
            param.add(1);
        else
            param.add(sbSpeedPlayer.getProgress());
        return param;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        ArrayList<Integer> param = setParam((SeekBar) findViewById(R.id.lifePlayerSeekBar), (SeekBar) findViewById(R.id.bulletPlayerSeekBar),
                (SeekBar) findViewById(R.id.numberOfPossibleMedkitrSeekBar), (SeekBar) findViewById(R.id.numberOfPossibleAmmoSeekBar),
                (SeekBar) findViewById(R.id.startEnemyCountSeekBar), (SeekBar) findViewById(R.id.startEnemyLifeCountSeekBar),
                (SeekBar) findViewById(R.id.AddEnemyOnNextLvlSeekBar), (SeekBar) findViewById(R.id.speedPlayerSeekBar));
        data.putIntegerArrayListExtra(requestString.PARAM, param);
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
            case R.id.yourSettingGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}