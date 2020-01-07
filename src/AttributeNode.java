import java.util.ArrayList;
import java.util.List;

public class AttributeNode {
    public String name;
    public List<String> values;
    public double[] thresholds;
    public boolean isCate;

    AttributeNode(String name) {
        this.name = name;
        this.values = new ArrayList<String>();
        this.thresholds = new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        this.isCate = true;
    }
    public void addValue(String v) {
        if (!values.contains(v)) values.add(v);
        setThresholds(v);
    }

    /**
     * if attribute is numeric, thresholds represent min and max vals
     * otherwise if it's categorical threshold represents number of categories
     * @param v
     */
    public void setThresholds(String v) {
        if (isNumeric(v)) {
            this.isCate = false;
            double i = Double.parseDouble(v);
            if (this.thresholds[0] == Double.NEGATIVE_INFINITY) {
                this.thresholds[0] = i;
                this.thresholds[1] = i;
            } else {
                if (this.thresholds[0] > i) this.thresholds[0] = i;
                if (this.thresholds[1] < i) this.thresholds[1] = i;
            }
        } else {
            this.thresholds[0] = 0;
            this.thresholds[1] = this.values.size() - 1;
        }
    }

    public double getLT() {
        return this.thresholds[0];
    }

    public double getUT() {
        return this.thresholds[1];
    }

    public List<String> getValues() {
        return this.values;
    }

    public String getName() {
        return this.name;
    }

    public double getSplit() {
        // Default take median of two thresholds
        return (this.thresholds[0] + this.thresholds[1]) / 2.0;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
