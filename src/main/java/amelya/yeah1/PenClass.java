package amelya.yeah1;

public class PenClass
{
    private HelloApplication app;

    public PenClass(HelloApplication app) {
        this.app = app;
    }

    //method for the button pen
    public void togglePen()
    {
        //once the pen button is used, it's pen size container for adjusting the size of the pen appears
        app.penActive = !app.penActive;
        app.penSizeContainer.setVisible(app.penActive);
    }
}