import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    public String name;
    public List<String> values;
    public double[] thresholds;
    public boolean isCate;

    TreeNode(String name) {
        this.name = name;
        this.values = new ArrayList<String>();
        this.thresholds = new double[]{-999, -999};
        this.isCate = true;
    }

    public void addValue(String v) {
        if (values.contains(v) == false) values.add(v);
        setThresholds(v);
    }

    public void setThresholds(String v) {
        if (isNumeric(v)) {
            this.isCate = false;
            double i = Double.parseDouble(v);
            if (this.thresholds[0] == -999) {
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

    public double getLowerThreshold() {
        return this.thresholds[0];
    }

    public double getUpperThreshold() {
        return this.thresholds[1];
    }

    public List<String> getValues() {
        return this.values;
    }


    private static boolean isInteger(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
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
