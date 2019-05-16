package com.stefanik36.agds.model.node.node_type;

import com.stefanik36.agds.model.Link;
import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.Similarity;
import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.Value;

public class NodeValue extends Node {
    private static final NodeType NODE_TYPE = NodeType.VALUE;

    private Value value;

    public NodeValue(NodeDescription nodeDescription, Object val) {
        super(nodeDescription);
        this.value = Value.of(val);
    }

    @Override
    public NodeType getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public void pushToken() {
        this.setSimilarityToken(false);

        double sValue = this.getSimilarityValueList().map(Similarity::getValue).sum().doubleValue();
        Node sFrom = this.getSimilarityValueList().last().getFrom();
        this.getLinks()
                .filter(l -> !l.contains(sFrom))
                .filter(l -> (l.nodeTypesAre(NodeType.VALUE, NodeType.VALUE) || l.nodeTypesAre(NodeType.VALUE, NodeType.OBJECT)))
                .filter(NodeValue::neighbourConnectionOnlyForDoubleNodeValues)
                .forEach(l -> l.pushSimilarityValue(this, sValue));
    }

    private static boolean neighbourConnectionOnlyForDoubleNodeValues(Link l) {
        return l.getWithNodeType(NodeType.VALUE).size() != 2 || ((NodeValue) l.getNode01()).getValue().getType().equals(Double.class);
    }

    @Override
    public boolean sameAs(Node n01) {
        if (!n01.isType(getNodeType())) {
            return false;
        }
        return ((NodeValue) n01).getValue().equals(getValue());
    }

    public Value getValue() {
        return value;
    }

    public <T> Value<T> getValue(Class<T> classOfValue) {
        if (value.getType().equals(classOfValue)) {
            return value;
        }
        throw new RuntimeException("Wrong type cast.");
    }


    public NodeAttribute getParentAttribute() {
        return getLinks()
                .filter(l -> l.contains(this))
                .flatMap(l -> l.getWithNodeType(NodeType.ATTRIBUTE))
                .map(n -> (NodeAttribute) n)
                .getOrElseThrow(() -> new RuntimeException("No parent attribute."));
    }

    public int countNodeObjects() {
        return getLinks()
                .filter(l -> l.contains(this))
                .flatMap(l -> l.getWithNodeType(NodeType.OBJECT))
                .map(Node::getAmount)
                .sum()
                .intValue();
    }
}







