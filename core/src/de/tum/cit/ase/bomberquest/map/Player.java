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

    public Player(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,2);
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
        circle.setRadius(0.47f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
         body.createFixture(circle, 1.0f);

        ///This fixture has the physics properties of the players hitbox.
        ///Doesnt really do that much thing, this sliding
//        playerr.setFriction(10f); //To prevent sliding:
//
//       playerr.setRestitution(3f);// to prevent bouncing
        // We're done with the shape, so we should dispose of it to free up memory.
//        circle.dispose();
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

        if(isDead == false){
            float xVelocity = 0;
            float yVelocity = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                xVelocity = -3.5f ;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                yVelocity = 3.5f;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                yVelocity = -3.5f;
            }

            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                xVelocity = 3.5f ;
            }
            this.hitbox.setLinearVelocity(xVelocity, yVelocity);
        }


    }



    @Override
    public TextureRegion getCurrentAppearance() {
//        if((int)this.getX() == 2 && (int)this.getY() == 15){
//            MusicTrack.PLAYER_MOVE.stop();
//            return Animations.CHARACTER_DEMISE.getKeyFrame(this.elapsedTime, true);
//        }
//        else

        if(isDead == false){

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                MusicTrack.PLAYER_MOVE.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,2);
                return Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                MusicTrack.PLAYER_MOVE.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,5);
                return Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                MusicTrack.PLAYER_MOVE.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,5);
                return Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                MusicTrack.PLAYER_MOVE.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,2);
                return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(this.elapsedTime, true);
            }
            MusicTrack.PLAYER_MOVE.stop();

        }
        else if(isDead == true){
            MusicTrack.PLAYER_MOVE.stop();
            facing = SpriteSheet.ORIGINAL_OBJECTS.at(3,8);
            return Animations.CHARACTER_DEMISE.getKeyFrame(this.elapsedTime, true);
        }

        return facing;
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
        isDead = dead;
    }

    public TextureRegion getFacing() {
        return facing;
    }

    public void setFacing(TextureRegion facing) {
        this.facing = facing;
    }

    public TextureRegion playerDies(){
        isDead = true;
        return Animations.CHARACTER_DEMISE.getKeyFrame(this.elapsedTime, true);
    }
}

