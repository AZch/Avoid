package models.Resource;

import wordcreators.avoid2.AllGameFile.GameView;

/**
 * Оружие
 */
public abstract class Gun extends Resource {
    private int countBulletInGun = 0;

    private int maxBulletInGun = 0;

    private int timeToReload = 0;

    protected int timeReload = 0;

    private int damage = 0;

    private boolean shot = false;

    private boolean reload = false;

    private int coolDownFire = 0;

    private int changeCoolDownFire = 0;

    private int maxAngle = 0;

    /**
     * конструктор ресурсов
     *
     * @param x        абсцисса
     * @param y        ордината
     * @param spriteH
     * @param spriteW
     * @param gameView игровое поле
     * @param id
     * @param count
     */
    public Gun(float x, float y, float spriteH, float spriteW, GameView gameView, int id, int count, int maxBulletInGun, int timeToReload, int damage, int coolDownFire, int maxAngle) {
        super(x, y, spriteH, spriteW, gameView, id, count);
        this.maxBulletInGun = maxBulletInGun;
        this.timeToReload = timeToReload;
        this.damage = damage;
        this.coolDownFire = coolDownFire;
        this.maxAngle = maxAngle;
    }

    public int getMaxBulletInGun() {
        return maxBulletInGun;
    }

    public boolean getShot() {
        return shot;
    }

    public int getMaxAngle() { return maxAngle; }

    public boolean getReload() {
        return reload;
    }

    public int getDamage() {
        return damage;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public int getCoolDownFire() {
        return changeCoolDownFire;
    }

    public void setCoolDownFire() {
        changeCoolDownFire = coolDownFire;
    }

    public void decrTimeReload() {
        timeReload--;
    }

    public void decrCoolDownFire() {
        if (changeCoolDownFire > 0)
            changeCoolDownFire--;
    }

    public void reloaded() {
        changeCoolDownFire = 0;
        reload = false;
    }

    /**
     * Добавление патрон в обойму
     * @param countAddBullet кол-во патрон для добавления
     * @return кол-во пуль, не вместившихся в обойму
     */
    private int addBullet(int countAddBullet) {
        if (countAddBullet + countBulletInGun > maxBulletInGun) {
            int saveCountBulletInGun = countBulletInGun;
            countBulletInGun = maxBulletInGun;
            return countAddBullet + saveCountBulletInGun - maxBulletInGun;
        }

        countBulletInGun += countAddBullet;
        return 0;
    }

    public int shot(int freeBullet) {
        if (countBulletInGun > 0) {
            shot = true;
        }
        else {
            shot = false;
            freeBullet = reload(freeBullet);
        }
        return freeBullet;
    }

    public int reload(int freeBullet) {
        reload = true;
        shot = false;
        timeReload = timeToReload;
        return addBullet(freeBullet);
    }

    protected void decrBulletInGun() {
        if (countBulletInGun > 0) {
            countBulletInGun--;
            //return true;
        }
    }

    public void stopFire() {
        shot = false;
    }

    public int getCountBulletInGun() {
        return countBulletInGun;
    }
}
