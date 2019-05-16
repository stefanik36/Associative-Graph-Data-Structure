package com.stefanik36.agds.model;

import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.node_type.NodeAttribute;
import com.stefanik36.agds.model.node.node_type.NodeDescription;
import com.stefanik36.agds.model.node.node_type.NodeObject;
import com.stefanik36.agds.model.node.node_type.NodeValue;
import com.stefanik36.agds.util.NewOrSame;
import com.stefanik36.agds.util.Util;
import io.vavr.collection.List;

import java.util.function.Supplier;

public class Agds {

    private List<Node> nodes;
    private Supplier<Boolean> similarityEndCondition;
    private long epochs;
    private NodeDescription nodeDescription;

    public Agds(List<String> attributesNames, SimilarityEndConditionType similarityEndCondition) {
        this.similarityEndCondition = () -> similarityEndCondition.getSimilarityEndCondition().apply(this);
        nodes = List.empty();
        epochs = 0;
        this.nodeDescription = createNodeDescription();
        createNodeAttributeList(attributesNames, nodeDescription);
    }

    private void createNodeAttributeList(List<String> parameterNames, NodeDescription description) {
        parameterNames.forEach(pn ->
                Util.newOrUpdate(
                        new NodeAttribute(description, pn),
                        nodes.filter(n -> n.isConnected(description))
                ).ifPresent(nn -> nodes = nodes.append(nn))
        );
        Util.connectEach(List.of(description), nodes.filter(n -> n.isType(NodeType.ATTRIBUTE)));
    }

    private NodeDescription createNodeDescription() {
        Util.newOrUpdate(new NodeDescription("param"), nodes).ifPresent(newNode -> nodes = nodes.append(newNode));
        return nodes.filter(n -> n.isType(NodeType.DESCRIPTION))
                .map(n -> (NodeDescription) n)
                .filter(nd -> nd.getName().equals("param"))
                .getOrElseThrow(() -> new RuntimeException("No description node."));
    }

    public void addObject(String name, List<Object> parameters) {
        List<Node> attributes = nodes.filter(n -> n.isType(NodeType.ATTRIBUTE));
        if (parameters.size() != attributes.size()) {
            throw new RuntimeException();
        }
        NodeObject object = new NodeObject(this.nodeDescription, name);
        Util.newOrUpdate(object, nodes).ifPresent(newNode -> nodes = nodes.append(newNode));

        // create or update
        List<NewOrSame<Node>> allNos = parameters.map(v -> new NodeValue(nodeDescription, v)).zip(attributes)
                .map(t -> Util.newOrSame(
                        t._1(),
                        nodes.filter(n -> n.isConnected(t._2()))
                ));

        // add new to this.nodes
        nodes = nodes.appendAll(allNos.filter(nos -> !nos.isSame()).map(NewOrSame::getObject));

        // add all connections to object
        allNos.map(NewOrSame::getObject).forEach(nv -> Util.connect(nv, object));

        // add new connections to attributes
        allNos.zip(attributes)
                .forEach(t -> {
                    if (!t._1().isSame()) {
                        Util.connect(t._1().getObject(), t._2());
                    }
                });

        // add new to attributes.values and sort
        allNos.zip(attributes)
                .forEach(t -> {
                    if (!t._1().isSame()) {
                        ((NodeAttribute) t._2()).addValue((NodeValue) t._1().getObject());
                    }
                });
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public NodeObject getObjectByName(String name) {
        return this.getNodes()
                .filter(n -> n.isType(NodeType.OBJECT))
                .map(n -> (NodeObject) n)
                .filter(no -> no.getName().equals(name))
                .getOrElseThrow(() -> new RuntimeException("No object with name."));
    }

    public void computeSimilarity(NodeObject nodeObject) {
        nodeObject.addSimilarityValue(new Similarity(
                nodes.filter(n -> n.isType(NodeType.DESCRIPTION))
                        .getOrElseThrow(() -> new RuntimeException("No description node.")),
                1.0
        ));
        nodeObject.setSimilarityToken(true);

        while (!similarityEndCondition.get()) {
            System.out.println(System.lineSeparator() + "--- EPOCH " + (epochs + 1) + " ---");
            List<Node> withTokens = nodes.filter(Node::hasSimilarityToken);
            if (withTokens.isEmpty()) {
                throw new RuntimeException("No tokens in nodes.");
            }
            withTokens.forEach(Node::pushToken);
            epochs++;
        }
    }

    public long getEpochs() {
        return epochs;
    }

    public List<Similarity> getSimilarity() {
        return nodes.filter(n -> n.isType(NodeType.OBJECT))
                .map(n -> new Similarity(n, n.getSimilarityValueList().map(Similarity::getValue).sum().doubleValue()));
    }
}
