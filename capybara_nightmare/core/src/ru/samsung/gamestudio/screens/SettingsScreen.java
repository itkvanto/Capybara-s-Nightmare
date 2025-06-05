package ru.capybara.nightmare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.capybara.nightmare.GameResources;
import ru.capybara.nightmare.managers.MemoryManager;
import ru.capybara.nightmare.MyGdxGame;
import ru.capybara.nightmare.components.ButtonView;
import ru.capybara.nightmare.components.ImageView;
import ru.capybara.nightmare.components.MovingBackgroundView;
import ru.capybara.nightmare.components.TextView;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;

    MovingBackgroundView backgroundView;
    TextView titleTextView;
    ImageView blackoutImageView;
    ButtonView returnButton;
    TextView CapyButton;
    TextView Capy1Button;
    TextView Capy2Button;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;

    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        titleTextView = new TextView(myGdxGame.largeWhiteFont, 256, 956, "Settings");
        blackoutImageView = new ImageView(85, 365, GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        clearSettingView = new TextView(myGdxGame.commonWhiteFont, 173, 599, "clear records");

        musicSettingView = new TextView(
                myGdxGame.commonWhiteFont,
                173, 717,
                "music: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );

        soundSettingView = new TextView(
                myGdxGame.commonWhiteFont,
                173, 658,
                "sound: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );
        CapyButton = new TextView(
                myGdxGame.commonWhiteFont,
                173, 550,
                "Standard Capybara"
        );
        Capy1Button = new TextView(
                myGdxGame.commonWhiteFont,
                173, 500,
                "Chill Capybara"
        );
        Capy2Button = new TextView(
                myGdxGame.commonWhiteFont,
                173, 450,
                "Mr. Capybara"
        );

        returnButton = new ButtonView(
                280, 350,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "return"
        );

    }

    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        blackoutImageView.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        clearSettingView.draw(myGdxGame.batch);
        CapyButton.draw(myGdxGame.batch);
        Capy1Button.draw(myGdxGame.batch);
        Capy2Button.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (CapyButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                GameResources.SHIP_IMG_PATH = "textures/capy.png";
            }
            if (Capy1Button.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                GameResources.SHIP_IMG_PATH = "textures/capy1.png";
            }
            if (Capy2Button.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                GameResources.SHIP_IMG_PATH = "textures/capy2.png";

            }
            if (clearSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                clearSettingView.setText("clear records (cleared)");
            }
            if (musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                myGdxGame.audioManager.updateSoundFlag();
            }
        }
    }

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }
}
