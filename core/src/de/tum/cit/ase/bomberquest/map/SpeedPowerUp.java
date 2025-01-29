package de.tum.cit.ase.bomberquest.map;



import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * This power will increase the speed of the player. Apart from that works like the usual powerUps.
 */
public class SpeedPowerUp extends ConcurrentBombPowerUp{

    private boolean isPowerUpTaken;


    public SpeedPowerUp(World world, float x, float y) {
        super(world, x, y);
        this.isPowerUpTaken = false;

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if(!isPowerTaken()) {
            return Textures.SPEEDPOWERUP;
        }
        else{
            return null;
        }
    }

    public boolean isPowerTaken() {
        return isPowerUpTaken;
    }

    public void setPowerTaken(boolean powerTaken) {
        this.isPowerUpTaken= powerTaken;
    }


}
