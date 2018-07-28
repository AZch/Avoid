package maker;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import wordcreators.avoid2.AllMainMenu.MainActivity;
import wordcreators.avoid2.R;

/**
 * Воспроизведение звуков в игре
 */
public class Sounds {

    // звук выстрела
    private SoundPool soundPool = null;
    // id звукового файла "выстрел"
    private int idSoundFire = 0;
    // id звукового файла удар
    private int idSoundHit = 0;


    // контекст
    Context context;

    public Sounds(Context context) {
        this.context = context;
        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);

        idSoundFire = soundPool.load(context, R.raw.fire, 1);

        idSoundHit = soundPool.load(context, R.raw.contact_with_player, 2);
    }

    // воспроизведение выстрела
    public void shot() {
        try {
            soundPool.play(idSoundFire, MainActivity.getSoundFire(), MainActivity.getSoundFire(), 0, 0, 1);
        } catch (Exception e) {
            killSounds();
            new Sounds(context);
            soundPool.play(idSoundFire, MainActivity.getSoundFire(), MainActivity.getSoundFire(), 0, 0, 1);
        }
    }

    // воспроизвести звук удара
    public void Hit() {
        try {
            soundPool.play(idSoundHit, MainActivity.getSoundFire(), MainActivity.getSoundFire(), 1, 0, 1);
        } catch (Exception e) {
            killSounds();
            new Sounds(context);
            soundPool.play(idSoundHit, MainActivity.getSoundFire(), MainActivity.getSoundFire(), 1, 0, 1);
        }
    }

    public void killSounds() {
        soundPool.release();
    }
}