package com.app.alieninvasion;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    Clip clip;
    URL[] urls = new URL[20];

    public Sound(){
        urls[0] = getClass().getResource("/Audio/turret_place_upgrade.wav");
        urls[1] = getClass().getResource("/Audio/turret_break.wav");
        urls[2] = getClass().getResource("/Audio/game_over.wav");
        urls[3] = getClass().getResource("/Audio/next_wave.wav");
        urls[4] = getClass().getResource("/Audio/turret_upgrade.wav");
        urls[5] = getClass().getResource("/Audio/error.wav");
        urls[6] = getClass().getResource("/Audio/BG1.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(urls[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch (Exception ignored){}
    }
    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

}
