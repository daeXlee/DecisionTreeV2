import java.util.ArrayList;
import java.util.List;

public class Instance {
    public AttributeNode node;
    public List<String> values;

    Instance(AttributeNode node) {
        this.node = node;
        this.values = new ArrayList<String>();
    }

    public void addValue(String v) {
        this.values.add(v);
    }
}
