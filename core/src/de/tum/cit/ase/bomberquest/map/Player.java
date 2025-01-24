package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.SpriteSheet;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;

    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;

    private boolean isDead = false;

    private TextureRegion facing;

    private float playerSpeed;

    public Player(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,2);
        this.playerSpeed = 3.2f;
    }

    /**
     * Creates a Box2D body for the player.
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
        circle.setRadius(0.49f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
         body.createFixture(circle, 1.0f);

        ///This fixture has the physics properties of the players hitbox.
        ///Doesnt really do that much thing, this sliding
//        player.setFriction(10f); //To prevent sliding:
//
//       player.setRestitution(3f);// to prevent bouncing
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the player as the user data of the body so we can look up the player from the body later.
        body.setUserData(this);

        return body;
    }

    /**
     * Move the player around in a circle by updating the linear velocity of its hitbox every frame.
     * This doesn't actually move the player, but it tells the physics engine how the player should move next frame.
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime) {
        this.elapsedTime += frameTime;
        // You can change this to make the player move differently, e.g. in response to user input.
        // See Gdx.input.isKeyPressed() for keyboard input

        if(!isDead){
            float xVelocity = 0;
            float yVelocity = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                xVelocity = -playerSpeed;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                yVelocity = playerSpeed;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                yVelocity = -playerSpeed;
            }

            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                xVelocity = playerSpeed;
            }
            this.hitbox.setLinearVelocity(xVelocity, yVelocity);
        }


    }



    @Override
    public TextureRegion getCurrentAppearance() {

        if(!isDead){

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                MusicTrack.PLAYER_MOVE2.stop();
                MusicTrack.PLAYER_MOVE1.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,2);
                return Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                MusicTrack.PLAYER_MOVE1.stop();
                MusicTrack.PLAYER_MOVE2.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,5);
                return Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                MusicTrack.PLAYER_MOVE1.stop();
                MusicTrack.PLAYER_MOVE2.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,5);
                return Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                MusicTrack.PLAYER_MOVE2.stop();
                MusicTrack.PLAYER_MOVE1.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,2);
                return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(this.elapsedTime, true);
            }
            MusicTrack.PLAYER_MOVE1.stop();
            MusicTrack.PLAYER_MOVE2.stop();

            return facing;
        }
        else {
            this.hitbox.setActive(false);
            TextureRegion playerDemise = Animations.CHARACTER_DEMISE.getKeyFrame(this.elapsedTime, false);
            /// Check if the animation has finished
            if (Animations.CHARACTER_DEMISE.isAnimationFinished(this.elapsedTime)) {
                MusicTrack.PLAYER_DEMISE.stop();
                return null; ///return null as player is destroyed
            }
            return playerDemise;
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

    }

    public float getElapsedTime() {
        return elapsedTime;
    }
    ///What does elapsedTime mean for the player?

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.elapsedTime = 0; ///resets the elapsed time such that animation starts from 0th frame
        MusicTrack.PLAYER_MOVE1.stop();
        MusicTrack.PLAYER_MOVE2.stop();
        MusicTrack.PLAYER_DEMISE.play();
        isDead = dead;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public TextureRegion getFacing() {
        return facing;
    }

    public void setFacing(TextureRegion facing) {
        this.facing = facing;
    }
}

