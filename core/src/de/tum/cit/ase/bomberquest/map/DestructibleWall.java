package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class DestructibleWall implements Drawable {

    private final float x;
    private final float y;
    private boolean isDestroyed;
    private float elapsedTime;
    private final Body hitbox;

    /**
     * Create a destructible wall at the given position.
     *
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */
    public DestructibleWall(World world, float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox = createHitbox(world, x, y);
        this.isDestroyed = false;
        this.elapsedTime =0;
    }

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
        box.setAsBox(0.5f, 0.5f);
        // Attach the shape to the body as a fixture.
        body.createFixture(box, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        box.dispose();
        // Set the chest as the user data of the body so we can look up the chest from the body later.
        body.setUserData(this);
        return body;
    }

    public void tick(float frameTime) {
        if (isDestroyed && !Animations.DESTROY_WALL.isAnimationFinished(elapsedTime)) {
            elapsedTime += frameTime;
        }
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if (isDestroyed) {
            /// Play the destruction animation
            TextureRegion destroyWall = Animations.DESTROY_WALL.getKeyFrame(this.elapsedTime, false);

            /// Check if the animation has finished
            if (Animations.DESTROY_WALL.isAnimationFinished(this.elapsedTime)) {
                hitbox.setActive(false); /// /// Deactivate the wall's hitbox when it's destroyed.
                return null; ///return null as wall is destroyed
            }
            return destroyWall;
        }
        return Textures.DESTRUCTIBLEWALL;
    }

    @Override
    public void destroy() {
        if (!isDestroyed) {
            isDestroyed = true;
            elapsedTime = 0; /// Reset elapsed time to start animation from the beginning (0th frame)
        }
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
