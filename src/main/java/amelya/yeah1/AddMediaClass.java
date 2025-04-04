package amelya.yeah1;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddMediaClass
{
    private HelloApplication app;

    public AddMediaClass(HelloApplication app) {
        this.app = app;
    }

    //to add media, being audio and video when button add media is clicked
    public void addMedia(BorderPane layout, Stage stage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.mp3"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            //the condition for when the media with extension .mp4 is selected
            if (file.getName().endsWith(".mp4"))
            {
                // Create MediaView for video
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(300); // Adjust the size as needed
                mediaView.setPreserveRatio(true);

                // Create a StackPane to center the MediaView
                StackPane mediaContainer = new StackPane(mediaView);
                mediaContainer.setAlignment(Pos.CENTER); // Center the MediaView

                // Add play/pause functionality on mouse click
                mediaView.setOnMouseClicked(event ->
                {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
                    {
                        mediaPlayer.pause(); // Pause if currently playing
                    }
                    else
                    {
                        mediaPlayer.play(); // Play if paused or stopped
                    }
                });

                //to allow for the removal of media
                app.addedElements.add(mediaContainer);
                // Set the MediaView in the center of the layout
                layout.setCenter(mediaContainer);

            }
            //the condition for when the media with extension .mp3 is selected
            else if (file.getName().endsWith(".mp3"))
            {
                // For audio files, create a simple play/pause button
                Button playPauseButton = new Button("Play");

                playPauseButton.setOnAction(event ->
                {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        //to pause audio if played
                        mediaPlayer.pause();

                        //if the audio is paused, the button text changes to Play
                        playPauseButton.setText("Play");
                    }
                    else
                    {
                        //to play audio if paused or stopped
                        mediaPlayer.play();

                        //if the audio is played, the button text changes to Pause
                        playPauseButton.setText("Pause");
                    }
                });

                // Center the button in the layout
                StackPane mediaContainer = new StackPane(playPauseButton);
                mediaContainer.setAlignment(Pos.CENTER);

                //to allow for the removal of media
                app.addedElements.add(mediaContainer);
                layout.setCenter(mediaContainer);
            }
        }
    }
}