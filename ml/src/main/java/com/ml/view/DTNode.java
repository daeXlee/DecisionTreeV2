package com.ml.view;

import java.util.HashMap;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

public class DTNode {
    
    private Button treeNode;
    private Point2D treeNodePoint;
    private TitledPane infoNode;
    private HashMap<DTNode, DTLine> children;
    private int totalChildren;
    private final int defaultRadius = 30;
    
    public DTNode(String attributeName, int totalChildren, Point2D nodePosition) {
        this.totalChildren = totalChildren;
        this.treeNode = new Button();
        this.treeNode.setStyle("-fx-background-radius: 100;");
        this.treeNode.setMinSize(defaultRadius, defaultRadius);
        this.treeNode.setMaxSize(defaultRadius, defaultRadius);
        this.treeNodePoint = nodePosition;
        this.treeNode.setLayoutX(nodePosition.getX());
        this.treeNode.setLayoutY(nodePosition.getY());

        this.infoNode = new TitledPane();
        this.infoNode.setText(attributeName);
        this.infoNode.setExpanded(true);
    }

    public void setSize(double radius) {
        treeNode.setPrefSize(radius, radius);
    }

    public Button getTreeNode() {
        return treeNode;
    }

    public TitledPane getInfoNode() {
        return infoNode;
    }

    public void addChildNode(String attributeName, int childNumber) {
        double xCoord = 0.0;
        double yCoord = this.treeNodePoint.getY() + this.treeNode.getHeight() + this.treeNode.getHeight() / 2; 

        if (totalChildren % 2 == 0) {
            if (childNumber <= totalChildren / 2) {
                xCoord = this.treeNodePoint.getX() + ((2 * childNumber) - totalChildren) * this.treeNode.getWidth();
            }
            else {
                xCoord = this.treeNodePoint.getX() + ((2 * childNumber + 1) - totalChildren) * this.treeNode.getWidth();
            }
        }
        else {
            // Odd Case
        }

        Point2D childNodePoint = new Point2D(xCoord, yCoord);

        this.children.put(new DTNode(attributeName, 2, childNodePoint), new DTLine(this.treeNodePoint, childNodePoint));
    }

    public HashMap<DTNode, DTLine> getChildren() {
        return this.children;
    }
}
