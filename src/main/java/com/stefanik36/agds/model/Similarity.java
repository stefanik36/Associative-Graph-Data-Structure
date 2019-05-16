package com.stefanik36.agds.model;

import com.stefanik36.agds.model.node.Node;

public class Similarity {
    private Node from;
    private double value;

    public Similarity(Node from, double value) {
        this.from = from;
        this.value = value;
    }

    public Node getFrom() {
        return from;
    }

    public double getValue() {
        return value;
    }
}
