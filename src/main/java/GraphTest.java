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


    @Test
    public void predefinedGraph(){
        Point p00 = new Point(0,0);
        Point p10 = new Point(1,0);
        Point p11 = new Point(1,1);
        Point p12 = new Point(1,2);
        Point p20 = new Point(2,0);


        p00.setOutput(5);


        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);
        linkedHashMultimap.put(p00, p11);
        linkedHashMultimap.put(p00, p12);

        linkedHashMultimap.put(p10, p20);
        linkedHashMultimap.put(p11, p20);
        linkedHashMultimap.put(p12, p20);


        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);
        pointsOrder.add(p11);
        pointsOrder.add(p12);
        pointsOrder.add(p20);

        //Graph graph = new Graph(linkedHashMultimap,pointsOrder,new RanGenFaux());

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(linkedHashMultimap,pointsOrder,ranGen);
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



}
