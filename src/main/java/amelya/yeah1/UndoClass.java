package amelya.yeah1;

import javafx.scene.image.Image;

public class UndoClass
{
    private HelloApplication app;

    public UndoClass(HelloApplication app)
    {
        this.app = app;
    }

    public void undo()
    {
        //to do the conditions when the undoStack has something inside
        if (!app.undoStack.isEmpty())
        {
            //to allow the last state on the canvas to pop up when the undo button is clicked
            Image lastState = app.undoStack.pop();

            // Save the current state of the canvas to the redo stack
            app.redoStack.push(app.canvas.snapshot(null, null));

            //to clear the canvas and restore the last state of the canvas
            app.graphicsContent.clearRect(0, 0, app.canvas.getWidth(), app.canvas.getHeight());
            app.graphicsContent.drawImage(lastState, 0, 0);
        }

    }
}