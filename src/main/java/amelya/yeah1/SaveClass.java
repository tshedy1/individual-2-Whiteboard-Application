package amelya.yeah1;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveClass {
    private HelloApplication app;

    public SaveClass(HelloApplication app) {
        this.app = app;
    }

    public void saveCanvas(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Canvas");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Get the root container that holds everything
                BorderPane root = (BorderPane) stage.getScene().getRoot();

                // Create snapshot parameters with transparent background
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(javafx.scene.paint.Color.TRANSPARENT);

                // Calculate the visible area (excluding toolbars)
                double contentWidth = app.canvas.getWidth();
                double contentHeight = app.canvas.getHeight();

                // Create image with the content dimensions
                WritableImage writableImage = new WritableImage(
                        (int) contentWidth,
                        (int) contentHeight
                );

                // Get the content pane that holds canvas and other elements
                Pane contentPane = (Pane) root.getCenter();

                // Take snapshot of just the content area
                contentPane.snapshot(params, writableImage);

                // Convert to BufferedImage
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                // Determine file format
                String extension = getFileExtension(file.getName());

                if ("png".equalsIgnoreCase(extension)) {
                    ImageIO.write(bufferedImage, "png", file);
                    app.showAlertDialog("Success", "Image saved successfully as PNG");
                }
                else if ("jpg".equalsIgnoreCase(extension)) {
                    // Convert to RGB for JPG (no transparency)
                    BufferedImage rgbImage = new BufferedImage(
                            bufferedImage.getWidth(),
                            bufferedImage.getHeight(),
                            BufferedImage.TYPE_INT_RGB
                    );
                    rgbImage.createGraphics().drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
                    ImageIO.write(rgbImage, "jpg", file);
                    app.showAlertDialog("Success", "Image saved successfully as JPG");
                }
                else {
                    app.showAlertDialog("Error", "Unsupported file format");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                app.showAlertDialog("Error", "Failed to save image: " + e.getMessage());
            }
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex > 0 && lastIndex < fileName.length() - 1) ?
                fileName.substring(lastIndex + 1).toLowerCase() : "";
    }
}