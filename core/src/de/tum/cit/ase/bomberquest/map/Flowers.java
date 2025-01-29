package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */
public class Flowers implements Drawable {
    
    private final int x;
    private final int y;

    /**
     * Instantiates the flower object, with its x and y coordinates.
     * @param x is the x coordinate position
     * @param y is the y coordinate position
     * Exit will be placed randomly, in the GameMap Class.
     */
    public Flowers(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public TextureRegion getCurrentAppearance() {
        if(BomberQuestGame.level == 2){
            return Textures.BLUEFLOWERS;

        } else if(BomberQuestGame.level == 3){
            return Textures.REDFLOWERS;
        }
        return Textures.FLOWERS;

    }


    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }

    @Override
    public void destroy() {
    }
}
