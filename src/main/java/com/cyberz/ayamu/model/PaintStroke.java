package com.cyberz.ayamu.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;


import java.util.ArrayList;
import java.util.List;

public class PaintStroke {
    private StrokeLineCap lineCap;
    private Color strokeColor;
    private double strokeSize;
    private List<double[]> points = new ArrayList<>();

    public PaintStroke(StrokeLineCap lineCap, Color strokeColor, double strokeSize) {
        this.lineCap = lineCap;
        this.strokeColor = strokeColor;
        this.strokeSize = strokeSize;
    }


    public void addPoint(double x, double y){
        points.add(new double[]{x,y});
    }

    public StrokeLineCap getLineCap() {
        return lineCap;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public List<double[]> getPoints() {
        return points;
    }
}
