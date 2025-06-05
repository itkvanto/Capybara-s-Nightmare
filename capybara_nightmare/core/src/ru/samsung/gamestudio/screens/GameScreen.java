package ru.capybara.nightmare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.capybara.nightmare.*;
import ru.capybara.nightmare.components.*;
import ru.capybara.nightmare.managers.ContactManager;
import ru.capybara.nightmare.managers.MemoryManager;
import ru.capybara.nightmare.objects.*;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    GameSession gameSession;
    ShipObject shipObject;

    ArrayList<ColoredSquare> squaresArray;
    ArrayList<BulletObject> bulletArray;
    ArrayList<UpgradeObject> upgradeArray;
    private int currentWave;
    private float waveTimer;
    private static final float WAVE_DURATION = 30f; // 30 seconds per wave
    private static final int SQUARES_PER_WAVE = 10;
    private static final int UPGRADE_SCORE_THRESHOLD = 400; // Каждые 200 очков
    private int lastUpgradeScore = 0;
    private Random random;

    ContactManager contactManager;

    // PLAY state UI
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    TextView waveTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        random = new Random();

        contactManager = new ContactManager(myGdxGame.world);

        squaresArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        upgradeArray = new ArrayList<>();
        currentWave = 1;
        waveTimer = WAVE_DURATION;

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        waveTextView = new TextView(myGdxGame.commonWhiteFont, 400, 1215);
        pauseButton = new ButtonView(
                605, 1200,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 282, 842, "Pause");
        homeButton = new ButtonView(
                138, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                393, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {
        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            updateWave(delta);
            spawnSquares();
            checkAndSpawnUpgrade();

            if (shipObject.needToShoot()) {
                int bulletCount = shipObject.getBulletCount();
                for (int i = 0; i < bulletCount; i++) {
                    float offset = (i - (bulletCount - 1) / 2f) * 20f;
                    BulletObject laserBullet = new BulletObject(
                            shipObject.getX() + offset, shipObject.getY() + shipObject.height / 2,
                            GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                            GameResources.BULLET_IMG_PATH,
                            myGdxGame.world
                    );
                    bulletArray.add(laserBullet);
                }
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }

            if (!shipObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateSquares();
            updateBullets();
            updateUpgrades();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            waveTextView.setText("Wave: " + currentWave);
            liveView.setLeftLives(shipObject.getLiveLeft());

            myGdxGame.stepWorld();
        }

        draw();
    }

    private void updateWave(float delta) {
        waveTimer -= delta;
        if (waveTimer <= 0) {
            currentWave++;
            waveTimer = WAVE_DURATION;
        }
    }

    private void spawnSquares() {
        if (squaresArray.size() < SQUARES_PER_WAVE) {
            float x = random.nextFloat() * (GameSettings.SCREEN_WIDTH - 100);
            float y = GameSettings.SCREEN_HEIGHT;
            ColoredSquare square = new ColoredSquare(x, y, currentWave, myGdxGame.world);
            squaresArray.add(square);
        }
    }

    private void checkAndSpawnUpgrade() {
        int currentScore = gameSession.getScore();
        if (currentScore >= lastUpgradeScore + UPGRADE_SCORE_THRESHOLD) {
            lastUpgradeScore = currentScore;
            float x = random.nextFloat() * (GameSettings.SCREEN_WIDTH - 50);
            float y = GameSettings.SCREEN_HEIGHT;
            UpgradeObject upgrade = new UpgradeObject(x, y, myGdxGame.world);
            upgradeArray.add(upgrade);
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        for (ColoredSquare square : squaresArray) square.draw(myGdxGame.batch);
        for (UpgradeObject upgrade : upgradeArray) upgrade.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        waveTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    private void updateSquares() {
        for (int i = 0; i < squaresArray.size(); i++) {
            ColoredSquare square = squaresArray.get(i);
            square.update(Gdx.graphics.getDeltaTime());

            if (square.isOutOfBounds(GameSettings.SCREEN_HEIGHT) || square.isDestroyed()) {
                myGdxGame.world.destroyBody(square.body);
                squaresArray.remove(i--);
                if (square.isOutOfBounds(GameSettings.SCREEN_HEIGHT)) {
                    shipObject.hit();
                }
            }
        }
    }

    private void updateBullets() {
        for (int i = 0; i < bulletArray.size(); i++) {
            BulletObject bullet = bulletArray.get(i);
            if (bullet.hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(bullet.body);
                bulletArray.remove(i--);
            }
        }
    }

    private void updateUpgrades() {
        for (int i = 0; i < upgradeArray.size(); i++) {
            UpgradeObject upgrade = upgradeArray.get(i);
            upgrade.update(Gdx.graphics.getDeltaTime());

            if (upgrade.isOutOfBounds(GameSettings.SCREEN_HEIGHT) || upgrade.hit()) {
                if (upgrade.hit()) {
                    shipObject.upgradeBulletCount();
                }
                myGdxGame.world.destroyBody(upgrade.body);
                upgradeArray.remove(i--);
            }
        }
    }

    private void restartGame() {
        for (ColoredSquare square : squaresArray) {
            myGdxGame.world.destroyBody(square.body);
        }
        squaresArray.clear();

        for (UpgradeObject upgrade : upgradeArray) {
            myGdxGame.world.destroyBody(upgrade.body);
        }
        upgradeArray.clear();

        for (BulletObject bullet : bulletArray) {
            myGdxGame.world.destroyBody(bullet.body);
        }
        bulletArray.clear();

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        currentWave = 1;
        waveTimer = WAVE_DURATION;
        lastUpgradeScore = 0;
        gameSession.restartGame();
    }
}
