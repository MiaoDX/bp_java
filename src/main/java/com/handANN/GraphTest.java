package com.handANN;//import org.testng.annotations.Test;

import com.google.common.collect.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/4.
 */
public class GraphTest {

    @Test
    public void whyCannotUseXChartInTest() throws InterruptedException, IOException {
        IRanGen ranGen = new RanGen(0);
        int num = 10;
        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;

        Function OnePlusSinPiX = MatchingFunctions.OnePlusSinPiX;

        FunctionFaux functionFaux = new FunctionFaux(ranGen,same,-2.0, 2.0, num);

        List<Double> x = functionFaux.getDomainValues();
        List<Double> y1 = functionFaux.getFunctionValues();


        functionFaux = new FunctionFaux(ranGen,sinf,-2.0, 2.0, num);
        List<Double> y2 = functionFaux.getFunctionValues();
        AreaLineChartWithXChart.show(x, y1, y2);
        Thread.sleep(10000);
    }


    public static void gettingBetter(Graph graph){
        int time = 1;
        double oldError = 100;
        double nowError = 0.0;

        do{
            time ++;
            graph.train();

            if(time % 1000 == 0){
                System.out.println(time + ":" + graph.getOutput() +";" + graph.getError());
            }


            nowError = graph.getError();
            assertThat(nowError).isLessThan(oldError);
            oldError = nowError;

        }while (nowError > 0.001);

        graph.beautifyWeightedGraphOutput();
        System.out.println("\n" + time + ":" + graph.getOutput() +";" + graph.getError());

    }


    public static void testPerformance(FunctionFaux functionFaux,Graph graph) throws InterruptedException, IOException {
        List<Double> x = functionFaux.getDomainValues();
        List<Double> shouldY = functionFaux.getFunctionValues();

        List<Double> weGot = new ArrayList<>();

        for(Double i : x){
            graph.updateGraph(i, 0.0);//Just for test,so set output zero
            graph.forward();
            weGot.add(graph.getOutput());
        }

        AreaLineChartWithXChart.show(x, shouldY, weGot);
        System.out.println("######################");
    }



    /**
     * Try to test our tool with http://blog.csdn.net/wsywl/article/details/6364744
     */
    @Test
    public void predefinedGraph() throws InterruptedException, IOException {
        Point p00 = new Point(0,0);

        Point p10 = new Point(1,0);
        Point p11 = new Point(1,1);
        Point p12 = new Point(1,2);
        Point p13 = new Point(1,3);
        Point p14 = new Point(1,4);

        Point p20 = new Point(2,0);


        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);
        linkedHashMultimap.put(p00, p11);
        linkedHashMultimap.put(p00, p12);
        linkedHashMultimap.put(p00, p13);
        linkedHashMultimap.put(p00, p14);

        linkedHashMultimap.put(p10, p20);
        linkedHashMultimap.put(p11, p20);
        linkedHashMultimap.put(p12, p20);
        linkedHashMultimap.put(p13, p20);
        linkedHashMultimap.put(p14, p20);


        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);
        pointsOrder.add(p11);
        pointsOrder.add(p12);
        pointsOrder.add(p13);
        pointsOrder.add(p14);
        pointsOrder.add(p20);


        p20.setActivationF(ActivationFuntions.sameLambda);
        p20.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        IRanGen ranGen = new RanGen(0);
        Graph graph = new Graph(linkedHashMultimap,pointsOrder,ranGen, 0.1);

        int num = 50;

        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;
        Function ThreeSinAddTwoCos = MatchingFunctions.ThreeSinAddTwoCos;
        Function OnePlusThreeSinAddTwoCos = MatchingFunctions.OnePlusThreeSinAddTwoCos;
        Function OnePlusSinPiX = MatchingFunctions.OnePlusSinPiX;

        FunctionFaux functionFaux = new FunctionFaux(ranGen,OnePlusSinPiX,-2.0, 2.0, num);

        int time = 0;
        double allowedError = 0.01;
        double sumMSE = 0.0;
        double oldMSE = 1000;

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

//            assertThat(sumMSE).isLessThan(oldMSE+1);    //should be strict less,why not???  -> no,since MSE is not our loss function,this can happen.
            oldMSE = sumMSE;

            if(time % 1000 == 0){
                System.out.println(time + " MSE: " + sumMSE + ":" + graph.getOutput() +";" + graph.getError());
            }



        }while (time < 100000 && sumMSE > allowedError);


        System.out.println("\n\nALL DONE,total time: " + time);
        System.out.println(graph.getError());
        graph.beautifyWeightedGraphOutput();

        testPerformance(functionFaux, graph);

    }



}
