package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.screen.GameScreen;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.SpriteSheet;

public class Player2 extends Player{



    public Player2(World world, float x, float y) {
        super(world, x, y);
        this.isDead = false;
        this.facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,2);
        this.playerSpeed = 3.2f;
    }

    @Override
    public void tick(float frameTime) {
        this.elapsedTime += frameTime;
        // You can change this to make the player move differently, e.g. in response to user input.
        // See Gdx.input.isKeyPressed() for keyboard input
        float xVelocity = 0;
        float yVelocity = 0;

        if(!isDead){
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                xVelocity = -playerSpeed;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                yVelocity = playerSpeed;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                yVelocity = -playerSpeed;
            }

            else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                xVelocity = playerSpeed;
            }
        }
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }



    @Override
    public TextureRegion getCurrentAppearance() {

        if(!isDead && !GameScreen.isGameWon()){
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                MusicTrack.PLAYER_MOVE2.stop();
                MusicTrack.PLAYER_MOVE1.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,2);
                return Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                MusicTrack.PLAYER_MOVE1.stop();
                MusicTrack.PLAYER_MOVE2.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(2,5);
                return Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                MusicTrack.PLAYER_MOVE1.stop();
                MusicTrack.PLAYER_MOVE2.play();
                facing = SpriteSheet.ORIGINAL_OBJECTS.at(1,5);
                return Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
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
                isDeathAnimationFinished = true;
                return null;  ///return null as player is destroyed
            }
            return playerDemise;
        }
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public float getY() {
        return super.getY();
    }


    @Override
    public float getPlayerSpeed() {
        return super.getPlayerSpeed();
    }


}
