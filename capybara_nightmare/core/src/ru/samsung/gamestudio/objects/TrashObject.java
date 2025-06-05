package ru.capybara.nightmare.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.capybara.nightmare.GameResources;
import ru.capybara.nightmare.GameSettings;

import java.util.Random;

public class TrashObject extends GameObject {

    private static final int paddingHorizontal = 30;

    private int livesLeft;

    public TrashObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.TRASH_BIT,
                world
        );

        body.setLinearVelocity(new Vector2(0, -GameSettings.TRASH_VELOCITY));
        livesLeft = 1;
    }

    @Override
    public void update(float delta) {
        setY(getY() - (int)(GameSettings.TRASH_VELOCITY * delta));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - width / 2, getY() - height / 2, width, height);
    }

    @Override
    public boolean hit() {
        return true;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

    public boolean isOutOfBounds(float screenHeight) {
        return getY() + height < 0;
    }
}
