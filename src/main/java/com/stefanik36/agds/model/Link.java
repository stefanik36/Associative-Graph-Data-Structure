package com.stefanik36.agds.model;

import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.node_type.NodeAttribute;
import com.stefanik36.agds.model.node.node_type.NodeObject;
import com.stefanik36.agds.model.node.node_type.NodeValue;
import io.vavr.collection.List;

import java.util.Objects;

public class Link {
    private Node node01;
    private Node node02;

    public Link(Node n01, Node n02) {
        this.node01 = n01;
        this.node02 = n02;
    }

    private double getNeighbourWeight(NodeValue from, NodeValue to) {
        NodeAttribute pa01 = from.getParentAttribute();
        NodeAttribute pa02 = to.getParentAttribute();
        if (!pa01.equals(pa02)) {
            throw new RuntimeException("Parent attribute is not the same.");
        }
        NodeValue max = pa01.getMaxValue();
        NodeValue min = pa01.getMinValue();

        List<Class> types = List.of(from, to)
                .map(nv -> nv.getValue().getType());

        if (types.filter(t -> t.equals(Double.class)).size() == types.size()) {
            Double dMin = min.getValue(Double.class).getVal();
            Double dMax = max.getValue(Double.class).getVal();
            if (dMax.equals(dMin)) {
                throw new RuntimeException("Min and max could not be the same.");
            }
            Double dLeft = from.getValue(Double.class).getVal();
            Double dRight = to.getValue(Double.class).getVal();
            return 1.0 - Math.abs(dLeft - dRight) / (dMax - dMin);
        } else if (types.filter(t -> t.equals(String.class)).size() == types.size()) {
            String dMin = min.getValue(String.class).getVal();
            String dMax = max.getValue(String.class).getVal();
            if (dMax.equals(dMin)) {
                throw new RuntimeException("Min and max could not be the same.");
            }
            return 1.0 / pa01.countNodeValues();
        } else {
            throw new RuntimeException("Unsupported types");
        }
    }

    private double getValueObjectWeight(NodeValue from, NodeObject to) {
        //TODO decide which one is correct:
//        return 1.0 / from.countNodeObjects();
        return 1.0 / from.getNodeDescription().countAttributes();
    }

    private double getObjectValueWeight(NodeObject from, NodeValue to) {
        return 1.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(node01, link.node01) &&
                Objects.equals(node02, link.node02);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node01, node02);
    }

    public Node getNode01() {
        return node01;
    }

    public void setNode01(Node node01) {
        this.node01 = node01;
    }

    public Node getNode02() {
        return node02;
    }

    public void setNode02(Node node02) {
        this.node02 = node02;
    }

    public <T extends Node> boolean contains(T n01) {
        return node01.equals(n01) || node02.equals(n01);
    }

    public boolean hasNodeType(NodeType nodeType) {
        return !List.of(node01, node02)
                .filter(n -> n.isType(nodeType))
                .isEmpty();
    }

    public boolean nodeTypesAre(NodeType type01, NodeType type02) {
        return (node01.isType(type01) && node02.isType(type02)) || (node01.isType(type02) && node02.isType(type01));
    }

    public List<Node> getWithNodeType(NodeType nodeType) {
        return List.of(node01, node02)
                .filter(n -> n.isType(nodeType));
    }

    public void pushSimilarityValue(Node from, double similarityValue) {
        Node to;
        if (from.equals(this.node01)) {
            to = node02;
        } else if (from.equals(this.node02)) {
            to = node01;
        } else {
            throw new RuntimeException("Link does not containsSameAs from node.");
        }


        PushTokenType pushTokenType = getPushTokenType(from, to);
        double weight = getProperWeight(from, to, pushTokenType);
        double value = weight * similarityValue;

        System.out.println("|" + show(from) + "| -> |" + show(to) + "| " + "w: " + weight + " * sv: " + similarityValue + " = " + value + " ");

        to.addSimilarityValue(new Similarity(from, value));
        if (!pushTokenType.equals(PushTokenType.VALUE_OBJECT)) {
            to.setSimilarityToken(true);
        }
    }

    private PushTokenType getPushTokenType(Node from, Node to) {
        if (from.isType(NodeType.OBJECT) && to.isType(NodeType.VALUE)) {
            return PushTokenType.OBJECT_VALUE;
        } else if (from.isType(NodeType.VALUE) && to.isType(NodeType.OBJECT)) {
            return PushTokenType.VALUE_OBJECT;
        } else if (from.isType(NodeType.VALUE) && to.isType(NodeType.VALUE)) {
            return PushTokenType.VALUE_VALUE;
        }
        throw new RuntimeException("Unsupported node types to compute weight.");
    }

    private double getProperWeight(Node from, Node to, PushTokenType pushTokenType) {
        switch (pushTokenType) {
            case OBJECT_VALUE:
                return getObjectValueWeight((NodeObject) from, (NodeValue) to);
            case VALUE_OBJECT:
                return getValueObjectWeight((NodeValue) from, (NodeObject) to);
            case VALUE_VALUE:
                return getNeighbourWeight((NodeValue) from, (NodeValue) to);
        }
        throw new RuntimeException("Unsupported node types to compute weight.");
    }

    enum PushTokenType {
        OBJECT_VALUE, VALUE_OBJECT, VALUE_VALUE
    }

    @Override
    public String toString() {
        return "Link{" +
                "node01=" + show(node01) +
                ", node02=" + show(node02) +
                '}';
    }

    private String show(Node n) {
        if (n.isType(NodeType.OBJECT)) {
            return ((NodeObject) n).getName();
        } else if (n.isType(NodeType.VALUE)) {
            return ((NodeValue) n).getParentAttribute().getName() + ": " + ((NodeValue) n).getValue().getVal();
        }
        return n.getNodeType().toString();
    }


}

























