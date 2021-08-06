import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Scorer {

    @FXML
    Label lbl_home, lbl_away, lbl_homescore, lbl_awayscore, lbl_homeovers, lbl_awayovers, lbl_runsrequired, lbl_runrate,
    lbl_bat1, lbl_bat2, lbl_bat1runs, lbl_bat2runs, lbl_bat1balls, lbl_bat2balls, lbl_bat1fours, lbl_bat2fours,
            lbl_bat1sixes, lbl_bat2sixes, lbl_bat1sr, lbl_bat2sr, lbl_partnershipruns, lbl_partnershipballs,
            lbl_partnershipfours, lbl_partnershipsixes, lbl_partnershipsr, lbl_bowler, lbl_bowlerovers,
            lbl_bowlermaidens, lbl_bowlerwickets, lbl_bowlerruns, lbl_bowlereconomy;

    public void initialize() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        lbl_homescore.setText("Emma");
        //MusicPlayer mp = new MusicPlayer();
        //mp.playMusic(1);

        Music mp = new Music();
        mp.playMusic();
    }
}
