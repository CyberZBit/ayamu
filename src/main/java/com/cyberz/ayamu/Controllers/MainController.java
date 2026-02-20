package com.cyberz.ayamu.Controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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


public class MainController {

    private double brushSize = 2.0;
    private double lastX, lastY;
    ObservableList<Double> pixelBrushSize = FXCollections.observableArrayList(2.0,4.0,8.0,10.0,18.0,24.0,30.0,40.0);
    ObservableList<WritableImage> versionController = FXCollections.observableArrayList();
    GraphicsContext gc;

    @FXML
    private Canvas canvasView;
    @FXML
    private ColorPicker colorPickerFC;
    @FXML
    private ChoiceBox paintSize;

    @FXML
    private void initialize(){
        paintSize.setItems(pixelBrushSize);
        gc = canvasView.getGraphicsContext2D();
    }

    @FXML
    private void startPath(MouseEvent evt) {
        lastX = evt.getX();
        lastY = evt.getY();
    }

    @FXML
    private void drawCircle(MouseEvent evt){
        gc.setStroke(colorPickerFC.getValue());
        gc.setLineWidth(brushSize);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(lastX, lastY, evt.getX(), evt.getY());

        lastX = evt.getX();
        lastY = evt.getY();

    }


    @FXML
    private void sizePicker(ActionEvent evt){
        brushSize = (double) paintSize.getValue();
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

}
