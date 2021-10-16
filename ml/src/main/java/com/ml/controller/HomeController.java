package com.ml.controller;

import com.ml.*;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.opencsv.CSVReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    private String datasetFilePath;

    @FXML
    private Label label;

    @FXML
    private Label errorLabel;

    @FXML
    private AnchorPane anchorid;

    @FXML
    private TextField textField;

    @FXML
    private void handleBrowseButtonAction(ActionEvent event) {
        final FileChooser fChooser = new FileChooser();
        Stage stage = (Stage)anchorid.getScene().getWindow();
        File datasetFile = fChooser.showOpenDialog(stage);
        errorLabel.setVisible(false);

        if (datasetFile != null) {
            datasetFilePath = datasetFile.getAbsolutePath();
            textField.setText(datasetFilePath);
        }
    }

    @FXML
    private void handleProcessButtonAction(ActionEvent event) {
        try {
            if ( datasetFilePath != null && !datasetFilePath.isBlank()) {
                if (datasetFilePath.endsWith(".csv")) {
                    // Switch Scene
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Visualizer.fxml"));
                    Parent root = loader.load();
                    VisualizerController visualizerController = loader.getController();

                    visualizerController.setDecisionTreeData(datasetFilePath);
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                else {
                    errorLabel.setText("Invalid File Type. Must be csv file.");
                    errorLabel.setVisible(true);
                    System.out.println("Invalid File Type. Must be csv file.");
                }
            }
            else {
                errorLabel.setVisible(true);
            }
        }
        catch (Exception e) {
            errorLabel.setText(e.getLocalizedMessage());
            errorLabel.setVisible(true);
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        errorLabel.setVisible(false);
    }
}
