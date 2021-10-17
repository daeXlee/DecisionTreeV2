package com.ml.view;

import com.ml.DecisionTree.Node;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class TreeNodeGUI {
    
    private final int radius = 30;
    private Point2D point;
    
    public TreeNodeGUI(Node node) {
        this.point = point;
        // TODO: Set Node Info
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(3);
        gc.strokeOval(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);
    }
}
