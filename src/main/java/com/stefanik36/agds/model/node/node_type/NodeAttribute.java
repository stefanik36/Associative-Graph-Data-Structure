package com.stefanik36.agds.model.node.node_type;

import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.Value;
import com.stefanik36.agds.util.Util;
import io.vavr.collection.List;

public class NodeAttribute extends Node {
    private static final NodeType NODE_TYPE = NodeType.ATTRIBUTE;

    private String name;
    private List<NodeValue> values;

    public NodeAttribute(NodeDescription nodeDescription, String name) {
        super(nodeDescription);
        this.values = List.empty();
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
        return ((NodeAttribute) n01).getName().equals(getName());
    }

    public String getName() {
        return name;
    }

    public void addValue(NodeValue nodeValue) {
        values = values.append(nodeValue);
        values = values.sortBy(Value::compareTo, NodeValue::getValue);

        Util.disconnectEach(values, values);
        for (int i = 0; i < values.size() - 1; i++) {
            NodeValue n01 = values.get(i);
            NodeValue n02 = values.get(i + 1);
            Util.connect(n01, n02);
        }

    }

    public NodeValue getMaxValue() {
        if (values.isEmpty()) {
            throw new RuntimeException("No values in attribute.");
        }
        return values.last();
    }

    public NodeValue getMinValue() {
        if (values.isEmpty()) {
            throw new RuntimeException("No values in attribute.");
        }
        return values.get();
    }

    public int countNodeValues() {
        return values.map(Node::getAmount).sum().intValue();
    }
}
