package com.stefanik36.agds.model.node.node_type;

import com.stefanik36.agds.model.node.Value;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class NodeValueTest {

    @Test
    public void getValue() {
        NodeValue nodeValue = new NodeValue(null, 90.0);
        Value<Double> result = nodeValue.getValue(Double.class);
        assertEquals(result.getType(), Double.class);
    }

    @Test(expected = RuntimeException.class)
    public void getValueShouldThrowException() {
        NodeValue nodeValue = new NodeValue(null, 90.0);
        nodeValue.getValue(String.class);
    }
}