package models.Resource;

import models.Objects;
import wordcreators.avoid2.AllGameFile.GameView;

/**
 * Ресурсы
 */
public abstract class Resource extends Objects {

    private int id = 0;

    private int count = 0;

    // указатель использования
    private boolean used = false;

    /**
     * конструктор ресурсов
     * @param x абсцисса
     * @param y ордината
     * @param gameView игровое поле
     */
    public Resource(float x, float y, float spriteH, float spriteW, GameView gameView, int id, int count) {
        super(x, y, spriteH, spriteW, gameView);
        this.id = id;
        this.count = count;
    }


    /**
     * получить состояние ресурса (использован / не использован)
     * @return состояние
     */
    public boolean getUsed() { return used; }

    /**
     * Делает ресурс использованным
     */
    protected void setUsed() {

        if (count == 1) {
            used = true;
            count--;
        }
        else
            count--;
    }

    public void useFalse() {
        used = false;
    }

    protected void useTrue() {
        used = true;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void decrCount() { count--; }

    public void incrCount() { count++; }

    public void raise() {
        used = true;
    }

    public abstract void use();

    public void drop(float newX, float newY) {
        used = false;
        setX(newX);
        setY(newY);
    }
}