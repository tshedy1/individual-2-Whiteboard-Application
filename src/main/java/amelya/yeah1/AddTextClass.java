package amelya.yeah1;

import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class AddTextClass {
    private HelloApplication app;

    public AddTextClass(HelloApplication app) {
        this.app = app;
    }

    public void addText(BorderPane layout)
    {
        Label textLabel = new Label("Double-click to edit");
        textLabel.setFont(new Font(20));
        textLabel.setTextFill(app.colorPicker.getValue());
        textLabel.setStyle("-fx-border-color: #007bff; -fx-padding: 5px; -fx-background-color: white;");
        textLabel.setLayoutX(100);
        textLabel.setLayoutY(100);
        textLabel.setCursor(Cursor.MOVE);

        // Make label draggable
        makeDraggable(textLabel);

        // Add context menu for resizing
        ContextMenu contextMenu = new ContextMenu();
        MenuItem increaseSize = new MenuItem("Increase Font Size");
        MenuItem decreaseSize = new MenuItem("Decrease Font Size");
        MenuItem editText = new MenuItem("Edit Text");

        increaseSize.setOnAction(e -> {
            textLabel.setFont(new Font(textLabel.getFont().getSize() + 2));
        });

        decreaseSize.setOnAction(e -> {
            double newSize = textLabel.getFont().getSize() - 2;
            if (newSize >= 8) {
                textLabel.setFont(new Font(newSize));
            }
        });

        editText.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(textLabel.getText());
            dialog.setTitle("Edit Text");
            dialog.setHeaderText("Enter new text:");
            dialog.showAndWait().ifPresent(textLabel::setText);
        });

        removeText.setOnAction(e -> {
            Pane container = (Pane) textLabel.getParent();
            if (container != null) {
                container.getChildren().remove(textLabel);
            }
        });
        
        contextMenu.getItems().addAll(editText, increaseSize, decreaseSize);

        textLabel.setOnContextMenuRequested(e -> contextMenu.show(textLabel, e.getScreenX(), e.getScreenY()));

        // Double click to edit
        textLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TextInputDialog dialog = new TextInputDialog(textLabel.getText());
                dialog.setTitle("Edit Text");
                dialog.setHeaderText("Enter new text:");
                dialog.showAndWait().ifPresent(textLabel::setText);
            }
        });

        // Ensure we have a proper container for canvas and text elements
        if (!(layout.getCenter() instanceof Pane)) {
            Pane container = new Pane(app.canvas);
            layout.setCenter(container);
        }

        Pane container = (Pane) layout.getCenter();
        container.getChildren().add(textLabel);
        app.addedElements.add(container);

        // Save the state after adding text
        app.saveCanvasState();
    }

    private void makeDraggable(Label label) {
        final double[] offset = new double[2];

        label.setOnMousePressed(e -> {
            offset[0] = e.getSceneX() - label.getLayoutX();
            offset[1] = e.getSceneY() - label.getLayoutY();
        });

        label.setOnMouseDragged(e -> {
            label.setLayoutX(e.getSceneX() - offset[0]);
            label.setLayoutY(e.getSceneY() - offset[1]);
        });
    }
}
