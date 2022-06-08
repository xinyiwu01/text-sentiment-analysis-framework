package com.andrewgrey.framework.core;

public enum Action {
    DATA("Choose Data Source"), VISUAL("Select Visualization Type");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
