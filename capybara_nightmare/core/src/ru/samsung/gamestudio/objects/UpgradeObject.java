package ru.capybara.nightmare.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import ru.capybara.nightmare.GameResources;
import ru.capybara.nightmare.GameSettings;

public class UpgradeObject extends GameObject {
    private static final float SIZE = 50f;
    private final Color color;

    public UpgradeObject(float x, float y, World world) {
        super(GameResources.BULLET_IMG_PATH, (int)x, (int)y, (int)SIZE, (int)SIZE, GameSettings.TRASH_BIT, world);
        this.color = new Color(0f, 1f, 0f, 1f);

        body.setGravityScale(0);
        body.setType(BodyDef.BodyType.KinematicBody);
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(0, -GameSettings.SQUARE_VELOCITY);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(texture, getX() - width / 2, getY() - height / 2, width, height);
        batch.setColor(Color.WHITE);

        GameResources.getInstance().getFont().draw(
            batch,
            "+",
            getX() - width/4,
            getY() + height/4
        );
    }

    @Override
    public boolean hit() {
        return true;
    }

    public boolean isOutOfBounds(float screenHeight) {
        return getY() + height < 0;
    }
} 