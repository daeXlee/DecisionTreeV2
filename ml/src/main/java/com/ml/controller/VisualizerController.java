package com.ml.controller;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ml.AttributeInfo;
import com.ml.DecisionTree;
import com.ml.view.*;
import com.opencsv.CSVReader;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VisualizerController implements Initializable{

    private DecisionTree dt;

    @FXML
    private BorderPane root;

    @FXML
    private VBox infoGroup;

    @FXML
    private Label header;

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        // Switch Scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../Home.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        DTNode rootNode = new DTNode("Root", 2, new Point2D(331, 40));
        root.setCenter(rootNode.getTreeNode());
        
        Platform.runLater(() -> {
            dt.printTree();
        });
        // dt.printTree();
        // rootNode.addChildNode("Child 1", 0);
        // Map.Entry<DTNode, DTLine> childNode = rootNode.getChildren().entrySet().iterator().next();
        // DTNode childTreeNode = childNode.getKey();
        // DTLine childTreeLIne = childNode.getValue();

        // treeGroup.getChildren().add(rootNode.getTreeNode());
        // treeGroup.getChildren().add(childTreeNode.getTreeNode());
        // treeGroup.getChildren().add(childTreeLIne.getLine());


        // infoGroup.getChildren().add(rootNode.getInfoNode());
    }

    public void setDecisionTreeData(String datasetFilePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(datasetFilePath));
            List<AttributeInfo> attributeInfos = new ArrayList<>();
            List<String[]> linesInCSV = reader.readAll();
            String[] headerLine = linesInCSV.get(0);
            linesInCSV.remove(0);
    
            for (String attributeName : headerLine) {
                attributeInfos.add(new AttributeInfo(attributeName));
            }
    
            for (String[] lineInCSV : linesInCSV) {
                for (int i = 0; i < lineInCSV.length; i++) {
                    attributeInfos.get(i).addValue(lineInCSV[i]);
                }
            }
            dt = new DecisionTree(linesInCSV, attributeInfos, 4);
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
