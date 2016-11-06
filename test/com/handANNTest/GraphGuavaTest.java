package com.handANNTest;

import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableValueGraph;
import com.handANN.*;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/11/5.
 */
public class GraphGuavaTest {
    public static void testPerformance(FunctionFaux functionFaux, GraphGuava graph) throws InterruptedException, IOException {
        List<Double> x = functionFaux.getDomainValues();
        List<Double> shouldY = functionFaux.getFunctionValues();

        List<Double> weGotY = new ArrayList<>();

        for (Double i : x) {
            graph.updateGraph(Arrays.asList(i), Arrays.asList(0.0));//Just for test,so set output zero
            graph.forward();
            weGotY.add(graph.getLastLayerNodesOutput().get(0));
        }

        functionFaux.widenDomainAndRemoveSampleSet(1);

        List<Double> moreX = functionFaux.getWidenDomainValues();
        List<Double> moreY = functionFaux.getWidenFunctionValues();


        List<Double> weGotMoreY = new ArrayList<>();
        for (Double i : moreX) {
            graph.updateGraph(Arrays.asList(i), Arrays.asList(0.0));
            graph.forward();
            weGotMoreY.add(graph.getLastLayerNodesOutput().get(0));
        }


        functionFaux.denseDomainAndRemoveSampleSet(2);

        List<Double> denseX = functionFaux.getDenseDomainValues();
        List<Double> denseY = functionFaux.getDenseFunctionValues();
        List<Double> weGotDenseY = new ArrayList<>();
        List<Double> mseList = new ArrayList<>();
        for (Double i : denseX) {
            graph.updateGraph(Arrays.asList(i), Arrays.asList(0.0));
            graph.forward();
            weGotDenseY.add(graph.getLastLayerNodesOutput().get(0));
            mseList.add(graph.getLastLayerNodesErrors().get(0) * graph.getLastLayerNodesErrors().get(0));
        }

        double mse = Math.sqrt(mseList.stream().mapToDouble(input -> input).sum()) / mseList.size();


        System.out.println(mse);

        AreaLineChartWithXChart.show(x, shouldY, weGotY, "Sample_Answer");

        assertThat(moreX.size()).isEqualTo(moreY.size()).isEqualTo(weGotMoreY.size()).isGreaterThan(0);

        AreaLineChartWithXChart.show(moreX, moreY, weGotMoreY, "Just_more_Answer", "larger domain truth", "we got at larger domain");

        System.out.println(denseX.size() + ";" + denseY.size() + ";" + weGotDenseY.size());

        assertThat(denseX.size()).isEqualTo(denseY.size()).isEqualTo(weGotDenseY.size()).isGreaterThan(0);

        AreaLineChartWithXChart.show(denseX, denseY, weGotDenseY, "Dense_test_set", "test set truth", "we got at test set, mse error:" + mse);


    }

    public static void gettingBetter(GraphGuava graph) {
        int time = 1;
        double oldError = 100;
        double nowError = 0.0;

        do {
            time++;
            graph.train();

            if (time % 1000 == 0) {
                System.out.println(time + ":" + graph.getLastLayerNodesOutput() + "," + graph.getLastLayerNodesErrors());
            }


            nowError = Math.abs(graph.getLastLayerNodesErrors().get(0));
            assertThat(nowError).isLessThan(oldError);
            oldError = nowError;

        } while (nowError > 0.001);


        System.out.println("\n" + time + ":" + graph.getLastLayerNodesErrors());

    }

    @Test
    public void testConstructor() {

        List<Integer> layerNum = Arrays.asList(1, 2, 1);
        IRanGen ranGen = new RanGenProvided(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);

        GraphGuava graphGuava = new GraphGuava(layerNum, ranGen, 1.0 / 20);

        List<List<Point>> allLayersNodes = graphGuava.getAllLayersNodes();
        MutableValueGraph<Point, Double> weightedGraphGuava = graphGuava.getWeightedGraphGuava();
        ImmutableGraph<Point> immutableGraph = ImmutableGraph.copyOf(graphGuava.getWeightedGraphGuava());

        Point p00 = allLayersNodes.get(0).get(0);
        Point p10 = allLayersNodes.get(1).get(0);
        Point p11 = allLayersNodes.get(1).get(1);
        Point p20 = allLayersNodes.get(2).get(0);


        // test constructor

        assertThat(weightedGraphGuava.edgeValue(p00, p10)).isEqualTo(0.1);
        assertThat(weightedGraphGuava.edgeValue(p00, p11)).isEqualTo(0.2);
        assertThat(weightedGraphGuava.edgeValue(p10, p20)).isEqualTo(0.3);
        assertThat(weightedGraphGuava.edgeValue(p11, p20)).isEqualTo(0.4);


        assertThat(p00.getBias()).isEqualTo(0.5);
        assertThat(p10.getBias()).isEqualTo(0.6);
        assertThat(p11.getBias()).isEqualTo(0.7);
        assertThat(p20.getBias()).isEqualTo(0.8);

    }

    @Test
    public void oneLayerWithHalfAndSameActivationFunction() {

        List<Integer> layerNum = Arrays.asList(1, 1);
        IRanGen ranGen = new RanGenProvided(0.5, 0.5, 0.5);

        GraphGuava graph = new GraphGuava(layerNum, ranGen, 1.0 / 20);

        List<List<Point>> allLayersNodes = graph.getAllLayersNodes();
        MutableValueGraph<Point, Double> weightedGraphGuava = graph.getWeightedGraphGuava();
        ImmutableGraph<Point> immutableGraph = ImmutableGraph.copyOf(graph.getWeightedGraphGuava());

        Point p00 = allLayersNodes.get(0).get(0);
        Point p10 = allLayersNodes.get(1).get(0);

        p00.setActivationF(ActivationFuntions.halfLambda);
        p00.setDifferentiationF(ActivationFuntions.halfDifferentiationLambda);

        p10.setActivationF(ActivationFuntions.sameLambda);
        p10.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        //init
        graph.updateGraph(Arrays.asList(1.0), Arrays.asList(1.0));
        assertThat(p00.getInput()).isEqualTo(1.0);
        //assertThat(p00.getOutput()).isEqualTo(0.5);   // different here
        assertThat(p00.getBias()).isEqualTo(0.5);
        assertThat(p10.getInput()).isEqualTo(0.0);
        assertThat(p10.getOutput()).isEqualTo(0.0);
        assertThat(p10.getBias()).isEqualTo(0.5);
        assertThat(weightedGraphGuava.edgeValue(p00, p10)).isEqualTo(0.5);


        //forward
//        graph.forward();
        graph.forwardWithStream();

        assertThat(p00.getOutput()).isEqualTo(0.5);
        assertThat(p10.getOutput()).isEqualTo(0.75);
        //assertThat(graph.getError()).isEqualTo(1.0 / 4);
        assertThat(graph.getLastLayerNodesErrors()).isEqualTo(Arrays.asList(1.0 / 4));


//        graph.backwardTheta();
        graph.backwardThetaWithStream();

        assertThat(p10.getTheta()).isEqualTo(1.0 / 4);    //sameDifferentiation is 1
        assertThat(p00.getTheta()).isEqualTo(1.0 / 16);


//        graph.backwardWeight();
        graph.backwardWeightWithStream();

        assertThat(weightedGraphGuava.edgeValue(p00, p10)).isEqualTo(81.0 / 160);
        assertThat(p10.getBias()).isEqualTo(0.5 + 0.05 * (1.0 / 4));

 /*
        //assert getting better
        //GraphTest.gettingBetter(graph);

*/
    }

    @Test
    public void layer_1half_2_half_sigmoid_1SameActivationFunction() {

        List<Integer> layerNum = Arrays.asList(1, 2, 1);
        IRanGen ranGen = new RanGenProvided(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);

        double learnRatio = 1.0 / 20;
        GraphGuava graph = new GraphGuava(layerNum, ranGen, learnRatio);

        List<List<Point>> allLayersNodes = graph.getAllLayersNodes();
        MutableValueGraph<Point, Double> weightedGraphGuava = graph.getWeightedGraphGuava();

        Point p00 = allLayersNodes.get(0).get(0);
        Point p10 = allLayersNodes.get(1).get(0);
        Point p11 = allLayersNodes.get(1).get(1);
        Point p20 = allLayersNodes.get(2).get(0);

        p00.setActivationF(ActivationFuntions.halfLambda);
        p00.setDifferentiationF(ActivationFuntions.halfDifferentiationLambda);
//        p10.setActivationF(ActivationFuntions.halfLambda);
//        p10.setDifferentiationF(ActivationFuntions.halfDifferentiationLambda);
        p11.setActivationF(ActivationFuntions.halfLambda);
        p11.setDifferentiationF(ActivationFuntions.halfDifferentiationLambda);

        p20.setActivationF(ActivationFuntions.sameLambda);
        p20.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        //init
        graph.updateGraph(Arrays.asList(1.0), Arrays.asList(1.0));
        assertThat(p00.getInput()).isEqualTo(1.0);
        assertThat(p00.getBias()).isEqualTo(0.5);

        assertThat(p10.getInput()).isEqualTo(0.0);
        assertThat(p10.getOutput()).isEqualTo(0.0);
        assertThat(p10.getBias()).isEqualTo(0.6);

        assertThat(p11.getInput()).isEqualTo(0.0);
        assertThat(p11.getOutput()).isEqualTo(0.0);
        assertThat(p11.getBias()).isEqualTo(0.7);

        assertThat(p20.getInput()).isEqualTo(0.0);
        assertThat(p20.getOutput()).isEqualTo(0.0);
        assertThat(p20.getBias()).isEqualTo(0.8);

        assertThat(weightedGraphGuava.edgeValue(p00, p10)).isEqualTo(0.1);
        assertThat(weightedGraphGuava.edgeValue(p00, p11)).isEqualTo(0.2);
        assertThat(weightedGraphGuava.edgeValue(p10, p20)).isEqualTo(0.3);
        assertThat(weightedGraphGuava.edgeValue(p11, p20)).isEqualTo(0.4);


        //forward
        graph.forward();
//        graph.forwardWithStream();

        assertThat(p00.getOutput()).isEqualTo(0.5);

        assertThat(p10.getInput()).isEqualTo(0.05);
        assertThat(p10.getOutput()).isEqualTo(0.6570104626734988);

        assertThat(p11.getInput()).isEqualTo(0.1);
        assertThat(p11.getOutput()).isCloseTo(0.4, Offset.offset(1e-5));

        assertThat(p20.getInput()).isCloseTo(0.3571, Offset.offset(1e-4));
        assertThat(p20.getOutput()).isCloseTo(1.1571, Offset.offset(1e-4));

        assertThat(graph.getLastLayerNodesErrors().get(0)).isCloseTo(-0.1571, Offset.offset(1e-4));


        double error = -0.1571;
//        graph.backwardTheta();
        graph.backwardThetaWithStream();


        assertThat(p20.getTheta()).isCloseTo(error, Offset.offset(1e-4));  //sameDifferentiation is 1

        assertThat(p10.getTheta()).isCloseTo(-0.01062, Offset.offset(1e-4)); // halfDifferentiationLambda is 0.5
        assertThat(p11.getTheta()).isCloseTo(-0.03142, Offset.offset(1e-4));


        assertThat(p00.getTheta()).isCloseTo(-0.003673, Offset.offset(1e-4));


//       graph.backwardWeight();
        graph.backwardWeightWithStream();

        assertThat(weightedGraphGuava.edgeValue(p00, p10)).isCloseTo(0.0997345, Offset.offset(1e-4));
        assertThat(weightedGraphGuava.edgeValue(p00, p11)).isCloseTo(0.1992145, Offset.offset(1e-4));
        assertThat(weightedGraphGuava.edgeValue(p10, p20)).isCloseTo(0.29483918645, Offset.offset(1e-4));
        assertThat(weightedGraphGuava.edgeValue(p11, p20)).isCloseTo(0.396858, Offset.offset(1e-4));

        assertThat(p00.getBias()).isCloseTo(0.49981635, Offset.offset(1e-4));
        assertThat(p10.getBias()).isCloseTo(0.599469, Offset.offset(1e-4));
        assertThat(p11.getBias()).isCloseTo(0.698429, Offset.offset(1e-4));
        assertThat(p20.getBias()).isCloseTo(0.792145, Offset.offset(1e-4));


        //assert getting better
        gettingBetter(graph);


    }

    /**
     * Try to test our tool with http://blog.csdn.net/wsywl/article/details/6364744
     */
    @Test
    public void predefinedGraph() throws InterruptedException, IOException {


        List<Integer> layerNum = Arrays.asList(1, 3, 3, 4, 1);
        IRanGen ranGen = new RanGen(0);

        double learnRatio = 1.0 / 20;
        GraphGuava graph = new GraphGuava(layerNum, ranGen, learnRatio);

        List<List<Point>> allLayersNodes = graph.getAllLayersNodes();
        MutableValueGraph<Point, Double> weightedGraphGuava = graph.getWeightedGraphGuava();

        //Point p20 = allLayersNodes.get(2).get(0);

        Point plast = graph.getLastLayerNodes().get(0);

        plast.setActivationF(ActivationFuntions.sameLambda);
        plast.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);


        int num = 100;

        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;
        Function ThreeSinAddTwoCos = MatchingFunctions.ThreeSinAddTwoCos;
        Function OnePlusThreeSinAddTwoCos = MatchingFunctions.OnePlusThreeSinAddTwoCos;
        Function OnePlusSinPiX = MatchingFunctions.OnePlusSinPiX;
        Function FiveSinADDFourCosX = MatchingFunctions.FiveSinADDFourCosX;

        Function OnePlusTwoSinPlusThreeExp = MatchingFunctions.OnePlusTwoSinPlusThreeExp;

        FunctionFaux functionFaux = new FunctionFaux(ranGen, OnePlusTwoSinPlusThreeExp, -Math.PI, Math.PI, num);

        int time = 0;
        double allowedError = 1e-4;
        double sumMSE = 0.0;
        double oldMSE = 1000;

        int maxEpoch = 100000;

        List<Double> domainValues = functionFaux.getDomainValues();
        List<Double> functionValues = functionFaux.getFunctionValues();

        List<Double> sumMSEListsX = new ArrayList<>();
        List<Double> sumMSEListsY = new ArrayList<>();
        List<Double> mseList = new ArrayList<>();
        do {

            time++;
            sumMSE = 0.0;
            double tmpSumMSE = 0.0;
            mseList.clear();

            for (int i = 0; i < num; i++) {
//                graph.updateGraph(domainValues.get(i), functionValues.get(i));
                graph.updateGraph(Arrays.asList(domainValues.get(i)), Arrays.asList(functionValues.get(i)));

//                graph.trainWithStream();
                graph.train();


                double error = graph.getLastLayerNodesErrors().get(0);

//                sumMSE += error*error;

                mseList.add(error * error);
            }

//            sumMSE = Math.sqrt(sumMSE)/num;


//            assertThat(mseList.size()).isEqualTo(num);


//            sumMSE = Math.sqrt(mseList.stream().mapToDouble(input -> input).sum())/mseList.size();


            mseList.sort(Comparator.naturalOrder());
            List<Double> tenth2nineoutoften = mseList.subList(num / 10, num * 9 / 10);
            sumMSE = Math.sqrt(tenth2nineoutoften.stream().mapToDouble(input -> input).sum()) / tenth2nineoutoften.size();


            List<Double> lastTenth = mseList.subList(num * 9 / 10, num);
            List<Double> firstTenth = mseList.subList(0, num / 10);
            List<Double> outsideTenth = new ArrayList<>();
            outsideTenth.addAll(firstTenth);
            outsideTenth.addAll(lastTenth);
            sumMSE = Math.sqrt(outsideTenth.stream().mapToDouble(input -> input).sum()) / outsideTenth.size();

//            assertThat(sumMSE).isCloseTo(tmpSumMSE, Offset.offset(0.001));

            oldMSE = sumMSE;

            if (time % 1000 == 0) {
                System.out.println(time + " MSE: " + sumMSE + ":" + graph.getLastLayerNodesErrors());
            }

            if (time % 100 == 0) {
                sumMSEListsX.add(0.0 + time);
                sumMSEListsY.add(sumMSE);
            }


        } while (time < maxEpoch && sumMSE > allowedError);


        System.out.println("\n\nALL DONE,total time: " + time);
        System.out.println(graph.getLastLayerNodesErrors());


        AreaLineChartWithXChart.show(sumMSEListsX, sumMSEListsY, "error before 20000", "error mse");

        testPerformance(functionFaux, graph);


    }


}
