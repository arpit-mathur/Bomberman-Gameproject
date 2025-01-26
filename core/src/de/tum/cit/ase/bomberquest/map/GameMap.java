package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;

import java.util.*;
import static de.tum.cit.ase.bomberquest.screen.GameScreen.*;

/**
 * Represents the game map.
 * Holds all the objects and entities in the game.
 */
public class GameMap {

    // A static block is executed once when the class is referenced for the first time.
    static {
        // Initialize the Box2D physics engine.
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }

    // Box2D physics simulation parameters (you can experiment with these if you want, but they work well as they are)
    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;
    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;

    /** The game, in case the map needs to access it. */
    private final BomberQuestGame game;
    /** The Box2D world for physics simulation. */
    private final World world;

    public float mapWidth, mapHeight;
    private int mapMaxX, mapMaxY;
    // Game objects
    private Player player;
    private ArrayList<Enemy> enemies;

    private final Flowers[][] flowers;
    ///Walls of the Selected Map
    private ArrayList<IndestructibleWall> indestructibleWalls;
    private ArrayList<DestructibleWall> destructibleWalls;
    private Exit exit;
    private ArrayList<ConcurrentBombPowerUp> concurrentBombPowerUps;
    private ArrayList<BombBlastPowerUp> bombBlastPowerUp;


    private ArrayList<Bomb> bombs;
    // Tracks elapsed time since the bomb was planted
    // Indicates if the bomb is being monitored
    private CollisionDetecter collisionDetecter;


    /**
     *
     * @param game
     * @param coordinatesAndObjects
     */
    public GameMap(BomberQuestGame game, HashMap<String, String> coordinatesAndObjects) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        this.collisionDetecter = new CollisionDetecter();
        this.world.setContactListener(collisionDetecter);

        this.bombs = new ArrayList<>();
        this.player = getPlayer();

        this.mapMaxX = 0;
        this.mapMaxY =0;

        //Initialized the walls, chests and Breakable walls, and flowers
        this.indestructibleWalls = new ArrayList<>();
        this.destructibleWalls = new ArrayList<>();
        this.exit = new Exit(world, 1, 11);
        this.concurrentBombPowerUps = new ArrayList<>();
        this.bombBlastPowerUp = new ArrayList<>();
        this.enemies = new ArrayList<>();
        parseKeyValueToBuild(coordinatesAndObjects);

        /// +1 as an account for Index
        this.flowers = new Flowers[getMapMaxX()+1][getMapMaxY()+1];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        this.mapWidth = flowers.length * TILE_SIZE_PX * SCALE;
        this.mapHeight = flowers[0].length * TILE_SIZE_PX * SCALE;
    }

    public void parseKeyValueToBuild(Map<String, String> coordinatesAndObjects) {

        for (String key : game.getCoordinatesAndObjects().keySet()) {
            String[] coordinates = key.split(",");
            try {
                ///x coordinate
                int x = Integer.parseInt(coordinates[0].trim());

                if (x > mapMaxX) {
                    this.mapMaxX = x;
                }

                ///y coordinate
                int y = Integer.parseInt(coordinates[1].trim());

                if (y > mapMaxY) {
                    this.mapMaxY = y;
                }
                ///value of our object
                String object = coordinatesAndObjects.get(key);

                switch (object) {
                    case "0" -> this.indestructibleWalls.add(new IndestructibleWall(world, x, y));
                    case "1" -> this.destructibleWalls.add(new DestructibleWall(world, x, y));
                    case "2" -> this.player = new Player(world, x, y);
                    case "3" -> this.enemies.add(new Enemy(world, x, y));
                    //case "4" -> this.chest = new Chest(world, x, y);
                    case "5" -> {
                        this.concurrentBombPowerUps.add(new ConcurrentBombPowerUp(world, x, y));
                        this.destructibleWalls.add(new DestructibleWall(world, x, y));
                    }
                    case "6" -> {
                        this.bombBlastPowerUp.add(new BombBlastPowerUp(world, x, y));
                        this.destructibleWalls.add(new DestructibleWall(world, x, y));
                    }
                }
            }catch (Exception e){
                System.err.println("Invalid coordinate format: " + key);
            }
        }
    }

    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {

        if(this.player !=null) {
            this.player.tick(frameTime);
        }
        if (!this.enemies.isEmpty()) {
            for (Enemy enemy : this.getEnemies()){
                enemy.tick(frameTime);
            }
        }
        if(!this.bombs.isEmpty()) {
            getBombs()
                    .parallelStream()
                    .forEach(bomb -> bomb.tick(0.017f));
        }

        getConcurrentBombPowerUps().forEach(power -> {
            float player_X = Math.round(getPlayer().getX());
            float player_Y = Math.round(getPlayer().getY());
            if(power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()){
                MusicTrack.POWERUP_TAKEN.play();
                power.setPowerTaken(true);
                power.destroy();
                Bomb.incrementMaxConcurrentBombs();
            }
        }
        );

        getBombBlastPowerUp().forEach(power -> {
                    float player_X = Math.round(getPlayer().getX());
                    float player_Y = Math.round(getPlayer().getY());
                    if(power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()){
                        MusicTrack.POWERUP_TAKEN.play();
                        power.setPowerTaken(true);
                        power.destroy();
                        getPlayer().setPlayerSpeed(5f);
                    }
                }
        );

        float player_X1 = Math.round(getPlayer().getX());
        float player_Y1 = Math.round(getPlayer().getY());
        if(getExit().getX() == player_X1 && getExit().getY() == player_Y1){
            MusicTrack.PLAYER_MOVE1.stop();
            MusicTrack.PLAYER_MOVE2.stop();
            MusicTrack.POWERUP_TAKEN.play();
            game.goToLostScreen();
        }


        getDestructibleWalls()
                .parallelStream()
                .forEach(wall -> wall.tick(0.017f));

        /// Manual timer logic for the bomb
        for(Bomb bomb : getBombs()){
            if (bomb.isBombActive()) {

                float playerX = Math.round(getPlayer().getX());
                float playerY = Math.round(getPlayer().getY());

                float bombX = Math.round(bomb.getX());
                float bombY = Math.round(bomb.getY());

                /// Check if the player has moved away from the bomb
                if ((playerX != bombX || playerY != bombY) && bomb.getBombTimer() > 0.5f && bomb.getBombTimer() < Bomb.BOMB_EXPLOSION_TIME) {
                    bomb.setSensor(false); // Disable the sensor, making the bomb a solid hitbox
                }

                /// Putting all the nearby objects that are affected by the bomb explosion in the new Hashmap,
                ///to trigger the destroy() method for each of them.

                if (bomb.getBombTimer() >= Bomb.BOMB_EXPLOSION_TIME) {
                    /// Defined explosion radius
                    MusicTrack.BOMB_EXPLOSION.play();
                    float explosionRadius = bomb.getExplosionRadius();

                    /// used parallel streams for concurrent processes
                    getDestructibleWalls()
                            .parallelStream()
                            .forEach(wall -> {
                                // Check if the wall is aligned with the bomb in either X or Y direction
                                boolean isAlignedX = wall.getX() == bombX && Math.abs(wall.getY() - bombY) <= explosionRadius;
                                boolean isAlignedY = wall.getY() == bombY && Math.abs(wall.getX() - bombX) <= explosionRadius;

                                // Destroy only if it's in the explosion radius and aligned with the bomb
                                if ((isAlignedX || isAlignedY) && !wall.isDestroyed()) {
                                    wall.destroy();
                                }
                            });

                    getEnemies()
                            .forEach(enemy -> {
                                // Round enemy's coordinates to integers
                                float enemyX = Math.round(enemy.getX());
                                float enemyY = Math.round(enemy.getY());

                                // Check if the enemy is aligned with the bomb in either X or Y direction
                                boolean isAlignedX = enemyX == bombX && Math.abs(enemyY - bombY) <= explosionRadius;
                                boolean isAlignedY = enemyY == bombY && Math.abs(enemyX - bombX) <= explosionRadius;

                                // Destroy the enemy only if it's within the explosion radius and aligned with the bomb
                                if ((isAlignedX || isAlignedY) && !enemy.isDestroyed()) {
                                    enemy.destroy();
                                }
                            });

                    ///  Applying the same logic for players death
                    // Round enemy's coordinates to integers
                    float playernewX = Math.round(getPlayer().getX());
                    float playernewY = Math.round(getPlayer().getY());

                    // Check if the enemy is aligned with the bomb in either X or Y direction
                    boolean isAlignedpX = playernewX == bombX && Math.abs(playernewY - bombY) <= explosionRadius;
                    boolean isAlignedpY = playernewY == bombY && Math.abs(playernewX - bombX) <= explosionRadius;

                    // Destroy the enemy only if it's within the explosion radius and aligned with the bomb
                    if ((isAlignedpX || isAlignedpY) && !getPlayer().isDead()) {
                        getPlayer().setDead(true);
                    }
                    bomb.setBombActive(false);
                    bomb.destroy();
                    Bomb.decrementActiveBombs();
                }
            }
        }
        doPhysicsStep(frameTime);

    }

    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     * @param frameTime Time since last frame in seconds
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }

    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }


    ///Getters and Setters
    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void plantBomb(float x, float y) {
        if (Bomb.getActiveBombs() <= Bomb.getMaxConcurrentBombs()) {
            MusicTrack.BOMB_PLANT.play();
            // Dispose of the previous bomb to free memory
//            if (this.bomb != null) {
//                this.bomb.destroy();
//            }
            // Create a new bomb at the specified position
            Bomb bomb =new Bomb(world,x,y);
            this.bombs.add(bomb);
            Bomb.incrementActiveBombs();
            }
    }

    public ArrayList<ConcurrentBombPowerUp> getConcurrentBombPowerUps() {
        return concurrentBombPowerUps;
    }

    public void setConcurrentBombPowerUps(ArrayList<ConcurrentBombPowerUp> concurrentBombPowerUps) {
        this.concurrentBombPowerUps = concurrentBombPowerUps;
    }

    public ArrayList<BombBlastPowerUp> getBombBlastPowerUp() {
        return bombBlastPowerUp;
    }

    public void setBombBlastPowerUp(ArrayList<BombBlastPowerUp> bombBlastPowerUp) {
        this.bombBlastPowerUp = bombBlastPowerUp;
    }

    ///We need these getters to render them in the GameScreen
    public ArrayList<IndestructibleWall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

    public void setIndestructibleWalls(ArrayList<IndestructibleWall> indestructibleWalls) {
        this.indestructibleWalls = indestructibleWalls;
    }

    public ArrayList<DestructibleWall> getDestructibleWalls() {
        return destructibleWalls;
    }

    public void setBreakableWallsOfSelectedMap(ArrayList<DestructibleWall> destructibleWallsOfSelectedMap) {
        this.destructibleWalls = destructibleWallsOfSelectedMap;
    }


    /** Returns the flowers on the map. */
    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
    }

    public int getMapMaxX() {
        return mapMaxX;
    }

    public int getMapMaxY() {
        return mapMaxY;
    }

    public CollisionDetecter getCollisionDetecter() {
        return collisionDetecter;
    }

    public void setCollisionDetecter(CollisionDetecter collisionDetecter) {
        this.collisionDetecter = collisionDetecter;
    }

    public float getPhysicsTime() {
        return physicsTime;
    }

    public BomberQuestGame getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public Exit getExit() {
        return exit;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }
}
