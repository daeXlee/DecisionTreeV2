package com.ml.view;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class DTLine {
    private Line treeLine;

    public DTLine(Point2D parentNodePoint, Point2D childNodePoint) {
        this.treeLine = new Line(parentNodePoint.getX(), parentNodePoint.getY(), childNodePoint.getX(), childNodePoint.getY());
    }

    public Line getLine() {
        return treeLine;
    }
}
