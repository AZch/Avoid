package wordcreators.avoid2.AllSettingsWindow;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import constants.requestString;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class SoundSetting extends AppCompatActivity implements View.OnTouchListener {

    // указатель на игру в меню
    LinearLayout gameLayout;

    // класс с игрой
    GameView gameView;

    // игровой поток
    Thread gameThread;

    // параметры звука
    private ArrayList<Integer> soundParam;

    // все seekBar
    private ArrayList<SeekBar> allSeekBar = new ArrayList<>();

    // разница между основным SeekBar (soundAllSeekBar) и всеми остальными
    private ArrayList<Integer> diffAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sound_setting);

        soundParam = getIntent().getIntegerArrayListExtra(requestString.SOUND);

        SeekBar soundAllSB = (SeekBar) findViewById(R.id.soundAllSeekBar);
        allSeekBar.add(soundAllSB);
        soundAllSB.setProgress(soundParam.get(0));
        final TextView soundAllT = (TextView) findViewById(R.id.soundAllTextView);
        int progress = soundAllSB.getProgress();
        soundAllT.setText(getString(R.string.soundAll) + " " + progress);
        diffAll.add(soundAllSB.getProgress() - progress);

        soundAllSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundAllT.setText(getString(R.string.soundFireText) + " " + progress);
                for (SeekBar seekBarCheck : allSeekBar)
                    if (seekBarCheck.getProgress() > progress) {
                        seekBarCheck.setProgress(progress);
                    }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setSoundSeekBar((SeekBar) findViewById(R.id.soundMusicSeekBar), (TextView) findViewById(R.id.soundMusicTextView), getString(R.string.soundMusic), 1);
        setSoundSeekBar((SeekBar) findViewById(R.id.soundFireSeekBar), (TextView) findViewById(R.id.soundFireTextView), getString(R.string.soundFireText), 2);

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.soundSettingGame);
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

    private void setSoundSeekBar(SeekBar seekBar, final TextView textView, final String text, final int indexParam) {
        seekBar.setProgress(soundParam.get(indexParam));
        allSeekBar.add(seekBar);
        int progress = seekBar.getProgress();
        textView.setText(text + " " + progress);
        diffAll.add(allSeekBar.get(0).getProgress() - seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(text + " " + progress);
                if (progress > allSeekBar.get(0).getProgress()) {
                    allSeekBar.get(0).setProgress(progress);
                }
                diffAll.set(indexParam, allSeekBar.get(0).getProgress() - progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private ArrayList<Integer> setSoundParam() {
        ArrayList<Integer> soundParam = new ArrayList<>();
        for (SeekBar seekBar : allSeekBar)
            soundParam.add(seekBar.getProgress());
        return soundParam;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        soundParam = setSoundParam();
        data.putIntegerArrayListExtra(requestString.SOUND, soundParam);
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
            case R.id.soundSettingGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}