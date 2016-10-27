package com.handANN;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;

/**
 * Created by miao on 2016/10/26.
 */
public class AreaLineChartWithXChart {

    public static void main(String[] args) throws InterruptedException, IOException {
        IRanGen ranGen = new RanGen(0);
        int num = 10;
        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;

        Function OnePlusSinPiX = MatchingFunctions.OnePlusSinPiX;

        FunctionFaux functionFaux = new FunctionFaux(ranGen, same, -2.0, 2.0, num);

        List<Double> x = functionFaux.getDomainValues();
        List<Double> y1 = functionFaux.getFunctionValues();


        functionFaux = new FunctionFaux(ranGen, sinf, -2.0, 2.0, num);
        List<Double> y2 = functionFaux.getFunctionValues();
        show(x, y1, y2);
    }


    public static void show(List<Double> x, List<Double> y1, List<Double> y2, String picName) throws InterruptedException, IOException {
// Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Check").xAxisTitle("x").yAxisTitle("f").build();
        XYSeries seriesLiability = chart.addSeries("Should be", x, y1);
        seriesLiability.setMarker(SeriesMarkers.NONE);
        chart.addSeries("We got", x, y2);
        new SwingWrapper<XYChart>(chart).displayChart();
        BitmapEncoder.saveBitmap(chart, "./" + picName, BitmapEncoder.BitmapFormat.PNG);
        Thread.sleep(5000);
    }

    public static void show(List<Double> x, List<Double> y1, List<Double> y2) throws InterruptedException, IOException {
        show(x, y1, y2, "Our_answer");
    }

    public static void show(List<List<Double>> moreXandAllY, List<String> moreXandAllYName) throws InterruptedException, IOException {
        show(moreXandAllY, moreXandAllYName, "Our_answer");
    }

    public static void show(List<List<Double>> moreXandAllY, List<String> moreXandAllYName,  String picName) throws InterruptedException, IOException {

        if(moreXandAllY.size() != moreXandAllYName.size()){
            throw new InterruptedException("The size of moreXandAllY and moreXandAllYName should be the same");
        }



        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Check").xAxisTitle("x").yAxisTitle("f").build();


        for (int i = 1;i < 3; i++){ // zero position is for X
            System.out.println(moreXandAllY.get(0));
            chart.addSeries(moreXandAllYName.get(i), moreXandAllY.get(0), moreXandAllY.get(i)).setMarker(SeriesMarkers.NONE);
        }

        for (int i = 4;i < moreXandAllY.size(); i++){ // zero position is for X
            System.out.println(moreXandAllY.get(3));
            chart.addSeries(moreXandAllYName.get(i), moreXandAllY.get(3), moreXandAllY.get(i)).setMarker(SeriesMarkers.NONE);
        }


        new SwingWrapper<XYChart>(chart).displayChart();
        BitmapEncoder.saveBitmap(chart, "./" + picName, BitmapEncoder.BitmapFormat.PNG);
        Thread.sleep(5000);
    }
}
