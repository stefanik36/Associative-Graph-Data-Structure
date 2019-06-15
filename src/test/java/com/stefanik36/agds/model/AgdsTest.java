package com.stefanik36.agds.model;

import com.stefanik36.agds.model.node.NodeType;
import com.stefanik36.agds.model.node.node_type.NodeObject;
import io.vavr.collection.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AgdsTest {

    public static void main(String[] args) {
        new AgdsTest().irisData01();
    }

    @Test
    public void test01() {
        Agds agds = new Agds(List.of("sle", "swi", "ple", "pwi", "class"), SimilarityEndConditionType.NEVER);

        agds.addObject("R1", List.of(5.0, 2.3, 3.3, 1.0, "VERSI"));

        assertTrue(agds.getNodes()
                .filter(no -> no.isType(NodeType.VALUE))
                .map(v -> agds.getNodes()
                        .filter(n -> n.isType(NodeType.OBJECT))
                        .get().isConnected(v)
                ).filter(b -> !b).isEmpty());
    }

    @Test
    public void test02() {
        Agds agds = new Agds(List.of("sle", "swi", "ple", "pwi", "class"), SimilarityEndConditionType.NEVER);

        agds.addObject("R1", List.of(5.0, 2.3, 3.3, 1.0, "VERSI"));
        agds.addObject("R2", List.of(5.8, 2.6, 4.0, 1.2, "VERSI"));
        agds.addObject("R3", List.of(5.4, 3.0, 4.5, 1.5, "VERSI"));

        assertEquals(22, agds.getNodes().size());
    }

    @Test
    public void simpleData01() {

        Agds agds = new Agds(List.of("a", "b"), SimilarityEndConditionType.ALL_OBJECTS_HAS_SIMILARITY);

        agds.addObject("R1", List.of(4.0, 5.1));
        agds.addObject("R2", List.of(5.0, 2.0));
        agds.addObject("R3", List.of(5.0, 5.1));

        NodeObject r2 = agds.getObjectByName("R1");

        agds.computeSimilarity(r2);
        List<Similarity> result = agds.getSimilarity();
        assertEquals(3, result.size());
        assertEquals(1.0, result.get(0).getValue(), 0.0001);
        assertEquals(0.0, result.get(1).getValue(), 0.0001);
        assertEquals(0.5, result.get(2).getValue(), 0.0001);
    }


    @Test
    public void irisData01() {

        Agds agds = new Agds(List.of("sle", "swi", "ple", "pwi", "class"), SimilarityEndConditionType.NO_TOKENS);

        agds.addObject("R1", List.of(5.0, 2.3, 3.3, 1.0, "VERSI"));
        agds.addObject("R2", List.of(5.8, 2.6, 4.0, 1.2, "VERSI"));
        agds.addObject("R3", List.of(5.4, 3.0, 4.5, 1.5, "VERSI"));
        agds.addObject("R4", List.of(6.3, 3.3, 4.7, 1.6, "VERSI"));
        agds.addObject("R5", List.of(6.0, 2.7, 5.1, 1.6, "VERSI"));
        agds.addObject("R6", List.of(6.7, 3.0, 5.0, 1.7, "VERSI"));
        agds.addObject("R7", List.of(5.9, 3.2, 4.8, 1.8, "VERSI"));
        agds.addObject("R8", List.of(6.0, 2.2, 5.0, 1.5, "VIRGIN"));
        agds.addObject("R9", List.of(4.9, 2.5, 4.5, 1.7, "VIRGIN"));
        agds.addObject("R10", List.of(6.0, 3.0, 4.8, 1.8, "VIRGIN"));
        agds.addObject("R11", List.of(5.8, 2.7, 5.1, 1.9, "VIRGIN"));
        agds.addObject("R12", List.of(5.7, 2.5, 5.0, 2.0, "VIRGIN"));
        agds.addObject("R13", List.of(6.5, 3.2, 5.1, 2.0, "VIRGIN"));


        NodeObject r2 = agds.getObjectByName("R2");

        agds.computeSimilarity(r2);
        List<Similarity> result = agds.getSimilarity();
        List<Similarity> sortedResult = result.sortBy(Double::compareTo, Similarity::getValue).reverse();

        System.out.println(System.lineSeparator() + "=== RESULTS ===");
        result.forEach(s -> System.out.println(((NodeObject) s.getFrom()).getName() + " " + s.getValue()));

        System.out.println(System.lineSeparator() + "=== RESULTS SORTED ===");
        sortedResult.forEach(s -> System.out.println(((NodeObject) s.getFrom()).getName() + " " + s.getValue()));

        assertEquals(13, sortedResult.size());

        assertEquals(1.0, sortedResult.get(0).getValue(), 0.0001);
        assertEquals("R2", ((NodeObject) sortedResult.get(0).getFrom()).getName());

        assertEquals(0.78, sortedResult.get(1).getValue(), 0.01);
        assertEquals("R5", ((NodeObject) sortedResult.get(1).getFrom()).getName());

        assertEquals(0.77, sortedResult.get(2).getValue(), 0.01);
        assertEquals("R3", ((NodeObject) sortedResult.get(2).getFrom()).getName());

        assertEquals(0.75, sortedResult.get(3).getValue(), 0.01);
        assertEquals("R1", ((NodeObject) sortedResult.get(3).getFrom()).getName());

    }

}