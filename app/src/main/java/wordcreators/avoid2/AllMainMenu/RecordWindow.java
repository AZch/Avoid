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
import android.widget.TextView;

import constants.paramSaveLoad;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class RecordWindow extends AppCompatActivity implements View.OnTouchListener  {

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

        setContentView(R.layout.activity_record_window);

        TextView easyMaxRoom = (TextView) findViewById(R.id.easyMaxRoomTextView);
        easyMaxRoom.setText(getString(R.string.recordEasy) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.EASY_MAX_ROOM, 1)) );
        TextView easyMaxDeadEnemy = (TextView) findViewById(R.id.easyMaxDeadEnemyTextView);
        easyMaxDeadEnemy.setText(getString(R.string.recordKillEasy) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.EASY_DEAD_ENEMY, 0)) );

        TextView normalMaxRoom = (TextView) findViewById(R.id.normalMaxRoomTextView);
        normalMaxRoom.setText(getString(R.string.recordNormal) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.NORMAL_MAX_ROOM, 1)) );
        TextView normalMaxDeadEnemy = (TextView) findViewById(R.id.normalMaxDeadEnemyTextView);
        normalMaxDeadEnemy.setText(getString(R.string.recordKillNormal) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.NORMAL_DEAD_ENEMY, 0)) );

        TextView hardMaxRoom = (TextView) findViewById(R.id.hardMaxRoomTextView);
        hardMaxRoom.setText(getString(R.string.recordHard) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.HARD_MAX_ROOM, 1)) );
        TextView hardMaxDeadEnemy = (TextView) findViewById(R.id.hardMaxDeadEnemyTextView);
        hardMaxDeadEnemy.setText(getString(R.string.recordKillHard) + " " + String.valueOf(getIntent().getIntExtra(paramSaveLoad.HARD_DEAD_ENEMY, 0)) );

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.recordGame);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            case R.id.recordGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}