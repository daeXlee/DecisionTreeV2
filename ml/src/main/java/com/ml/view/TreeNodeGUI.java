package com.ml.view;

import com.ml.DecisionTree.Node;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class TreeNodeGUI {
    
    private final int radius = 5;
    private Point2D point;
    private Circle circle;
    
    public TreeNodeGUI(Node node) {
        this.circle = new Circle(radius);
        this.circle.setStrokeWidth(5);
        // TODO: Set Node Info
    }

    public void setPoint(Point2D point) {
        this.point = point;
        circle.setCenterX(point.getX());
        circle.setCenterY(point.getY());
    }

    public Circle getTreeNode() {
        return this.circle;
    }
}
