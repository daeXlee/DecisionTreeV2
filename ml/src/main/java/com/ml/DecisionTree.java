package com.ml;

import java.util.Hashtable;

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
}
