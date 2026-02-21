package com.cyberz.ayamu.Controllers;


import com.cyberz.ayamu.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;


public class MainController {

    @FXML
    private Canvas canvasView;

    private Stage brushEditorStage = null;
    private Color brushColor = Color.GRAY;
    private StrokeLineCap lineMode = StrokeLineCap.ROUND;
    private double brushSize = 2.0;
    private double lastX, lastY;
    ObservableList<WritableImage> versionController = FXCollections.observableArrayList();
    GraphicsContext gc;

    @FXML
    private void initialize(){
        gc = canvasView.getGraphicsContext2D();
    }

    @FXML
    private void startPath(MouseEvent evt) {
        lastX = evt.getX();
        lastY = evt.getY();
    }

    @FXML
    private void drawCircle(MouseEvent evt){
        gc.setStroke(brushColor);
        gc.setLineWidth(brushSize);
        gc.setLineCap(lineMode);
        gc.strokeLine(lastX, lastY, evt.getX(), evt.getY());

        lastX = evt.getX();
        lastY = evt.getY();

    }

    //fires after onDragRelease event
    @FXML
    private void saveCanvasChange(){

        ///https://stackoverflow.com/questions/33988596/how-to-copy-contents-of-one-canvas-to-another

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = canvasView.snapshot(params, null);
        versionController.add(image);
    }


    @FXML
    private void onUndoButtonCombo(KeyEvent evt ){

        if(evt.getCode().equals(KeyCode.Z) && evt.isControlDown()){
            System.out.println("Pressing CRL + Z");
            doUndoChange();
        }

    }

    private void doUndoChange() {
        if (versionController.size() < 2) {
            canvasView.getGraphicsContext2D().clearRect(0, 0, canvasView.getWidth(), canvasView.getHeight());

            //prevents old changes from appearing
            versionController.clear();
            return;
        }

        versionController.remove(versionController.size() - 1);

        WritableImage previousState = versionController.get(versionController.size() - 1);
        GraphicsContext graphicsContext = canvasView.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasView.getWidth(), canvasView.getHeight());
        graphicsContext.drawImage(previousState, 0, 0);

        System.out.println("Undo successful. States remaining: " + versionController.size());
    }


    @FXML
    private void openBrushEditor() throws IOException {
        //Check if the window is already open
        if (brushEditorStage != null && brushEditorStage.isShowing()) {
            brushEditorStage.toFront();
            brushEditorStage.requestFocus();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("components/brushEditor.fxml"));
        Parent root = fxmlLoader.load();

        BrushEditor brushEditor = fxmlLoader.getController();
        brushEditor.setMainController(this, brushColor, brushSize, lineMode);

        brushEditorStage = new Stage();
        brushEditorStage.setScene(new Scene(root));
        brushEditorStage.setTitle("Edit brush");
        brushEditorStage.setResizable(false);

        Window mainWindow = canvasView.getScene().getWindow();
        brushEditorStage.initOwner(mainWindow);

        brushEditorStage.show();
    }


    @FXML
    private void saveCanvasImage() {
        FileChooser fileLoc = new FileChooser();
        fileLoc.setTitle("Save Picture");
        fileLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileLoc.showSaveDialog(Main.primaryStage);

        if (file != null) {
            try {

                WritableImage image = canvasView.snapshot(null, null);
                var bufferedImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
                javax.imageio.ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                System.err.println("Failed to save image: " + ex.getMessage());
            }
        }
    }

    //crossover functions used in other windows
    public void updateBrushSize(Double size) {
        brushSize = size;
    }

    public void updateBrushColor(Color color){
        brushColor = color;
    }

    public void updateBrushMode(StrokeLineCap cap){
        lineMode = cap;
    }

}
