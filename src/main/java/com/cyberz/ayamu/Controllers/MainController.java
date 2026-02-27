package com.cyberz.ayamu.Controllers;


import com.cyberz.ayamu.Main;
import com.cyberz.ayamu.misc.ConfirmChoiceWindow;
import com.cyberz.ayamu.model.PaintStroke;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainController {


    @FXML private Canvas canvasView;
    @FXML private Slider brushSizeSlider;

    private Stage brushEditorStage = null;
    private Color brushColor = Color.GRAY;
    private StrokeLineCap lineMode = StrokeLineCap.ROUND;
    private double brushSize = 2.0;
    private GraphicsContext gc;
    private List<PaintStroke> strokeHistory = new ArrayList<>();
    private PaintStroke currentStroke;


    @FXML
    private void initialize(){
        gc = canvasView.getGraphicsContext2D();

        canvasView.setOnMouseEntered(mouseEvent -> Main.mainScene.setCursor(Cursor.CROSSHAIR));

        if (brushSizeSlider != null) {
            brushSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                brushSize = newVal.doubleValue();
            });
        }

    }

    @FXML
    //onMousePressed
    private void startPath(MouseEvent evt) {
        currentStroke = new PaintStroke(lineMode, Color.valueOf(String.valueOf(brushColor)), brushSize);
        currentStroke.addPoint(evt.getX(), evt.getY());
        gc.setStroke(brushColor);
        gc.setLineWidth(brushSize);
        gc.setLineCap(lineMode);
        gc.beginPath();
        gc.moveTo(evt.getX(), evt.getY());
        gc.stroke();
    }


    @FXML
    //onMouseDragged
    private void drawCircle(MouseEvent evt){
        currentStroke.addPoint(evt.getX(), evt.getY());
        gc.lineTo(evt.getX(), evt.getY());
        gc.setStroke(brushColor);
        gc.setLineWidth(brushSize);
        gc.setLineCap(lineMode);
        gc.stroke();
    }


    @FXML
    //onMouseReleased
    private void saveCanvasChange(){
        strokeHistory.add(currentStroke);
    }

    private void redrawCanvas() {
        gc.clearRect(0, 0, canvasView.getWidth(), canvasView.getHeight());

        for (PaintStroke stroke : strokeHistory) {
            gc.setStroke(stroke.getStrokeColor());
            gc.setLineWidth(stroke.getStrokeSize());
            gc.setLineCap(stroke.getLineCap());
            gc.beginPath();

            List<double[]> points = stroke.getPoints();
            if (!points.isEmpty()) {
                gc.moveTo(points.get(0)[0], points.get(0)[1]);
                for (int i = 1; i < points.size(); i++) {
                    gc.lineTo(points.get(i)[0], points.get(i)[1]);
                }
                gc.stroke();
            }
        }
    }

    @FXML
    private void onUndoButtonCombo(KeyEvent evt ){
        if(evt.getCode().equals(KeyCode.Z) && evt.isControlDown()){
            if(!strokeHistory.isEmpty()){
                doUndoChange();
            }
        }
    }


    @FXML
    private void doUndoChange() {
        strokeHistory.remove(strokeHistory.size() - 1);
        redrawCanvas();
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

    @FXML
    private void eraseCanvasContents(){
        if(ConfirmChoiceWindow.showWindow("Are you sure you want to remove all contents on this canvas?")){
            canvasView.getGraphicsContext2D().clearRect(0, 0, canvasView.getWidth(), canvasView.getHeight());
        }


    }

    @FXML
    private void setBrushColor(ActionEvent evt){
        Button btn = (Button) evt.getSource();
        String hexCode = (String) btn.getUserData();

        brushColor = Color.valueOf(hexCode);

    }

    @FXML
    private void openColorPicker(){
        Dialog<Color> dialog = new Dialog<>();
        ColorPicker picker = new ColorPicker();

        dialog.getDialogPane().setContent(picker);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.setResultConverter(b -> {
            picker.getValue();
            brushColor = picker.getValue();
            return null;
        });
        dialog.showAndWait();
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
