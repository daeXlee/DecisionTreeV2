package com.ml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class App 
{
    public static void main( String[] args ) throws IOException, CsvException
    {
        try (CSVReader reader = new CSVReader(new FileReader("src\\Datasets\\dataset.csv"))) {
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

            File file = new File("TreeResult.txt");
            PrintStream stream = new PrintStream(file);
            System.setOut(stream);

            DecisionTree dt = new DecisionTree(linesInCSV, attributeInfos, 4);
            dt.printTree();
        }
    }
}
