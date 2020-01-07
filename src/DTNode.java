import java.util.ArrayList;
import java.util.List;

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
    // Children
    public List<DTNode> children;
    //Left child. Can directly access and update. <= threshold.
    public DTNode left = null;
    //Right child. Can directly access and update. > threshold.
    public DTNode right = null;

    DTNode(int classLabel, int attribute, int threshold, double infoGain) {
        this.classLabel = classLabel;
        this.attribute = attribute;
        this.threshold = threshold;
        this.infoGain = infoGain;
        this.children = new ArrayList<DTNode>();
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
