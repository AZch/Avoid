package wordcreators.avoid2.AllGameFile;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import models.Objects;
import models.Resource.Ammo;
import models.Resource.Gun;
import models.Resource.Medkit;
import models.Resource.Resource;

import static android.content.ContentValues.TAG;

/**
 * Инвентарь игрока
 */
public class Inventory {

    private ArrayList<Objects> thingsObj;

    // ссылка на массу вещей
    private WeightThings weightThings;

    private int maxWeight = 50; // максимальная масса

    private int weight = 0; // имеющаяся масса

    private Objects firstGun = null;

    private Objects secondGun = null;

    public Inventory(Context context, int heightInventory) {
        // инициализация инвенторя
        thingsObj = new ArrayList<>();
        weightThings = new WeightThings(context, heightInventory);
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getWeight() {
        return weight;
    }

    public boolean setFirstGun(Objects gun) {
        if (gun instanceof Gun || gun == null) {
            firstGun = gun;
            return true;
        }
        return false;
    }

    public boolean setSecondGun(Objects gun) {
        if (gun instanceof Gun || gun == null) {
            secondGun = gun;
            return true;
        }
        return false;
    }

    public Objects getGun(int fireGun) {
        if (fireGun == 0)
            return firstGun;
        else
            return secondGun;
    }

    public boolean isPlace(Objects thing) {
        /*int weight = 0;
        for (int i = 0; i < things.size(); i++) {
            if (weight + weightThings.getMassThings(id) * count > maxWeight)
                return false;
            else
                weight += weightThings.getMassThings(things.get(i).get(0)) * things.get(i).get(1);
        }*/
        if (weight + weightThings.getMassThings(((Resource) thing).getId()) * ((Resource) thing).getCount() > maxWeight)
            return false;

        return true;
    }

    private void addThingsInInventory(Objects thing) {

        weight += weightThings.getMassThings(((Resource) thing).getId()) * ((Resource) thing).getCount();

        for (int i = 0; i < thingsObj.size(); i++)
            if (thingsObj.get(i) instanceof Medkit && thing instanceof Medkit) {
                ((Resource) thingsObj.get(i)).incrCount();
                return;
            } else if (thingsObj.get(i) instanceof Ammo && thing instanceof Ammo) {
                ((Resource) thingsObj.get(i)).incrCount();
                return;
            }
        thingsObj.add(thing);
    }

    public boolean addSubject(Objects thing) {
        // если нет места в инвенторе
        if (!isPlace(thing))
            return false;

        addThingsInInventory(thing);

        Log.d(TAG, "addSubject: " + weight);

        return true;
    }

    public ArrayList<Objects> getThings() {
        Log.d(TAG, "get: " + weight);
        return thingsObj;
    }

    public Bitmap getBitmapThingId(int id) { return weightThings.getBitmapThings(id); }

    public void removeThing(Objects thing, int idInInventory) {
        weight -= weightThings.getMassThings(((Resource) thing).getId()) * ((Resource) thing).getCount();
        //if (((Resource) thingsObj.get(idInInventory)).getCount() == 1)
            thingsObj.remove(idInInventory);
        /*else
            ((Resource) thingsObj.get(idInInventory)).decrCount();*/
        Log.d(TAG, "remove: " + weight);
    }

    public float getHeightOneThing() {
        return weightThings.getHeightOneThing();
    }

    public boolean useThing(int idInInventory) {
        if (idInInventory < 0 || idInInventory >= thingsObj.size())
            return false;
        else {
            ((Resource) thingsObj.get(idInInventory)).use();
            decrInventory(((Resource) thingsObj.get(idInInventory)).getId(), 1);
            if (((Resource) thingsObj.get(idInInventory)).getCount() == 0)
                thingsObj.remove(idInInventory);
            return true;
        }
    }

    private void decrInventory(int id, int count) {
        weight -= weightThings.getMassThings(id) * count;
    }

    public boolean replaceThing(Objects oldThing, Objects newThing, boolean yourInvent) {

        if (yourInvent) {
            if (weight - weightThings.getMassThings(((Resource) oldThing).getId()) * ((Resource) oldThing).getCount() + weightThings.getMassThings(((Resource) newThing).getId()) * ((Resource) newThing).getCount() > maxWeight)
                return false;

            removeThing(oldThing, thingsObj.indexOf(oldThing));
        }

        if (!addSubject(newThing))
            return false;

        return true;
    }

    public Objects getFirstGun() {
        return firstGun;
    }

    public Objects getSecondGun() {
        return secondGun;
    }
}