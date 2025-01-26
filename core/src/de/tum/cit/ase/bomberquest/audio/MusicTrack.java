package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * Currently, only one track is used, but this could be extended to include multiple tracks.
 * Using an enum for this purpose is a good practice, as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 * See the assets/audio folder for the actual music files.
 * Feel free to add your own music tracks and use them in the game!
 */
public enum MusicTrack {

    MENU_BGM("MenuBGM.mp3", 0.15f,false),
    PLAYER_MOVE1("Player_moving1.mp3", 0.15f,true),
    PLAYER_MOVE2("Player_moving2.mp3", 0.15f,true),
    Level_THEME("Level-Theme.mp3", 0.15f,false),
    Level_THEME2("Level-Theme2.mp3", 0.15f,false),
    LEVEL_COMPLETED("Level-Completed.mp3", 0.15f,false),
    PLAYER_DEMISE("Player_Demise.mp3", 0.15f,false),
    BOMB_PLANT("Bomb_plantsfx.mp3",0.15f,false),
    BOMB_EXPLOSION("Bomb_explosionsfx.mp3",0.15f,false),
    POWERUP_TAKEN("ConBombPowerUp.mp3",0.15f,false),
    GAME_PAUSE("GamePauseSFX.mp3",0.15f,false),
    GAME_OVER("GameOver.mp3",0.15f,false);

    /** The music file owned by this variant. */
    private final Music music;

    MusicTrack(String fileName, float volume, boolean loop) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(loop);
        this.music.setVolume(volume);
    }

    /**
     * Play this music track.
     * This will not stop other music from playing - if you add more tracks, you will have to handle that yourself.
     */
    public void play() {
        this.music.play();
    }
    public void stop() {
        this.music.stop();
    }
}
