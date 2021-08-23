package com.ml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecisionTree {

    public class Node {
        //If leaf, classification to return.
        public String classification;
        //Attribute split label index.
        public int attribute;
        //Threshold that continous attributes are split on.
        public double threshold;
        // Children nodes: Continous - <=, > Nominal - All options
        public Hashtable<String, Node> children;

        public Node(String classification, int attribute, double threshold) {
            this.classification = classification;
            this.attribute = attribute;
            this.threshold = threshold;
            this.children = new Hashtable<String,Node>();
        }
        
        public boolean isLeaf() {
            return this.children.isEmpty();
        }
    }

    private Node root;
    private List<String[]> trainData;
    private List<AttributeInfo> attributeInfoList;
    private AttributeInfo classAttribute;
    private int classIndex;  // Index of Classification Attribute in the dataset

    // Used to determine when a node is a leaf
    final int MAX_PER_LEAF = 3;
    final int MAX_DEPTH = 20;

    public double getGainRatio(List<String[]> data, int attributeIndex, double threshold) {
        AttributeInfo attributeInfo = attributeInfoList.get(attributeIndex);
        // Key: {Attr. Option}, Value: {Key: Class Option Value: Count}
        HashMap<String, Map<String, Long>> labelCounts = new HashMap<>();
        initializeLabelMap(labelCounts, attributeInfo);

        for (String[] instance : data) {
            if (attributeInfo.isContinous()) {

            }
            else {
                String key = instance[attributeIndex];
                Map<String, Long> innerMap = labelCounts.get(key);
                
                innerMap.put(instance[classIndex], innerMap.get(instance[classIndex]) + 1);
                labelCounts.put(key, innerMap);
            }
        }

        return 0;
    }

    private void initializeLabelMap(HashMap<String, Map<String, Long>> map, AttributeInfo attributeInfo) {
        Map<String, Long> classOptionMap = classAttribute.getValues().stream().collect(Collectors.toMap(s -> s, s -> 0l));

        for (String attributeOption : attributeInfo.getValues()) {
            map.put(attributeOption, classOptionMap);
        }
    }

    public double entropy(long[] labelCounts) {
        double sum = 0;
        long total = Arrays.stream(labelCounts).sum();

        if (total == 0) {
            return sum;
        }

        for (long labelCount : labelCounts) {
            sum -= (double)(labelCount / total) * log2((double)(labelCount / total));
        }

        return sum;
    }

    public double log2(double x) {
        return x == 0 ? 0 : Math.log(x) / Math.log(2);
    }
}
