package com.stefanik36.agds.util;

import com.stefanik36.agds.model.Link;
import com.stefanik36.agds.model.node.Node;
import io.vavr.collection.List;

import java.util.Optional;

public class Util {
    public static <T extends Node> NewOrSame<T> newOrSame(T n01, List<T> nodes) {
        List<T> sames = nodes.filter(n -> n.sameAs(n01));
        if (!sames.isEmpty()) {
            T same = sames.get();
            same.inc();
            return new NewOrSame<>(same, true);
        }
        return new NewOrSame<>(n01, false);
    }

    public static <T extends Node> Optional<T> newOrUpdate(T n01, List<Node> nodes) {
        List<? extends Node> same = nodes.filter(n -> n.sameAs(n01));
        if (!same.isEmpty()) {
            same.forEach(Node::inc);
            return Optional.empty();
        }
        return Optional.of(n01);
    }

    public static <T extends Node> Link connect(T n01, T n02) {
        if (!n01.equals(n02)) {
            Link link = new Link(n01, n02);
            n01.addLink(link);
            n02.addLink(link);
            return link;
        }
        throw new RuntimeException("Connection of the same nodes.");
    }

    public static <T extends Node> void connectEach(List<T> ns01, List<T> ns02) {
        ns01.forEach(n01 -> ns02.forEach(n02 -> connect(n01, n02)));
    }

    public static <T extends Node> void disconnect(T n01, T n02) {
        n01.setLinks(n01.getLinks().reject(l -> l.contains(n01) && l.contains(n02)));
        n02.setLinks(n02.getLinks().reject(l -> l.contains(n01) && l.contains(n02)));
    }

    public static <T extends Node> void disconnectEach(List<T> ns01, List<T> ns02) {
        ns01.forEach(n01 -> ns02.filter(n -> !n.equals(n01)).forEach(n02 -> disconnect(n01, n02)));
    }

}
