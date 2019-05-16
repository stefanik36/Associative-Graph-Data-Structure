package com.stefanik36.agds.model.node.node_type;

import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.Similarity;
import com.stefanik36.agds.model.node.Node;

public class NodeObject extends Node {
    private static final NodeType NODE_TYPE = NodeType.OBJECT;

    private String name;

    public NodeObject(NodeDescription nodeDescription, String name) {
        super(nodeDescription);
        this.name = name;
    }

    @Override
    public NodeType getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public boolean sameAs(Node n01) {
        if (!n01.isType(getNodeType())) {
            return false;
        }
        return ((NodeObject) n01).getName().equals(getName());
    }

    public String getName() {
        return name;
    }

    @Override
    public void pushToken() {
        this.setSimilarityToken(false);

        double sValue = this.getSimilarityValueList().map(Similarity::getValue).sum().doubleValue();
        Node sFrom = this.getSimilarityValueList().last().getFrom();
        getLinks()
                .filter(l -> l.nodeTypesAre(NodeType.OBJECT, NodeType.VALUE))
                .filter(l -> !l.contains(sFrom))
                .forEach(l -> l.pushSimilarityValue(this, sValue));
    }
}
