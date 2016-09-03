package service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by RedShift on 3.9.2016..
 */
public class MediaPlayerService extends Service<Void> {

    private MediaPlayer player;
    private Media pick;
    private String songPath;
    public double songDuration;

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
                songDuration = pick.getDuration().toSeconds();
                System.out.println("duration" + pick.getDuration().toSeconds());
            }
        });
    }

    public Double getSongDuration() {
        if (player != null) {
            return songDuration;
        }
        return 0.0;
    }
}
