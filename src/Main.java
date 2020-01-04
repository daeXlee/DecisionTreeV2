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
        if (Integer.parseInt(args[1]) == 1) {
            if (args.length != 3) {
                System.out.println("Error: Usage Main <dataset.csv> 1 <attribute>");
                System.exit(0);
            }
            List<AttributeNode> attrList = preProcess(args[0]);
            List<List<Double>> dataset = parseDataSet(args[0], attrList);
            int predictAttr = -1;
            for (int i = 0; i < attrList.size(); i++) {
                if (attrList.get(i).getName().equals(args[2]))
                    predictAttr = i;
            }
            if (predictAttr == -1) {
                System.out.println("Invalid attribute");
                System.exit(0);
            }
            // Build Tree
            DecisionTree dTree = new DecisionTree(dataset, attrList, predictAttr);
            dTree.printTree();
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


//        for (List<Double> instance : dataset) {
//            for (Double s : instance) {
//                System.out.print(s + " ");
//            }
//            System.out.println();
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
}
