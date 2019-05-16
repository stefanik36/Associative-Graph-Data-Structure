package com.stefanik36.agds.model;

import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.NodeType;

import java.util.function.Function;

public enum SimilarityEndConditionType {
    NEVER(agds -> false),
    ALL_OBJECTS_HAS_SIMILARITY(agds ->
            agds.getNodes()
                    .filter(n -> n.isType(NodeType.OBJECT))
                    .filter(n -> n.getSimilarityValueList().isEmpty())
                    .isEmpty()
    ),
    NO_TOKENS(agds ->
            agds.getNodes()
                    .filter(Node::hasSimilarityToken)
                    .isEmpty()
    );


    private final Function<Agds, Boolean> similarityEndCondition;

    SimilarityEndConditionType(Function<Agds, Boolean> similarityEndCondition) {
        this.similarityEndCondition = similarityEndCondition;
    }

    public Function<Agds, Boolean> getSimilarityEndCondition() {
        return similarityEndCondition;
    }
}
