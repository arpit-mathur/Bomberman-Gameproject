package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(9, 1);

    public static final TextureRegion CHEST = SpriteSheet.OBJECTS.at(4, 12);

    public static final TextureRegion WALL = SpriteSheet.OBJECTS.at(4, 4);

    public static final TextureRegion WALL2 = SpriteSheet.OBJECTS.at(4, 5);
    
}
