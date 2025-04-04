package amelya.yeah1;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HelloApplication extends Application
{
    Canvas canvas;
    GraphicsContext graphicsContent;
    ColorPicker colorPicker;
    ImageCursor eraserCursor;
    Stack<Image> undoStack = new Stack<>();
    Stack<Image> redoStack = new Stack<>();
    List<Node> addedElements = new ArrayList<>();

    Slider penSizeSlider;
    Label penSizeLabel;
    HBox penSizeContainer;

    Slider eraserSizeSlider;
    Label eraserSizeLabel;
    HBox eraserSizeContainer;

    boolean penActive = false;
    boolean eraserActive = false;

    Button eraserButton;
    Button penButton;
    Button activeButton = null;
    Button removeButton;
    Button saveButton;
    Button textButton;
    Button imageButton;
    Button mediaButton;
    Button redoButton;
    Button undoButton;

    //Instance of helper classes
    private RemoveClass removeClass;
    private PenClass penClass;
    private EraserClass eraserClass;
    private AddMediaClass addMediaClass;
    private AddTextClass addTextClass;
    private AddImageClass addImageClass;
    private SaveClass saveClass;
    private UndoClass undoClass;
    private RedoClass redoClass;

    @Override
    public void start(Stage stage)
    {
        // Initializing helper classes
        removeClass = new RemoveClass(this);
        penClass = new PenClass(this);
        eraserClass = new EraserClass(this);
        addMediaClass = new AddMediaClass(this);
        addTextClass = new AddTextClass(this);
        addImageClass = new AddImageClass(this);
        saveClass = new SaveClass(this);
        undoClass = new UndoClass(this);
        redoClass = new RedoClass(this);

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f4f4f4;");

        //for setting up the canvas
        canvas = new Canvas(800, 600);
        graphicsContent = canvas.getGraphicsContext2D();
        graphicsContent.setLineWidth(5);
        eraserCursor = createEraserCursor();

        // Toolbars, the left anf the top one
        HBox topToolbar = createTopToolbar(stage, layout);
        VBox sideToolBar = createSideToolbar();
        layout.setLeft(sideToolBar);
        layout.setTop(topToolbar);
        layout.setCenter(canvas);

        //for Drawing
        setupDrawing();

        Scene scene = new Scene(layout, 900, 700);
        addKeyboardShortcuts(scene);

        stage.setTitle("Interactive Digital Whiteboard Application");
        stage.setScene(scene);
        stage.show();
    }

    //to allow use of ctrl + or ctrl + y
    private void addKeyboardShortcuts(Scene scene)
    {
        scene.setOnKeyPressed(event ->
        {
            if (event.isControlDown())
            {
                //to call the function undo from undoClass when keycode ctrl + Z is clicked
                if (event.getCode() == KeyCode.Z)
                {
                    //
                    undoClass.undo();
                }
                //to call the function redo from redoClass when keycode ctrl + Y is clicked
                else if (event.getCode() == KeyCode.Y)
                {
                    redoClass.redo();
                }
            }
        });
    }

    //to place the buttons that have been created
    private VBox createSideToolbar()
    {
        VBox sideToolbar = new VBox(5);

        //how the appearance of the side Tool bar should be
        sideToolbar.setStyle("-fx-padding: 10px; -fx-background-color: lightblue; -fx-spacing: 15px; " +
                "-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        eraserButton = createButton("Eraser", e ->
        {
            eraserClass.toggleEraser();
            setActiveButton(eraserButton);
            if (penActive)
            {
                penClass.togglePen();
                resetButtonStyle(penButton);
            }
        });

        penButton = createButton("Pen", e -> {
            penClass.togglePen();
            setActiveButton(penButton);
            if (eraserActive) {
                eraserClass.toggleEraser();
                resetButtonStyle(eraserButton);
            }
        });

        sideToolbar.getChildren().addAll(eraserButton, penButton);
        return sideToolbar;
    }

    //to place the buttons that have been created
    private HBox createTopToolbar(Stage stage, BorderPane layout)
    {
        HBox topToolbar = new HBox(20);

        //how the toolBar should be, its appearance
        topToolbar.setStyle("-fx-padding: 10px; -fx-background-color: lightblue; -fx-spacing: 10px; " +
                "-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 10px;");

        //buttons on the topToolBar
        removeButton = createButton("Remove", e ->
        {
            removeClass.removeLastElement(layout);
            setActiveButton(null);
        });

        saveButton = createButton("Save", e ->
        {
            saveClass.saveCanvas(stage);
            setActiveButton(null);
        });

        textButton = createButton("Add Text", e ->
        {
            addTextClass.addText(layout);
            setActiveButton(textButton);
        });

        imageButton = createButton("Add Image", e ->
        {
            addImageClass.addImage(layout, stage);
            setActiveButton(imageButton);
        });

        mediaButton = createButton("Add Media", e ->
        {
            addMediaClass.addMedia(layout, stage);
            setActiveButton(mediaButton);
        });

        undoButton = createButton("Undo", e ->
        {
            undoClass.undo();
            setActiveButton(null);
        });

        redoButton = createButton("Redo", e ->
        {
            redoClass.redo();
            setActiveButton(null);
        });

        //function call for methods that set up the eraser and pen size
        setupPenSizeControl();
        setupEraserSizeControl();

        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setStyle("-fx-background-color: skyblue; -fx-border-color: #007bff; -fx-border-width: 2px; -fx-border-radius: 5px;");

        topToolbar.getChildren().addAll(removeButton, saveButton, textButton, imageButton, mediaButton, undoButton, redoButton, colorPicker, eraserSizeContainer, penSizeContainer);
        return topToolbar;
    }

    //to create the button to be used, how they should be when the mouse is placed on and exited
    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler)
    {
        Button button = new Button(text);

        button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-radius: 5px;");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white;"));

        button.setOnMouseExited(e ->
            {
                if (button != activeButton)
                {
                    button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; " +
                            "-fx-padding: 5px 15px; -fx-background-radius: 5px; -fx-border-width: 0;");
                }
            });

            button.setOnAction(handler);
            return button;
    }

    private void setActiveButton(Button button)
    {
        // Reset previous active button if exists
        if (activeButton != null)
        {
            resetButtonStyle(activeButton);
        }

        // Set new active button
        activeButton = button;

        if (activeButton != null)
        {
            activeButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-size: 16px; " +
                    "-fx-padding: 7px 17px; -fx-background-radius: 5px; " +
                    "-fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 5px;");
        }
    }

    private void resetButtonStyle(Button button)
    {
        button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-padding: 5px 15px; -fx-background-radius: 5px; -fx-border-width: 0;");
    }

    //to allow the drawing, also how the mouse should be during drawing
    private void setupDrawing()
    {
        //to set the mouse to cross(+) when drawing
        canvas.setCursor(Cursor.CROSSHAIR);

        //to allow the mouse the draw when pressed when the colorPicker is used
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e ->
        {
            if (penActive)
            {
                //to save the current canvas state before drawing
                saveCanvasState();

                //to allow the mouse to draw the lines(strokes) using the chosen color and stroke size
                //to allow the mouse to begin a new path when drawing, by using the selected color
                graphicsContent.beginPath();
                graphicsContent.moveTo(e.getX(), e.getY());
                graphicsContent.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e ->
        {
            if (penActive)
            {
                graphicsContent.setStroke(colorPicker.getValue());
                graphicsContent.lineTo(e.getX(), e.getY());
                graphicsContent.stroke();

            }
            //to allow the mouse to clear whatever is on the canvas when the eraser button is selected
            else if (eraserActive)
            {
                graphicsContent.clearRect(e.getX() - 10, e.getY() - 10, 20, 20);
            }
        });
    }

    //method for the slider to increase and decrease the pen size
    private void setupPenSizeControl()
    {
        penSizeSlider = new Slider(1, 20, 5);
        penSizeSlider.setShowTickMarks(true);
        penSizeSlider.setShowTickLabels(true);
        penSizeSlider.setMajorTickUnit(1);
        penSizeSlider.setBlockIncrement(1);

        penSizeLabel = new Label("Size: " + (int) penSizeSlider.getValue());

        penSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            graphicsContent.setLineWidth(newVal.doubleValue());
            penSizeLabel.setText("Size: " + newVal.intValue());
        });

        penSizeContainer = new HBox(5, penSizeLabel, penSizeSlider);
        penSizeContainer.setPadding(new Insets(5));
        penSizeContainer.setVisible(false);
    }

    //method for the slider to increase and decrease the eraser size
    private void setupEraserSizeControl() {
        eraserSizeSlider = new Slider(1, 20, 5);
        eraserSizeSlider.setShowTickMarks(true);
        eraserSizeSlider.setShowTickLabels(true);
        eraserSizeSlider.setMajorTickUnit(1);
        eraserSizeSlider.setBlockIncrement(1);

        eraserSizeLabel = new Label("Size: " + (int) eraserSizeSlider.getValue());

        eraserSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            graphicsContent.setLineWidth(newVal.doubleValue());
            eraserSizeLabel.setText("Size: " + newVal.intValue());
        });

        eraserSizeContainer = new HBox(5, eraserSizeLabel, eraserSizeSlider);
        eraserSizeContainer.setPadding(new Insets(5));
        eraserSizeContainer.setVisible(false);
    }

    private ImageCursor createEraserCursor()
    {
        //size of the cursor
        int size = 24;

        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics2D = bufferedImage.createGraphics();

        // Set eraser color to white
        graphics2D.setColor(java.awt.Color.WHITE);
        graphics2D.fillRect(0, 0, size, size);

        // Draw eraser in circle form
        graphics2D.setColor(java.awt.Color.BLACK);
        graphics2D.drawOval(2, 2, size - 4, size - 4);

        // Draw cross inside the circle
        graphics2D.drawLine(size / 2, 4, size / 2, size - 4);
        graphics2D.drawLine(4, size / 2, size - 4, size / 2);

        graphics2D.dispose();

        //Convert the drawn eraser to JavaFX Image
        Image eraserImage = SwingFXUtils.toFXImage(bufferedImage, null);

        return new ImageCursor(eraserImage, size / 2.0, size / 2.0);
    }

    //to save the current state of the canvas
    public void saveCanvasState()
    {
        // Save the current state of the canvas (as an Image)
        Image currentImage = canvas.snapshot(null, null);

        //to return to the current state of the canvas being currentImage when the button undo is clicked
        undoStack.push(currentImage);

        //Clear all the items in the redo stack whenever a new drawing is made
        redoStack.clear();
    }

    //the alert dialog to be displayed for confirmation
    public boolean confirmAction(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    //the alert dialog to be displayed for alerts in errors or message
    public void showAlertDialog(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

