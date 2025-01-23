package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * A bomb is a static object with a hitbox, so the player cannot walk through it.
 */
public class Bomb implements Drawable {
    
    // We would normally get the position from the hitbox, but since we don't need to move the bomb, we can store the position directly.
    private final float x;
    private final float y;
    private float elapsedTime;
    private final Body hitbox;
    public static final float BOMB_EXPLOSION_TIME = 4;

    public static final int SMALL_EXPLOSION_RADIUS = 1;
    public static final int BIG_EXPLOSION_RADIUS = 2;

    private boolean increasedBombRadius = false;


    /**
     * Create a bomb at the given position.
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public Bomb(World world, float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox = createHitbox(world, x, y);
        this.elapsedTime = 0;
        // Since the hitbox never moves, and we never need to change it, we don't need to store a reference to it.
    }
    
    /**
     * Create a Box2D body for the bomb.
     * @param world The Box2D world to add the body to.
     */
    private Body createHitbox(World world,float x, float y) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Static bodies never move, but static bodies can collide with them.
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set the initial position of the body.
        bodyDef.position.set(x,y);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a polygon shape for the chest.
        PolygonShape box = new PolygonShape();
        // Make the polygon a square with a side length of 1 tile.
        box.setAsBox(0.4f, 0.4f);
        // Attach the shape to the body as a fixture.
        body.createFixture(box, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        box.dispose();
        // Set the chest as the user data of the body so we can look up the chest from the body later.
        body.setUserData(this);
        return body;
    }

    public void tick() {
        this.elapsedTime += 0.025f;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        /// If the bomb has exploded, show the explosion animation.
        if (elapsedTime >= BOMB_EXPLOSION_TIME) {
            destroy(); /// Deactivate the bomb's hitbox when the bomb explodes.
            /// Show the explosion animation
            return Animations.BOMB_BLAST.getKeyFrame(this.elapsedTime - BOMB_EXPLOSION_TIME, false);
        }
        /// Shows the ticking animation, looping as long as the bomb is ticking
        else if (elapsedTime < BOMB_EXPLOSION_TIME) {
            return Animations.BOMB_TICKING.getKeyFrame(this.elapsedTime, true);
        }
        /// null when no bomb is planted
        return null;
    }

    public int getExplosionRadius() {
        if(isIncreasedBombRadius()) {
            return BIG_EXPLOSION_RADIUS;
        }
        else {
            return SMALL_EXPLOSION_RADIUS;
        }
    }

    public boolean isIncreasedBombRadius() {
        return increasedBombRadius;
    }

    public void setIncreasedBombRadius(boolean increasedBombRadius) {
        this.increasedBombRadius = increasedBombRadius;
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
    public void destroy(){
        hitbox.setActive(false);
    }

    /// Used to solidify the bomb as soon as the player is outside the bomb grid
    public void setSensor(boolean isSensor) {
        for (Fixture fixture : hitbox.getFixtureList()) {
            fixture.setSensor(isSensor);
        }
    }

}
