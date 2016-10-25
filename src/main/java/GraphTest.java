//import org.testng.annotations.Test;

import com.google.common.collect.*;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by miao on 2016/10/4.
 */
public class GraphTest {


    /**
     * Try to test our tool with http://blog.csdn.net/wsywl/article/details/6364744
     */
    @Test
    public void predefinedGraph(){
        Point p00 = new Point(0,0);
        Point p10 = new Point(1,0);
        Point p11 = new Point(1,1);
        Point p12 = new Point(1,2);
        Point p20 = new Point(2,0);


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

        p20.setActivationF(ActivationFuntions.sameLambda);
        p20.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(linkedHashMultimap,pointsOrder,ranGen);

        int num = 20;

        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;

        FunctionFaux functionFaux = new FunctionFaux(ranGen,same,-Math.PI,Math.PI, num);

        int time = 0;
        double allowedError = 0.05;
        double sumMSE = 0.0;

        List<Double> domainValues = functionFaux.getDomainValues();
        List<Double> functionValues = functionFaux.getFunctionValues();



        do{

            time ++;
            sumMSE = 0.0;

            for(int i = 0;i < num;i ++){
                graph.updateGraph(domainValues.get(i), functionValues.get(i));

                graph.train();

                sumMSE += graph.getError()*graph.getError();

            }

            if(time % 1000 == 0){
                System.out.println(time + " MSE: " + sumMSE + ":" + graph.getOutput() +";" + graph.getError());
            }



        }while (time < 100000 && sumMSE > allowedError);


        System.out.println("\n\nALL DONE,total time: " + time);
        System.out.println(graph.getError());
        graph.beautifyWeightedGraphOutput();

    }



}
