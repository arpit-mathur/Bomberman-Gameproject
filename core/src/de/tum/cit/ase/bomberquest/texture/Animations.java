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
     * The animation for the bomb blast with bigger radius.
     */
    public static final Animation<TextureRegion> BOMB_BLAST_LONG = new Animation<>(0.05f,
            SpriteSheet.BOMB_BLAST_LONG.at(5, 1),
            SpriteSheet.BOMB_BLAST_LONG.at(5, 6),
            SpriteSheet.BOMB_BLAST_LONG.at(10, 1),
            SpriteSheet.BOMB_BLAST_LONG.at(10, 6),
            SpriteSheet.BOMB_BLAST_LONG.at(10, 1),
            SpriteSheet.BOMB_BLAST_LONG.at(5, 6),
            SpriteSheet.BOMB_BLAST_LONG.at(5, 1),
            SpriteSheet.BOMB_BLAST_LONG.at(10, 11)
    );

    /**
     * The animation for the bomb blast with bigger radius.
     */
    public static final Animation<TextureRegion> BOMB_BLAST_DEFAULT = new Animation<>(0.05f,
            SpriteSheet.BOMB_BLAST_SHORT.at(6, 2),
            SpriteSheet.BOMB_BLAST_SHORT.at(6, 7),
            SpriteSheet.BOMB_BLAST_SHORT.at(11, 2),
            SpriteSheet.BOMB_BLAST_SHORT.at(11, 7),
            SpriteSheet.BOMB_BLAST_SHORT.at(11, 2),
            SpriteSheet.BOMB_BLAST_SHORT.at(6, 7),
            SpriteSheet.BOMB_BLAST_SHORT.at(6, 2),
            SpriteSheet.BOMB_BLAST_SHORT.at(10, 11)
    );


    /// Added animation of breaking the wall (Aryan)
    public static final Animation<TextureRegion> DESTROY_WALL = new Animation<>(0.2f,
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


}
