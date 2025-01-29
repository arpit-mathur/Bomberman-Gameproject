package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.w3c.dom.Text;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(9,2);

    public static final TextureRegion EXIT = SpriteSheet.ORIGINAL_OBJECTS.at(4, 12);

    public static final TextureRegion CB_POWERUP = SpriteSheet.ORIGINAL_OBJECTS.at(15, 1);

    public static final TextureRegion BR_POWERUP = SpriteSheet.ORIGINAL_OBJECTS.at(15, 2);

    public static final TextureRegion INDESTRUCTIBLEWALL = SpriteSheet.ORIGINAL_OBJECTS.at(4, 4);

    public static final TextureRegion DESTRUCTIBLEWALL = SpriteSheet.ORIGINAL_OBJECTS.at(4, 5);

    public static final TextureRegion SPEEDPOWERUP = SpriteSheet.ORIGINAL_OBJECTS.at(15, 4);

    public static final TextureRegion BLUEFLOWERS = SpriteSheet.ACTUAL_BASIC_TILES.at(2, 6);

    public static final TextureRegion REDFLOWERS = SpriteSheet.ACTUAL_BASIC_TILES.at(10, 3);

    public static final TextureRegion REDWALLS = SpriteSheet.ACTUAL_BASIC_TILES.at(1, 6);

    public static final TextureRegion BLUEWALLS = SpriteSheet.ACTUAL_BASIC_TILES.at(1, 2);

    public static final TextureRegion REDINDESTRUCTIBLEWALLS = SpriteSheet.ACTUAL_BASIC_TILES.at(1,5);

    public static final TextureRegion BlUE_INDESTRUCTIBLE_WALLS = SpriteSheet.ACTUAL_BASIC_TILES.at(1, 1);

}

