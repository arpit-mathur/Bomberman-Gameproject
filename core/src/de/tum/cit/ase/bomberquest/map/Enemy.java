
package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Enemy implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;

    private boolean isDestroyed;
    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;

    public Enemy(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.isDestroyed = false;
    }

    /**
     * Creates a Box2D body for the Enemy.
     * This is what the physics engine uses to move the player around and detect collisions with other bodies.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
         BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
//        bodyDef.awake = true;
//        bodyDef.fixedRotation = true;
//        bodyDef.bullet = true;
//        bodyDef.active = true;
//        bodyDef.angularDamping = 45.0f;
//        bodyDef.angularVelocity = 2.0f;
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the player.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the player is 0.6 tiles wide).
        /// Changed the radius of the Hitbox, such that there is no overlapping with the walls.
        circle.setRadius(0.47f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
        body.createFixture(circle, 1.0f);

//        enemy.setSensor(false);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the player as the user data of the body so we can look up the player from the body later.
        body.setUserData(this);

        return body;
    }

    /**
     * This doesn't actually move the player, but it tells the physics engine how the player should move next frame.
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime) {
        this.elapsedTime += frameTime;
        // Make the player move in a circle with radius 2 tiles
        // You can change this to make the player move differently, e.g. in response to user input.
        // See Gdx.input.isKeyPressed() for keyboard input
        ///These things are responsible for the movement of the enemy.
        float xVelocity = (float) Math.sin(this.elapsedTime) * 2;
        float yVelocity = (float) Math.cos(this.elapsedTime) * 2;
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }


    @Override
    public TextureRegion getCurrentAppearance() {
        if (isDestroyed) {
            /// Play the Enemy Demise animation
            TextureRegion enemyDemise = Animations.ENEMY_DEMISE.getKeyFrame(this.elapsedTime, false);
            /// Check if the animation has finished
            if (Animations.ENEMY_DEMISE.isAnimationFinished(this.elapsedTime)) {
                return null; ///return null as wall is destroyed
            }
            return enemyDemise;
        }
        return Animations.ENEMY_ANIMATION.getKeyFrame(this.elapsedTime, true);
    }

    @Override
    public float getX() {
        // The x-coordinate of the player is the x-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().x;
    }

    @Override
    public float getY() {
        // The y-coordinate of the player is the y-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().y;
    }

    @Override
    public void destroy() {
        if(!isDestroyed) {
            isDestroyed = true;
            hitbox.setActive(false); /// Deactivate the wall's hitbox when it's destroyed.
            this.elapsedTime = 0; ///resets the elapsed time such that animation starts from 0th frame
        }
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}

