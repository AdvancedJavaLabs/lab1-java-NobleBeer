package org.itmo;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.Z_Result;

import java.util.Arrays;
import java.util.Random;

@JCStressTest
@Outcome(id = "true", expect = Expect.ACCEPTABLE, desc = "Все вершины посещены корректно, гонок данных нет")
@Outcome(id = "false", expect = Expect.FORBIDDEN, desc = "Выявлены несогласованности в данных")
@State
public class ParallelBFSSynchronizationTest {

    private static final int GRAPH_SIZE = 1_000_000;
    private final Graph graph = new RandomGraphGenerator()
            .generateGraph(new Random(42), GRAPH_SIZE, 2_000_000);;

    private int result1;
    private int result2;
    private int result3;
    private int result4;
    private int result5;

    @Actor
    public void actor1() {
        result1 = graph.parallelBFS(0);
    }

    @Actor
    public void actor2() {
        result2 = graph.parallelBFS(0);
    }

    @Actor
    public void actor3() {
        result3 = graph.parallelBFS(0);
    }

    @Actor
    public void actor4() {
        result4 = graph.parallelBFS(0);
    }

    @Actor
    public void actor5() {
        result5 = graph.parallelBFS(0);
    }

    @Arbiter
    public void arbiter(Z_Result result) {
        boolean allEqual = Arrays.stream(new int[]{result1, result2, result3, result4, result5})
                .allMatch(value -> value == result1);

        result.r1 = allEqual && result1 == GRAPH_SIZE;
    }

}
