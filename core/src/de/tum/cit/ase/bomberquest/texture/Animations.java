package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {

    /**
     * The animation for the character walking down.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 5)
    );
    /**
     * The animation for the character walking left.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 2)
    );
    /**
     * The animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 5)
    );
    /**
     * The animation for the character walking right.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 2)
    );
    /**
     * The animation for the character's demise.
     */
    public static final Animation<TextureRegion> CHARACTER_DEMISE = new Animation<>(0.35f,
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 8)
    );
    /**
     * The animation for the ticking Bomb.
     */
    public static final Animation<TextureRegion> BOMB_TICKING = new Animation<>(0.25f,
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 2)
    );

    /**
     * The animation for the bomb blast.
     */
    public static final Animation<TextureRegion> BOMB_BLAST = new Animation<>(0.13f,
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 15)
    );

    /// Added animation of breaking the wall (Aryan)
    public static final Animation<TextureRegion> BREAK_WALL = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 9),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 10),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 11),
            SpriteSheet.ORIGINAL_OBJECTS.at(3, 8)
    );

    public static final Animation<TextureRegion> ENEMY_ANIMATION = new Animation<>(0.13f,
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 2)
    );



}
