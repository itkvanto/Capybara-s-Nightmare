package ru.capybara.nightmare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameResources {
    private static GameResources instance;
    private Texture backgroundTexture;
    private Texture blackoutFullTexture;
    private Texture blackoutTopTexture;
    private Texture blackoutMiddleTexture;
    private Texture buttonShortBgTexture;
    private Texture buttonLongBgTexture;
    private Texture pauseIconTexture;
    private Texture liveTexture;
    private Texture bulletTexture;
    private Texture shipTexture;
    private Texture squareTexture;
    private Texture powerUpTexture;
    private BitmapFont font;

    // Images for textures
    public static final String BACKGROUND_IMG_PATH = "textures/background.png";
    public static final String BLACKOUT_FULL_IMG_PATH = "textures/blackout_full.png";
    public static final String BLACKOUT_TOP_IMG_PATH = "textures/blackout_top.png";
    public static final String BLACKOUT_MIDDLE_IMG_PATH = "textures/blackout_middle.png";
    public static final String BUTTON_SHORT_BG_IMG_PATH = "textures/button_background_short.png";
    public static final String BUTTON_LONG_BG_IMG_PATH = "textures/button_background_long.png";
    public static final String PAUSE_IMG_PATH = "textures/pause_icon.png";
    public static final String LIVE_IMG_PATH = "textures/life.png";
    public static final String BULLET_IMG_PATH = "textures/bullet.png";
    public static  String SHIP_IMG_PATH = "textures/capy.png";
    public static final String SQUARE_IMG_PATH = "textures/ghost.png";

    public static final String BACKGROUND_MUSIC_PATH = "sounds/background_music.mp3";
    public static final String DESTROY_SOUND_PATH = "sounds/destroy.mp3";
    public static final String SHOOT_SOUND_PATH = "sounds/shoot.mp3";


    private GameResources() {
        loadTextures();
        loadFont();
    }

    public static GameResources getInstance() {
        if (instance == null) {
            instance = new GameResources();
        }
        return instance;
    }

    private void loadTextures() {
        backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_IMG_PATH));
        blackoutFullTexture = new Texture(Gdx.files.internal(BLACKOUT_FULL_IMG_PATH));
        blackoutTopTexture = new Texture(Gdx.files.internal(BLACKOUT_TOP_IMG_PATH));
        blackoutMiddleTexture = new Texture(Gdx.files.internal(BLACKOUT_MIDDLE_IMG_PATH));
        buttonShortBgTexture = new Texture(Gdx.files.internal(BUTTON_SHORT_BG_IMG_PATH));
        buttonLongBgTexture = new Texture(Gdx.files.internal(BUTTON_LONG_BG_IMG_PATH));
        pauseIconTexture = new Texture(Gdx.files.internal(PAUSE_IMG_PATH));
        liveTexture = new Texture(Gdx.files.internal(LIVE_IMG_PATH));
        bulletTexture = new Texture(Gdx.files.internal(BULLET_IMG_PATH));
        shipTexture = new Texture(Gdx.files.internal(SHIP_IMG_PATH));
        squareTexture = new Texture(Gdx.files.internal(SQUARE_IMG_PATH));
    }

    private void loadFont() {
        font = new BitmapFont();
        font.getData().setScale(2f);
    }

    public Texture getBackgroundTexture() { return backgroundTexture; }
    public Texture getBlackoutFullTexture() { return blackoutFullTexture; }
    public Texture getBlackoutTopTexture() { return blackoutTopTexture; }
    public Texture getBlackoutMiddleTexture() { return blackoutMiddleTexture; }
    public Texture getButtonShortBgTexture() { return buttonShortBgTexture; }
    public Texture getButtonLongBgTexture() { return buttonLongBgTexture; }
    public Texture getPauseIconTexture() { return pauseIconTexture; }
    public Texture getLiveTexture() { return liveTexture; }
    public Texture getBulletTexture() { return bulletTexture; }
    public Texture getShipTexture() { return shipTexture; }
    public Texture getSquareTexture() { return squareTexture; }
    public Texture getPowerUpTexture() { return powerUpTexture; }
    public BitmapFont getFont() { return font; }

    public void dispose() {
        backgroundTexture.dispose();
        blackoutFullTexture.dispose();
        blackoutTopTexture.dispose();
        blackoutMiddleTexture.dispose();
        buttonShortBgTexture.dispose();
        buttonLongBgTexture.dispose();
        pauseIconTexture.dispose();
        liveTexture.dispose();
        bulletTexture.dispose();
        shipTexture.dispose();
        squareTexture.dispose();
        powerUpTexture.dispose();
        font.dispose();
    }
}
