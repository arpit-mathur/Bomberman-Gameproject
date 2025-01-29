package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.screen.GameScreen;
import de.tum.cit.ase.bomberquest.screen.Hud;

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

    private Player2 player2;

    private ArrayList<Enemy> enemies;

    private final Flowers[][] flowers;
    private List<ExplosionSegment> segments = new ArrayList<>();
    ///Walls of the Selected Map
    private ArrayList<IndestructibleWall> indestructibleWalls;
    private ArrayList<DestructibleWall> destructibleWalls;
    private Exit exit;
    private ArrayList<ConcurrentBombPowerUp> concurrentBombPowerUps;
    private ArrayList<BombBlastPowerUp> bombBlastPowerUp;
    private ArrayList<SpeedPowerUp> speedIncreasePowerUps;
    private ArrayList<TimePowerUp> timerPowerUps;



    private final ArrayList<Bomb> bombs;
    private final ArrayList<Bomb> bombsByEnemy;
    // Tracks elapsed time since the bomb was planted
    // Indicates if the bomb is being monitored
    private CollisionDetecter collisionDetecter;


    /**
     *This constructor, initializes every single, attribute, and object declared in this class,
     * It initializes the Array lists of Bomb, bombsByEnemy, timerPowerUp, indestructibleWalls, destructibleWalls,
     * concurrentBombPowerUps, bombBlastPowerUP, speedIncreasePowerups, the list of enemies, and
     * it sets up the flowers according, to the area of the map.
     * This Constructor takes in the HashMap of the coordinates and values, and invokes the parseKeyValuesToBuild(),
     * which builds the objects on the coordinates specified in the HashMap.
     * placeExitRandomly() will place the exit randomly, beneath a destructible wall.
     * randomlyPlacedPowerups() will place the powerUps randomly beneath a destructible Wall.
     * @param game
     * @param coordinatesAndObjects
     */
    public GameMap(BomberQuestGame game, HashMap<String, String> coordinatesAndObjects) {

        this.game = game;
        this.world = new World(Vector2.Zero, true);
        this.collisionDetecter = new CollisionDetecter();
        this.world.setContactListener(collisionDetecter);
        this.bombs = new ArrayList<>();
        this.bombsByEnemy = new ArrayList<>();
        this.timerPowerUps = new ArrayList<>();
        this.mapMaxX = 0;
        this.mapMaxY =0;
        //Initialized the walls, chests and Breakable walls, and flowers
        this.indestructibleWalls = new ArrayList<>();
        this.destructibleWalls = new ArrayList<>();
        this.concurrentBombPowerUps = new ArrayList<>();
        this.bombBlastPowerUp = new ArrayList<>();
        this.speedIncreasePowerUps = new ArrayList<>();
        this.enemies = new ArrayList<>();
        parseKeyValueToBuild(coordinatesAndObjects);

        // +1 as an account for Index
        this.flowers = new Flowers[getMapMaxX()+1][getMapMaxY()+1];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        this.mapWidth = flowers.length * TILE_SIZE_PX * SCALE;
        this.mapHeight = flowers[0].length * TILE_SIZE_PX * SCALE;
        placeExitRandomly();
        randomlyPlacePowerUps();

    }

    /**
     * This method will take in the coordinates and the valus of the objects as a HashMap and will parse them to build the Map objects.
     * @param coordinatesAndObjects
     */
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
                    case "2" -> {
                        if(game.isMultiPlayerSelected()){
                            this.player = new Player(world, x, y+1);
                            this.player2 = new Player2(world, x, y-1);
                        } else {
                            this.player = new Player(world, x, y);
                        }
                    }
                    case "3" -> this.enemies.add(new Enemy(world, x, y));
                    case "4" -> {
                        this.exit = new Exit(world, x, y);
                        this.destructibleWalls.add(new DestructibleWall(world, x, y));
                    }
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
                System.err.println("Error while parsing the Map file");
            }
        }
    }


    /**
     * This method will place the Exit randomly, and it will only place the exit under a destructible wall, since it uses its
     * coordinates.
     */
    public void placeExitRandomly(){
        if(getExit() == null) {
            /// This code will be executed if there is no Exit in the map file
            if (!destructibleWalls.isEmpty()) {
                Random random = new Random();
                int wallno = random.nextInt(destructibleWalls.size());
                DestructibleWall wallForExit = destructibleWalls.get(wallno);
                float exitX = wallForExit.getX();
                float exitY = wallForExit.getY();
                this.exit = new Exit(world, exitX, exitY);
            }

        }
    }

    /**
     * This method follows the same approach as above, will place the powerups randomly
     * at the same coordinates as the destructible wall.
     */
    public void randomlyPlacePowerUps(){

        if(!destructibleWalls.isEmpty()){
            for(int i = 0; i < destructibleWalls.size(); i++){
                Random random1 = new Random();
                int wall3 = random1.nextInt(destructibleWalls.size());
                float floatIndex = i + 17 * wall3/3;

                int indexOfWall = Math.round(floatIndex);

                if(indexOfWall < destructibleWalls.size() && indexOfWall >= 0){
                    DestructibleWall wall1=  destructibleWalls.get(indexOfWall);
                    float speedPowerUpX = wall1.getX();
                    float speedPowerUpY = wall1.getY();
                    this.speedIncreasePowerUps.add(new SpeedPowerUp(world, speedPowerUpX, speedPowerUpY));
                }
            }
            if(bombBlastPowerUp.isEmpty()){
                for(int i = 0; i < destructibleWalls.size(); i++){
                    Random random1 = new Random();
                    int number = random1.nextInt(destructibleWalls.size());
                    float floatIndex = i + 19 * number/5 ;

                    int indexOfWall = Math.round(floatIndex);
                    if(indexOfWall < destructibleWalls.size() && indexOfWall >= 0){
                        DestructibleWall wall1=  destructibleWalls.get(indexOfWall);
                        float BBPowerUpX = wall1.getX();
                        float BBPowerUpY = wall1.getY();
                        this.bombBlastPowerUp.add(new BombBlastPowerUp(world, BBPowerUpX, BBPowerUpY));
                    }
                }
            }
            if(concurrentBombPowerUps.isEmpty()){
                for(int i = 0; i < destructibleWalls.size(); i++){
                    Random random1 = new Random();
                    int number = random1.nextInt(destructibleWalls.size());
                    float floatIndex = i + 17* number/2 ;

                    int indexOfWall = Math.round(floatIndex);

                    if(indexOfWall < destructibleWalls.size() && indexOfWall >= 0){
                        DestructibleWall wall1=  destructibleWalls.get(indexOfWall);
                        float CBowerUpX = wall1.getX();
                        float CBPowerUpY = wall1.getY();
                        this.concurrentBombPowerUps.add(new ConcurrentBombPowerUp(world, CBowerUpX, CBPowerUpY));
                    }
                }
            }
            if(timerPowerUps.isEmpty()){
                for(int i = 0; i < destructibleWalls.size(); i++){
                    Random random1 = new Random();
                    int number = random1.nextInt(destructibleWalls.size());
                    float floatIndex = i + 16 * number/2 ;

                    int indexOfWall = Math.round(floatIndex);

                    if(indexOfWall < destructibleWalls.size() && indexOfWall >= 0){
                        DestructibleWall wall1=  destructibleWalls.get(indexOfWall);
                        float CBowerUpX = wall1.getX();
                        float CBPowerUpY = wall1.getY();
                        this.timerPowerUps.add(new TimePowerUp(world, CBowerUpX, CBPowerUpY));
                    }
                }
            }
        }
    }

    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     * it calls, the helper methods, to update the powerUps and the exits.
     */
    public void tick(float frameTime) {

        if(this.player !=null) {
            float playerX = Math.round(getPlayer().getX());
            float playerY = Math.round(getPlayer().getY());
            float player2X = Math.round(getPlayer().getX());
            float player2Y = Math.round(getPlayer().getY());
            this.player.tick(frameTime);

            if ((playerX != player2X || playerY != player2Y)){
                this.player.setSensor(false);
            }
        }
        getDestructibleWalls()
                .parallelStream()
                .forEach(wall -> wall.tick(0.017f));

        if(this.player2 != null){
            this.player2.tick(frameTime);
            float playerX = Math.round(getPlayer().getX());
            float playerY = Math.round(getPlayer().getY());
            float player2X = Math.round(getPlayer().getX());
            float player2Y = Math.round(getPlayer().getY());
            if ((playerX != player2X || playerY != player2Y)){
                this.player2.setSensor(false);
            }
        }
        if(game.isMultiPlayerSelected()){
            if (!this.enemies.isEmpty()) {
                for (Enemy enemy : this.getEnemies()){
                    enemy.tick(player.getX(), player.getY(), player2.getX(), player2.getY(), frameTime);
                }
            }
        }else {
            if (!this.enemies.isEmpty()) {
                for (Enemy enemy : this.getEnemies()){
                    enemy.tick(player.getX(), player.getY(), frameTime);
                }
            }
        }

        if(!this.bombs.isEmpty()) {
            getBombs()
                    .forEach(bomb -> bomb.tick(0.017f));
        }
        if(!this.bombsByEnemy.isEmpty()) {
            getBombsByEnemy()
                    .forEach(bomb -> bomb.tick(0.017f));
        }
        if(!this.segments.isEmpty()) {
            getSegments()
                    .forEach(segment -> segment.tick(0.017f));
        }

        updateBRPowerUp();

        updateConcurrentBombPowerUP();

        updateSpeedPowerUp();

        updateSpeedPowerUp();

        updateTimerPowerUP();

        checkExit();

        /// Logic of Enemies planting bomb with probability of 40% when the player is within certain range
        /// Each enemy can place bomb only once.
        getEnemies().forEach(enemy -> {
            if(!enemy.isDestroyed() && !enemy.isPlantedBomb()) {
                Vector2 playerPosition = new Vector2(player.getX(), player.getY());
                Vector2 enemyPosition = new Vector2(enemy.getX(), enemy.getY());
                /// Ensures that the probability of this code executing is 40%
                if(Math.random() < 0.4f) {
                    if (Math.abs(playerPosition.dst(enemyPosition)) <= 3) {
                        plantBombForEnemies(Math.round(enemy.getX()), Math.round(enemy.getY()));
                        enemy.setPlantedBomb(true);
                    }
                }
            }
        });

        /// Manual timer logic for the bomb
        for(Bomb bomb : getBombs()){
            if (bomb.isBombActive()) {

                float playerX = Math.round(getPlayer().getX());
                float playerY = Math.round(getPlayer().getY());

                float bombX = Math.round(bomb.getX());
                float bombY = Math.round(bomb.getY());

                /// Check if the player has moved away from the bomb
            if(game.isMultiPlayerSelected()){
                float player2X = Math.round(getPlayer2().getX());
                float player2Y = Math.round(getPlayer2().getY());
                if ((playerX != bombX || playerY != bombY) && bomb.getBombTimer() > 0.7f && bomb.getBombTimer() < Bomb.BOMB_EXPLOSION_TIME) {
                    bomb.setSensor(false); // Disable the sensor, making the bomb a solid hitbox
                }
                else if ((player2X != bombX || player2Y != bombY) && bomb.getBombTimer() > 0.7f && bomb.getBombTimer() < Bomb.BOMB_EXPLOSION_TIME) {
                    bomb.setSensor(false); // Disable the sensor, making the bomb a solid hitbox
                }

            } else if((playerX != bombX || playerY != bombY) && bomb.getBombTimer() > 0.7f && bomb.getBombTimer() < Bomb.BOMB_EXPLOSION_TIME) {
                bomb.setSensor(false); // Disable the sensor, making the bomb a solid hitbox
            }


                /// Putting all the nearby objects that are affected by the bomb explosion in the new Hashmap,
                ///to trigger the destroy() method for each of them.
                if (bomb.getBombTimer() >= Bomb.BOMB_EXPLOSION_TIME) {
                    /// Defined explosion radius
                    MusicTrack.BOMB_EXPLOSION.play();
                    float explosionRadius = Bomb.getCurrentBombRadius();

                    /// Creates the explosion animation for each segment of the bomb
                    ///and destroys the destroyable objects in that segment
                    List<ExplosionSegment> newSegments = segmentsOfExplosion(bombX, bombY, explosionRadius);
                    segments.addAll(newSegments);

                    bomb.setBombActive(false);
                    bomb.destroy();
                    Bomb.decrementActiveBombs();
                }
            }
        }

        for(Bomb bomb : getBombsByEnemy()){
            if (bomb.isBombActive()) {
                /// Putting all the nearby objects that are affected by the bomb explosion in the new Hashmap,
                ///to trigger the destroy() method for each of them.
                if (bomb.getBombTimer() >= Bomb.BOMB_EXPLOSION_TIME) {
                    /// Defined explosion radius
                    MusicTrack.BOMB_EXPLOSION.play();
                    float explosionRadius = Bomb.getCurrentBombRadius();
                    int bombX = Math.round(bomb.getX());
                    int bombY = Math.round(bomb.getY());
                    /// Creates the explosion animation for each segment of the bomb
                    ///and destroys the destroyable objects in that segment
                    List<ExplosionSegment> newSegments = segmentsOfExplosion(bombX, bombY, explosionRadius);
                    segments.addAll(newSegments);
                    bomb.setBombActive(false);
                    bomb.destroy();
                    Bomb.decrementActiveBombsForEnemies();
                }
            }
        }
        doPhysicsStep(frameTime);
    }


    /**
     * This will update the state of Blast Radius powerup depending upon which game mode, is selected.
     * If its multiplayer, it is going to work for both player, else if single player then that.
     * If the player's coordinates are almost the same as the power ups coordinates then the power up is taken.
     */
    public void updateBRPowerUp(){
        if(game.isMultiPlayerSelected()){
            getBombBlastPowerUp().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        float player2X = Math.round(getPlayer2().getX());
                        float player2Y = Math.round(getPlayer2().getY());
                        if((power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()) ||
                                power.getX() == player2X && power.getY() == player2Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                            Bomb.incrementCurrentBombRadius();
                        }
                    }
            );

        } else {
            getBombBlastPowerUp().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        if(power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                            Bomb.incrementCurrentBombRadius();
                        }
                    }
            );
        }
    }


    /**
     * This method updates the state of ConcurrentBombPowerUP, works with the same approach as above.
     */
    public void updateConcurrentBombPowerUP(){

        if(game.isMultiPlayerSelected()){
            getConcurrentBombPowerUps().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        float player2X = Math.round(getPlayer2().getX());
                        float player2Y = Math.round(getPlayer2().getY());
                        if((power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()) ||
                                power.getX() == player2X && power.getY() == player2Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                            Bomb.incrementMaxConcurrentBombs();
                        }
                    }
            );
        } else {
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
        }
    }

    /**
     * This method updates the state of the Speed PowerUp.
     * If the Players coordinate matches the power up's coordinates the powerUp will be taken by the player .
     */
    public void updateSpeedPowerUp(){

        if(game.isMultiPlayerSelected()){
            getSpeedIncreasePowerUps().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        float player2X = Math.round(getPlayer2().getX());
                        float player2Y = Math.round(getPlayer2().getY());
                        if((power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()) ||
                                power.getX() == player2X && power.getY() == player2Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                            if(player.getPlayerSpeed() < 5.0f){
                                player.setPlayerSpeed(player.getPlayerSpeed() + 0.3f);
                            }
                        }
                    }
            );
        } else {

            getSpeedIncreasePowerUps().forEach(speedpower -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        if(speedpower.getX() == player_X && speedpower.getY() == player_Y && !speedpower.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            speedpower.setPowerTaken(true);
                            speedpower.destroy();
                            if(player.getPlayerSpeed() < 5.0f){
                                player.setPlayerSpeed(player.getPlayerSpeed() + 0.3f);
                            }
                        }
                    }
            );
        }
    }

    /**
     * This method updates the state of the timerPowerUP, with the same logic as the above powerUps.
     */
    public void updateTimerPowerUP(){
        if(game.isMultiPlayerSelected()){
            getTimerPowerUps().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        float player2X = Math.round(getPlayer2().getX());
                        float player2Y = Math.round(getPlayer2().getY());
                        if((power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()) ||
                                power.getX() == player2X && power.getY() == player2Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                           game.getHud().addTime(5);
                        }
                    }
            );
        } else {

            getTimerPowerUps().forEach(power -> {
                        float player_X = Math.round(getPlayer().getX());
                        float player_Y = Math.round(getPlayer().getY());
                        if(power.getX() == player_X && power.getY() == player_Y && !power.isPowerTaken()){
                            MusicTrack.POWERUP_TAKEN.play();
                            power.setPowerTaken(true);
                            power.destroy();
                            game.getHud().addTime(5);
                        }
                    }
            );
        }

    }

    /**
     * This method, checks the exit, and executes the corresponding actions, in case of multilevel games.
     * If a single level game is played, once if the player enters the victory screen will be displayed.
     * The exit will only be activated if all the enemies are destroyed.
     */
    public void checkExit(){

        float player_X1 = Math.round(getPlayer().getX());
        float player_Y1 = Math.round(getPlayer().getY());
        if(getRemainingEnemies() == 0) {
            if (game.isMultiLevelSelected()) {
                if (getExit().getX() == player_X1 && getExit().getY() == player_Y1) {
                    MusicTrack.Level_THEME.stop();
                    MusicTrack.Level_THEME2.stop();
                    MusicTrack.ENEMIES_CLEAR.play();
                    MusicTrack.Level_THEME3.play();
                    game.getHud().resetTimer();
                    game.loadDefaultMap();

                }
            } else if (game.isMultiLevelMadness()) {
                if(BomberQuestGame.level == 1){
                    if (getExit().getX() == player_X1 && getExit().getY() == player_Y1) {
                        MusicTrack.Level_THEME.stop();
                        MusicTrack.Level_THEME2.play();
                        MusicTrack.Level_THEME3.stop();
                        BomberQuestGame.level = 2;
                        game.getHud().resetTimer();
                        game.level2Map();
                    }
                } else if(BomberQuestGame.level == 2){
                    if (getExit().getX() == player_X1 && getExit().getY() == player_Y1) {
                        MusicTrack.Level_THEME.stop();
                        MusicTrack.Level_THEME2.stop();
                        MusicTrack.Level_THEME3.play();
                        BomberQuestGame.level = 3;
                        game.getHud().resetTimer();
                        game.level3Map();

                    }
                } else if(BomberQuestGame.level == 3){
                    if (getExit().getX() == player_X1 && getExit().getY() == player_Y1) {
                        GameScreen.setGameWon(true);
                        game.goToVictoryScreen();
                    }
                }
            } else {
                if (getExit().getX() == player_X1 && getExit().getY() == player_Y1) {
                    GameScreen.setGameWon(true);
                    game.goToVictoryScreen();
                }
            }
        }
    }

    /**
     *
     * @param x The X-coordinate of the Bomb which is about to explode
     * @param y The Y-coordinate of the Bomb which is about to explode
     * @param radius Current Bomb's explosion radius
     * @return List of explosion Segments created in each direction,
     * used to create the bomb's explosion animation and destruction of objects in each segment
     */
    private List<ExplosionSegment> segmentsOfExplosion(float x, float y, float radius) {
        List<ExplosionSegment> newSegments = new ArrayList<>();
        newSegments.add(new ExplosionSegment(Math.round(x), Math.round(y), 0, 0, false));
        destroySegmentObjects(x, y); // Destroy objects at the bomb's tile first

        // Directions for up, down, left, right
        int[][] directions = {
                {0, 1},
                {0, -1},
                {-1, 0},
                {1, 0}
        };

        for (int[] dir : directions) {
            for (int i = 1; i <= radius; i++) {
                float segmentX = x + dir[0] * i;
                float segmentY = y + dir[1] * i;
                /// if there is an Indestructible wall at a segment the loop breaks
                /// that means it will not create any bomb segment beyond the wall
                if (isIndestructibleWallAt(segmentX, segmentY)) {
                    break;
                }

                // Create an explosion segment
                boolean isEndSegment = (i == radius);
                segments.add(new ExplosionSegment(
                        Math.round(segmentX),
                        Math.round(segmentY),
                        dir[0], dir[1], isEndSegment
                ));

                destroySegmentObjects(segmentX,segmentY);
            }
        }

        newSegments.add(new ExplosionSegment(Math.round(x), Math.round(y), 0, 0, false));
        return newSegments;
    }

    private boolean isIndestructibleWallAt(float x, float y) {
        return indestructibleWalls.stream().anyMatch(w -> w.getX() == x && w.getY() == y);
    }

    private void destroySegmentObjects(float x, float y) {
        //Destroy all the destructible walls
        getDestructibleWalls()
                .parallelStream()
                .forEach(wall -> {
                    if (wall.getX() == x && wall.getY() == y && !wall.isDestroyed()) {
                        wall.destroy();
                    }
                });

        // Destroy enemies
        getEnemies().forEach(enemy -> {
            if (Math.round(enemy.getX()) == x && Math.round(enemy.getY()) == y && !enemy.isDestroyed()) {
                Hud.addToScore(
                        BomberQuestGame.level == 2 ? 200
                                : BomberQuestGame.level == 3 ? 400
                                : 100
                );
                game.getHud().addTime(BomberQuestGame.level == 2 ? 7
                        : BomberQuestGame.level == 3 ? 10
                        : 4);
                enemy.destroy();
            }
        });

        // Player Demise
        if(game.isMultiPlayerSelected()){
            if (Math.round(getPlayer().getX()) == x && Math.round(getPlayer().getY()) == y && !getPlayer().isDead()) {
                getPlayer().setDead(true);

            } else if((Math.round(getPlayer2().getX()) == x && Math.round(getPlayer2().getY()) == y && !getPlayer2().isDead())) {
                getPlayer2().setDead(true);
            }
        } else{
            if (Math.round(getPlayer().getX()) == x && Math.round(getPlayer().getY()) == y && !getPlayer().isDead()) {
                getPlayer().setDead(true);
            }
        }

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

    public ArrayList<Bomb> getBombsByEnemy() {
        return bombsByEnemy;
    }

    /**
     *
     * @param x New Bomb's X-coordinate
     * @param y New Bomb's Y-coordinate
     */
    public void plantBomb(float x, float y) {
        if (Bomb.getActiveBombs() <= Bomb.getMaxConcurrentBombs()) {
            MusicTrack.BOMB_PLANT.play();
            // Create a new bomb at the specified position
            Bomb bomb =new Bomb(world,x,y);
            this.bombs.add(bomb);
            Bomb.incrementActiveBombs();
        }
    }
    /**
     *
     * @param x New Bomb's X-coordinate
     * @param y New Bomb's Y-coordinate
     */
    public void plantBombForEnemies(float x, float y) {
        if (Bomb.getActiveBombsForEnemies() < 1) {
            MusicTrack.BOMB_PLANT.play();
            // Create a new bomb at the specified position
            Bomb bomb = new Bomb(world,x,y);
            this.bombsByEnemy.add(bomb);
            Bomb.incrementActiveBombsForEnemies();
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

    public List<ExplosionSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<ExplosionSegment> segments) {
        this.segments = segments;
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

    public int getRemainingEnemies(){
        return (int)enemies.stream().filter(e -> !e.isDestroyed()).count();
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public ArrayList<SpeedPowerUp> getSpeedIncreasePowerUps() {
        return speedIncreasePowerUps;
    }

    public Player2 getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player2 player2) {
        this.player2 = player2;
    }

    public ArrayList<TimePowerUp> getTimerPowerUps() {
        return timerPowerUps;
    }
}
