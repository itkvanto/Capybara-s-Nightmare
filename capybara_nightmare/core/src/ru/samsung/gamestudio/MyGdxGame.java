package ru.capybara.nightmare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import ru.capybara.nightmare.managers.AudioManager;
import ru.capybara.nightmare.screens.GameScreen;
import ru.capybara.nightmare.screens.MenuScreen;
import ru.capybara.nightmare.screens.SettingsScreen;

import static ru.capybara.nightmare.GameSettings.*;

public class MyGdxGame extends Game {

    public World world;

    public BitmapFont largeWhiteFont;
    public BitmapFont commonWhiteFont;
    public BitmapFont commonBlackFont;

    public Vector3 touch;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public AudioManager audioManager;

    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public SettingsScreen settingsScreen;

    float accumulator = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        touch = new Vector3();
        world = new World(GameSettings.GRAVITY, true);

        // Initialize fonts
        commonWhiteFont = new BitmapFont();
        commonWhiteFont.getData().setScale(2f);
        commonWhiteFont.setColor(Color.WHITE);

        commonBlackFont = new BitmapFont();
        commonBlackFont.getData().setScale(2f);
        commonBlackFont.setColor(Color.BLACK);

        largeWhiteFont = new BitmapFont();
        largeWhiteFont.getData().setScale(3f);
        largeWhiteFont.setColor(Color.WHITE);

        audioManager = new AudioManager();
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        settingsScreen = new SettingsScreen(this);
        setScreen(menuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        commonWhiteFont.dispose();
        commonBlackFont.dispose();
        largeWhiteFont.dispose();
        world.dispose();
        audioManager.dispose();
    }

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
}
