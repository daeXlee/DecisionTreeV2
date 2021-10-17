package com.ml.view;

import java.util.List;

import com.ml.DecisionTree;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DecisionTreeGUI extends Canvas {
    private DecisionTree decisionTree;
    private List<DTNode> dtNodes;

    public DecisionTreeGUI(DecisionTree decisionTree) {
        this.decisionTree = decisionTree;
        widthProperty().addListener(event -> drawTree());
        heightProperty().addListener(event -> drawTree());

        drawTree();
    }

    public void drawTree() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        TreeNodeGUI root = new TreeNodeGUI(decisionTree.getRoot());
        int treeDepth = decisionTree.getDepth();

        System.out.println(this.getWidth() + "," + this.getWidth());
        drawTree(gc, root, 0, this.getWidth(), 0, this.getWidth() / treeDepth);
    }

    private void drawTree(GraphicsContext gc, TreeNodeGUI node, double xMin, double xMax, double yMin, double yMax) {
        Point2D point = new Point2D((xMin + xMax) / 2, (yMin + yMax) / 2);
        node.setPoint(point);
        node.draw(gc);
    }
}
