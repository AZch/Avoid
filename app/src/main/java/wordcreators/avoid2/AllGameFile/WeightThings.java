package wordcreators.avoid2.AllGameFile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ScrollView;

import java.util.ArrayList;

import wordcreators.avoid2.R;

/**
 * масса всех вещей по id
 */
public class WeightThings {
    private ArrayList<Integer> massThings = new ArrayList<>();

    private ArrayList<Bitmap> bitmapThings = new ArrayList<>();

    public WeightThings(Context context, int heightInventory) {
        setMassThings();
        setBitmapThings(context, heightInventory);
    }

    private void setMassThings() {
        massThings.add(10); // масса аптечки
        massThings.add(5); // масса боеприпасов
        massThings.add(15); // масса пистолета
        massThings.add(25); // масса автомата
        massThings.add(30); // масса пулемёта
    }

    private void setBitmapThings(Context context, int heightInventory) {
        Bitmap setBM = null;
        setBM = BitmapFactory.decodeResource(context.getResources(), R.drawable.medkit);
        bitmapThings.add(Bitmap.createScaledBitmap(setBM, setBM.getWidth() / (setBM.getHeight() / (heightInventory / 5)), heightInventory / 5, false)); // аптечка

        setBM = BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo);
        bitmapThings.add(Bitmap.createScaledBitmap(setBM, setBM.getWidth() / (setBM.getHeight() / (heightInventory / 5)), heightInventory / 5, false)); // боеприпасы

        setBM = BitmapFactory.decodeResource(context.getResources(), R.drawable.gun);
        bitmapThings.add(Bitmap.createScaledBitmap(setBM, setBM.getWidth() / (setBM.getHeight() / (heightInventory / 5)), heightInventory / 5, false)); // пистолет

        setBM = BitmapFactory.decodeResource(context.getResources(), R.drawable.automatic_gun);
        bitmapThings.add(Bitmap.createScaledBitmap(setBM, setBM.getWidth() / (setBM.getHeight() / (heightInventory / 5)), heightInventory / 5, false)); // автомат

        setBM = BitmapFactory.decodeResource(context.getResources(), R.drawable.machine_gun);
        bitmapThings.add(Bitmap.createScaledBitmap(setBM, setBM.getWidth() / (setBM.getHeight() / (heightInventory / 5)), heightInventory / 5, false)); // пулемёт
    }

    // возвращяет массу вещи по её индексу, если вещь не найдена, то вернёт -1
    public int getMassThings(int id) {
        if (id >= massThings.size())
            return -1;
        else
            return massThings.get(id);
    }

    // возвращяет изображение вещи по индексу, если вещь не найдена, то -1
    public Bitmap getBitmapThings(int id) {
        if (id >= bitmapThings.size())
            return null;
        else
            return bitmapThings.get(id);
    }

    public float getHeightOneThing() {
        return bitmapThings.get(0).getHeight();
    }
}
