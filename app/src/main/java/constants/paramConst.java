package constants;

/**
 * константы для параметров игры
 */
public class paramConst {
    // желательный фпс игры
    public final static int MAX_FPS = 25;
    // макс. кол-во кадров которые можно пропустить
    public final static int MAX_FRAME_SKIPS = 5;
    // период, которые занимает кадр (обновления и рисование)
    public final static int FRAME_PERIOD = 1000 / MAX_FPS;

    // фпс анимации объектов на карте
    public final static int FPS_ANIMATION_OBJECTS = 10;

    // кол-во урона игроку от врага
    public final static int damageEnemy = 20;

    // кол-во здоровья восполняемого аптечкой
    public final static int addLive = 15;

    // получение параметров из настроек
    public static final String PREFS_FILE = "account";

    // задержка между выстрелами
    public static final int coolDownFireGun = 10; // адержка для стрельбы из пистолета
    public static final int coolDownFireAK = 5; // задержка для стрельбы из AK
    public static final int coolDownFireMachineGan = 2; // задержка стрельбы из пулемёта

    // урон различного оружия
    public final static int damageFireGun = 10; // урон пистолета
    public final static int damageAK = 20; // урон автомата
    public final static int damageMachineGun = 30; // урон пулемёта

    // максимальный угол разброса для разных оружий
    public final static int maxAngleFireGun = 7; // угол отклона пистолета
    public final static int maxAngleAK = 9; // угол отклона автомата
    public final static int maxAngleMachineGun = 11; // угол отклона пулемёта

    // из какой активиты был сделан вызов
    public final static int MAIN_ACTIVITY = 0;
    public final static int DIFFICULTY_WINDOW = 1;
    public final static int RECORD_WINDOW = 2;
    public final static int GAME_SETTING_WINDOW =3;
    public final static int IMAGE_WINDOW = 4;
    public final static int SETTINGS_WINDOW = 5;
    public final static int SOUND_SETTING = 6;
    public final static int YOUR_SETTING_WINDOW = 7;

    /**
     * Набор стандартных параметров для звука
     */
    public static final int soundAll = 60; // общий звук
    public static final int soundMusic = 60; // громкость фоновой музыки
    public static final int soundFire = 60; // звук выстрела

    /**
     * Параметры игры
     */
    public static final int countrolFire = 0; // способ стрельбы
    public static final int updateEnemy = 0; // способ обновления состояния врагов

    /**
     * Наборы параметров для игры
     */
    // лёгкая игра
    public static final int easyLifePlayer = 5;
    public static final int easyBulletCount = 180;
    public static final int easyMaxMedkit = 6;
    public static final int easyMaxAmmo = 6;
    public static final int easyStartEnemy = 30;
    public static final int easyEnemyLife = 5;
    public static final int easyAddNextEnemy = 2;
    public static final int easySpeed = 14;

    // нормальная игра
    public static final int normalLifePlayer = 4;
    public static final int normalBulletCount = 150;
    public static final int normalMaxMedkit = 4;
    public static final int normalMaxAmmo = 4;
    public static final int normalStartEnemy = 40;
    public static final int normalEnemyLife = 6;
    public static final int normalAddNextEnemy = 2;
    public static final int normalSpeed = 12;

    // сложная игра
    public static final int hardLifePlayer = 3;
    public static final int hardBulletCount = 120;
    public static final int hardMaxMedkit = 3;
    public static final int hardMaxAmmo = 3;
    public static final int hardStartEnemy = 50;
    public static final int hardEnemyLife = 7;
    public static final int hardAddNextEnemy = 3;
    public static final int hardSpeed = 10;

    // произвольные параметры
    // минимальные
    public static final int randomMinLifePlayer = 2;
    public static final int randomMinBulletCount = 120;
    public static final int randomMinMaxMedkit = 3;
    public static final int randomMinMaxAmmo = 3;
    public static final int randomMinStartEnemy = 30;
    public static final int randomMinEnemyLife = 4;
    public static final int randomMinAddNextEnemy = 1;
    public static final int randomMinSpeed = 8;
    // диапозон
    public static final int randomDiapLifePlayer = 3;
    public static final int randomDiapBulletCount = 60;
    public static final int randomDiapMaxMedkit = 3;
    public static final int randomDiapMaxAmmo = 3;
    public static final int randomDiapStartEnemy = 20;
    public static final int randomDiapEnemyLife = 3;
    public static final int randomDiapAddNextEnemy = 2;
    public static final int randomDiapSpeed = 8;
}