package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class BombBlastPowerUp extends Chest {
    private boolean powerTaken;

    public BombBlastPowerUp(World world, float x, float y){
        super(world,x,y);
        this.powerTaken = false;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if(!isPowerTaken()) {
            return Textures.BRPowerUp;
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
