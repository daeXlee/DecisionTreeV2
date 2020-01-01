/**
 * Adapted from CS 540 Decision Tree Homework
 */
public class DTNode {
    //If leaf, label to return.
    public int classLabel;
    //Attribute split label.
    public int attribute;
    //Threshold that attributes are split on.
    public int threshold;
    //Info Gain
    public double infoGain;
    //Left child. Can directly access and update. <= threshold.
    public DTNode left = null;
    //Right child. Can directly access and update. > threshold.
    public DTNode right = null;

    DTNode(int classLabel, int attribute, int threshold, double infoGain) {
        this.classLabel = classLabel;
        this.attribute = attribute;
        this.threshold = threshold;
        this.infoGain = infoGain;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
