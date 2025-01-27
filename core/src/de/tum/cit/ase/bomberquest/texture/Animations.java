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
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.075f,
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 5)
    );
    /**
     * The animation for the character walking left.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.075f,
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(1, 2)
    );
    /**
     * The animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.075f,
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 5)
    );
    /**
     * The animation for the character walking right.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.075f,
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(2, 2)
    );
    /**
     * The animation for the character's demise.
     */
    public static final Animation<TextureRegion> CHARACTER_DEMISE = new Animation<>(0.3f,
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
    public static final Animation<TextureRegion> BOMB_TICKING = new Animation<>(0.2f,
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(4, 2)
    );

    /**
     * The animation for the center of bomb explosion
     */
    public static final Animation<TextureRegion> EXPLOSION_CENTER = new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 3)
    );

    /**
     * The animation for the explosion's right-end
     */
    public static final Animation<TextureRegion> EXPLOSION_RIGHT_END= new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 10),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 10),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 10),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 5)
    );

    /**
     * The animation for the explosion's left-end
     */
    public static final Animation<TextureRegion> EXPLOSION_LEFT_END= new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 1)
    );

    /**
     * The animation for the explosion's Top-end
     */
    public static final Animation<TextureRegion> EXPLOSION_TOP_END = new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(5, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(5, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(10, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(10, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(10, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(5, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(5, 3)
    );

    /**
     * The animation for the explosion's bottom end
     */
    public static final Animation<TextureRegion> EXPLOSION_BOTTOM_END = new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(9, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(9, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(14, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(14, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(14, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(9, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(9, 3)
    );

    /**
     * The animation for the explosion's vertical part
     */
    public static final Animation<TextureRegion> EXPLOSION_VERTICAL = new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(6, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(6, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(11, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(11, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(11, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(6, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(6, 3)
    );

    /**
     * The animation for the explosion's horizontal part
     */
    public static final Animation<TextureRegion> EXPLOSION_HORIZONTAL = new Animation<>(0.05f,
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(12, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(7, 2)
    );

    /// Added animation of breaking the wall (Aryan)
    public static final Animation<TextureRegion> DESTROY_WALL = new Animation<>(0.07f,
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

    /**
     * The animation for the enemy's demise.
     */
    public static final Animation<TextureRegion> ENEMY_DEMISE = new Animation<>(0.25f,
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 7),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 8),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 9),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 10),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 11),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 12)
    );

    public static final Animation<TextureRegion> ENEMY_MOVING_RIGHT = new Animation<>(0.25f,
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 1),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 3),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 2),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 1)
    );
    public static final Animation<TextureRegion> ENEMY_MOVING_LEFT = new Animation<>(0.25f,
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 4),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 6),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 5),
            SpriteSheet.ORIGINAL_OBJECTS.at(16, 4)
    );


}
