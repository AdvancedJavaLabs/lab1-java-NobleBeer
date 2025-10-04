package org.itmo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BFSTest {

    private static int[] sizes;
    private static int[] connections;
    private static Random random;

    @BeforeAll
    static void init() {
        sizes = new int[]{10, 100, 1000, 10_000, 10_000, 50_000, 100_000, 1_000_000, 2_000_000};
        connections = new int[]{50, 500, 5000, 50_000, 100_000, 1_000_000, 1_000_000, 10_000_000, 10_000_000};
        random = new Random(42);
    }

    @Test
    void bfsTest() throws IOException {
        try (FileWriter fw = new FileWriter("tmp/results.txt")) {
            for (int i = 0; i < sizes.length; i++) {
                System.out.println("--------------------------");
                System.out.println("Generating graph of size " + sizes[i] + " ...wait");
                var graph = new RandomGraphGenerator().generateGraph(random, sizes[i], connections[i]);
                System.out.println("Generation completed!\nStarting bfs");
                var serialTime = executeSerialBfsAndGetTime(graph);
                var parallelTime = executeParallelBfsAndGetTime(graph);
                fw.append("Times for " + sizes[i] + " vertices and " + connections[i] + " connections: ");
                fw.append("\nSerial: ").append(String.valueOf(serialTime));
                fw.append("\nParallel: ").append(String.valueOf(parallelTime));
                fw.append("\n--------\n");
            }
            fw.flush();
        }
    }

    @Test
    void parallelBfsTest() {
        IntStream.range(0, sizes.length).forEach(i -> {
            var graph = new RandomGraphGenerator().generateGraph(random, sizes[i], connections[i]);
            var visitedVertices = executeParallelBfsAndGetVisitedVertices(graph);
            assertEquals(sizes[i], visitedVertices);
        });
    }

    private long executeSerialBfsAndGetTime(Graph g) {
        long startTime = System.currentTimeMillis();
        g.bfs(0);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long executeParallelBfsAndGetTime(Graph g) {
        var startTime = System.currentTimeMillis();
        g.parallelBFS(0);
        var endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long executeParallelBfsAndGetVisitedVertices(Graph g) {
        return g.parallelBFS(0);
    }

}
