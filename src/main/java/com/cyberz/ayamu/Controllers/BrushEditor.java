package com.cyberz.ayamu.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class BrushEditor {

    @FXML
    private ChoiceBox modePicker;

    @FXML
    private ColorPicker brushColorPicker;

    @FXML
    private ChoiceBox brushSizePicker;

    private double brushSize;
    private MainController mainController;
    private Color currentColor;
    private StrokeLineCap lineMode;
    ObservableList<Double> pixelBrushSize = FXCollections.observableArrayList(2.0, 4.0, 8.0, 10.0, 18.0, 24.0, 30.0, 40.0);
    ObservableList<StrokeLineCap> brushModesList = FXCollections.observableArrayList(StrokeLineCap.ROUND, StrokeLineCap.SQUARE, StrokeLineCap.BUTT);

    public void setMainController(MainController mainController, Color currentColor, double brushSize, StrokeLineCap lineMode) {
        this.mainController = mainController;
        this.currentColor = currentColor;
        this.brushSize = brushSize;
        this.lineMode = lineMode;

        brushSizePicker.setValue(this.brushSize);
        brushColorPicker.setValue(this.currentColor);
        modePicker.setValue(this.lineMode);

    }


    @FXML
    private void initialize() {
        modePicker.setItems(brushModesList);
        brushSizePicker.setItems(pixelBrushSize);
    }

    @FXML
    private void sizePicker(ActionEvent evt) {
        brushSize = (double) brushSizePicker.getValue();
        applyChangesBrushSize();
    }

    @FXML
    private void setLineMode(ActionEvent evt) {
        lineMode = (StrokeLineCap) modePicker.getValue();
        applyChangesBrushMode();
    }

    @FXML
    private void pickColor(ActionEvent evt) {
        if(mainController != null){
            mainController.updateBrushColor(brushColorPicker.getValue());
        }
    }


    @FXML
    private void applyChangesBrushSize() {
        // Only update if the mainController has actually been loaded - since initialize runs before the constructor
        if (mainController != null) {
            mainController.updateBrushSize((Double) brushSizePicker.getValue());
        }
    }

    @FXML
    private void applyChangesBrushMode() {
        if (mainController != null) {
            mainController.updateBrushMode(lineMode);
        }
    }


}
