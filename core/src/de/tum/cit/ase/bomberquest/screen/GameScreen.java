package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.map.*;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;

    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final int SCALE = 4;

    /// for the width and height of the game window.
    public static int viewWidth, viewHeight;

    private static boolean gameLost;
    private static boolean gameWon;
    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private CollisionDetecter collisionDetecter;

    private final Stage stage;
    /// The Level increases as the player completes challenges
    private static int level;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();

        this.hud = game.getHud();
        // Create and configure the camera for the game view
        this.mapCamera = new OrthographicCamera();
        this.mapCamera.setToOrtho(false);
//
//        this.collisionDetecter = map.getCollisionDetecter();
        /// Initialising
        viewWidth = Gdx.graphics.getWidth();
        viewHeight = Gdx.graphics.getHeight();
        gameLost = false;
        Viewport viewport = new ScreenViewport(mapCamera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch());
        level = 1;
    }

    /**
     * The render method is called every frame to render the game.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || gameLost) {
            game.goToMenu();
            ///We need to dispose the bloody screen properly. In order to load a new map properly.

        }else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            MusicTrack.GAME_PAUSE.play();
            game.goToPauseScreen();
        }

        // Clear the previous frame from the screen, or else the picture smears
        ScreenUtils.clear(Color.BLACK);

        // Cap frame time to 250ms to prevent spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update the map state
        map.tick(frameTime);

        // Update the camera
        updateCamera();

        // Render the map on the screen
        renderMap();

        // Render the HUD on the screen
        hud.render(frameTime);

    }

    /**
     * Updates the camera to match the current state of the game.
     * Currently, this just centers the camera at the origin.
     */
    private void updateCamera() {
        mapCamera.setToOrtho(false);
        /// Clamp is used --- why this min and why this max? (to make it Responsive)
        /// Horizontal Centering
        if(map.getMapWidth() > viewWidth) {
            mapCamera.position.x = MathUtils.clamp(map.getPlayer().getX() * TILE_SIZE_PX * SCALE,
                    (float) viewWidth / (2),
                    map.mapWidth - (float) viewWidth / 2);
        }
        else {
            mapCamera.position.x = map.mapWidth / 2f;
        }
        /// Vertical centering
        if (map.getMapHeight() > viewHeight) {
            mapCamera.position.y = MathUtils.clamp(
                    map.getPlayer().getY() * TILE_SIZE_PX * SCALE,
                    (float) viewHeight / 2,
                    map.mapHeight - (float) viewHeight / 2
            );
        } else {
            mapCamera.position.y = map.mapHeight / 2f;
        }
        mapCamera.update(); // This is necessary to apply the changes
    }

    private void renderMap() {
        // This configures the spriteBatch to use the camera's perspective when rendering
        spriteBatch.setProjectionMatrix(mapCamera.combined);

        // Start drawing
        spriteBatch.begin();

        // Render everything in the map here, in order from lowest to highest (later things appear on top)
        // You may want to add a method to GameMap to return all the drawables in the correct order
        for(Flowers flowers : map.getFlowers()){
            if(flowers != null){
                draw(spriteBatch, flowers);
            }
        }

        for(ExplosionSegment segment : map.getSegments()){
            draw(spriteBatch,segment);
        }



        for(ConcurrentBombPowerUp powerUp : map.getConcurrentBombPowerUps()){
            if(powerUp!= null){
                draw(spriteBatch, powerUp);
            }
        }

        for(BombBlastPowerUp powerUp : map.getBombBlastPowerUp()){
            if(powerUp!= null){
                draw(spriteBatch, powerUp);
            }
        }


        if(!map.getBombs().isEmpty()) {
            for(Bomb bomb : map.getBombs()){
                if(bomb!= null){
                    draw(spriteBatch, bomb);
                }
            }
        }

        for(IndestructibleWall indestructibleWall : map.getIndestructibleWalls()){
            if(indestructibleWall != null){
                draw(spriteBatch, indestructibleWall);
            }
        }

        if(!map.getDestructibleWalls().isEmpty()) {
            for (DestructibleWall destructibleWall : map.getDestructibleWalls()) {
                if (destructibleWall != null) {
                    draw(spriteBatch, destructibleWall);
                }
            }
        }

        for(SpeedPowerUp power: map.getSpeedIncreasePowerUps()){
            if(power != null){
                draw(spriteBatch, power);
            }
        }


        draw(spriteBatch, map.getExit());

        if(game.isMultiPlayerSelected()){

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !map.getPlayer().isDead() && Bomb.getActiveBombs() < Bomb.getMaxConcurrentBombs()){
                float bombX = Math.round(map.getPlayer().getX());
                float bombY = Math.round(map.getPlayer().getY());
                map.plantBomb(bombX,bombY);

            } else if(Gdx.input.isKeyJustPressed(Input.Keys.X) && !map.getPlayer().isDead() && Bomb.getActiveBombs() < Bomb.getMaxConcurrentBombs()){
                float bombX = Math.round(map.getPlayer2().getX());
                float bombY = Math.round(map.getPlayer2().getY());
                map.plantBomb(bombX,bombY);
            }
        } else {

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !map.getPlayer().isDead() && Bomb.getActiveBombs() < Bomb.getMaxConcurrentBombs()) {
                float bombX = Math.round(map.getPlayer().getX());
                float bombY = Math.round(map.getPlayer().getY());
                map.plantBomb(bombX, bombY);
            }
        }


        for(Enemy enemy : map.getEnemies()){
            if(enemy != null){
                draw(spriteBatch, enemy);
            }
        }
        if(map.getPlayer().isDeathAnimationFinished()){
            game.goToLostScreen();
            ///We need a different Screen if player 1 dies
        } else {
            if(game.isMultiPlayerSelected()){
                draw(spriteBatch, map.getPlayer());
                draw(spriteBatch, map.getPlayer2());
            } else {
                draw(spriteBatch, map.getPlayer());
            }

        }


        // Finish drawing, i.e. send the drawn items to the graphics card
        spriteBatch.end();
    }

    /**
     * Draws this object on the screen.
     * The texture will be scaled by the game scale and the tile size.
     * This should only be called between spriteBatch.begin() and spriteBatch.end(), e.g. in the renderMap() method.
     * @param spriteBatch The SpriteBatch to draw with.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();
        // Drawable coordinates are in tiles, so we need to scale them to pixels
        if (texture != null) {
            float x = drawable.getX() * TILE_SIZE_PX * SCALE;
            float y = drawable.getY() * TILE_SIZE_PX * SCALE;

            // Calculate width and height of the texture in pixels
            float width = texture.getRegionWidth() * SCALE;
            float height = texture.getRegionHeight() * SCALE;
            // Draw the texture
            spriteBatch.draw(texture, x, y, width, height);
        }
    }

    /**
     * Called when the window is resized.
     * This is where the camera is updated to match the new window size.
     * @param width The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        mapCamera.setToOrtho(false);
        hud.resize(width, height);
    }

    public static boolean isGameLost() {
        return gameLost;
    }

    public static boolean isGameWon() {
        return gameWon;
    }

    public static void setGameWon(boolean gameWon) {
        GameScreen.gameWon = gameWon;
    }

    public static void setGameLost(boolean gameLost) {
        GameScreen.gameLost = gameLost;
    }

    // Unused methods from the Screen interface
    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        GameScreen.level = level;
    }
}
