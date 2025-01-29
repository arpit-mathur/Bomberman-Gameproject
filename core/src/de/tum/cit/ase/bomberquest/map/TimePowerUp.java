package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * This PowerUp will increase the time the player has.
 */
public class TimePowerUp extends Exit{

    /**
     * Attribute to manage if power is taken or not.
     */
    private boolean powerTaken;


    public TimePowerUp(World world, float x, float y) {
        super(world, x, y);
        this.powerTaken = false;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if(!isPowerTaken()) {
            return Textures.TIMER_POWER_UP;
        }
        else{
            return null;
        }
    }

    public boolean isPowerTaken() {
        return powerTaken;
    }

    public void setPowerTaken(boolean powerTaken) {
        this.powerTaken = powerTaken;
    }

}

