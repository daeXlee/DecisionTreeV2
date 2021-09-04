package com.ml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DecisionTree {

    public class Node {
        // If leaf, classification to return.
        public String classification;
        // Attribute split label index.
        public int attribute;
        // Threshold that continous attributes are split on.
        public double threshold;
        // Children nodes: Continous - <=, > Nominal - All options
        public HashMap<String, Node> children;

        public Node(String classification, int attribute, double threshold) {
            this.classification = classification;
            this.attribute = attribute;
            this.threshold = threshold;
            this.children = new HashMap<String,Node>();
        }
        
        public boolean isLeaf() {
            return this.children.isEmpty();
        }
    }

    private Node root;
    private List<String[]> trainData;
    private List<AttributeInfo> attributeInfoList;
    private int classIndex;  // Index of Classification Attribute in the dataset
    private double classThreshold; // Only set if Classification is Continuous

    // Used to determine when a node is a leaf
    final int MAX_PER_LEAF = 3;
    final int MAX_DEPTH = 20;

    public DecisionTree(List<String[]> trainData, List<AttributeInfo> attributeInfoList, int classIndex) {
        this.trainData = trainData;
        this.attributeInfoList = attributeInfoList;
        this.classIndex = classIndex;
        this.root = buildTree();
    }

    // Build Tree
    private Node buildTree() {
        // Initial Best Split
        Pair<Integer, Double> splitInformation = getSplitAttribute(trainData);
        AttributeInfo bestSplitAttributeInfo = attributeInfoList.get(splitInformation.getLeft());
        Node root = new Node("", splitInformation.getLeft(), splitInformation.getRight());

        if (bestSplitAttributeInfo.isContinous()) {
            List<String[]> lessEqualChild = trainData.stream()
                .filter(x -> NumberUtils.toDouble(x[splitInformation.getLeft()]) <= splitInformation.getRight())
                .collect(Collectors.toList());
            root.children.put("<=", treeBuilder(lessEqualChild, 1));

            List<String[]> greaterChild = trainData.stream()
                .filter(x -> NumberUtils.toDouble(x[splitInformation.getLeft()]) > splitInformation.getRight())
                .collect(Collectors.toList());
            root.children.put(">", treeBuilder(greaterChild, 1));
        }
        else {
            for (String attributeOption : bestSplitAttributeInfo.getValues()) {
                List<String[]> childData = trainData.stream()
                    .filter(x -> x[splitInformation.getLeft()].equals(attributeOption))
                    .collect(Collectors.toList());
                root.children.put(attributeOption, treeBuilder(childData, 1));
            }
        }

        return root;
    }

    private Node treeBuilder(List<String[]> data, int depth) {
        // Check if Leaf Node: if size is less then MAX_PER_LEAF or hits Depth or if all data is one classification
        AttributeInfo classAttribute = attributeInfoList.get(classIndex);
        String classLabel = "";
        long majorityClassCount = 0;

        if (classAttribute.isContinous()) {
            long lessThanEqualCount =  data.stream()
                .filter(x -> NumberUtils.toDouble(x[classIndex]) <= classThreshold).count();
            long greaterCount = data.stream()
                .filter(x -> NumberUtils.toDouble(x[classIndex]) > classThreshold).count();

            majorityClassCount = Math.max(lessThanEqualCount, greaterCount);
            classLabel = lessThanEqualCount <= greaterCount ? ">" : "<=";
            
        }
        else {
            for (String attributeOption : classAttribute.getValues()) {
                long tempCount = data.stream()
                    .filter(x -> x[classIndex].equals(attributeOption)).count();

                if (tempCount > majorityClassCount)
                {
                    majorityClassCount = tempCount;
                    classLabel = attributeOption;
                }
            }
        }

        if (data.size() <= MAX_PER_LEAF || depth == MAX_DEPTH || majorityClassCount == data.size()) {
            return new Node(classLabel, -1, -1);
        }

        Pair<Integer, Double> splitInformation = getSplitAttribute(data);
        AttributeInfo bestSplitAttributeInfo = attributeInfoList.get(splitInformation.getLeft());
        Node innerNode = new Node("", splitInformation.getLeft(), splitInformation.getRight());

        if (bestSplitAttributeInfo.isContinous()) {
            List<String[]> lessEqualChild = data.stream()
                .filter(x -> NumberUtils.toDouble(x[splitInformation.getLeft()]) <= splitInformation.getRight())
                .collect(Collectors.toList());
            innerNode.children.put("<=", treeBuilder(lessEqualChild, depth + 1));

            List<String[]> greaterChild = data.stream()
                .filter(x -> NumberUtils.toDouble(x[splitInformation.getLeft()]) > splitInformation.getRight())
                .collect(Collectors.toList());
            innerNode.children.put(">", treeBuilder(greaterChild, depth + 1));
        }
        else {
            for (String attributeOption : bestSplitAttributeInfo.getValues()) {
                List<String[]> childData = data.stream()
                    .filter(x -> x[splitInformation.getLeft()].equals(attributeOption))
                    .collect(Collectors.toList());
                innerNode.children.put(attributeOption, treeBuilder(childData, depth + 1));
            }
        }

        return innerNode;
    }

    // Get Best Split
    public Pair<Integer, Double> getSplitAttribute(List<String[]> data) {
        double maxInfoGain = 0.0;
        double gainRatio;
        int maxInfoIndex = 0;
        double maxInfoThreshold = 0.0;

        for (int i = 0; i < attributeInfoList.size(); i++) {
            // Skip Classification Attribute
            if (i == this.classIndex) continue;

            if (attributeInfoList.get(i).isContinous()) {
                for (String value : attributeInfoList.get(i).getValues()) {
                    if (NumberUtils.isCreatable(value)) {
                        gainRatio = getGainRatio(data, i, NumberUtils.toDouble(value));
                        
                        if (maxInfoGain < gainRatio) {
                            maxInfoGain = gainRatio;
                            maxInfoIndex = i;
                            maxInfoThreshold = NumberUtils.toDouble(value);
                        }
                    }
                }
            }
            else {
                gainRatio = getGainRatio(data, i, 0);  // Nominal Attr. don't care about threshold
                
                if (maxInfoGain < gainRatio) {
                    maxInfoGain = gainRatio;
                    maxInfoIndex = i;
                }
            }
        }

        return new MutablePair<>(maxInfoIndex, maxInfoThreshold);
    }

    // TODO: Implement when Classification is Continuous
    public double getGainRatio(List<String[]> data, int attributeIndex, double threshold) {
        AttributeInfo attributeInfo = attributeInfoList.get(attributeIndex);
        boolean isContinous = attributeInfo.isContinous();
        // Key: {Attr. Option}, Value: {Key: Class Option Value: Count}
        HashMap<String, Map<String, Long>> labelCounts = new HashMap<>();
        initializeLabelMap(labelCounts, attributeInfo);

        for (String[] instance : data) {
            String attributeOption;
            String classDecisionOption = instance[classIndex];

            if (isContinous) {
                double instanceAttributeValue = NumberUtils.toDouble(instance[attributeIndex]);
                attributeOption = instanceAttributeValue <= threshold ? "<=" : ">";
            }
            else {
                attributeOption = instance[attributeIndex];
            }

            Map<String, Long> innerClassMap = labelCounts.get(attributeOption);
            innerClassMap.put(classDecisionOption, innerClassMap.get(classDecisionOption) + 1);
        }

        HashMap<String, Long> classLabelCounts = new HashMap<>();

        for (Map<String, Long> classCountMap : labelCounts.values()) {
            for (Map.Entry<String, Long> classCount : classCountMap.entrySet()) {
                Long count = classLabelCounts.get(classCount.getKey());

                count = count == null ? classCount.getValue() : count + classCount.getValue();
                classLabelCounts.put(classCount.getKey(), count);
            }
        }

        Long total = classLabelCounts.values().stream().collect(Collectors.summingLong(Long::longValue));
        double classEntropy = entropy(classLabelCounts.values());
        double attributeGainCalc = 0.0;
        List<Long> attributeTotalCounts = new ArrayList<>();

        for (Map<String, Long> classCountMap : labelCounts.values()) {
            Long attributeTotalCount = classCountMap.values().stream().collect(Collectors.summingLong(Long::longValue));
            double attributeEntropy = entropy(classCountMap.values());

            attributeTotalCounts.add(attributeTotalCount);
            attributeGainCalc += ((double)attributeTotalCount / total) * attributeEntropy;

        }

        double gain = classEntropy - attributeGainCalc;
        double splitInfo = entropy(attributeTotalCounts);

        return gain / splitInfo;
    }

    private void initializeLabelMap(HashMap<String, Map<String, Long>> map, AttributeInfo attributeInfo) {
        AttributeInfo classAttribute = attributeInfoList.get(classIndex);
        Map<String, Long> classOptionMap;

        // Initialize Inner Class Decision Map
        if (classAttribute.isContinous()) {
            classOptionMap = new HashMap<>();
            classOptionMap.put("<=", 0l);
            classOptionMap.put(">", 0l);
        }
        else {
            classOptionMap =  classAttribute.getValues().stream().collect(Collectors.toMap(s -> s, s -> 0l));
        }

        if (attributeInfo.isContinous()) {
            map.put("<=", new HashMap<>(classOptionMap));
            map.put(">", new HashMap<>(classOptionMap));
        }
        else {
            attributeInfo.getValues().forEach(s -> map.put(s, new HashMap<>(classOptionMap)));
        }
    }

    public double entropy(Collection<Long> collection) {
        double sum = 0.0;
        Long total = collection.stream().collect(Collectors.summingLong(Long::longValue));

        if (total == 0) {
            return sum;
        }

        for (long labelCount : collection) {
            sum -= (double)labelCount / total * log2((double)labelCount / total);
        }

        return sum;
    }

    public double log2(double x) {

        return x == 0 ? 0 : Math.log(x) / Math.log(2);
    }

    public void printTree() {
        AttributeInfo classAttributeInfo = attributeInfoList.get(classIndex);
        System.out.print("Classification: " + classAttributeInfo.getName() + " Labels: ");

        if (classAttributeInfo.isContinous()) {
            System.out.println("[<=" + classThreshold + ", >" + classThreshold + "]");
        }
        else {
            System.out.println(classAttributeInfo.getValues().toString());
        }

        printTreeNode("", root);
    }

    public void printTreeNode(String prefix, Node node) {
        AttributeInfo attributeInfo = attributeInfoList.get(node.attribute); 
        boolean isAttributeContinuous = attributeInfo.isContinous();
        String currentAttributeStr = prefix + attributeInfo.getName() + " : ";
           
        for (Map.Entry<String, Node> childNode : node.children.entrySet()) {
            System.out.println(currentAttributeStr +
                childNode.getKey() + (isAttributeContinuous ? node.threshold : ""));

            if (childNode.getValue().isLeaf()) {
                System.out.println("|---" + prefix + childNode.getValue().classification +
                    (attributeInfoList.get(classIndex).isContinous() ? classThreshold : ""));
            }
            else {
                printTreeNode("|---" + prefix, childNode.getValue());
            }
        }
    }
}
