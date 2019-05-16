package com.stefanik36.agds.util;

import com.stefanik36.agds.model.node.Node;
import com.stefanik36.agds.model.node.node_type.NodeValue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

    @Test
    public void disconnect() {
        Node n01 = new NodeValue(null, 4.4);
        NodeValue n02 = new NodeValue(null, 4.5);
        NodeValue n03 = new NodeValue(null, 4.6);

        Util.connect(n01, n02);
        Util.connect(n01, n03);

        assertTrue(n01.isConnected(n02));
        assertTrue(n01.isConnected(n03));
        assertFalse(n02.isConnected(n03));

        Util.disconnect(n01, n02);

        assertFalse(n02.isConnected(n03));
        assertFalse(n01.isConnected(n02));
        assertTrue(n01.isConnected(n03));
    }
}