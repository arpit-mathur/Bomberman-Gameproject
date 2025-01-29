package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.screen.Hud;
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

    private float xVelocity;
    private float yVelocity;

    private boolean plantedBomb;

    public Enemy(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.isDestroyed = false;
        this.xVelocity=0;
        this.yVelocity=0;
        this.plantedBomb = false;
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

    public void tick(float x, float y, float frameTime) {
        this.elapsedTime += frameTime;
        ///This code is responsible for the movement of the enemy.

        float speed = BomberQuestGame.level==2 ? 2.7f
                : BomberQuestGame.level==3 ? 3.2f
                :2.3f;
        if (Math.round(x) == Math.round(this.getX()) && Math.round(y) == Math.round(this.getY())) {
            xVelocity = 0;
            yVelocity = 0;
        }
        else if(Math.round(y) == Math.round(this.getY())){
            ///We are kind of setting direction in it
            if(x <= this.getX()) {
                xVelocity = -speed;
                yVelocity = 0;
            }else{
                xVelocity = speed;
                yVelocity = 0;
            }
        }
        else if(Math.round(x) == Math.round(this.getX())){
            ///We are kind of setting direction in it
            if(y <= this.getY()) {
                xVelocity = 0;
                yVelocity = -speed;
            }else{
                xVelocity = 0;
                yVelocity = speed;
            }
        }
        else {
            xVelocity = (float)Math.sin(this.elapsedTime) * speed;
            yVelocity = (float)Math.cos(this.elapsedTime) * speed;
        }

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    public void tick(float x, float y, float x2, float y2, float frameTime) {
        this.elapsedTime += frameTime;
        ///This code is responsible for the movement of the enemy.

        float speed = 2.4f;
        ///Once they are dead
        if (Math.round(x) == Math.round(this.getX()) && Math.round(y) == Math.round(this.getY())) {
            xVelocity = 0;
            yVelocity = 0;

        } else if (Math.round(x2) == Math.round(this.getX()) && Math.round(y2) == Math.round(this.getY())) {
            xVelocity = 0;
            yVelocity = 0;

        } else if(Math.round(y) == Math.round(this.getY()) || Math.round(y2) == Math.round(this.getY())){
            if(x <= this.getX() || x2 <= this.getX()) {

                xVelocity = -speed;
                yVelocity = 0;
            }else{
                xVelocity = speed;
                yVelocity = 0;
            }
        }
        else if(Math.round(x) == Math.round(this.getX()) || Math.round(x2) == Math.round(this.getX())){
            ///We are kind of setting direction in it
            if(y <= this.getY()) {
                xVelocity = 0;
                yVelocity = -speed;

            }else{
                xVelocity = 0;
                yVelocity = speed;
            }
        }
        else {
            xVelocity = (float)Math.sin(this.elapsedTime) * speed;
            yVelocity = (float)Math.cos(this.elapsedTime) * speed;
        }

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if (isDestroyed) {
            TextureRegion enemyDemise;
            if(BomberQuestGame.level == 2) {
                enemyDemise = Animations.BLUE_ENEMY_DEMISE.getKeyFrame(this.elapsedTime, false);
            }
            else if (BomberQuestGame.level == 3) {
                enemyDemise = Animations.RED_ENEMY_DEMISE.getKeyFrame(this.elapsedTime, false);
            }
            else{
                enemyDemise = Animations.ENEMY_DEMISE.getKeyFrame(this.elapsedTime, false);
            }
            if (Animations.ENEMY_DEMISE.isAnimationFinished(this.elapsedTime)) {
                return null; ///return null as enemy is destroyed
            }
            return enemyDemise;
        }
        else {
            if(xVelocity >= 0) {
                if(BomberQuestGame.level == 2){
                    return Animations.BLUE_ENEMY_MOVING_RIGHT.getKeyFrame(this.elapsedTime, true);

                }
                else if(BomberQuestGame.level == 3){
                    return Animations.RED_ENEMY_MOVING_RIGHT.getKeyFrame(this.elapsedTime, true);

                }
                else {
                    return Animations.ENEMY_MOVING_RIGHT.getKeyFrame(this.elapsedTime, true);
                }
            }

            else{
                if (BomberQuestGame.level == 2) {
                    return Animations.BLUE_ENEMY_MOVING_LEFT.getKeyFrame(this.elapsedTime, true);

                } else if(BomberQuestGame.level == 3){
                    return Animations.RED_ENEMY_MOVING_LEFT.getKeyFrame(this.elapsedTime, true);
                }
                else{
                    return Animations.ENEMY_MOVING_LEFT.getKeyFrame(this.elapsedTime, true);
                }
            }
        }
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

    public boolean isPlantedBomb() {
        return plantedBomb;
    }

    public void setPlantedBomb(boolean plantedBomb) {
        this.plantedBomb = plantedBomb;
    }
}