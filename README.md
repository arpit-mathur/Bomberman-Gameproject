# Bomber Quest


Enter into the world of wacky, bomb wielding ninjas to destroy the enemies around, and seal the victory! In order to make that possible and make it enjoyable and understandable for all the newcomers as well as the professional mouse wielding, keyboard smashing programmers, we have created this ReadMe file to find your way around how things work in this world on Screen, in the Game, and in the Code.
So tie your seat belts, get your bombs, because we are about start our quest of traversing the deep dungeons (allbeit beatiful), lines of codes, while discovering what makes this game, unique, the codes so beautiful and the logic in which the world of dungeon works.

### How do you run and use the Game? 
You can clone this repository and then paste it into your IDE, and import LibGdX. Once you have no errors, you can simply run the game by clicking on the run button. 

## Project Structure and Class Hierarchy:

First of all we will start with the one of the Core classes, in our Project, BomberQuestGame, GameScreen, and the GameMap.
These Aforementioned classes, are the holy trinity of our Game and the culmination of all the building blocks of codes, which have been set up in the other classes.

### BomberQuestGame Class

This class is holy shrine of our code, where a lot of stuff starts off from. This Class extends the Game Class of the LibGDX library. This Game Class is the abstract class,and our BomberQuestGame, inherits from this class and overrides its methods. BomberQuestGame manages some important attributes, that are responsible for our unique pixel art like look, which is the SpriteBatch and The Skin. You can call SpriteBatch the artist of our world, he is the one that depicts the Objects which are Drawable, as the wonderful, artist it is The spriteBatch will only draw the objects of which the Classes are implementing the drawable interface.

And each Artist has his own Style, and our spriteBatch is not different. It is defined by the attribute of skin of the class Skin. Spritebatch has its special workplace, the GameScreen class. We will come to the specifics once we get there.very game has its own style and artist, and overrides it's superclass methods, but what makes our class different? It contains one of our critical events that is the spine of our game. Here we Manage, our important methods that are responsible for loading Different maps, and levels of dungeons, the user selects. It keeps track of the user's Choices, and executes it on command.

Well, an artist is of no use if he has nothing to draw, so we also Implemented our loadingMapfiles method in this class, with methods that help to load file from user's computer, we are trying to expand our library, with multiplayer maps, and a special Level called MultiLevelMadness, we have tried to take things to another level.

### The GameScreen Class
You might have read about this class as the Workplace of the Artist, but it is not just that it also keeps track of the time, since last frame was rendered, it brings every frame, to life. This class implements the Screen interface, and while implementing its methods, it adds its own twist to it.

By using the updateCamera() method it never looses track of its player!
In the Render method, one of the important methods that are being called are RenderMap method, which is a very special place for spriteBatch, using its draw() method, in this GameScreen Class, the spritebatch draws, every object that implements drawable.

### The GameMap Class
You might have enjoyed the artwork and the visuals rendered on your Screen by GameScreen, but the true world lives in The GameMap.From walls, to players, enemies, flowers and bombs, everything! The living, breathing, wacky dungeon world, lives in harmony over here.... right?

Well sometimes things do go down south here, but what does not goes down is our trusty tick() method, it constantly updates on how the things physically move in the dungeon for every frame. But before everything else, at the Start of the class a static block needs to be executed, to initialize the physics engine. Our tick() updates, the state of every object be it good or bad! We make use of the helper Methods, like segmentsOfExplosion, and so on.

Now that we know our three pillars of the game, we will go and investigate about each and every class in every package, So lets begin with the Texture package.

### The texture Package:

#### Animations
Here each and every Animation method exists which is public static, which brings to life the movement of our demons and our player. We also take care of the animation when there is destruction of walls, or a bomb is dropped. With one look you might have noticed that we make use of SpriteSheets over here and its at() method. That's what we will talk about next,

#### SpriteSheet
We have this enum which contains a way to store the spritesheets we have in a folder and access them with the at() method. So how do we store the textures from those spriteSheet?

#### Textures
We do that in the textures class, we store every texture and the look of an object in this Class. Very useful indeed.

### The map package:

All of Classes except, CollisionDetector and GameMap implements the drawable interface.

#### Player
The Player Class represents our player, which needs to survive this dungeon, in order to know if he is dead or not, we have a boolean attribute isDead, to keep track of that and trigger actions such as update his appearance if he is dead, or if he is alive update the appearance. And most importantly in order ot move he is of the dynamic bodytype.  
The player is always located with his x and y coordinates and cannot go through the walls, or out of the map.

#### Player2
This class inherits everything from Player, except its movement abilities, while player moves with our keyboard arrows, this player2 moves up, down left and right, with the keys W, S, A, D respectively.

#### IndestructibleWall
This is the wall, that cannot be destroyed by explosions, and on every new level, in MultiLevelMadness, you will get to see different colours of walls.

#### DestructibleWall
This is the wall, that can be destroyed by explosion, and can get different colors in different levels, in our MultiLevelMadness. We made good use of the getCurrentAppearance() method to achieve this! Same goes for the Indestructible wall.

#### Bomb
Where there is weapon, there is the way, by destroying through the walls, we manage the Bomb class with the following notable attributes:  BOMB_EXPLOSION_TIME, maxConcurrentBombs, currentBombRadius, and limiting the max bomb radius to 8 with MAX_BOMB_RADIUS. We keep track of the bombs, with the help of incrementActiveBombs and other helper method. 

The setSensor() method, will help us solidify the bomb, after planting the bomb. The actual ticking timer of the bomb runs in the tick() method of the GameMap, resulting in the explosion.And to take care of the explosion, we make use of the Helper Class ExplosionSegment.  

#### Exit
We made sure that the player is always trapped in the dungeon, but we are not that cruel, we place and exit, somewhere in the Map. Randomly!  Well yeah that is a bit cruel
We make use of the placeExit() to place the exit within the GameMap under a destructible.

#### ConcurrentBombPowerUp
To work a bit smartly and reduce some code, we made use of Inheritance for this, so that this powerUp inherits from its SuperClass exit. The player will be taken by the player once the player walks over it, and with this powerUp the player can now keep one additional bomb at once.


#### BombBlastPowerUp
With this the radius of the Bomb increases by every additional tile in each direction.

#### SpeedPowerUp
Taking this powerUp will help the player to run faster, away from the bombs and the enemies. To check whether all these powerUps are taken we do that in the GameMap class, with the help of their respective helper methods used in tick().

#### CollisionDetector
We make use of the CollisionDetector Class, to detect whether the enemy and player have come into contact, and as a consequence set the isDead attribute to true. This class uses the ContactListener Interface.

#### Enemy
Here lies the Enemy and all the information regarding. Player Stay away from him !!!!

#### Flower
Flower represents the floor or the colorful ground of the dungeon, maybe it can turn iceCold with some icy enemies, or hot as a lava with others.

#### ExplosionSegment
Since we are surrounding by different kinds of obstacles, from breakable, to unbreakable walls, special, care needs to be taken while implementing the animation of Bomb, and for this we make use of the ExplosionSegment helper Class. 

### The screen package:

#### HUD (Heads Up Display)

We have a heads up display, that will keep track of the time, it will tell you on which level you are, in case of MultilevelMadness, the amount of powerUps you have and the score the player reached.

#### LostScreen (implements Screen)

This screen will be displayed in case of Player death or if the timer runs out. and it gives the option to restart the game, or go to the main menu.

#### PauseScreen (implements Screen)

If you need a break? just press the "escape" key, and it will take you to the pause screen. Where your entire game, and the progress will be paused for the time being. You will have the options to resume, quit or go to main menu.

#### VictoryScreen (implements Screen)

If there is a will, there is a way, and if you managed to see this screen, congrats! This screen will be displayed on winning any of the games, and will show the score that you achieved in your game.

#### Menu Screen (implements Screen)

This is the Screen which you will see when you run the game, and you will have a host of options to choose from, you can either play a quick game, or have a mini Challenge, or load your own map and play, or if you want to challenge and compete with your friends with Multiplayer, or if you want a real challenge, try out our MultiLevelMadness.
If the volume is too low or too loud, you can change it with the volume slider on the main menu.

#### MusicTrack
We have this enum where we used all of our sound effects. From small sound effects of player movement or powerUps taken, to the Main menu sounds.


### Bonus Section & Extra Functionality:

1. We have our enemies, which are bit smart, so they will move towards the player when the player is within a certain radius. We execute that out in the tick() of the Enemy Class.
2. Enemies are evil, ours even worse, they can also now place bombs
3. We have extra PowerUp for running faster, and for additional time, plus scattered them randomly each time. 
4. We have Multiplayer mode, which helps two people to play against each other. We carry most of this out with loadMultiplayer()
5. We also have a Dynamic timer, where the timer increases, if you kill the enemy.
6. Our MultiLevelMadness is an interesting game mode, where players directly pass to new map after beating one, and top of that have cool new visual aesthetic plus increased difficulty and more the number of enemies to defeat.
7. We also have a point Systems that rewards points after killing an enemy.
8. In the new Maps which are loaded, we have additionally scattered every single powerup, randomly accross the map, to make things more interesting. We do that in the GameMap Class with the randomly place powerUps methods.
9. We have an additional volume Slider that helps
10. We created three new extra Maps, just for the MultiLevel game, with cool new aesthetic and different enemies, from normal, to icey to firey warm. We do this implementation in the BomberQuestGame Class, with the help of multiLevelMaps(), level2Map(), level3Map() method. 






