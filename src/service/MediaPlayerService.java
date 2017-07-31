package service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by RedShift on 3.9.2016..
 */
public class MediaPlayerService extends Service<Void> {

    private MediaPlayer player;
    private Media pick;
    private String songPath;
    public double songDuration;
    private String songName;
    private int stringPosition;
    public boolean playing ;
    private String songNameSpaces;
    public static final List<String> SUPPORTED_FILE_EXTENSIONS = Arrays.asList(".mp3", ".m4a");
    public static final int FILE_EXTENSION_LEN = 3;

    @Override
    protected Task<Void> createTask() {

        if (songPath == null) {
            throw new IllegalStateException();
        }
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                player.play();
                return null;
            }
        };
    }


    @Override
    public void start() {
        System.out.println("State: " + this.getState());

        if (this.getState() == State.READY) {
            super.start();
        } else {
            System.out.println("Cannot restart uprunning service.");
        }
    }

    public void stop() {
        player.stop();
        this.reset();
    }

    public void setSong(String songPath) {
        this.songPath = songPath;
    }

    public void preparePlayer() {
        System.out.println("Playing...");
        pick = new Media(songPath);
        player = new MediaPlayer(pick);
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                songName = player.getMedia().getSource();

                songDuration = pick.getDuration().toSeconds();
                stringPosition = pick.getSource().lastIndexOf('/' + 1);
                songNameSpaces = pick.getSource().substring(stringPosition);

                songName = songName.substring(0, songName.length() - FILE_EXTENSION_LEN);
                songName = songName.substring(songName.lastIndexOf("/") + 1).replaceAll("%20", " ");

                System.out.println("SONG NAME: " + songName);
                System.out.println("_________________________________________");
                System.out.println("PATH: " + songPath);
                System.out.println("DURATION: " + songDuration);
            }
        });
    }

    public Double getSongDuration() {
        if (player != null) {
            return songDuration;
        }
        return 0.0;
    }

    public String getSongName(){
        if (player != null) {
             return songName;
        }
        return "";
    }


}
