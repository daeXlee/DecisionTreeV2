import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<AttributeNode> attrList = new ArrayList<AttributeNode>();
        List<List<Double>> trainData = transformDataSet(parseDataSet(args[0]), attrList);
        List<List<Double>> testData = transformDataSet(parseDataSet(args[1]), attrList);

        // Build tree.
        DecisionTree mTree = new DecisionTree(trainData, attrList, Integer.parseInt(args[2]));
//        System.out.println("Predicting Attribute: " + attrList.get(Integer.parseInt(args[2])).name);
        // Print tree.
        mTree.printTree();
        // mTree.printTest(testData);
    }
    /**
     * Initial scan of dataset and puts into a data structure
     * @param file
     * @return
     */
    private static List<List<String>> parseDataSet(String file) {
        int n = 0;
        int m = 0;
        // Figure out the dimensions
        try {
            Scanner scan = new Scanner(new File(file));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                // Remove rows with missing data containing "?"
                if (line.indexOf('?') == -1) {
                    String[] split = line.split(",");
                    if (m != 0 && split.length != m) m = 0;
                    else m = split.length;
                    n++;
                }
            }
            scan.close();
        }
        catch (IOException e) {
        }
        // Read the data and convert to integers
        if (n == 0 || m == 0) return null;
        else {
            List<List<String>> dataSet = new ArrayList<List<String>>();
            try {
                Scanner scan = new Scanner(new File(file));
                for (int i = 0; i < n; i ++) {
                    List<String> data = new ArrayList<String>();
                    String line = scan.nextLine();
                    while (line.indexOf('?') != -1) line = scan.nextLine();
                    String[] split = line.split(",");
                    // Do not read the first column
                    for (int j = 1; j < m; j++) {
//                        System.out.println(split[j]);
                        data.add(split[j]);
                    }
                    dataSet.add(data);
                }
                scan.close();
            }
            catch (IOException e) {
            }
            return dataSet;
        }
    }

    private static List<List<Double>> transformDataSet(List<List<String>> rawData, List<AttributeNode> attrList) {
        // Get Attribute Information
        int col = rawData.get(0).size();
        int row = rawData.size();

        if (attrList.isEmpty()) {
            for (int i = 0; i < col; i++) {
                AttributeNode a = new AttributeNode(rawData.get(0).get(i));
                for (int j = 1; j < row; j++) {
                    a.addValue(rawData.get(j).get(i));
                }
                attrList.add(a);
            }
        }

        List<List<Double>> dataset = new ArrayList<List<Double>>();
        for (int i = 1; i < row; i++) {
            ArrayList<Double> data = new ArrayList<Double>();
            for (int j = 0; j < col; j++) {
                if (attrList.get(j).isCate) {
                    data.add((double) attrList.get(j).getValues().indexOf(rawData.get(i).get(j)));
                } else {
                    data.add(Double.parseDouble(rawData.get(i).get(j)));
                }
            }
            dataset.add(data);
        }
        return dataset;
    }
}
