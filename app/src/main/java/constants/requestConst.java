package constants;

/**
 * коды для получения результата
 */
public class requestConst {
    // получение параметров
    public static final int REQUEST_PARAM = 1;

    // получения приближения карты игры
    public static final int REQUEST_ZOOM = 2;

    // получение текстур для картинок
    public static final int GALLERY_REQUEST_PLAYER = 3; // текстура игрока
    public static final int GALLERY_REQUEST_ENEMY = 4; // текстура врага
    public static final int GALLERY_REQUEST_WALL = 5; // текстура стены
    public static final int GALLERY_REQUEST_BULLET = 6; // текстура патрона
    public static final int GALLERY_REQUEST_MEDKIT = 7; // текстура аптечки
    public static final int GALLERY_REQUEST_AMMO = 8; // текстура коробки с боеприпасами
    public static final int GALLERY_REQUEST_PORTAL = 9; // текстура портала
    public static final int GALLERY_REQUEST_ENEMY_DEAD = 10; // враг умер
    public static final int GALLERY_REQUEST_ENEMY_TRIG = 11; // враг заметил игрока

    // получение параметров громкости
    public static final int REQUEST_PARAM_SOUND = 12;

    // получение параметров игры
    public static final int REQUEST_PARAM_GAME = 13;

    // получение макисмальной комнаты в забеге
    public static final int REQUEST_MAX_ROOM_KILL = 14;
}