package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.Bomb;

/**
 * Represents the screen shown when the player loses the game.
 * Displays the final score, with suitable message according to the score,
 * and provides options for the player to restart or exit.
 */
public class LostScreen implements Screen{

    private final BomberQuestGame game;

    private final Stage stage;

    /**
     * Constructor to create the LostScreen instance.
     * Sets up the stage, UI elements, and event listeners.
     *
     * @param game The game instance used to access all the other objects in the world
     */
    public LostScreen(BomberQuestGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Game Over", game.getSkin(), "title")).padBottom(80).row();

        Label score = new Label("Your Score: " + Hud.getScoreCount(), game.getSkin());
        score.setFontScale(1.3f);
        table.add(score).padBottom(40).row();

      if(Hud.getScoreCount() > 2000) {
            Label message = new Label("You Were So Close, Just One More Bomb!", game.getSkin());
            message.setFontScale(1.5f);
            table.add(message).padBottom(40).row();
        }
        else if(Hud.getScoreCount() < 700){
            Label message = new Label("You could do better, come on! ;)", game.getSkin());
            message.setFontScale(1.3f);
            table.add(message).padBottom(30).row();
        }
        else{
            Label message = new Label("Oops, Did You Just Lose?", game.getSkin());
            message.setFontScale(1.3f);
            table.add(message).padBottom(30).row();
        }

        TextButton comebackButton = new TextButton("Comeback Time!", game.getSkin());
        table.add(comebackButton).width(350).row();
        comebackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.resetHud();
                MusicTrack.GAME_OVER.stop();
                MusicTrack.Level_THEME.play();
                Bomb.setActiveBombs(0);
                Bomb.setMaxConcurrentBombs(1);
                Bomb.setCurrentBombRadius(1);
                if(game.isMultiLevelSelected()){
                    game.loadChallenge();

                } else if (game.isMultiPlayerSelected()) {
                    game.loadMultiplayer();

                } else if (game.isPersonalMapSelected()) {
                  game.loadTheSelectedMapAgain(game.getCoordinatesAndObjects());

                } else if(game.isMultiLevelMadness()){
                    game.multiLevelMaps();
                } else {
                    game.loadDefaultMap();
                }
            }
        });

        TextButton goToMenu = new TextButton("Go to Main Menu", game.getSkin());
        table.add(goToMenu).width(400).row();
        goToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BomberQuestGame.level = 0;
                Bomb.setActiveBombs(0);
                Bomb.setMaxConcurrentBombs(1);
                Bomb.setCurrentBombRadius(1);
                MusicTrack.GAME_OVER.stop();
                game.goToMenu();
            }
        });

        /// Exit button to Quit whole application
        TextButton exitButton = new TextButton("I want to Quit!", game.getSkin());
        table.add(exitButton).width(350).row();

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the game
            }
        });
    }

    /**
     * Called when this screen becomes the current screen for a game.
     * Sets the input processor to handle UI interactions.
     */
    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the LostScreen elements and updates the stage.
     *
     * @param deltaTime Time in seconds since the last frame was rendered.
     */
    @Override
    public void render(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            resetMethod(game);
        }
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.BLACK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Resets the game state and loads the appropriate game mode based on the previous selection.
     *
     * @param game The game instance used to reset and load game maps.
     */
    static void resetMethod(BomberQuestGame game) {
        game.resetHud();
        MusicTrack.GAME_OVER.stop();
        MusicTrack.Level_THEME.play();
        Bomb.setActiveBombs(0);
        Bomb.setMaxConcurrentBombs(1);
        Bomb.setCurrentBombRadius(1);
        if(game.isMultiLevelSelected()){
            game.loadChallenge();

        } else if (game.isMultiPlayerSelected()) {
            game.loadMultiplayer();

        } else if (game.isPersonalMapSelected()) {
            game.loadTheSelectedMapAgain(game.getCoordinatesAndObjects());

        } else if(game.isMultiLevelMadness()){
            game.multiLevelMaps();
        } else {
            game.loadDefaultMap();
        }
    }

    /**
     * Called when the game window is resized.
     *
     * @param width  The new width of the game window.
     * @param height The new height of the game window.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Cleans up resources used by the LostScreen.
     * Disposes of the stage and other disposable resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
