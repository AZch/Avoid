package wordcreators.avoid2.AllSettingsWindow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import constants.requestString;
import constants.requestConst;
import wordcreators.avoid2.AllGameFile.GameView;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.ErrorScreen;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class ImageWindow extends AppCompatActivity implements View.OnTouchListener {

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

        setContentView(R.layout.activity_image_window);

        setStartImageView((ImageView) findViewById(R.id.playerImageView), MainActivity.playerImage);
        setStartImageView((ImageView) findViewById(R.id.enemyImageView), MainActivity.enemyImage);
        setStartImageView((ImageView) findViewById(R.id.wallImageView), MainActivity.wallImage);
        setStartImageView((ImageView) findViewById(R.id.bulletImageView), MainActivity.bulletImage);
        setStartImageView((ImageView) findViewById(R.id.medkitImageView), MainActivity.medkitImage);
        setStartImageView((ImageView) findViewById(R.id.ammoImageView), MainActivity.ammoImage);
        setStartImageView((ImageView) findViewById(R.id.portalImageView), MainActivity.portalImage);
        setStartImageView((ImageView) findViewById(R.id.enemyDeadImageView), MainActivity.enemyDeadImage);
        setStartImageView((ImageView) findViewById(R.id.enemyTrigImageView), MainActivity.enemyTrigImage);

        SeekBar zoomSB = (SeekBar) findViewById(R.id.zoomSeekBar);
        zoomSB.setProgress(SettingsWindow.getZoom() - 2);
        final TextView zoomT = (TextView) findViewById(R.id.zoomTextView);
        int progress = zoomSB.getProgress();
        zoomT.setText(getString(R.string.zoomText) + " " + String.valueOf(progress + 1));

        zoomSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoomT.setText(getString(R.string.zoomText) + " " + String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        if (MainActivity.getCountUseTexture() == 0) {
            RadioButton standartTexture = (RadioButton) findViewById(R.id.standartTextureRadioButton);
            standartTexture.setChecked(true);
        }
        else if (MainActivity.getCountUseTexture() == 1) {
            RadioButton customTexture = (RadioButton) findViewById(R.id.customTextureRadioButton);
            customTexture.setChecked(true);
        }

        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(),
                MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), true, this);

        gameLayout = (LinearLayout) findViewById(R.id.imageGame);
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

    private void setStartImageView(ImageView iv, Bitmap bm) {
        float sizeView = android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.TextureNewIconButton), getResources().getDisplayMetrics());
        if (bm != null && !bm.isRecycled())
            iv.setImageBitmap(Bitmap.createScaledBitmap(bm, (int)sizeView, (int)sizeView, false));
    }

    public void yourTextureRadioButton(View view) {
        MainActivity.setOtherTexturePack(1);
    }

    public void standartTextureRadioButton(View view) {
        MainActivity.setStandartTexturePack();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        SeekBar zoomSB = (SeekBar) findViewById(R.id.zoomSeekBar);
        int zoom = zoomSB.getProgress() + 2;
        data.putExtra(requestString.ZOOM, zoom);
        ArrayList<Bitmap> allTextureGame = new ArrayList<>();
        //allTextureGame.add(playerBM);
        //allTextureGame.add(enemyBM);
        data.putExtra(requestString.IMAGE, allTextureGame);
        setResult(RESULT_OK, data);
        finish();

        gameView.stopThread();
        //      gameLayout.removeAllViews();
        gameView = null;
        gameThread = null;
    }

    public void newTexturePlayer(View view) {
        Intent texturePlayer = new Intent(Intent.ACTION_PICK);
        texturePlayer.setType("image/*");
        startActivityForResult(texturePlayer, requestConst.GALLERY_REQUEST_PLAYER);
    }

    public void newTextureEnemy(View view) {
        Intent textureEnemy = new Intent(Intent.ACTION_PICK);
        textureEnemy.setType("image/*");
        startActivityForResult(textureEnemy, requestConst.GALLERY_REQUEST_ENEMY);
    }

    public void newTextureWall(View view) {
        Intent textureWall = new Intent(Intent.ACTION_PICK);
        textureWall.setType("image/*");
        startActivityForResult(textureWall, requestConst.GALLERY_REQUEST_WALL);
    }

    public void newTextureBullet(View view) {
        Intent textureBullet = new Intent(Intent.ACTION_PICK);
        textureBullet.setType("image/*");
        startActivityForResult(textureBullet, requestConst.GALLERY_REQUEST_BULLET);
    }

    public void newTextureMedkit(View view) {
        Intent textureMedkit = new Intent(Intent.ACTION_PICK);
        textureMedkit.setType("image/*");
        startActivityForResult(textureMedkit, requestConst.GALLERY_REQUEST_MEDKIT);
    }

    public void newTextureAmmo(View view) {
        Intent textureAmmo = new Intent(Intent.ACTION_PICK);
        textureAmmo.setType("image/*");
        startActivityForResult(textureAmmo, requestConst.GALLERY_REQUEST_AMMO);
    }

    public void newTexturePortal(View view) {
        Intent texturePortal = new Intent(Intent.ACTION_PICK);
        texturePortal.setType("image/*");
        startActivityForResult(texturePortal, requestConst.GALLERY_REQUEST_PORTAL);
    }

    public void newTextureEnemyDead(View view) {
        Intent textureEnemyDead = new Intent(Intent.ACTION_PICK);
        textureEnemyDead.setType("image/*");
        startActivityForResult(textureEnemyDead, requestConst.GALLERY_REQUEST_ENEMY_DEAD);
    }

    public void newTextureEnemyTrig(View view) {
        Intent textureEnemyTrig = new Intent(Intent.ACTION_PICK);
        textureEnemyTrig.setType("image/*");
        startActivityForResult(textureEnemyTrig, requestConst.GALLERY_REQUEST_ENEMY_TRIG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            switch (requestCode) {
                case requestConst.GALLERY_REQUEST_PLAYER:
                    try {
                        MainActivity.playerImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView playerIV = (ImageView) findViewById(R.id.playerImageView);
                    MainActivity.playerImage = Bitmap.createScaledBitmap(MainActivity.playerImage, playerIV.getWidth(), playerIV.getHeight(), false);
                    playerIV.setImageBitmap(MainActivity.playerImage);
                    break;
                case requestConst.GALLERY_REQUEST_ENEMY:
                    try {
                        MainActivity.enemyImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView enemyIV = (ImageView) findViewById(R.id.enemyImageView);
                    MainActivity.enemyImage = Bitmap.createScaledBitmap(MainActivity.enemyImage, enemyIV.getWidth(), enemyIV.getHeight(), false);
                    enemyIV.setImageBitmap(MainActivity.enemyImage);
                    break;
                case requestConst.GALLERY_REQUEST_WALL:
                    try {
                        MainActivity.wallImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();;
                    }
                    ImageView wallIV = (ImageView) findViewById(R.id.wallImageView);
                    MainActivity.wallImage = Bitmap.createScaledBitmap(MainActivity.wallImage, wallIV.getWidth(), wallIV.getHeight(), false);
                    wallIV.setImageBitmap(MainActivity.wallImage);
                    break;
                case requestConst.GALLERY_REQUEST_BULLET:
                    try {
                        MainActivity.bulletImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView bulletIV = (ImageView) findViewById(R.id.bulletImageView);
                    MainActivity.bulletImage = Bitmap.createScaledBitmap(MainActivity.bulletImage, bulletIV.getWidth(), bulletIV.getHeight(), false);
                    bulletIV.setImageBitmap(MainActivity.bulletImage);
                    break;
                case requestConst.GALLERY_REQUEST_MEDKIT:
                    try {
                        MainActivity.medkitImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView medkitIV = (ImageView) findViewById(R.id.medkitImageView);
                    MainActivity.medkitImage = Bitmap.createScaledBitmap(MainActivity.medkitImage, medkitIV.getWidth(), medkitIV.getHeight(), false);
                    medkitIV.setImageBitmap(MainActivity.medkitImage);
                    break;
                case requestConst.GALLERY_REQUEST_AMMO:
                    try {
                        MainActivity.ammoImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView ammoIV = (ImageView) findViewById(R.id.ammoImageView);
                    MainActivity.ammoImage = Bitmap.createScaledBitmap(MainActivity.ammoImage, ammoIV.getWidth(), ammoIV.getHeight(), false);
                    ammoIV.setImageBitmap(MainActivity.ammoImage);
                    break;
                case requestConst.GALLERY_REQUEST_PORTAL:
                    try {
                        MainActivity.portalImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView portalIV = (ImageView) findViewById(R.id.portalImageView);
                    MainActivity.portalImage = Bitmap.createScaledBitmap(MainActivity.portalImage, portalIV.getWidth(), portalIV.getHeight(), false);
                    portalIV.setImageBitmap(MainActivity.portalImage);
                    break;
                case requestConst.GALLERY_REQUEST_ENEMY_DEAD:
                    try {
                        MainActivity.enemyDeadImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView enemyDeadyIV = (ImageView) findViewById(R.id.enemyDeadImageView);
                    MainActivity.enemyDeadImage = Bitmap.createScaledBitmap(MainActivity.enemyDeadImage, enemyDeadyIV.getWidth(), enemyDeadyIV.getHeight(), false);
                    enemyDeadyIV.setImageBitmap(MainActivity.enemyDeadImage);
                    break;
                case requestConst.GALLERY_REQUEST_ENEMY_TRIG:
                    try {
                        MainActivity.enemyTrigImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView enemyTrigIV = (ImageView) findViewById(R.id.enemyTrigImageView);
                    MainActivity.enemyTrigImage = Bitmap.createScaledBitmap(MainActivity.enemyTrigImage, enemyTrigIV.getWidth(), enemyTrigIV.getHeight(), false);
                    enemyTrigIV.setImageBitmap(MainActivity.enemyTrigImage);
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
            case R.id.imageGame:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.addTouch(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }
}