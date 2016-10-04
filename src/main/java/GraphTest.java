//import org.testng.annotations.Test;

import com.google.common.collect.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by miao on 2016/10/4.
 */
public class GraphTest {

    /*
    @Test
    public void testGenerator(){
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(4);
        integerList.add(1);
        Graph graph = new Graph(integerList);

        System.out.println(graph);

        graph.forward();

        System.out.println(graph);

    }

*/

    @Test
    public void predefinedGraph(){
        Point p00 = new Point(0,0);
        Point p10 = new Point(1,0);
        Point p11 = new Point(1,1);
        Point p12 = new Point(1,2);
        Point p20 = new Point(2,0);


        p00.setOutput(5);


        Multimap<Point, Point> listMultiMap = LinkedHashMultimap.create();
        listMultiMap.put(p00, p10);
        listMultiMap.put(p00, p11);
        listMultiMap.put(p00, p12);

        listMultiMap.put(p10, p20);
        listMultiMap.put(p11, p20);
        listMultiMap.put(p12, p20);


        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);
        pointsOrder.add(p11);
        pointsOrder.add(p12);
        pointsOrder.add(p20);

        //Graph graph = new Graph(listMultiMap,pointsOrder,new RanGenFaux());

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(listMultiMap,pointsOrder,ranGen);
        FunctionFaux functionFaux = new FunctionFaux(ranGen, 2);

        int time = 0;

        do{
            time ++;
            List<Double> doubles = functionFaux.getNextInputAndTarget();
//            double input = 5;
//            double target = 0.9;

            double input = doubles.get(0);
            double target = doubles.get(1);
            //System.out.println(doubles);

            graph.updateGraph(input, target);

            //System.out.println("Enter,error:" + graph.getError());

            graph.train();


            if (time%1000 == 0){
                System.out.println("time:" + time + ",error:" + graph.getError());
            }


            //System.out.println("Out,error:" + graph.getError());
        }while (time < 1000000 || Math.abs(graph.getError()) > 0.001);

        System.out.println(graph.getError());

//        List<Double> doubles = functionFaux.getNextInputAndTarget();
//        double input = doubles.get(0);
//        double target = doubles.get(1);
//        graph.updateGraph(input,target);
//        graph.train();
//
//        System.out.println(graph.getError());

    }

/*
    @Test
    public void  arrayListCopy(){
        System.out.println("Enter arrayListCopy");

        List<Point> list = new ArrayList<Point>();

        Point p1 = new Point(1,1);
        Point p2 = new Point(1,2);
        Point p3 = new Point(1,3);

        list.add(p1);
        list.add(p2);
        list.add(p3);

        List<Point> list1 = new ArrayList<Point>();
        list1.addAll(list);

        Collections.reverse(list1);


        p1.setOutput(100);

        System.out.println(list);
        System.out.println(list1);

        System.out.println("Leave arrayListCopy");

    }*/


}
