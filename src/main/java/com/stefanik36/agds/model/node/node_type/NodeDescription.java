package com.stefanik36.agds.model.node.node_type;

import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.Node;

public class NodeDescription extends Node {
    private static final NodeType NODE_TYPE = NodeType.DESCRIPTION;

    private String name;

    public NodeDescription(String name) {
        super(null);
        this.name = name;
    }

    @Override
    public NodeType getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public void pushToken() {
        throw new RuntimeException("Operation not supported.");
    }

    @Override
    public boolean sameAs(Node n01) {
        if (!n01.isType(getNodeType())) {
            return false;
        }
        return ((NodeDescription) n01).getName().equals(getName());
    }

    public String getName() {
        return name;
    }

    public double countAttributes() {
        return getLinks()
                .flatMap(l -> l.getWithNodeType(NodeType.ATTRIBUTE))
                .map(Node::getAmount)
                .sum()
                .intValue();
    }
}
