package com.ml;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class App 
{
    public static void main( String[] args ) throws IOException, CsvException
    {
        List<AttributeInfo> attributeInfos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader("src\\Datasets\\dataset.csv"))) {
            String[] lineInCSV = reader.readNext();

            // Parse & Create Attribute Infos based on headers
            for (String attributeName : lineInCSV) {
                attributeInfos.add(new AttributeInfo(attributeName));
            }

            while ((lineInCSV = reader.readNext()) != null) {
                for (int i = 0; i < lineInCSV.length; i++) {
                    attributeInfos.get(i).addValue(lineInCSV[i]);
                }
            }
        }

        for (AttributeInfo attributeInfo : attributeInfos) {
            System.out.println(attributeInfo.getName() + " " + attributeInfo.getValues().toString());
        }
    }
}
