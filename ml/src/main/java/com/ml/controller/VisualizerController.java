package com.ml.controller;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ml.AttributeInfo;
import com.ml.DecisionTree;
import com.ml.view.*;
import com.opencsv.CSVReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VisualizerController implements Initializable{

    private DecisionTreeGUI dt;

    @FXML
    private BorderPane rootContainer;
    @FXML
    private VBox infoGroup;
    @FXML
    private Label header;

    public VisualizerController(DecisionTree dt) {
        this.dt = new DecisionTreeGUI(dt);
    }

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
        Pane wrapperPane = new Pane();
        rootContainer.setCenter(wrapperPane);
        
        wrapperPane.getChildren().add(dt);
        dt.widthProperty().bind(wrapperPane.widthProperty());
        dt.heightProperty().bind(wrapperPane.heightProperty());
    }
}
