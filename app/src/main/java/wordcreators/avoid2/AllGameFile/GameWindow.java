package wordcreators.avoid2.AllGameFile;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EventListener;

import constants.requestString;
import maker.Geometric_Calculate;
import models.Human.Player;
import models.Objects;
import models.Resource.Gun;
import models.Resource.Resource;
import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

import static android.content.ContentValues.TAG;

public class GameWindow extends AppCompatActivity implements View.OnTouchListener {

    // игровое поле и игровой цикл
    private GameView gameView;

    // все нажатые клавишы
    private static ArrayList<Character> moveKey = new ArrayList<>();

    // координаты центра джостика
    float joystickX, joystickY;

    // координаты центра джистика стрельбы
    float joystickFireX, joystickFireY;

    // весь джостик
    ImageView joystick; // поле джостика
    private ImageView joystickMove; // подвижная часть джостика

    // весь джостик стрельбы
    private ImageView joystickFire; // поле джостика
    private ImageView joyStickMoveFire; // подвижная часть джостика

    // направление стрельбы
    private float fireAngle;

    // фоновая музыка для игры
    private MediaPlayer mediaPlayer;

    // точка касания по выбранному оружию для проверки смены оружия
    private float touchGunX = 0;

    // кнопка паузы игры
    ImageView stopButton;

    // кнопка продолжения игры
    ImageView resumeButton;

    // кнопка остановки игры
    ImageView exitButton;

    // игровая поверхность
    LinearLayout gameLayout;

    // анимации
    Animation animation;

    // активное оружие
    ImageView gunImageView;

    // индикатор загрузки уровня
    ProgressBar loadLvl;
    // вывод процента загрузки
    TextView loadLvlTextView;

    // вывод того что загружается
    TextView whatLoad;

    // инвентарь игрока
    LinearLayout inventory;

    // точка касания по определённой понели инвенторя
    float[] downInventPanel = new float[2];

    // точка отпускания от определенной понели инвенторя
    float[] upInventPanel = new float[2];

    // определенная панель инвенторя
    int idTouchPanel = -1;

    boolean touchRun = false;

    private ArrayList<Objects> otherThings = new ArrayList<>();

    private boolean scrolling = true;

    private ImageView touchThing;

    private ScrollView yourThing;

    private ScrollView otherThing;

    ConstraintLayout allGameElement;

    // используется для реализации двойного нажатия
    private int countTap = 0;

    // время первого нажатия на предмет
    private long timeFirstTap = 0;

    // true когда анимация завершилась
    private boolean endAnimation = false;

    // кол-во жизней игрока
    TextView lifeText;

    // кол-во патрон игрока
    TextView bulletCountText;

    // кол-во пройденный комнат
    TextView roomsCountText;

    // кол-во убитых противников
    TextView enemyDeadCountText;

    // кол-во патронов игрока в оружии сейчас
    TextView thisBulletCountText;

    // показ того что было добавлено в инвентарь
    ImageView addInInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_window);

        lifeText = findViewById(R.id.lifeCountText);
        bulletCountText = findViewById(R.id.bulletCountText);
        roomsCountText = findViewById(R.id.roomsCountText);
        enemyDeadCountText = findViewById(R.id.deadEnemyCountText);
        thisBulletCountText = findViewById(R.id.thisCountBulletText);

        addInInventory = findViewById(R.id.addInInventory);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.foor_showdown);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(MainActivity.getSoundMusicGame(), MainActivity.getSoundMusicGame());
        mediaPlayer.start();

        // указатели зигрузки
        loadLvl = (ProgressBar) findViewById(R.id.progressLoad);
        loadLvl.setVisibility(View.INVISIBLE);
        loadLvlTextView = (TextView) findViewById(R.id.LoadProgressTextView);
        loadLvlTextView.setVisibility(View.INVISIBLE);
        whatLoad = (TextView) findViewById(R.id.whatLoadTextView);
        whatLoad.setVisibility(View.INVISIBLE);

        // игровое поле
        gameView = new GameView(this, MainActivity.getMaxLongQwad(), MainActivity.getMaxShortQwad(), 500, 500, false, this);

        gameView.setGameWindow(this);

        gameLayout = (LinearLayout) findViewById(R.id.gameLayout);
        gameLayout.addView(gameView);
        gameLayout.setOnTouchListener(this);

        gunImageView = (ImageView) findViewById(R.id.activeGunImageView);

        // кнопки управления состоянием игры
        stopButton = (ImageView) findViewById(R.id.menuButton);

        resumeButton = (ImageView) findViewById(R.id.resumeButton);
        resumeButton.setVisibility(View.INVISIBLE);

        exitButton = (ImageView) findViewById(R.id.closeGameButton);
        exitButton.setVisibility(View.INVISIBLE);

        // оружие из которого ведётся огонь
        ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
        activeGun.setOnTouchListener(this);

        // игровые джостики
        joystick = (ImageView) findViewById(R.id.joystickField);
        joystick.setOnTouchListener(this);

        joystickMove = (ImageView) findViewById(R.id.joystickMove);

        joystickFire = (ImageView) findViewById(R.id.joystickFireField);

        joyStickMoveFire = (ImageView) findViewById(R.id.joystickFireMove);

        // отрисовка элементов для ведения огна
        if (MainActivity.getControlFire() == 0)
            joystickFire.setOnTouchListener(this);
        else if (MainActivity.getControlFire() == 1) {
            joystickFire.setVisibility(View.INVISIBLE);
            joystickFire.setEnabled(false);
            joyStickMoveFire.setVisibility(View.INVISIBLE);
            joyStickMoveFire.setEnabled(false);
        }
        animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                endAnimation = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // задание иконок о состоянии игры
        gameView.lifeIcon = (ImageView) findViewById(R.id.lifeIcon);
        gameView.bulletIcon = (ImageView) findViewById(R.id.bulletIcon);
        gameView.roomsIcon = (ImageView) findViewById(R.id.roomsIcon);
        gameView.deadEnemyIcon = (ImageView) findViewById(R.id.deadEnemyIcon);

        // начальное скрытие инвенторя
        inventory = (LinearLayout) findViewById(R.id.inventoryLayout);
        inventory.setVisibility(View.INVISIBLE);
        inventory.setEnabled(false);

        addToListnerInventory();

        touchThing = new ImageView(getApplicationContext());
        allGameElement = (ConstraintLayout) findViewById(R.id.allGameElementLayout);
        allGameElement.addView(touchThing);
        // запуск самой игры
        Thread gameThread = new Thread(gameView);
        gameThread.start();
    }

    private void addToListnerInventory() {
        inventory.setOnTouchListener(this);

        ImageView leftArm = (ImageView) findViewById(R.id.leftArm);
        leftArm.setOnTouchListener(this);

        ImageView rightArm = (ImageView) findViewById(R.id.rightArm);
        rightArm.setOnTouchListener(this);

        yourThing = (ScrollView) findViewById(R.id.scrollOtherThings);
        yourThing.setOnTouchListener(this);

        otherThing = (ScrollView) findViewById(R.id.scrollInventory);
        otherThing.setOnTouchListener(this);

        LinearLayout allInventory = (LinearLayout) findViewById(R.id.yourInventory);
        allInventory.setOnTouchListener(this);

        LinearLayout allOtherThings = (LinearLayout) findViewById(R.id.otherThings);
        allOtherThings.setOnTouchListener(this);
    }

    private void setGunInInventory(Objects gun, boolean firstSlot, boolean yourInvent) {
        Objects thisGun, otherGun;
        if (firstSlot) {
            thisGun = ((Player) gameView.getPlayer()).getInventory().getFirstGun();
            otherGun = ((Player) gameView.getPlayer()).getInventory().getSecondGun();
        } else {
            otherGun = ((Player) gameView.getPlayer()).getInventory().getFirstGun();
            thisGun = ((Player) gameView.getPlayer()).getInventory().getSecondGun();
        }

        if (gun instanceof Gun) { // есди перетаскиваемая вещь - оружие
            if (thisGun != null) { // если есть оружия в выбранном слоте
                if (otherGun == null) { // если в соседнем слоте пусто
                    if (firstSlot) {
                        ((Player) gameView.getPlayer()).getInventory().setSecondGun(thisGun);
                        ((Player) gameView.getPlayer()).getInventory().setFirstGun(gun);
                    } else {
                        ((Player) gameView.getPlayer()).getInventory().setFirstGun(thisGun);
                        ((Player) gameView.getPlayer()).getInventory().setSecondGun(gun);
                    }
                    //((Player) gameView.getPlayer()).getInventory().setSecondGun(((Player) gameView.getPlayer()).getInventory().getFirstGun());
                    //if (((Player) gameView.getPlayer()).getInventory().setFirstGun(gun))
                    if (yourInvent) {
                        ((Player) gameView.getPlayer()).getInventory().removeThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), idTouchPanel);
                    } else {
                        gameView.riseThing(gun);
                    }
                } else if (!(((Player) gameView.getPlayer()).getInventory().replaceThing(gun, thisGun, yourInvent))) {
                    gameView.dropThing(thisGun);

                    if (firstSlot)
                        ((Player) gameView.getPlayer()).getInventory().setFirstGun(gun);
                    else
                        ((Player) gameView.getPlayer()).getInventory().setSecondGun(gun);

                    if (yourInvent) {
                        ((Player) gameView.getPlayer()).getInventory().removeThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), idTouchPanel);
                    } else {
                        gameView.riseThing(gun);
                    }
                } else {
                    if (firstSlot)
                        ((Player) gameView.getPlayer()).getInventory().setFirstGun(gun);
                    else
                        ((Player) gameView.getPlayer()).getInventory().setSecondGun(gun);

                    if (yourInvent) {
                        //((Player) gameView.getPlayer()).getInventory().removeThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), idTouchPanel);
                    } else {
                        gameView.riseThing(gun);
                    }
                }
            } else {
                if (firstSlot)
                    ((Player) gameView.getPlayer()).getInventory().setFirstGun(gun);
                else
                    ((Player) gameView.getPlayer()).getInventory().setSecondGun(gun);

                if (yourInvent) {
                    ((Player) gameView.getPlayer()).getInventory().removeThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), idTouchPanel);
                } else {
                    gameView.riseThing(gun);
                }
            }
        }
    }

    private void actionUpInventory(MotionEvent event, float xOtherInvent, float yOtherInvent, float biasX, float biasY, boolean yourInvent) {
        if (!scrolling) {
            touchThing.setImageBitmap(null);
            scrolling = true;
            if (downInventPanel[0] == -1 && downInventPanel[1] == -1)
                return;
            if (yourInvent) {
                if (((Player) gameView.getPlayer()).getInventory().getThings().size() == 0)
                    return;
            } else {
                if (otherThings.size() == 0)
                    return;
            }
            Log.d(TAG, "onTouch: yInv" + event.getAction() + " " + event.getX() + " " + event.getY());
            upInventPanel[0] = event.getX();
            upInventPanel[1] = event.getY();
            downInventPanel[0] = -1;
            downInventPanel[1] = -1;
            if (((!yourInvent && event.getY() > yOtherInvent) || (yourInvent && event.getY() < yOtherInvent)) && event.getX() > xOtherInvent) {
                if (yourInvent) {
                    gameView.dropThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel));
                    ((Player) gameView.getPlayer()).getInventory().removeThing(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), idTouchPanel);
                } else {
                    if (((Player) gameView.getPlayer()).getInventory().addSubject(otherThings.get(idTouchPanel)))
                        gameView.riseThing(otherThings.get(idTouchPanel));
                }
                drawInventory();
            } else {
                float orientX = event.getX() + biasX;
                float orientY = event.getY() + biasY;
                if (orientX >= 0 && orientX < inventory.getWidth() / 4 &&
                        orientY >= 0 && orientY < inventory.getHeight() / 2) {
                    if (yourInvent) {
                        setGunInInventory(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), true, yourInvent);
                    } else {
                        setGunInInventory(otherThings.get(idTouchPanel), true, yourInvent);
                    }

                    drawInventory();
                    Log.d(TAG, "onTouch: fg");
                } else if (orientX >= inventory.getWidth() / 4 && orientX < inventory.getWidth() / 2 &&
                        orientY >= 0 && orientY < inventory.getHeight() / 2) {
                    if (yourInvent) {
                        setGunInInventory(((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel), false, yourInvent);
                    } else {
                        setGunInInventory(otherThings.get(idTouchPanel), false, yourInvent);
                    }
                    drawInventory();
                    Log.d(TAG, "onTouch: sg");
                }
            }
        }
    }

    private void activeTouchThingBM(Bitmap bitmap, MotionEvent event, float biasX, float biasY) {
        touchThing.setX(event.getX() + inventory.getX() + biasX - bitmap.getWidth() / 2);
        touchThing.setY(event.getY() + inventory.getY() + biasY - bitmap.getHeight() / 2);
        touchThing.setImageBitmap(bitmap);
    }

    private void printActiveThing(MotionEvent event, float biasX, float biasY, boolean yourInvent) {
        if (!scrolling) {
            scrolling = false;
            Bitmap setBM;
            if (yourInvent) {
                setBM = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getThings().get(idTouchPanel)).getId());
            } else {
                setBM = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) otherThings.get(idTouchPanel)).getId());
            }
            activeTouchThingBM(setBM, event, biasX, biasY);
        }
    }

    private void actionDownInventory(View view, MotionEvent event, boolean yourInventory) {
        if (event.getX() > 4 * view.getWidth() / 5) {
            if (yourInventory) {
                if (((Player) gameView.getPlayer()).getInventory().getThings().size() == 0)
                    return;
            } else {
                otherThings = Geometric_Calculate.getOtherThings(gameView);
                if (otherThings.size() == 0)
                    return;
            }
            scrolling = false;
            downInventPanel[0] = event.getX();
            downInventPanel[1] = event.getY();
            Log.d(TAG, "onTouch: yInv" + event.getAction() + " " + event.getX() + " " + event.getY());

            idTouchPanel = (int) Math.floor(event.getY() / (((Player) gameView.getPlayer()).getInventory().getHeightOneThing()));
        } else {
            if (countTap == 0) {
                timeFirstTap = System.currentTimeMillis();
                countTap++;
            } else if (System.currentTimeMillis() - timeFirstTap < 1000) { // использование
                idTouchPanel = (int) Math.floor(event.getY() / (((Player) gameView.getPlayer()).getInventory().getHeightOneThing()));
                if (yourInventory) {
                    if (((Player) gameView.getPlayer()).getInventory().getThings().size() == 0)
                        return;
                    ((Player) gameView.getPlayer()).getInventory().useThing(idTouchPanel);
                } else {
                    otherThings = Geometric_Calculate.getOtherThings(gameView);
                    if (otherThings.size() == 0)
                        return;
                    ((Resource) otherThings.get(idTouchPanel)).use();
                }
                drawInventory();
                countTap = 0;
            } else {
                countTap = 0;
            }
        }
    }

    private void changeArm() {
        Objects saveOtherGun = ((Player) gameView.getPlayer()).getInventory().getSecondGun();
        ((Player) gameView.getPlayer()).getInventory().setSecondGun(((Player) gameView.getPlayer()).getInventory().getFirstGun());
        ((Player) gameView.getPlayer()).getInventory().setFirstGun(saveOtherGun);
    }

    private void printArmThing(MotionEvent event, float biasX, float biasY, boolean leftArm) {
        Bitmap setBM = null;
        if (leftArm) {
            if (((Player) gameView.getPlayer()).getInventory().getFirstGun() != null)
                setBM = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId());
        } else {
            if (((Player) gameView.getPlayer()).getInventory().getSecondGun() != null)
                setBM = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId());
        }

        if (setBM != null) {
            activeTouchThingBM(setBM, event, biasX, biasY);
        }
    }

    private void actionUpInventoryArms(MotionEvent event, float biasThisX, float biasThisY, boolean leftArm) {
        Objects armCheck;
        touchThing.setImageBitmap(null);
        if (leftArm) {
            armCheck = ((Player) gameView.getPlayer()).getInventory().getFirstGun();
        } else {
            armCheck = ((Player) gameView.getPlayer()).getInventory().getSecondGun();
        }

        if (armCheck != null) {
            float orientX = event.getX() + biasThisX,
                    orientY = event.getY() + biasThisY;
            if (leftArm) {
                if (orientX > inventory.getWidth() / 4 && orientX < inventory.getWidth() / 2 &&
                        orientY > 0 && orientY < inventory.getHeight() / 2) { // перенос к правой руке
                    changeArm();
                    drawInventory();
                }
            } else {
                if (orientX > 0 && orientX < inventory.getWidth() / 4 &&
                        orientY > 0 && orientY < inventory.getHeight() / 2) {
                    changeArm();
                    drawInventory();
                }
            }

            if (orientX > inventory.getWidth() / 2 && orientX < inventory.getWidth() &&
                    orientY > 0 && orientY < inventory.getHeight() / 2) { // перенос в вещи на карте
                if (leftArm) {
                    gameView.dropThing(((Player) gameView.getPlayer()).getInventory().getFirstGun());
                    ((Player) gameView.getPlayer()).getInventory().setFirstGun(null);
                } else {
                    gameView.dropThing(((Player) gameView.getPlayer()).getInventory().getSecondGun());
                    ((Player) gameView.getPlayer()).getInventory().setSecondGun(null);
                }
                drawInventory();
            }

            if (orientX > inventory.getWidth() / 2 && orientX < inventory.getWidth() &&
                    orientY > inventory.getHeight() / 2 && orientY < inventory.getHeight()) { // перенос в вещи игрока
                if (leftArm) {
                    if (((Player) gameView.getPlayer()).getInventory().addSubject(((Player) gameView.getPlayer()).getInventory().getFirstGun()))
                        ((Player) gameView.getPlayer()).getInventory().setFirstGun(null);
                } else {
                    if (((Player) gameView.getPlayer()).getInventory().addSubject(((Player) gameView.getPlayer()).getInventory().getSecondGun()))
                        ((Player) gameView.getPlayer()).getInventory().setSecondGun(null);
                }
                drawInventory();
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.leftArm:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventoryArms(event, 0, 0, true);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printArmThing(event, 0, 0, true);
                }
                break;

            case R.id.rightArm:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventoryArms(event, inventory.getWidth() / 4, 0, false);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printArmThing(event, inventory.getWidth() / 4, 0, false);
                }
                break;

            // нажатие на панель с вещями вокруг
            case R.id.scrollOtherThings:
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printActiveThing(event, inventory.getWidth() / 2, 0, false);
                }
                if (!scrolling && event.getAction() != MotionEvent.ACTION_UP)
                    return true;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventory(event, 0, view.getHeight(), inventory.getWidth() / 2, 0, false);
                }
                return false;

            // нажатие на панель с вещями вокруг
            case R.id.otherThings:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionDownInventory(view, event, false);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventory(event, 0, view.getHeight(), inventory.getWidth() / 2, 0, false);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printActiveThing(event, inventory.getWidth() / 2, 0, false);
                }
                //return false;
                break;

            // нажатие на панель с инвентарём игрока
            case R.id.scrollInventory:
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printActiveThing(event, inventory.getWidth() / 2, inventory.getHeight() / 2, true);
                }
                if (!scrolling && event.getAction() != MotionEvent.ACTION_UP)
                    return true;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventory(event, 0, 0, inventory.getWidth() / 2, inventory.getHeight() / 2, true);
                }
                return false;
            // нажатие на панель с инвентарём игрока
            case R.id.yourInventory:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionDownInventory(view, event, true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    actionUpInventory(event, 0, 0, inventory.getWidth() / 2, inventory.getHeight() / 2, true);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    printActiveThing(event, inventory.getWidth() / 2, inventory.getHeight() / 2, true);
                }
                break;

            case R.id.joystickField:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((Player) gameView.getPlayer()).startMove(System.currentTimeMillis());
                    //Log.d(TAG, "onTouch: Down");
                    joystickX = joystick.getWidth() / 2;
                    joystickY = joystick.getHeight() / 2;
                    moveJoystick(joystickMove, joystick, event, (float) gameView.goAngle);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    gameView.go = true;
                    gameView.goAngle = (float) (Math.atan2(joystickY - event.getY(), event.getX() - joystickX)) * (-1);
                    moveJoystick(joystickMove, joystick, event, (float) gameView.goAngle);
                    //Log.d(TAG, "onTouch: Move");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((Player) gameView.getPlayer()).stopMove();
                   // Log.d(TAG, "onTouch: Up");
                    gameView.go = false;
                    joystickMove.setX(joystick.getX() + joystick.getWidth() / 2 - joystickMove.getWidth() / 2);
                    joystickMove.setY(joystick.getY() + joystick.getHeight() / 2 - joystickMove.getHeight() / 2);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Log.d(TAG, "onTouch: Cancel");
                    event.setAction(MotionEvent.ACTION_MOVE);
                    onTouch(view, event);
                }
                break;

            case R.id.joystickFireField:
                if (MainActivity.getControlFire() == 0) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        joystickFireX = joystickFire.getWidth() / 2;
                        joystickFireY = joystickFire.getHeight() / 2;
                        moveJoystick(joyStickMoveFire, joystickFire, event, fireAngle);
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        fireAngle = (float) (Math.atan2(joystickFireY - event.getY(), event.getX() - joystickFireX)) * (-1);
                        gameView.startFire(fireAngle);
                        moveJoystick(joyStickMoveFire, joystickFire, event, fireAngle);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        gameView.stopFire();
                        joyStickMoveFire.setX(joystickFire.getX() + joystickFire.getWidth() / 2 - joyStickMoveFire.getWidth() / 2);
                        joyStickMoveFire.setY(joystickFire.getY() + joystickFire.getHeight() / 2 - joyStickMoveFire.getHeight() / 2);
                    }
                }
                break;
            case R.id.gameLayout:
                if (MainActivity.getControlFire() == 1) { // стрельба нажатием на экран
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        fireAngle = (float) (Math.atan2(gameView.getPlayer().getScreenY() - event.getY(), event.getX() - gameView.getPlayer().getScreenX())) * (-1);
                        gameView.startFire(fireAngle);
                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                        gameView.stopFire();
                    else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        fireAngle = (float) (Math.atan2(gameView.getPlayer().getScreenY() - event.getY(), event.getX() - gameView.getPlayer().getScreenX())) * (-1);
                        gameView.startFire(fireAngle);
                    }
                }
                break;
            case R.id.activeGunImageView:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setAnimation(animation);
                    touchGunX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (touchGunX - event.getX() > 0) { // свайп влево
                        ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
                        //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
                        if (gameView.fireGun == 1 && ((Player) gameView.getPlayer()).getInventory().getFirstGun() != null) {
                            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId()));
                            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getCountBulletInGun()));
                            gameView.fireGun = 0;
                        } else if (gameView.fireGun == 0 && ((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
                            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId()));
                            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getCountBulletInGun()));
                            gameView.fireGun = 1;
                        } else {
                            activeGun.setImageBitmap(null);
                        }
                    } else { // свайп вправо
                        ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
                        //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
                        if (gameView.fireGun == 1 && ((Player) gameView.getPlayer()).getInventory().getFirstGun() != null) {
                            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId()));
                            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getCountBulletInGun()));
                            gameView.fireGun = 0;
                        } else if (gameView.fireGun == 0 && ((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
                            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId()));
                            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getCountBulletInGun()));
                            gameView.fireGun = 1;
                        } else {
                            activeGun.setImageBitmap(null);
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void updateActiveGun() {
        if (((Player) gameView.getPlayer()).getInventory().getFirstGun() != null && ((Player) gameView.getPlayer()).getInventory().getSecondGun() == null) {
            //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
            ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId()));
            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getCountBulletInGun()));
            gameView.fireGun = 0;
            return;
        }

        if (((Player) gameView.getPlayer()).getInventory().getFirstGun() == null && ((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
            //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
            ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId()));
            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getCountBulletInGun()));
            gameView.fireGun = 1;
            return;
        }

        if (((Player) gameView.getPlayer()).getInventory().getFirstGun() == null && ((Player) gameView.getPlayer()).getInventory().getSecondGun() == null) {
            //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
            ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
            activeGun.setImageBitmap(null);
            //activeGunBullet.setText("");
            gameView.fireGun = -1;
            return;
        }

        if (gameView.fireGun == 0 && ((Player) gameView.getPlayer()).getInventory().getFirstGun() != null) {
            //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
            ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId()));
            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getCountBulletInGun()));
            gameView.fireGun = 0;
            return;
        }

        if (gameView.fireGun == 1 && ((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
            //TextView activeGunBullet = (TextView) findViewById(R.id.activeGunBullet);
            ImageView activeGun = (ImageView) findViewById(R.id.activeGunImageView);
            activeGun.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId()));
            //activeGunBullet.setText(String.valueOf(((Gun) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getCountBulletInGun()));
            gameView.fireGun = 1;
            return;
        }
    }

    private void moveJoystick(ImageView joystickMove, ImageView joystick, MotionEvent event, float angle) {
        float newJoystickMoveX = event.getX() + joystick.getX() - joystickMove.getWidth() / 2,
              newJoystickMoveY = event.getY() + joystick.getY() - joystickMove.getHeight() / 2;
        if (destination(joystick.getX() + joystick.getWidth() / 2, joystick.getY() + joystick.getHeight() / 2, event.getX() + joystick.getX(), event.getY() + joystick.getY()) +
                destination(joystickMove.getX() + joystickMove.getWidth() / 2, joystickMove.getY() + joystickMove.getHeight() / 2, joystickMove.getX() + joystickMove.getWidth() / 2, joystickMove.getY()) <
                destination(joystick.getX() + joystick.getWidth() / 2, joystick.getY() + joystick.getHeight() / 2, joystick.getX() + joystick.getWidth() / 2, joystick.getY())) {
                    joystickMove.setX(newJoystickMoveX);
                    joystickMove.setY(newJoystickMoveY);
        }
        else {
            float radius = destination(joystick.getX() + joystick.getWidth() / 2, joystick.getY() + joystick.getHeight() / 2, joystick.getX() + joystick.getWidth() / 2, joystick.getY()) -
                    destination(joystickMove.getX() + joystickMove.getWidth() / 2, joystickMove.getY() + joystickMove.getHeight() / 2, joystickMove.getX() + joystickMove.getWidth() / 2, joystickMove.getY());
            joystickMove.setX(joystick.getX() + joystick.getWidth() / 2 + radius * (float) Math.cos(angle) - joystickMove.getWidth() / 2);
            joystickMove.setY(joystick.getY() + joystick.getHeight() / 2 + radius * (float) Math.sin(angle) - joystickMove.getHeight() / 2);
        }
    }

    public void openMenu(View view) {
        try {
            view.startAnimation(animation);
            view.startAnimation(animation);
            if (gameView != null)
                gameView.pauseGame();
            if (mediaPlayer != null)
                mediaPlayer.pause();
            resumeButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //resumeButton.startAnimation(animation);
    }

    public void resumeGame(View view) {
        view.startAnimation(animation);
        while (!endAnimation);
        mediaPlayer.start();
        resumeButton.setVisibility(View.INVISIBLE);
        //resumeButton.clearAnimation();
        exitButton.setVisibility(View.INVISIBLE);
        gameView.resumeGame();
    }

    public void exitGame(View view) {
        view.startAnimation(animation);
        while (!endAnimation);
        mediaPlayer.release();
        gameView.stopThread();
        Intent data = new Intent();
        data.putExtra(requestString.MAX_ROOM, gameView.getMaxRoom());
        data.putExtra(requestString.MAX_DEAD_ENEMY, gameView.getMaxDeadEnemy());
        setResult(RESULT_OK, data);
        gameView = null;
        finish();
    }

    public void openInventory(View view) {
        view.startAnimation(animation);
        if (inventory.isEnabled())
            closeInventory();
        else
            drawInventory();
    }

    public void thingDrawInventory() {
        if (inventory.isEnabled())
            drawInventory();
    }

    private void animateButton(View view) {
        view.startAnimation(animation);
        while (!endAnimation);
        //view.startAnimation(animation);
        //while (!endAnimation);
    }

    // отрисовка инвенторя
    public void drawInventory() {
        //ArrayList<ArrayList<Integer>> cellsInInventory = ((Player) gameView.getPlayer()).getInventory().getCells();
        //for
        LinearLayout ly;
        ImageView thing;
        Bitmap guns = null;
        Matrix matrix = new Matrix();

        if (((Player) gameView.getPlayer()).getInventory().getFirstGun() != null) {
            guns = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getFirstGun()).getId());
            if (guns != null) {

                //matrix.setRotate(270, (float) guns.getWidth() / 2, (float) guns.getHeight() / 2);
                guns = Bitmap.createBitmap(guns, 0, 0, guns.getWidth(), guns.getHeight(), matrix, true);
                thing = (ImageView) findViewById(R.id.leftArm);
                thing.setImageBitmap(guns);
            }
        } else {
            thing = (ImageView) findViewById(R.id.leftArm);
            thing.setImageBitmap(null);
        }

        if (((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
            guns = ((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) ((Player) gameView.getPlayer()).getInventory().getSecondGun()).getId());
            if (guns != null) {
                //matrix.setRotate(270, (float) guns.getWidth() / 2, (float) guns.getHeight() / 2);
                guns = Bitmap.createBitmap(guns, 0, 0, guns.getWidth(), guns.getHeight(), matrix, true);
                thing = (ImageView) findViewById(R.id.rightArm);
                thing.setImageBitmap(guns);
            }
        } else {
            thing = (ImageView) findViewById(R.id.rightArm);
            thing.setImageBitmap(null);
        }


        ly = (LinearLayout) findViewById(R.id.yourInventory);
        ArrayList<Objects> thingInventory = ((Player) gameView.getPlayer()).getInventory().getThings();
        ly.removeAllViews();

        LinearLayout stroke;
        TextView countThing;

        for (int i = 0; i < thingInventory.size(); i++) {
            stroke = new LinearLayout(getApplicationContext());
            stroke.setOrientation(LinearLayout.HORIZONTAL);
            thing = new ImageView(getApplicationContext());
            thing.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) thingInventory.get(i)).getId()));
            //thing.setScaleType(ImageView.ScaleType.FIT_START);
            countThing = new TextView(getApplicationContext());
            countThing.setText(String.valueOf(((Resource) ((Player) gameView.getPlayer()).getInventory().getThings().get(i)).getCount()));
            countThing.setGravity(Gravity.CENTER);
            //countThing.setTextSize(thing.getHeight());
            stroke.addView(thing);
            stroke.addView(countThing);
            ly.addView(stroke);
        }

        ly = (LinearLayout) findViewById(R.id.otherThings);
        ly.removeAllViews();
        for (int i = 0; i < gameView.getObjResource().size(); i++) {
            if (!((Resource) gameView.getObjResource().get(i)).getUsed() && Geometric_Calculate.destination(gameView.getPlayer(), gameView.getObjResource().get(i)) <= 15 * 15) {
                stroke = new LinearLayout(getApplicationContext());
                stroke.setOrientation(LinearLayout.HORIZONTAL);
                thing = new ImageView(getApplicationContext());
                thing.setImageBitmap(((Player) gameView.getPlayer()).getInventory().getBitmapThingId(((Resource) gameView.getObjResource().get(i)).getId()));
                //thing.setScaleType(ImageView.ScaleType.FIT_START);
                countThing = new TextView(getApplicationContext());
                countThing.setText(String.valueOf(((Resource) gameView.getObjResource().get(i)).getCount()));
                countThing.setGravity(Gravity.CENTER);
                //countThing.setTextSize(thing.getHeight());
                stroke.addView(thing);
                stroke.addView(countThing);
                ly.addView(stroke);
            }
        }


        inventory.setEnabled(true);
        inventory.setVisibility(View.VISIBLE);
    }

    // закрытие инвенторя
    public void closeInventory() {
        inventory.setVisibility(View.INVISIBLE);
        inventory.setEnabled(false);
    }

    // все элементы игры нивидимы
    public void setInvisibleAll() {

        //allGameElement.setVisibility(View.INVISIBLE);

        // скрытие указателей загрузки игры
        //RelativeLayout progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        //progressLayout.setVisibility(View.VISIBLE);

        gameLayout.setVisibility(View.INVISIBLE);
        gameView.lifeIcon.setVisibility(View.INVISIBLE);
        gameView.bulletIcon.setVisibility(View.INVISIBLE);
        gameView.roomsIcon.setVisibility(View.INVISIBLE);
        gameView.deadEnemyIcon.setVisibility(View.INVISIBLE);
        gunImageView.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        if (gameView.getPause()) {
            resumeButton.setVisibility(View.INVISIBLE);
            exitButton.setVisibility(View.INVISIBLE);
        }
        joystick.setVisibility(View.INVISIBLE);
        joystickMove.setVisibility(View.INVISIBLE);
        if (MainActivity.getControlFire() == 0) {
            joystickFire.setVisibility(View.INVISIBLE);
            joyStickMoveFire.setVisibility(View.INVISIBLE);
        }

        lifeText.setVisibility(View.INVISIBLE);
        bulletCountText.setVisibility(View.INVISIBLE);
        roomsCountText.setVisibility(View.INVISIBLE);
        enemyDeadCountText.setVisibility(View.INVISIBLE);
        thisBulletCountText.setVisibility(View.INVISIBLE);

        findViewById(R.id.touchThingOnMap).setVisibility(View.INVISIBLE);
        findViewById(R.id.reloadGun).setVisibility(View.INVISIBLE);
        findViewById(R.id.inventoryButton).setVisibility(View.INVISIBLE);

        // показ указателей загрузки уровня
        loadLvl.setVisibility(View.VISIBLE);
        loadLvlTextView.setVisibility(View.VISIBLE);
        whatLoad.setVisibility(View.VISIBLE);
    }

    // все элементы игры видимы
    public void setVisibleAll() {
        gameLayout.setVisibility(View.VISIBLE);
        gameView.lifeIcon.setVisibility(View.VISIBLE);
        gameView.bulletIcon.setVisibility(View.VISIBLE);
        gameView.roomsIcon.setVisibility(View.VISIBLE);
        gameView.deadEnemyIcon.setVisibility(View.VISIBLE);
        gunImageView.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        if (gameView.getPause()) {
            resumeButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
        }
        joystick.setVisibility(View.VISIBLE);
        joystickMove.setVisibility(View.VISIBLE);
        if (MainActivity.getControlFire() == 0) {
            joystickFire.setVisibility(View.VISIBLE);
            joyStickMoveFire.setVisibility(View.VISIBLE);
        }

        lifeText.setVisibility(View.VISIBLE);
        bulletCountText.setVisibility(View.VISIBLE);
        roomsCountText.setVisibility(View.VISIBLE);
        enemyDeadCountText.setVisibility(View.VISIBLE);
        thisBulletCountText.setVisibility(View.VISIBLE);

        findViewById(R.id.touchThingOnMap).setVisibility(View.VISIBLE);
        findViewById(R.id.reloadGun).setVisibility(View.VISIBLE);
        findViewById(R.id.inventoryButton).setVisibility(View.VISIBLE);

        loadLvl.setVisibility(View.INVISIBLE);
        loadLvlTextView.setVisibility(View.INVISIBLE);
        whatLoad.setVisibility(View.INVISIBLE);
    }

    /**
     * Экран загрузки нового уровня
     * @param progressLoad индикатор загрузки (должен быть с каждым вызовом больше и необходимы начальное заполнение 0 и конечным 100)
     */
    public void loadGame(int progressLoad, String whatLoadStr) {
        if (progressLoad == 0) {
            setInvisibleAll();
        }

        loadLvl.setProgress(progressLoad);
        loadLvlTextView.setText(String.valueOf(progressLoad) + "%");
        whatLoad.setText(whatLoadStr);

        if (progressLoad == 100) {
            setVisibleAll();
        }
    }

    public void takeThingOnMap(View view) {
        view.startAnimation(animation);
        otherThings = Geometric_Calculate.getOtherThings(gameView);
        float minDest = 10000, dest;
        int thingTake = -1;
        for (int i = 0; i < otherThings.size(); i++) {
            dest = Geometric_Calculate.destination(otherThings.get(i), gameView.getPlayer());
            if (dest < minDest * minDest) {
                thingTake = i;
                minDest = dest;
            }
        }
        if (thingTake != -1) {
            gameView.startTimeAddInventory();
            addInInventory.setImageBitmap(otherThings.get(thingTake).getBitmap());
            if (otherThings.get(thingTake) instanceof Gun) {
                if (((Player) gameView.getPlayer()).getInventory().getFirstGun() == null) {
                    ((Player) gameView.getPlayer()).getInventory().setFirstGun(otherThings.get(thingTake));
                    gameView.riseThing(otherThings.get(thingTake));
                    return;
                } else if (((Player) gameView.getPlayer()).getInventory().getSecondGun() == null) {
                    ((Player) gameView.getPlayer()).getInventory().setSecondGun(otherThings.get(thingTake));
                    gameView.riseThing(otherThings.get(thingTake));
                    return;
                }
            }
            if (((Player) gameView.getPlayer()).getInventory().addSubject(otherThings.get(thingTake)))
                gameView.riseThing(otherThings.get(thingTake));
        }
    }

    public void reloadGun(View view) {
        view.startAnimation(animation);
        if (gameView.fireGun == 0 && ((Player) gameView.getPlayer()).getInventory().getFirstGun() != null) {
             ((Player) gameView.getPlayer()).reload(true);
        } else if (gameView.fireGun == 1 && ((Player) gameView.getPlayer()).getInventory().getSecondGun() != null) {
            ((Player) gameView.getPlayer()).reload(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        openMenu(findViewById(R.id.menuButton));
    }

    @Override
    public void onBackPressed() {
        openMenu(findViewById(R.id.menuButton));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
                gameView.setPortrait();
            else
                gameView.setLandscape();
        }
        catch (Exception e) {
            Log.d(TAG, "onConfigurationChanged: " + e);
        }
    }

    /**
     * Расстояние между точками
     * @param x1 аюсцисса первой точки
     * @param y1 ордината пепрвой точки
     * @param x2 абсцисса второй точки
     * @param y2 ордината второй точки
     * @return расстояние между точками
     */
    public float destination(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2)));
    }

    public int getHeightScrollInventory() {
        ScrollView scrollInventory = (ScrollView) findViewById(R.id.scrollInventory);
        return scrollInventory.getHeight();
    }
}