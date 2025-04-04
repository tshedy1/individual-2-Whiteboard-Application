package amelya.yeah1;

import javafx.scene.Cursor;

public class EraserClass
{
    private HelloApplication app;

    public EraserClass(HelloApplication app)
    {
        this.app = app;
    }

    public void toggleEraser()
    {
        app.eraserActive = !app.eraserActive;
        app.eraserSizeContainer.setVisible(app.eraserActive);

        //if the pen is not active, then the cursor is set to eraser cursor
        if (app.eraserActive)
        {
            app.penActive = false;
            app.penSizeContainer.setVisible(false);
            app.canvas.setCursor(app.eraserCursor);
        }
        else
        {
            //if the eraser is not active, then the cursor is set to crosshair(to allow for drawing) cursor
            app.canvas.setCursor(Cursor.CROSSHAIR);
        }
    }
}