import com.sun.nio.sctp.PeerAddressChangeNotification;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // TODO: Add Feature for options, 1 being to run a test set which program will split 25% of data
        // TODO: Option 2 is to predict value of instance given all attributes except what is being predicted

        // Check for correct args
        if (!(Integer.parseInt(args[1]) == 1 || Integer.parseInt(args[1]) == 2)) {
            System.out.println("Failed to put correct option");
            System.exit(0);
        }
        if (args.length != 3) {
            System.out.println("Error: Usage Main <dataset.csv> <option> <attribute/instance>");
            System.exit(0);
        }
        List<AttributeNode> attrList = preProcess(args[0]);
        List<List<Double>> dataset = parseDataSet(args[0], attrList);
        int predictAttr = -1;  // Attributed to be predicted

        if (Integer.parseInt(args[1]) == 1) {
            // Search for attribute specified
            for (int i = 0; i < attrList.size(); i++) {
                if (attrList.get(i).getName().compareTo(args[2]) == 0)
                    predictAttr = i;
            }
            if (predictAttr == -1) {
                System.out.println("Invalid attribute");
                System.exit(0);
            }
            // Split array into training set and testing set
            int splitIndex = (int) Math.round(dataset.size() * 0.75);
            List<List<Double>> trainSet = dataset.subList(0, splitIndex);
//            System.out.println("TRAINING");
//            for (List<Double> instance : trainSet) {
//                for (Double s : instance) {
//                    System.out.print(s + " ");
//                }
//            System.out.println();
//            }
            List<List<Double>> testSet = dataset.subList(splitIndex, dataset.size());
//            System.out.println("TESTING");
//            for (List<Double> instance : testSet) {
//                for (Double s : instance) {
//                    System.out.print(s + " ");
//                }
//                System.out.println();
//            }
            // Build Tree
            DecisionTree dTree = new DecisionTree(trainSet, attrList, predictAttr);
            dTree.printTree();

            // Testing Phase
            dTree.printTest(testSet);
        } else {  // Option 2
            List<Double> instance = parseInstance(args[2], attrList);
            for (int i = 0; i < instance.size(); i++) {
                if (instance.get(i) == null) {
                    predictAttr = i;
                }
            }
            if (predictAttr == -1) {
                System.out.println("Invalid attribute");
                System.exit(0);
            }
            // Build Tree
            DecisionTree dTree = new DecisionTree(dataset, attrList, predictAttr);
            dTree.printTree();

            // Predict Instance
            dTree.predictInstance(instance);
        }

//        for (AttributeNode n : attrList) {
//            System.out.print(n.name + " ");
//            if (n.isCate) {
//                System.out.println("(Category): " + n.getValues());
//
//            } else {
//                System.out.println("(Numerical): (" + n.getLT() + ", " + n.getUT() + ")");
//            }
//        }



    }

    /**
     * Pre pass of dataset
     * Determines characteristics of attributes and stores them
     * Characteristics include range of values and type of measure used
     *
     * @param fileName
     * @return
     */
    private static List<AttributeNode> preProcess(String fileName) {
        List<AttributeNode> attrList = new ArrayList<AttributeNode>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = "";

            // Get Header and set attrList
            line = br.readLine();
            String split[] = line.split(",");
            for (String s : split) {
                AttributeNode n = new AttributeNode(s);
                attrList.add(n);
            }
            // Read and add values to attributeNodes
            while ((line = br.readLine()) != null) {
                String attrValues[] = line.split(",");
                for (int i = 0; i < attrValues.length; i++) {
                    attrList.get(i).addValue(attrValues[i]);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return attrList;
    }

    /**
     * Parses data into a 2D array of strings
     *
     * @param fileName
     * @return
     */
    private static List<List<Double>> parseDataSet(String fileName, List<AttributeNode> preProcess) {
        List<List<Double>> dataset = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            // Skip Header Line
            dataset = br.lines().skip(1).map((line) -> {
                String split[] = line.split(",");
                List<Double> instance = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    if (preProcess.get(i).isCate) {  // Check if attr is a category
                        instance.add((double) (preProcess.get(i).getValues().indexOf(split[i])));
                    } else {
                        instance.add(Double.parseDouble(split[i]));
                    }
                }
                return instance;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println(e);
        }
        return dataset;
    }

    private static List<Double> parseInstance(String fileName, List<AttributeNode> preProcess) {
        List<Double> instance = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String split[] = line.split(",");

            for (int i = 0; i < split.length; i++) {
                if (split[i].compareTo("?") == 0) {
                    instance.add(null);
                } else {
                    if (preProcess.get(i).isCate) {  // Check if attr is a category
                        instance.add((double) (preProcess.get(i).getValues().indexOf(split[i])));
                    } else {
                        instance.add(Double.parseDouble(split[i]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return instance;
    }
}
