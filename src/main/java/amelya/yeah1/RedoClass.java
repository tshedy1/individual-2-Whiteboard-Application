package amelya.yeah1;

import javafx.scene.image.Image;

public class RedoClass
{
    private HelloApplication app;

    public RedoClass(HelloApplication app)
    {
        this.app = app;
    }

    //method for the button redo
    public void redo()
    {
        if (!app.redoStack.isEmpty())
        {
            //to allow the last state on the canvas which was undone to pop up when the redo button is clicked
            Image lastUndoneState = app.redoStack.pop();

            // Save the current state of the canvas to the undo stack
            app.undoStack.push(app.canvas.snapshot(null, null));

            //to clear the canvas and restore the last state of the canvas which was undone
            app.graphicsContent.clearRect(0, 0, app.canvas.getWidth(), app.canvas.getHeight());
            app.graphicsContent.drawImage(lastUndoneState, 0, 0);
        }
    }
}
