package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.Bomb;

/**
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {

    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;

    private final BomberQuestGame game;

    private boolean enemyClearSoundPlayed;

    private static int remainingTime;

    private static boolean timerPaused;

    private float elapsedTime;

    private static final int TOTAL_TIME = 150; // Total time in seconds

    private static int scoreCount;

    public Hud(SpriteBatch spriteBatch, BitmapFont font,BomberQuestGame game) {
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.game = game;
        this.enemyClearSoundPlayed = false;
        this.elapsedTime = 0;
        timerPaused = false;
        scoreCount = 0;
    }

    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on the screen.
     */

    public void render(float frameTime) {
        // Render from the camera's perspective
        if (!timerPaused) {
            elapsedTime += frameTime;
        }
        int remainingTime = (int) (TOTAL_TIME - elapsedTime);

        spriteBatch.setProjectionMatrix(camera.combined);
        // Start drawing
        spriteBatch.begin();
        // Draw the HUD elements
        font.setColor(Color.YELLOW);
        if(game.getLevel()>0) {
            font.draw(spriteBatch, "LEVEL- " + game.getLevel(), Gdx.graphics.getWidth() / 2f - 80, Gdx.graphics.getHeight() - 10);
        }

        if(getScoreCount() >= 4000) {
            font.setColor(Color.RED);
        }else if(getScoreCount() >= 2000){
            font.setColor(Color.GREEN);
        }
        font.draw(spriteBatch, "SCORE- " + getScoreCount(), Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 30);

        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "Press Esc to Pause!", 20, 30);

        /// Bomb Radius
        if (Bomb.getCurrentBombRadius() == 8) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.YELLOW);
        }
        font.draw(spriteBatch, "Bomb Blast Radius: "+ Bomb.getCurrentBombRadius(), 20, Gdx.graphics.getHeight() - 10);

        /// Concurrent Bombs
        if (Bomb.getMaxConcurrentBombs() == 8) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.YELLOW);
        }
        font.draw(spriteBatch, "Max Concurrent Bombs: "+ Bomb.getMaxConcurrentBombs(),20, Gdx.graphics.getHeight() - 45);

        /// Remaining Enemies
        if (game.getMap().getRemainingEnemies() == 0) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.YELLOW);
        }
        font.draw(spriteBatch, "Remaining Enemies: "+ game.getMap().getRemainingEnemies(),20, Gdx.graphics.getHeight() - 80);

        ///Player Speed
        if (game.getMap().getPlayer().getPlayerSpeed() == 5.0f) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.YELLOW);
        }
        font.draw(spriteBatch, "Current Speed: "+ game.getMap().getPlayer().getPlayerSpeed(),20, Gdx.graphics.getHeight() - 115);

        if(game.getMap().getRemainingEnemies()==0 && !isEnemyClearSoundPlayed()){
            MusicTrack.ENEMIES_CLEAR.play();
            MusicTrack.Level_THEME.stop();
            MusicTrack.Level_THEME2.play();
            enemyClearSoundPlayed = true;
        }
        font.setColor(Color.GREEN);
        if(remainingTime <= 70 && !timerPaused){
            MusicTrack.Level_THEME.stop();
            MusicTrack.Level_THEME2.play();
        }
        if(remainingTime < 70 && remainingTime >20){
            font.setColor(Color.YELLOW);
        } else if(remainingTime <= 20 && remainingTime > 0){
            font.setColor(Color.RED);
        } else if (remainingTime == 0) {
            game.goToLostScreen();
        }
        font.draw(spriteBatch, "Remaining Time: " + remainingTime, Gdx.graphics.getWidth() / 2f - 170, Gdx.graphics.getHeight() - 40);

        if (remainingTime <= 0) {
            game.goToLostScreen();
        }


        // Finish drawing
        spriteBatch.end();
    }

    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    public boolean isEnemyClearSoundPlayed() {
        return enemyClearSoundPlayed;
    }

    public void setEnemyClearSoundPlayed(boolean enemyClearSoundPlayed) {
        this.enemyClearSoundPlayed = enemyClearSoundPlayed;
    }

    public static int getRemainingTime() {
        return remainingTime;
    }

    public static void setRemainingTime(int remainingTime) {
        Hud.remainingTime = remainingTime;
    }

    public static boolean isTimerPaused() {
        return timerPaused;
    }

    public void resetTimer() {
        setEnemyClearSoundPlayed(false);
        this.elapsedTime = 0;
        Hud.setRemainingTime(TOTAL_TIME); // This ensures the remaining time is reset to the total time
    }

    public static void setTimerPaused(boolean timerpaused) {
        Hud.timerPaused = timerpaused;
    }

    public static void addToScore(int score){
        scoreCount+= score;
    }

    public static int getScoreCount(){
        return scoreCount;
    }

    public static int getFinalScoreCount(){
        return scoreCount + getRemainingTime();
    }

    public void addTime(int secondsToAdd) {
        this.elapsedTime -= secondsToAdd; // Decrease elapsed time to add more time
        if (this.elapsedTime < 0) this.elapsedTime = 0; // Prevent negative elapsed time
    }
}
