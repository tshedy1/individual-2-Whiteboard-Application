package amelya.yeah1;

import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;

public class RemoveClass
{
    private HelloApplication app;

    public RemoveClass(HelloApplication app)
    {
        this.app = app;
    }

    //to remove everything that has been placed on the canvas, either drawing or media(image, audio, video)
    public void removeLastElement(BorderPane layout)
    {
        if (app.addedElements.isEmpty())
        {
            if (app.confirmAction("Remove Drawing", "Do you want to remove all drawings?")) {
                app.graphicsContent.clearRect(0, 0, app.canvas.getWidth(), app.canvas.getHeight());
            }
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Text", "Text", "Image", "Media", "Drawing");
        dialog.setTitle("Remove Element");
        dialog.setHeaderText("Choose what you want to remove:");
        dialog.setContentText("Element:");

        dialog.showAndWait().ifPresent(choice ->
        {
            switch (choice)
            {
                case "Text":
                    removeElementByType(layout, Pane.class);
                    break;
                case "Image":
                    removeElementByType(layout, ImageView.class);
                    break;
                case "Media":
                    removeElementByType(layout, StackPane.class);
                    break;
                case "Drawing":
                    if (app.confirmAction("Remove Drawing", "Do you want to remove all drawings?"))
                    {
                        app.graphicsContent.clearRect(0, 0, app.canvas.getWidth(), app.canvas.getHeight());
                    }
                    break;
            }
        });
    }

    private void removeElementByType(BorderPane layout, Class<?> type)
    {
        for (int i = app.addedElements.size() - 1; i >= 0; i--)
        {
            Node node = app.addedElements.get(i);

            if (type.isInstance(node))
            {
                layout.getChildren().remove(node);
                app.addedElements.remove(i);
                break;
            }
        }
    }
}