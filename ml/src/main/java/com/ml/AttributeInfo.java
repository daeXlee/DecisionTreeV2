package com.ml;

import java.util.HashSet;
import java.util.Set;

public class AttributeInfo {
    private String name;
    private Set<String> values;

    public AttributeInfo(String name) {
        this.name = name;
        this.values = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        this.values.add(value);
    }
}
