package com.ml.view;

import java.util.List;

import com.ml.DecisionTree;
import com.ml.DecisionTree.Node;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class DecisionTreeGUI extends Pane {
    private DecisionTree decisionTree;
    private double yInterval;
    private List<DTNode> dtNodes;

    public DecisionTreeGUI(DecisionTree decisionTree) {
        this.decisionTree = decisionTree;
        widthProperty().addListener(event -> drawTree());
        heightProperty().addListener(event -> drawTree());

        drawTree();
    }

    public void drawTree() {
        getChildren().clear();   
        this.yInterval = (this.getHeight() - 50) / decisionTree.getDepth();   
        drawTreeNode(this.decisionTree.getRoot(), 0, this.getWidth(), 0, null);
        // drawTree(root, 0, this.getWidth(), 0, this.getWidth() / treeDepth);
    }

    private void drawTreeNode(Node node, double xMin, double xMax, int currentDepth, Point2D parentPoint) {
        Point2D point = new Point2D((xMin + xMax) / 2, (2 * currentDepth * this.yInterval + this.yInterval) / 2);
        TreeNodeGUI guiNode = new TreeNodeGUI(node);
        guiNode.setPoint(point);
        getChildren().add(guiNode.getTreeNode());
        
        if (parentPoint != null) {
            drawLine(parentPoint, point);
        }

        int childIndex = 0;
        double xInterval = xMax / node.children.size();

        for (Node childNode : node.children.values()) {
            double childNodeXMin = xMin + childIndex * xInterval;
            drawTreeNode(childNode, childNodeXMin, childNodeXMin + xInterval, currentDepth + 1, point);
            childIndex++;
        }
    }

    private void drawLine(Point2D point1, Point2D point2) {
        Line line = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        getChildren().add(line);
    }
}
