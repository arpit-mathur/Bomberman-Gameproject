package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class DestructibleWall extends IndestructibleWall {
    /**
     * Create a chest at the given position.
     *
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */
    public DestructibleWall(World world, float x, float y) {
        super(world, x, y);
    }
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.DESTRUCTIBLEWALL;
    }
}
