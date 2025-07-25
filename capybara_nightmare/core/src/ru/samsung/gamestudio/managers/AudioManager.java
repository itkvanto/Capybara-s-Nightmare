package ru.capybara.nightmare.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ru.capybara.nightmare.GameResources;

public class AudioManager {

    public boolean isSoundOn;
    public boolean isMusicOn;

    public Music backgroundMusic;
    public Sound shootSound;
    public Sound explosionSound;

    public AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BACKGROUND_MUSIC_PATH));
        shootSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.SHOOT_SOUND_PATH));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.DESTROY_SOUND_PATH));

        backgroundMusic.setVolume(0.2f);
        backgroundMusic.setLooping(true);

        updateSoundFlag();
        updateMusicFlag();
    }

    public void updateSoundFlag() {
        isSoundOn = MemoryManager.loadIsSoundOn();
    }

    public void updateMusicFlag() {
        isMusicOn = MemoryManager.loadIsMusicOn();

        if (isMusicOn) backgroundMusic.play();
        else backgroundMusic.stop();
    }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        if (explosionSound != null) {
            explosionSound.dispose();
        }
        if (shootSound != null) {
            shootSound.dispose();
        }
    }

    public void toggleSound() {
        isSoundOn = !isSoundOn;
        if (isSoundOn) {
            backgroundMusic.play();
        } else {
            backgroundMusic.pause();
        }
    }

}
