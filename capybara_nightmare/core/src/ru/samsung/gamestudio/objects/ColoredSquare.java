package ru.capybara.nightmare.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import ru.capybara.nightmare.GameResources;
import ru.capybara.nightmare.GameSettings;

public class ColoredSquare extends GameObject {
    private static final float SIZE = 100f;
    private int requiredHits;
    private final Color color;

    public ColoredSquare(float x, float y, int wave, World world) {
        super(GameResources.SQUARE_IMG_PATH, (int)x, (int)y, (int)SIZE, (int)SIZE, GameSettings.TRASH_BIT, world);
        this.requiredHits = wave;
        this.color = new Color(
            (float) Math.random(),
            (float) Math.random(),
            (float) Math.random(),
            1
        );

        body.setGravityScale(0);
        body.setType(BodyDef.BodyType.KinematicBody);
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(0, -GameSettings.SQUARE_VELOCITY);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (requiredHits > 0) {
            batch.setColor(color);
            batch.draw(texture, getX() - width / 2, getY() - height / 2, width, height);
            batch.setColor(Color.WHITE);

            GameResources.getInstance().getFont().draw(
                batch,
                String.valueOf(requiredHits),
                getX() - width/4,
                getY() + height/4
            );
        }
    }

    @Override
    public boolean hit() {
        requiredHits--;
        return requiredHits <= 0;
    }

    public boolean isOutOfBounds(float screenHeight) {
        return getY() + height < 0;
    }

    public boolean isDestroyed() {
        return requiredHits <= 0;
    }
} 