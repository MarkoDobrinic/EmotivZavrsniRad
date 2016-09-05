package model;

import javafx.scene.control.TextField;

import java.util.List;

/**
 * Created by RedShift on 3.9.2016..
 */
public class EmotivTest {

    private int id;
    private int baselineId;
    private String description;
    private String genre;
    private String songname;
    private String artist;
    private List<EmotivTestMeasure> measures;
    private Double songDuration;

    public Double getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(Double songDuration) {
        this.songDuration = songDuration;
    }

    public List<EmotivTestMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<EmotivTestMeasure> measures) {
        this.measures = measures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(int baselineId) {
        this.baselineId = baselineId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "EmotivTest{" +
                "id=" + id +
                ", baselineId=" + baselineId +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", songname='" + songname + '\'' +
                ", artist='" + artist + '\'' +
                ", measures=" + measures +
                ", songDuration=" + songDuration +
                '}';
    }
}
