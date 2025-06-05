package ru.capybara.nightmare.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.capybara.nightmare.GameSettings;

public class BulletObject extends GameObject {

    public boolean wasHit;

    public BulletObject(float x, float y, int width, int height, String texturePath, World world) {
        super(texturePath, (int)x, (int)y, width, height, GameSettings.BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(0, GameSettings.BULLET_VELOCITY));
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasHit || (getY() - height / 2 > GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public boolean hit() {
        wasHit = true;
        return true;
    }
}
