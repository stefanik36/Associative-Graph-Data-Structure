package com.stefanik36.agds.model.node;

import com.stefanik36.agds.model.Link;
import com.stefanik36.agds.model.Similarity;
import com.stefanik36.agds.model.node.node_type.NodeDescription;
import io.vavr.collection.List;

public abstract class Node {
    private boolean similarityToken;
    private List<Link> links;
    private List<Similarity> similarityValueList;
    private int amount;
    private NodeDescription nodeDescription;

    public Node(NodeDescription nodeDescription) {
        this.amount = 1;
        this.links = List.empty();
        this.similarityValueList = List.empty();
        this.similarityToken = false;
        this.nodeDescription = nodeDescription;
    }

    public abstract boolean sameAs(Node n01);

    public abstract NodeType getNodeType();

    public abstract void pushToken();

    public void addSimilarityValue(Similarity similarity) {
        similarityValueList = similarityValueList.push(similarity);
    }

    public NodeDescription getNodeDescription() {
        return nodeDescription;
    }

    public void addLink(Link link) {
        this.links = this.links.append(link);
    }

    public boolean isType(NodeType nodeType) {
        return getNodeType().equals(nodeType);
    }

    public void inc() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isConnected(Node node) {
        return !this.links.filter(l -> l.contains(node)).isEmpty();
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Similarity> getSimilarityValueList() {
        return similarityValueList;
    }

    public boolean hasSimilarityToken() {
        return similarityToken;
    }

    public void setSimilarityToken(boolean similarityToken) {
        this.similarityToken = similarityToken;
    }
}
