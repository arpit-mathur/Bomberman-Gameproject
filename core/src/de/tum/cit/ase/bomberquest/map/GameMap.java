package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;

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
    // Game objects
    private Player player;

    private ArrayList<Enemy> enemies;
    private  Chest chest;
    private Bomb bomb;
    // Tracks elapsed time since the bomb was planted
    private float bombTimer = 0f;
    // Indicates if the bomb is being monitored
    private boolean isBombActive = false;

    private CollisionDetecter collisionDetecter;


    private IndestructibleWall[][] indestructibleWallsOfDefaultGame;

    private DestructibleWall[][] destructibleWallsOfDefaultGame;

    private  Flowers[][] flowers;

    ///Walls of the Selected Map
    private ArrayList<IndestructibleWall> indestructibleWallsOfSelectedMap;
    private ArrayList<DestructibleWall> destructibleWallsOfSelectedMap;
    private ArrayList<Chest> Chests;

    public GameMap(BomberQuestGame game) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);

        //setting the contactListener is needed in order to detect the collision between object .
        this.collisionDetecter = new CollisionDetecter();
        this.world.setContactListener(collisionDetecter);

        // Create a player with initial position (1, 3)
        this.player = new Player(this.world, 1, 15);

        ///What else I need to do?

        this.enemies = new ArrayList<>();
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 8, 15);
        this.bomb = new Bomb(world,100,100);


        // Create flowers in a 7x7 grid
        this.indestructibleWallsOfDefaultGame = new IndestructibleWall[29][17];
        for (int i = 0; i < indestructibleWallsOfDefaultGame.length; i++) {
            for (int j = 0; j < indestructibleWallsOfDefaultGame[i].length; j++) {
                // Place walls only on the boundary cells (edges)
                if (i==0 || i==28 || j==0 || j==16 ||(i % 2 == 0 && j % 2 == 0)) {
                    this.indestructibleWallsOfDefaultGame[i][j] = new IndestructibleWall(this.world, i, j);
                }
            }
        }

        this.destructibleWallsOfDefaultGame = new DestructibleWall[29][17];
        for (int i = 1; i < destructibleWallsOfDefaultGame.length; i++) {
            for (int j = 1; j < destructibleWallsOfDefaultGame[i].length; j++) {
                // Place walls only on the boundary cells (edges)
                if ((i % 2 == 1 && j % 2 == 1)&& i > 8 && j > 8) {
                    this.destructibleWallsOfDefaultGame[i][j] = new DestructibleWall(this.world, i, j);
                }
            }
        }

        this.flowers = new Flowers[29][17];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        this.mapWidth = flowers.length * TILE_SIZE_PX * SCALE;
        this.mapHeight = flowers[0].length * TILE_SIZE_PX * SCALE;
        this.destructibleWallsOfSelectedMap = new ArrayList<>();
        this.indestructibleWallsOfSelectedMap = new ArrayList<>();
    }

    /**
     * Our brand new constructor, we are getting fancy out here.
     * @param game
     * @param coordinatesAndObjects
     */
    public GameMap(BomberQuestGame game, HashMap<String, String> coordinatesAndObjects) {
        this.game = game;
        game.setDidUserSelectTheMap(true);

        this.world = new World(Vector2.Zero, true);
        this.collisionDetecter = new CollisionDetecter();
        this.world.setContactListener(collisionDetecter);


        //Initialized the walls, chests and Breakable walls, and flowers
        this.indestructibleWallsOfSelectedMap = new ArrayList<>();
        this.Chests = new ArrayList<>();
        this.destructibleWallsOfSelectedMap = new ArrayList<>();

        this.flowers = new Flowers[21][21];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        this.mapWidth = flowers.length * TILE_SIZE_PX * SCALE;
        this.mapHeight = flowers[0].length * TILE_SIZE_PX * SCALE;
        this.enemies = new ArrayList<>();
        this.bomb = new Bomb(world,100,100);
        parseKeyValueToBuild(coordinatesAndObjects);
    }

    public void parseKeyValueToBuild(Map<String, String> coordinatesAndObjects) {

        for (String key : game.getCoordinatesAndObjects().keySet()) {

            String[] coordinates = key.split(",");
            ///x coordinate
            int x = Integer.parseInt(coordinates[0].trim());
            ///y coordinate
            int y = Integer.parseInt(coordinates[1].trim());
            ///value of our object
            String object = coordinatesAndObjects.get(key);

            switch (object) {
                case "0" -> this.indestructibleWallsOfSelectedMap.add(new IndestructibleWall(world, x, y));
                case "1" -> this.destructibleWallsOfSelectedMap.add(new DestructibleWall(world, x, y));
                case "2" -> this.player = new Player(world, x, y);
                case "3" -> this.enemies.add(new Enemy(world, x, y));
                //case "4" -> this.chest = new Chest(world, x, y);
                //case "5" -> this.chest = new Chest(world, x, y);
                //case "6" -> this.chest = new Chest(world, x, y);
            }
        }

    }

        /**
         * Updates the game state. This is called once per frame.
         * Every dynamic object in the game should update its state here.
         * @param frameTime the time that has passed since the last update
         */
        public void tick(float frameTime) {

            this.player.tick(frameTime);

            if (!this.enemies.isEmpty()) {
                for (Enemy enemy : this.getEnemies()){
                    enemy.tick(frameTime);
                }
            }
            this.bomb.tick();

            getDestructibleWallsOfSelectedMap()
                    .parallelStream()
                    .forEach(wall -> wall.tick(0.017f));

            /// Manual timer logic for the bomb
            if (isBombActive) {
                float fixedTimeStep = 0.017f;
                bombTimer += fixedTimeStep;

                float playerX = Math.round(getPlayer().getX());
                float playerY = Math.round(getPlayer().getY());

                float bombX = Math.round(this.bomb.getX());
                float bombY = Math.round(this.bomb.getY());

                /// Check if the player has moved away from the bomb
                if ((playerX != bombX || playerY != bombY) && bombTimer > 0.7f && bombTimer < Bomb.BOMB_EXPLOSION_TIME) {
                    this.bomb.setSensor(false); // Disable the sensor, making the bomb a solid hitbox
                }

                /// Putting all the nearby objects that are affected by the bomb explosion in the new Hashmap,
                ///to trigger the destroy() method for each of them.

                if(bombTimer >= Bomb.BOMB_EXPLOSION_TIME){
                    /// Defined explosion radius
                    float explosionRadius = this.bomb.getExplosionRadius();

                    /// used parallel streams for concurrent processes
                    getDestructibleWallsOfSelectedMap()
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
                    if ((isAlignedpX || isAlignedpY)) {
                        getPlayer().setDead(true);
                    }
                    isBombActive = false; // Stop monitoring the bomb
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

    /** Returns the chest on the map. */
    public Chest getChest() {
        return chest;
    }

    /** Returns the walls on the map. */
    public List<IndestructibleWall> getIndestructibleWallsOfDefaultGame() {
        return Arrays.stream(indestructibleWallsOfDefaultGame).flatMap(Arrays::stream).toList();
    }

    /** Returns the Breakable_walls on the map. */
    public List<DestructibleWall> getDestructibleWallsOfDefaultGame() {
        return Arrays.stream(destructibleWallsOfDefaultGame).flatMap(Arrays::stream).toList();
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

    public Bomb getBomb() {
        return bomb;
    }

    public void plantBomb(float x, float y) {
        // Dispose of the previous bomb to free memory
        if (this.bomb != null) {
            this.bomb.destroy();
        }

        // Create a new bomb at the specified position
        this.bomb = new Bomb(this.world, x, y);

        // Initially set the bomb as a sensor
        this.bomb.setSensor(true);

        // Reset the timer and activate the monitoring state
        bombTimer = 0f;
        isBombActive = true;
    }

    ///We need these getters to render them in the GameScreen
    public ArrayList<IndestructibleWall> getIndestructibleWallsOfSelectedMap() {
        return indestructibleWallsOfSelectedMap;
    }

    public void setIndestructibleWallsOfSelectedMap(ArrayList<IndestructibleWall> indestructibleWallsOfSelectedMap) {
        this.indestructibleWallsOfSelectedMap = indestructibleWallsOfSelectedMap;
    }

    public ArrayList<DestructibleWall> getDestructibleWallsOfSelectedMap() {
        return destructibleWallsOfSelectedMap;
    }

    public void setBreakableWallsOfSelectedMap(ArrayList<DestructibleWall> destructibleWallsOfSelectedMap) {
        this.destructibleWallsOfSelectedMap = destructibleWallsOfSelectedMap;
    }

    public ArrayList<Chest> getChests() {
        return Chests;
    }

    /** Returns the flowers on the map. */
    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
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
}
