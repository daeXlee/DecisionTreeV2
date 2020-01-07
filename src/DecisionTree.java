import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTree {
    private DTNode root;
    private List<List<Double>> trainData;
    private List<AttributeNode> attrList;
    private int predictIndex;  // Index of predict attributed
    private double predictVal;  // Value that decides labels
    private AttributeNode predictAttr;

    // Used to determine when a node is a leaf
    final int MAX_PER_LEAF = 3;
    final int MAX_DEPTH = 20;

    // Build a decision tree given a training set
    DecisionTree(List<List<Double>> trainDataSet, List<AttributeNode> attrList, int predictIndex) {
        this.trainData = trainDataSet;
        this.attrList = attrList;
        this.predictIndex = predictIndex;
        this.predictAttr = attrList.get(predictIndex);
        // Set the label for predict attr
        /** CAN ONLY CLASSIFY USING BINARY LABEL
         * Currently gets the middle value and sets <= of val and > of val as the 2 labels
         **/
        this.predictVal = this.predictAttr.getSplit();
        this.root = buildTree();
    }

    private DTNode buildTree() {
        // Split initial by best info gain
        double split[] = getSplitAttribute(trainData);

        // Root Node
        DTNode root = new DTNode(-1, (int) split[0], (int) split[1], split[2]);

        List<List<Double>> leftData = trainData.stream()
                .filter(x -> x.get((int) split[0]) <= split[1])
                .collect(Collectors.toList());

        root.left = treeBuilder(leftData, 1);

        List<List<Double>> rightData = trainData.stream()
                .filter(x -> x.get((int) split[0]) > split[1])
                .collect(Collectors.toList());

        root.right = treeBuilder(rightData, 1);

        return root;
    }

    private DTNode treeBuilder(List<List<Double>> data, int depth) {
        int cnt0 = 0;
        int cnt1 = 0;
        int label;

        // Count the number of 0 or 1 in label
        for (List<Double> instance : data) {
            if (instance.get(predictIndex) <= predictVal) {
                cnt0++;
            } else {
                cnt1++;
            }
        }
        // Determine what label if leaf node
        label = (cnt0 <= cnt1) ? 1 : 0;

        // Check if leaf node based on params
        if (data.size() <= MAX_PER_LEAF || depth == MAX_DEPTH) {
            return new DTNode(label, -1, -1, 0);
        }

        // Split initial by best info gain
        double split[] = getSplitAttribute(data);

        // Check if leaf node if max info gain is 0
        if (split[0] == -1) {
            return new DTNode(label, -1, -1, split[2]);
        }

        // Inner Node
        DTNode innerNode = new DTNode(-1, (int) split[0], (int) split[1], split[2]);

        List<List<Double>> leftData = data.stream()
                .filter(x -> x.get((int) split[0]) <= split[1])
                .collect(Collectors.toList());

        innerNode.left = treeBuilder(leftData, depth + 1);

        List<List<Double>> rightData = data.stream()
                .filter(x -> x.get((int) split[0]) > split[1])
                .collect(Collectors.toList());

        innerNode.right = treeBuilder(rightData, depth + 1);

        return innerNode;
    }

    public int classify(List<Double> instance) {
        DTNode node = root;
        while (node != null) {
            if (node.isLeaf()) {
                return node.classLabel;
            } else {
                // Inner Node Check Which Split
                if (instance.get(node.attribute) <= node.threshold) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
        }
        return 0;
    }

    public String[] formatLabel(AttributeNode predictAttr, double splitVal) {
        String label[] = {"[", "["};
        if (predictAttr.isCate) {
            List<String> label0 = predictAttr.getValues().subList(0, (int)splitVal + 1);
            label[0] = label[0] + String.join(", ", label0);
            label[0] = label[0] + "]";

            List<String> label1 = predictAttr.getValues().subList((int)splitVal + 1, predictAttr.getValues().size());
            label[1] = label[1] + String.join(", ", label1);
            label[1] = label[1] + "]";
        } else {
            label[0] = "<=" + splitVal;
            label[1] = ">" + splitVal;
        }

        return label;
    }

    // Print the decision tree in the specified format
    public void printTree() {
        String l0 = formatLabel(this.predictAttr, this.predictVal)[0];
        String l1 = formatLabel(this.predictAttr, this.predictVal)[1];

        System.out.println("Predicting: " + predictAttr.getName() + " Label 0: " + l0 + " Label 1: " + l1);
        printTreeNode("", this.root);
    }

    public void printTreeNode(String prefixStr, DTNode node) {
        String printStr = prefixStr + attrList.get(node.attribute).name;
        String labels[] = formatLabel(attrList.get(node.attribute), node.threshold);
        System.out.print(printStr + " " + labels[0]);
        //System.out.print(printStr + " <= " + String.format("%d", node.threshold));
        if(node.left.isLeaf()) {
            System.out.print(" : " + String.valueOf(node.left.classLabel));
            System.out.printf(", Info Gain: %.4f\n", node.infoGain);
        }
        else {
            System.out.printf(", Info Gain: %.4f", node.infoGain);
            System.out.println();
            printTreeNode(prefixStr + "|\t", node.left);
        }
        System.out.print(printStr + " " + labels[1]);
//        System.out.print(printStr + " > " + String.format("%d", node.threshold));
        if(node.right.isLeaf()) {
            System.out.println(" : " + String.valueOf(node.right.classLabel));
        }
        else {
            System.out.println();
            printTreeNode(prefixStr + "|\t", node.right);
        }
    }

    public double printTest(List<List<Double>> testDataSet) {
        int numEqual = 0;
        int numTotal = 0;

        for (int i = 0; i < testDataSet.size(); i ++)
        {
            int predictionLabel = classify(testDataSet.get(i));
            String prediction = (predictionLabel == 0)
                    ? formatLabel(this.predictAttr, this.predictVal)[0]
                    : formatLabel(this.predictAttr, this.predictVal)[1];

            String groundTruth = (attrList.get(predictIndex).isCate)
                    ? attrList.get(predictIndex).getValues().get(testDataSet.get(i).get(predictIndex).intValue())
                    : String.valueOf(testDataSet.get(i).get(predictIndex));
            int groundTruthLabel = (testDataSet.get(i).get(predictIndex) <= predictVal) ? 0 : 1;

            System.out.println(i+1 + ": " + "Prediction: " + prediction + " Actual: " + groundTruth);
            if (groundTruthLabel == predictionLabel) {
                numEqual++;
            }
            numTotal++;
        }
        double accuracy = numEqual*100.0 / (double)numTotal;
        System.out.println(String.format("%.2f", accuracy) + "%");
        return accuracy;
    }

    public void predictInstance(List<Double> instance) {
        List<String> translatedInstance = new ArrayList<>();
        String prediction = (classify(instance) == 0) ? "<=" : " >";

        for (int i = 0; i < instance.size(); i++) {
            if (instance.get(i) == null) {
                translatedInstance.add("?");
            } else {
                if (attrList.get(i).isCate) {
                    translatedInstance.add(attrList.get(i).getValues().get(instance.get(i).intValue()));
                } else {
                    translatedInstance.add("" + instance.get(i));
                }
            }
        }
        System.out.print("Predicting Attribute: " + predictAttr.getName() + "\n" + "For Instance: ");
        for (String s : translatedInstance) {
            System.out.print(s + " ");
        }
        System.out.println("\nValue: " + prediction + predictVal);
    }

    public double[] getSplitAttribute(List<List<Double>> data) {
        double splitInfo[] = new double[3];
        double maxInfoGain = 0.0;

        // Loop through array of threshold limits of each attribute
        // TODO: May Condense by setting up attribute as an AttributeNode instead of just an index
        for (int i = 0; i < attrList.size(); i++) {
            // Skip Attribute of Interest
            if (i == this.predictIndex) continue;
            // Loop through the threshold values of each attribute
            // TODO: Better way to search through attribute thresholds, large range may cause problems going +1 or even
            //  small range <1
            for (int j = (int) attrList.get(i).getLT(); j <= attrList.get(i).getUT(); j++) {
                double temp = getInfoGain(data, i, j);
                if (temp > maxInfoGain) {
                    splitInfo[0] = i;  // Save best info gain attribute
                    splitInfo[1] = j;  // Save best info gain threshold
                    splitInfo[2] = temp;
                    maxInfoGain = temp;
                }
            }
        }

        if (maxInfoGain == 0.0) {
            splitInfo[0] = -1;
        }
        return splitInfo;
    }

    public double getInfoGain(List<List<Double>> data, int attribute, int threshold) {
        double hY, hYX;
        double cntLb0 = 0;
        double cntLb1 = 0;
        double total;
        double attrLeft0 = 0;
        double attrLeft1 = 0;
        double attrRight0 = 0;
        double attrRight1 = 0;
        double attrLeft, attrRight;

        // Calculate entropy of label
        for (List<Double> sample : data) {
            if (sample.get(this.predictIndex) <= predictVal) {
                cntLb0++;
                if (sample.get(attribute) <= threshold) {
                    attrLeft0++;
                } else {
                    attrRight0++;
                }
            } else {
                cntLb1++;
                if (sample.get(attribute) <= threshold) {
                    attrLeft1++;
                } else {
                    attrRight1++;
                }
            }
        }
        total = cntLb0 + cntLb1;
        hY = entropy(cntLb0, cntLb1, total);

        attrLeft = attrLeft0 + attrLeft1;
        attrRight = attrRight0 + attrRight1;
        hYX = (attrLeft / total) * entropy(attrLeft0, attrLeft1, attrLeft) +
                (attrRight / total) * entropy(attrRight0, attrRight1, attrRight);

        return hY - hYX;
    }

    public double entropy(double lb0, double lb1, double total) {
        if (total == 0)
            return 0;
        if (lb0 == 0 && lb1 == 0)
            return 0;
        if (lb0 == 0)
            return -1 * (lb1 / total) * log2(lb1 / total);
        if (lb1 == 0)
            return -1 * (lb0 / total) * log2(lb0 / total);

        return -1 * (lb0 / total) * log2(lb0 / total) - (lb1 / total) * log2(lb1 / total);
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

}
