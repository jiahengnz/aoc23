package day25;

import main.Day;

import java.util.*;

public class Day25 implements Day {

    class Node {
        String name;
        List<Node> connections = new ArrayList<>();

        public Node(String name) {
            this.name = name;
        }

        void addConnection(Node other) {
            connections.add(other);
            other.connections.add(this);
        }

        void contractRandom() {
            Node other = connections.remove((int) (Math.random() * connections.size()));

            while (connections.remove(other)) {
                ;
            }
            nodes.remove(other);
            this.name = this.name + " " + other.name;

            for (Node otherConn : other.connections) {
                if (otherConn == this) continue;
                otherConn.connections.remove(other);
                addConnection(otherConn);
                new Object();
            }

            if (this.connections.size() == 3) {
                System.out.println("3!!");
                System.out.println(this.name);
                System.out.println((this.name.length() + 1) / 4);

                System.exit(0);
            }
        }
    }

    List<Node> nodes;

    public void doThing(List<String> in) {
        while(true) {
            readInput(in);
            kargers();
            System.out.println(nodes.get(0).connections.size());
//            new Object();
        }
    }

    private void kargers() {
        while (nodes.size() > 2) {
            nodes.get((int) (Math.random() * nodes.size())).contractRandom();
        }
    }

    private void readInput(List<String> in) {
        nodes = new ArrayList<>();
        Map<String, Node> nodeMap = new HashMap<>();
        for (String line : in) {
            Scanner sc = new Scanner(line);
            String rootname = sc.next().substring(0, 3);
            Node root = nodeMap.computeIfAbsent(rootname, Node::new);
            while (sc.hasNext()) {
                Node next = nodeMap.computeIfAbsent(sc.next(), Node::new);
                root.addConnection(next);
            }
        }
        nodes.addAll(nodeMap.values());
//        System.out.println("nodes.size() = " + nodes.size());
    }

}
