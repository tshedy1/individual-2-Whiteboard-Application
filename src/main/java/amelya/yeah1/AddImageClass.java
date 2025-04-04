package amelya.yeah1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddImageClass
{
    private HelloApplication app;

    public AddImageClass(HelloApplication app) {
        this.app = app;
    }

    //th method for adding image when the button add image is used
    public void addImage(BorderPane layout, Stage stage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            //to allow the image to be dragged
            imageView.setOnMousePressed(event ->
            {
                imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getLayoutX(), imageView.getLayoutY(), imageView.getRotate()});
            });

            //to allow the image to rotate
            imageView.setOnMouseDragged(event ->
            {
                double[] data = (double[]) imageView.getUserData();

                if (event.isPrimaryButtonDown())
                {
                    double deltaX = event.getSceneX() - data[0];
                    double deltaY = event.getSceneY() - data[1];

                    imageView.setLayoutX(data[2] + deltaX);
                    imageView.setLayoutY(data[3] + deltaY);
                }
                else if (event.isSecondaryButtonDown())
                {
                    double angle = event.getSceneX() - data[0]; // Rotation angle based on X movement
                    imageView.setRotate(data[4] + angle * 0.5); // Adjust rotation sensitivity
                }
            });

            //to allow the resizing of the image with scroll wheel
            imageView.setOnScroll(event ->
            {
                double scaleFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;
                imageView.setFitWidth(imageView.getFitWidth() * scaleFactor);
            });
            app.addedElements.add(imageView);
            layout.getChildren().add(imageView);
        }
    }
}