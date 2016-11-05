package com.handANNTest;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.handANN.Point;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by miao on 2016/10/28.
 */
public class GuavaGraphUsageTest {

    @Test
    public void simpleUse() {
        MutableValueGraph<Integer, Double> weightedGraph = ValueGraphBuilder.directed().build();
        weightedGraph.addNode(1);
        weightedGraph.putEdgeValue(2, 3, 1.5);  // also adds nodes 2 and 3 if not already present
        weightedGraph.putEdgeValue(3, 5, 1.5);  // edge values (like Map values) need not be unique

        assertThat(weightedGraph.nodes().contains(1)).isEqualTo(true);
    }


    @Test
    public void traverseDirectedGraph() {
        MutableValueGraph<Integer, Double> wG = ValueGraphBuilder.directed().build();
        wG.putEdgeValue(0, 1, 0.1);
        wG.putEdgeValue(0, 2, 0.2);
        wG.putEdgeValue(0, 3, 0.3);

        wG.putEdgeValue(1, 4, 0.4);
        wG.putEdgeValue(2, 4, 0.5);
        wG.putEdgeValue(3, 4, 0.6);

        List<Integer> layer0 = Arrays.asList(0);
        List<Integer> layer1 = Arrays.asList(1, 2, 3);
        List<Integer> layer2 = Arrays.asList(4);

        List<List<Integer>> allLayer = Arrays.asList(layer0, layer1, layer2);

        int first = 0, last = 4;
        // forward

        for (List<Integer> nowLayer : allLayer) {
            System.out.println("\n\n\n now Layer: " + nowLayer);
            for (Integer nowNode : nowLayer) {
                System.out.println("\n\t now Node: " + nowNode);
                Set<Integer> successors = wG.successors(nowNode);

                successors.stream().map(nowSuccessor -> wG.edgeValue(nowNode, nowSuccessor));//.forEach(v -> System.out.println("\n\t\t now successor: " + nowNode + ",weight:" + v));
                //System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode ,v)
                //successors.forEach(v -> System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode ,v)));
            }
        }
        System.out.println(wG);
    }

    @Test
    public void testCanDealWithPoint() {
        MutableValueGraph<Point, Double> wG = ValueGraphBuilder.directed().build();

        Point p00 = new Point(0, 0);

        Point p10 = new Point(1, 0);
        Point p11 = new Point(1, 1);

        Point p20 = new Point(2, 0);

        wG.putEdgeValue(p00, p10, 0.1);
        wG.putEdgeValue(p00, p11, 0.2);

        wG.putEdgeValue(p10, p20, 0.3);
        wG.putEdgeValue(p11, p20, 0.4);


        List<Point> layer0 = Arrays.asList(p00);
        List<Point> layer1 = Arrays.asList(p10, p11);
        List<Point> layer2 = Arrays.asList(p20);

        List<List<Point>> allLayer = Arrays.asList(layer0, layer1, layer2);

        int first = 0, last = 4;
        // forward

        //layer0.stream().map(l0 -> layer1.stream().map(l1 -> wG.putEdgeValue(l0, l1, 100.0))).count();
        layer0.forEach(l0 -> layer1.forEach(l1 -> wG.putEdgeValue(l0, l1, 100.0)));

        for (List<Point> nowLayer : allLayer) {
            System.out.println("\n\n\n now Layer: " + nowLayer);
            for (Point nowNode : nowLayer) {
                System.out.println("\n\t now Node: " + nowNode);
                Set<Point> successors = wG.successors(nowNode);

                //successors.stream().map(nowSuccessor ->  wG.edgeValue(nowNode ,nowSuccessor));//.forEach(v -> System.out.println("\n\t\t now successor: " + nowNode + ",weight:" + v));
                //System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode ,v)
                successors.forEach(v -> System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode, v)));
            }
        }
        System.out.println(wG);
    }


    @Test
    public void testMapOrForEach() {
        List<Integer> layer0 = Arrays.asList(0, 1, 2, 3);
        List<Integer> layer1 = Arrays.asList(4, 5, 6);

        //layer0.stream().map(l0 -> layer1.stream().map(l1 -> l0*l1)).collect(Collectors.toList()).forEach(System.out::println);

        layer0.forEach(l0 -> layer1.forEach(l1 -> System.out.println(l0 * l1)));

    }

    @Test
    public void testRange() {

        List<Integer> a = Arrays.asList(0, 0, 0, 0);
        List<Integer> b = Arrays.asList(1, 2, 3, 4);


        IntStream.range(0, a.size()).forEach(i -> a.set(i, b.get(i)));

        a.stream().forEach(System.out::println);


        int[] firstRange = IntStream.rangeClosed(1, 4).toArray();

        for (int i = 0; i < firstRange.length; i++) {
            System.out.println(firstRange[i]);
        }
    }

    @Test
    public void testAssignmentOfDoubleList() {
        List<Double> a = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        List<Double> b = new ArrayList<>();
        b.addAll(a);
        a.set(1, 1000.0);
        assertThat(b.get(1)).isEqualTo(2.0);
    }

    @Test
    public void testListOperation() {
        List<Double> a = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        List<Double> b = new ArrayList<>();
        b.addAll(a);
        a.set(1, 1000.0);
//        List<Double> c = a.;

    }

}
