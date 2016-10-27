package com.handANNTest;
/**
 * Created by miao on 2016/10/5.
 */


import com.google.common.collect.LinkedHashMultimap;
import com.handANN.Graph;
import com.handANN.IRanGen;
import com.handANN.Point;
import com.handANN.RanGen;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test our graph with some other tests
 * https://theclevermachine.wordpress.com/tag/backpropagation/
 */
public class GraphTestOneAndTwoLayer {

    @Test
    public void oneLayer() {
        Point p00 = new Point(0, 0);
        Point p10 = new Point(1, 0);
        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);
        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(linkedHashMultimap, pointsOrder, ranGen);

        graph.updateGraph(1, 1);

        int time = 0;

        do {
            time++;
            graph.train();

            if (time % 1000 == 0) {
                System.out.println(time + ":" + graph.getOutput() + ";" + graph.getError());
            }


        } while (graph.getError() > 0.001);

        System.out.println("\n" + time + ":" + graph.getOutput() + ";" + graph.getError());

    }


    //    @Test
    public void twoLayer() {
        Point p00 = new Point(0, 0);
        Point p10 = new Point(1, 0);
        Point p20 = new Point(2, 0);

        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);
        linkedHashMultimap.put(p10, p20);

        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);
        pointsOrder.add(p20);

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(linkedHashMultimap, pointsOrder, ranGen);

        graph.updateGraph(1, 1);

        int time = 0;

        do {
            time++;

            graph.train();

            if (time % 1000 == 0) {
                System.out.println(time + ":" + graph.getOutput() + ";" + graph.getError());
            }

        } while (graph.getError() > 0.001);

        System.out.println("\n" + time + ":" + graph.getOutput() + ";" + graph.getError());
    }

}
