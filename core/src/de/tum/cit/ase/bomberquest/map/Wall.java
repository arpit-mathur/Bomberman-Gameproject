package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class Wall extends Chest  {
    /**
     * Wall extends Chest as it functions as a Hitbox too.
     * Create a Wall at the given position.
     *
     * @param world The Box2D world to add the wall's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */
    public Wall(World world, float x, float y) {
        super(world, x, y);
    }
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.WALL;
    }

}
