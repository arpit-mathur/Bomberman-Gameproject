package de.tum.cit.ase.bomberquest;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.Bomb;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.screen.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

/**
 * The BomberQuestGame class represents the core of the Bomber Quest game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class BomberQuestGame extends Game {

    /**
     * Sprite Batch for rendering game elements.
     * This eats a lot of memory, so we only want one of these.
     */
    private SpriteBatch spriteBatch;

    /** The game's UI skin. This is used to style the game's UI elements. */
    private Skin skin;

    private boolean isMultiLevelSelected = false;

    private boolean isMultiPlayerSelected = false;

   private boolean isPersonalMapSelected = false;
    /**
     * The file chooser for loading map files from the user's computer.
     * This will give you access to a {@link com.badlogic.gdx.files.FileHandle} object,
     * which you can use to read the contents of the map file as a String, and then parse it into a {@link GameMap}.
     */
    private final NativeFileChooser fileChooser;

    /**
     * The HashMap will store the coordinates of the respective Objects.
     * And the Attribute didUserSelectTheMap will keep track of whether the user has selected the map or not.
     */
    private HashMap<String, String> coordinatesAndObjects = new HashMap<>();
    /**
     * The map. This is where all the game objects are stored.
     * This is owned by {@link BomberQuestGame} and not by {@link GameScreen}
     * because the map should not be destroyed if we temporarily switch to another screen.
     */
    private GameMap map;

    private Hud hud;


    /**
     * Constructor for BomberQuestGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public BomberQuestGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     * During the class constructor, libGDX is not fully initialized yet.
     * Therefore this method serves as a second constructor for the game,
     * and we can use libGDX resources here.
     */
    @Override
    public void create() {
        // Initialize SpriteBatch for rendering
        this.spriteBatch = new SpriteBatch();

        // Load UI skin
        this.skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json"));

        hud = new Hud(spriteBatch, getSkin().getFont("font"), this);

        this.isMultiPlayerSelected = true;

//        // Load default map from "map-1.properties"
//        loadDefaultMap();

        // Navigate to the menu screen
        goToMenu();
    }

    /**
     * Loads the default map from "map-1.properties" in /maps
     */
    public void loadDefaultMap() {

        /// By the same logic as in doYourMagic()
        isMultiLevelSelected = false;
        coordinatesAndObjects.clear();
        isMultiPlayerSelected = false;
        FileHandle defaultMapFile = Gdx.files.internal("maps/map-1.properties");
        String mapContent = defaultMapFile.readString();
        String[] linesOfText = mapContent.split("\n");

        coordinatesAndObjects.clear();// Clear any previous data
        for (String line : linesOfText) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] keyValue = line.split("=");
            coordinatesAndObjects.put(keyValue[0].trim(), keyValue[1].trim());
        }

        // Initialize the GameMap object with default map
        this.map = new GameMap(this, coordinatesAndObjects);
        MusicTrack.MENU_BGM.stop();
        this.setScreen(new GameScreen(this));

    }

    public void loadChallenge() {

        /// By the same logic as in doYourMagic()
        isMultiLevelSelected = true;
        coordinatesAndObjects.clear();
        isMultiPlayerSelected = false;
        FileHandle defaultMapFile = Gdx.files.internal("maps/map-2.properties");
        String mapContent = defaultMapFile.readString();
        String[] linesOfText = mapContent.split("\n");

        coordinatesAndObjects.clear();// Clear any previous data
        for (String line : linesOfText) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] keyValue = line.split("=");
            coordinatesAndObjects.put(keyValue[0].trim(), keyValue[1].trim());
        }

        // Initialize the GameMap object with default map
        this.map = new GameMap(this, coordinatesAndObjects);
        MusicTrack.MENU_BGM.stop();
        MusicTrack.Level_THEME.play();
        this.setScreen(new GameScreen(this));
    }

    public void loadMultiplayer() {

        /// By the same logic as in doYourMagic()
        isMultiLevelSelected = false;
        coordinatesAndObjects.clear();
        isMultiPlayerSelected = true;
        FileHandle defaultMapFile = Gdx.files.internal("maps/map-1.properties");
        String mapContent = defaultMapFile.readString();
        String[] linesOfText = mapContent.split("\n");

        coordinatesAndObjects.clear();// Clear any previous data
        for (String line : linesOfText) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] keyValue = line.split("=");
            coordinatesAndObjects.put(keyValue[0].trim(), keyValue[1].trim());
        }

        // Initialize the GameMap object with default map
        this.map = new GameMap(this, coordinatesAndObjects);
        MusicTrack.MENU_BGM.stop();
        this.setScreen(new GameScreen(this));

    }

    public void loadTheSelectedMapAgain(HashMap<String, String> coordinatesAndObjects){

            isMultiLevelSelected = false;
//            this.coordinatesAndObjects.clear();
            isMultiPlayerSelected = false;
            // Initialize the GameMap object with default map
            this.map = new GameMap(this, coordinatesAndObjects);
            MusicTrack.MENU_BGM.stop();
            this.setScreen(new GameScreen(this));

    }


    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        MusicTrack.PLAYER_MOVE1.stop();
        MusicTrack.PLAYER_MOVE2.stop();
        MusicTrack.Level_THEME.stop();
        MusicTrack.Level_THEME2.stop();
        MusicTrack.MENU_BGM.play();
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        MusicTrack.MENU_BGM.stop();
        MusicTrack.Level_THEME.play();
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
    }

    ///goes to the pauseScreen
    public void goToPauseScreen(){
        MusicTrack.Level_THEME.stop();
        MusicTrack.Level_THEME2.stop();
        Hud.setTimerPaused(true);
        this.setScreen(new PauseScreen(this));
    }

    public void goToLostScreen(){
        Hud.setTimerPaused(true);
        MusicTrack.Level_THEME.stop();
        MusicTrack.Level_THEME2.stop();
        MusicTrack.PLAYER_MOVE1.stop();
        MusicTrack.PLAYER_MOVE2.stop();
        MusicTrack.GAME_OVER.play();
        this.setScreen(new LostScreen(this));
    }

    public void goToVictoryScreen(){
        Hud.setTimerPaused(true);
        MusicTrack.Level_THEME.stop();
        MusicTrack.Level_THEME2.stop();
        MusicTrack.PLAYER_MOVE1.stop();
        MusicTrack.PLAYER_MOVE2.stop();
        MusicTrack.LEVEL_COMPLETED.play();
        this.setScreen(new VictoryScreen(this));
    }

    /**
     * (Aryan) Goes to the map selected by user:
     */
    public void goToSelectedMap(){
        MusicTrack.MENU_BGM.stop();
        MusicTrack.Level_THEME.play();
        this.setScreen(new GameScreen(this));

    }

    /** Returns the skin for UI elements. */
    public Skin getSkin() {
        return skin;
    }

    /** Returns the main SpriteBatch for rendering. */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /** Returns the current map, if there is one. */
    public GameMap getMap() {
        return map;
    }

    public Hud getHud() {
        return hud;
    }

    public void resetHud() {
        hud = new Hud(spriteBatch, getSkin().getFont("font"), this);
    }

    /**
     * Switches to the given screen and disposes of the previous screen.
     * @param screen the new screen
     */
    @Override
    public void setScreen(Screen screen) {
        Screen previousScreen = super.screen;
        super.setScreen(screen);
        if (previousScreen != null) {
            previousScreen.dispose();
        }
    }

    /** Cleans up resources when the game is disposed. */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }


    //Getter for getFileChooser/ Aryan
    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }

    ///Getters and Setters for the Hashmap of coordinatesAndObjects and the attribute didUserSelectTheMap
    public HashMap<String, String> getCoordinatesAndObjects() {
        return coordinatesAndObjects;
    }

    public void setCoordinatesAndObjects(HashMap<String, String> coordinatesAndObjects) {
        this.coordinatesAndObjects = coordinatesAndObjects;
    }

    /**(Aryan)
     * The LoadFileChooser Method, which will be invoked once the user clicks on that button.
     * In this Method, the fileChooser Attribute of our class, will invoke a method fileChooser.chooseFile(),
     * which will invoke the fileChooser window for the user. This chooseFile() method, takes in two arguments,
     * first is the configuration, and the second fileChooserCallback, and we initialize both of these arguments first
     * in the LoadFileChooser() method, and then call the chooseFile() method.
     */
    public void LoadFileChooser() {

        ///First parameter of the chooseFile method, important to open the directory and lead you to the correct folder
        NativeFileChooserConfiguration configuration = new NativeFileChooserConfiguration();
        //title
        configuration.title = "Please Choose your file!";
        //Directory, most important attribute, without it the window won't show up
        configuration.directory = Gdx.files.getFileHandle("maps/", Files.FileType.Internal);
        //The name filter to filter it out by the suffixes
        configuration.nameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        };
        /**(Aryan)
         * This is another very important parameter, this will decide what happens upon, choosing the file, cancelling the process,
         * or if there is an Error:
         */
        NativeFileChooserCallback fileChooserCallback = new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) {
                isMultiPlayerSelected = false;
                isPersonalMapSelected = true;
                //read the properties file, which will give us the output in one line with "\n".
                String EntireText = file.readString();
                //Then we split the map properly, and we end up with our proper file as you can see right now in the map properties folder and store it into the array
                String[] linesOfText = EntireText.split("\n");

                ///This method will take that array, and will split it again on the basis of "=",
                ///which will give us the hashmap with the coordinatesAndObjects, and the end result of doYourMagic,
                ///will determine, what happens on when your file is selected, in our case we want the user to go the selected map in the game,
                ///So the end result should bring us to the selected map in the game.
                doYourMagic(linesOfText);
            }

            ///We can do something like show a new screen on cancellation, which is kinda easy lets see.
            @Override
            public void onCancellation() {
                System.out.println("why did you cancel?");
            }

            @Override
            public void onError(Exception exception) {
                System.out.println("reason: " + exception);
            }
        };

        ///Then we put the arguments in this method, so that everything comes together in the end.
        fileChooser.chooseFile(configuration, fileChooserCallback);
    }

    /**
     * This method does the magic of converting the Array of String, into the hashMap, and then invoking
     * the constructor of the GameMap class with that, and in the end calling the goToSelectedMap(), to go to that map.
     * The constructor will parse, the HashMap into Game Map, creating the objects in the Map.
     * @param linesOfText
     */
    public void doYourMagic(String[] linesOfText) {

        /// VVI to clear the previous objects.
        coordinatesAndObjects.clear();
        Bomb.setActiveBombs(0);
        Bomb.setMaxConcurrentBombs(1);
        for (String line : linesOfText) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] keyValue = line.split("=");
            coordinatesAndObjects.put(keyValue[0].trim(), keyValue[1].trim());

        }
        this.map = new GameMap(this, coordinatesAndObjects);
        goToSelectedMap();
    }

    public boolean isMultiLevelSelected() {
        return isMultiLevelSelected;
    }

    public void setMultiLevelSelected(boolean multiLevelSelected) {
        isMultiLevelSelected = multiLevelSelected;
    }

    public boolean isMultiPlayerSelected() {
        return isMultiPlayerSelected;
    }

    public void setMultiPlayerSelected(boolean multiPlayerSelected) {
        isMultiPlayerSelected = multiPlayerSelected;
    }

    public boolean isPersonalMapSelected() {
        return isPersonalMapSelected;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }
}
