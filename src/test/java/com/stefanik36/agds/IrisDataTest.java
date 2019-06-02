package com.stefanik36.agds;

import com.stefanik36.agds.model.Agds;
import com.stefanik36.agds.model.Similarity;
import com.stefanik36.agds.model.SimilarityEndConditionType;
import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.node_type.NodeObject;
import com.stefanik36.agds.util.IrisData;
import io.vavr.collection.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IrisDataTest {
    @Test
    public void getData01() {
        List<List<Object>> data = new IrisData().getData();
        assertEquals(data.size(), 150);
    }

    @Test
    public void irisData01() {
        Agds agds = new Agds(List.of("sle", "swi", "ple", "pwi", "class"), SimilarityEndConditionType.NO_TOKENS);

        List<List<Object>> data = new IrisData().getData();
        for (int i = 0; i < data.size(); i++) {
            agds.addObject("R"+i, data.get(i));
        }

        NodeObject sr = agds.getObjectByName("R1");

        agds.computeSimilarity(sr);
        List<Similarity> result = agds.getSimilarity();
        List<Similarity> sortedResult = result.sortBy(Double::compareTo, Similarity::getValue).reverse();

        System.out.println(System.lineSeparator() + "=== RESULTS ===");
        result.forEach(s -> System.out.println(((NodeObject) s.getFrom()).getName() + " " + s.getValue()));

        System.out.println(System.lineSeparator() + "=== RESULTS SORTED ===");
        sortedResult.forEach(s -> System.out.println(((NodeObject) s.getFrom()).getName() + " " + s.getValue()));

        assertEquals(150, sortedResult.size());

        assertEquals("R1", ((NodeObject) sortedResult.get(0).getFrom()).getName());
        assertEquals(1.0, sortedResult.get(0).getValue(), 0.0001);

        assertEquals("R49", ((NodeObject) sortedResult.get(1).getFrom()).getName());
        assertEquals(0.6, sortedResult.get(1).getValue(), 0.01);

        assertEquals("R47", ((NodeObject) sortedResult.get(2).getFrom()).getName());
        assertEquals(0.6, sortedResult.get(2).getValue(), 0.01);

        assertEquals("R45", ((NodeObject) sortedResult.get(3).getFrom()).getName());
        assertEquals(0.6, sortedResult.get(3).getValue(), 0.01);

    }


}