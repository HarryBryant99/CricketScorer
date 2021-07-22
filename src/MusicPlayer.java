// Java program to play an Audio
// file using Clip Object
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
    public MusicPlayer() {
        //playMusic();
    }

    public void playMusic(int type){
        String folder = null;
        int number = 0;
        switch(type) {
            case 0 :
                folder = "Boundary";
                number = getRandomNumberInRange(55);
                break; // optional
            case 1 :
                folder = "Over";
                number = getRandomNumberInRange(45);
                break; // optional
            case 2 :
                folder = "Wicket";
                number = getRandomNumberInRange(34);
                break; // optional
            default : // Optional
                // Statements
        }

        InputStream music;
        String filepath = "Music/" + folder + number + ".wav";

        try {
            music = new FileInputStream(new File(filepath));
            AudioStream audio = new AudioStream(music);
            AudioPlayer.player.start(audio);
        } catch (Exception e){

        }
    }

    private static int getRandomNumberInRange(int max) {
        Random r = new Random();
        return r.nextInt((max - 1) + 1) + 1;
    }
}